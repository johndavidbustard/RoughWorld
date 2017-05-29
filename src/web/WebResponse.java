package web;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class WebResponse 
{	
	//HTTP status code after processing, e.g. "200 OK", HTTP_OK
	public String status;

	//MIME type of content, e.g. "text/html"
	public String mimeType;

	public String location = null;
	
	//Data of the response, may be null.
	public InputStream data;
	public long range = -1;
	//Function to generate an output stream (if the data is too large to fit in memory)
	public OutputStreamCallback datacallback = null;
	public Object datacallbackMetadata = null;
	
	/**
	 * Headers for the HTTP response. Use addHeader()
	 * to add lines.
	 */
	public HashMap<String,String> header = new HashMap<String,String>();

	public WebResponse()
	{
		this.status = HTTP_OK;
	}

	/**
	 * Some HTTP response status codes
	 */
	public static final String
	    HTTP_OK = "200 OK",
	    HTTP_PARTIAL = "206 Partial Content",
		HTTP_PERMANENT_REDIRECT = "301 Moved Permanently",
	    HTTP_REDIRECT = "307 Temporary Redirect",
		HTTP_FORBIDDEN = "403 Forbidden",
		HTTP_NOTFOUND = "404 Not Found",
		HTTP_BADREQUEST = "400 Bad Request",
		HTTP_INTERNALERROR = "500 Internal Server Error",
		HTTP_NOTIMPLEMENTED = "501 Not Implemented";

	//MIME types of different files
	public static final String
		MIME_PLAINTEXT = "text/plain",
		MIME_HTML = "text/html",
		MIME_JAVASCRIPT = "text/javascript",
		MIME_DEFAULT_BINARY = "application/octet-stream";

	private static Hashtable theMimeTypes = new Hashtable();
	static
	{
		StringTokenizer st = new StringTokenizer(
			"htm		text/html "+
			"html		text/html "+
			"css		text/css "+
			"js			application/javascript "+
			"txt		text/plain "+
			"asc		text/plain "+
			"gif		image/gif "+
			"jpg		image/jpeg "+
			"jpeg		image/jpeg "+
			"..			image/jpeg "+
			"png		image/png "+
			"mp3		audio/mpeg "+
			"m3u		audio/mpeg-url " +
			//"avi		video/mp4 "+
			"mp4		video/mp4 "+
			"m4v		video/mp4 "+
			//"webm		video/webm "+
			"pdf		application/pdf "+
			"doc		application/msword "+
			"ogg		application/x-ogg "+
			"zip		application/octet-stream "+
			"exe		application/octet-stream "+
			"class		application/octet-stream " );
		while ( st.hasMoreTokens())
			theMimeTypes.put( st.nextToken(), st.nextToken());
	}

	public static WebResponse serveFile(HashMap<String, String> header, String path)
	{
		try
		{
			File f = new File(path);
			// Get MIME type from file name extension, if possible
			String mime = null;
			int dot = f.getCanonicalPath().lastIndexOf( '.' );
			if ( dot >= 0 )
				mime = (String)theMimeTypes.get( f.getCanonicalPath().substring( dot + 1 ).toLowerCase());
			if ( mime == null )
				mime = MIME_DEFAULT_BINARY;
	
			FileInputStream fis = new FileInputStream( f );
			return serveInputstream(header, fis, mime, f.length());
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static WebResponse serveInputstream(HashMap<String, String> header,InputStream fis,String mime,long maxlen)
	{
		// Support (simple) skipping:
		long startFrom = 0;
		long endat = -1;
		long rangebytes = -1;
		String resulttype = HTTP_OK;
		String range = header.get( "Range" );
		String rangeend = null;
		if ( range != null )
		{
			System.out.println(range);
			if ( range.startsWith( "bytes=" ))
			{
				resulttype = HTTP_PARTIAL;
				range = range.substring( "bytes=".length());
				int minus = range.indexOf( '-' );
				if ( minus > 0 )
				{
					rangeend = range.substring( minus+1 );
					range = range.substring( 0, minus );
				}
				try	
				{
					startFrom = Long.parseLong( range );
					endat = Long.parseLong(rangeend);
				}
				catch ( NumberFormatException nfe ) 
				{
					rangebytes = 512*1024;
					if(rangebytes>(maxlen-startFrom))
					{
						resulttype = HTTP_OK;
						rangebytes = (maxlen-startFrom);
					}
					else
					{
//						resulttype = HTTP_OK;
						resulttype = HTTP_PARTIAL;						
					}
//					rangebytes = (maxlen-startFrom);
				}
				
			}
			if(endat!=-1)
			{
				rangebytes = endat-startFrom;
			}
			else
			{
//				rangebytes = 512*1024;
//				if(rangebytes>(maxlen-startFrom))
					rangebytes = (maxlen-startFrom);
			}
		}
		else
		{
			rangebytes = maxlen-startFrom;
		}

		try
		{
			fis.skip( startFrom );
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		WebResponse r = new WebResponse( resulttype, mime, rangebytes, fis );
		r.addHeader( "Content-length", "" + (rangebytes));
		//bytes 0-499/1234
		r.addHeader( "Content-range", "bytes " + startFrom + "-" +
					(startFrom+rangebytes-1) + "/" + maxlen);
		
		return r;
	}

	public WebResponse( String status, String mimeType, long rng, InputStream data )
	{
		this.status = status;
		this.mimeType = mimeType;
		this.data = data;
		this.range = rng;
	}

	public WebResponse( String status, String mimeType, OutputStreamCallback data, Object metadata )
	{
		this.status = status;
		this.mimeType = mimeType;
		this.datacallback = data;
		this.datacallbackMetadata = metadata;
	}
	
	/**
	 * Convenience method that makes an InputStream out of
	 * given text.
	 */
	public WebResponse( String status, String location, String mimeType, String txt )
	{
		this.status = status;
		this.location = location;
		this.mimeType = mimeType;
		this.data = new ByteArrayInputStream(txt.getBytes());
	}

	/**
	 * Convenience method that makes an InputStream out of
	 * given text.
	 */
	public WebResponse( String status, String mimeType, String txt )
	{
		this.status = status;
		this.mimeType = mimeType;
		this.data = new ByteArrayInputStream(txt.getBytes());
	}

	/**
	 * Adds given line to the header.
	 */
	public void addHeader( String name, String value )
	{
		header.put( name, value );
	}

}
