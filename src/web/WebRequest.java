package web;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

//import org.apache.commons.fileupload.MultipartStream;

public class WebRequest implements Runnable
{
	WebInterface winterface;
	private Socket socket;
	InputStream in;
	public WebResponse r = null;
	public String path;
	public HashMap<String,String> parms = new HashMap<String,String>();
	public HashMap<String,String> header = new HashMap<String,String>();
	public HashMap<String,String> cookies = new HashMap<String,String>();
	
	
	private static java.text.SimpleDateFormat gmtFrmt;
	static
	{
		gmtFrmt = new java.text.SimpleDateFormat( "E, d MMM yyyy HH:mm:ss 'GMT'", Locale.UK);
		gmtFrmt.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	public boolean isSet(String param)
	{
		if(parms.containsKey(param))
		{
			String res = parms.get(param);
			if(res.equalsIgnoreCase("true"))
				return true;
		}
		return false;
	}
	
	public WebRequest(WebInterface i, Socket s)
	{
		winterface = i;
		socket = s;

		Thread t = new Thread( this );
		t.setDaemon( true );
		t.start();
	}
	
	public void run()
	{
		try
		{
			if(r==null)
			{
				InputStream is = socket.getInputStream();
				if ( is == null) return;
				in = is;
				
				// Read the request line
				String inLine = readline(in);
				if (inLine == null) return;
				StringTokenizer st = new StringTokenizer( inLine );
				if ( !st.hasMoreTokens())
					sendError( WebResponse.HTTP_BADREQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html" );
	
				String method = st.nextToken();
	
				if ( !st.hasMoreTokens())
					sendError( WebResponse.HTTP_BADREQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html" );
	
				String uri = st.nextToken();
	
				// Decode parameters from the URI
				int qmi = uri.indexOf( '?' );
				if ( qmi >= 0 )
				{
					decodeParms( uri.substring( qmi+1 ), parms );
					uri = WebSecurity.decodePercent( uri.substring( 0, qmi ));
				}
				else uri = WebSecurity.decodePercent(uri);
	
				path = uri.substring(1);
				//can have hidden params
	
				// If there's another token, it's protocol version,
				// followed by HTTP headers. Ignore version but parse headers.
				// NOTE: this now forces header names uppercase since they are
				// case insensitive and vary by client.
				//HashMap<String,String> header = new HashMap<String,String>();
				if ( st.hasMoreTokens())
				{
					String line = readline(in);
					while ( line.trim().length() > 0 )
					{
						int p = line.indexOf( ':' );
						if(p==-1)
						{
							line = readline(in);
							continue;
						}
						String key = line.substring(0,p).trim().toLowerCase();
						String val = line.substring(p+1).trim();
						if(key.equalsIgnoreCase("Cookie"))
						{
							String[] pairs = val.split(";");
							for(int i=0;i<pairs.length;i++)
							{
								int ind = pairs[i].indexOf("=");
								int find = 0;
								if(i!=0)
									find = 1;
								String ck = pairs[i].substring(find,ind);
								String cv = pairs[i].substring(ind+1);
								cookies.put(ck, cv);
								System.out.println("cookie:"+ck+"="+cv);
							}
						}
						else
						{
							header.put( key, val );
						}
						line = readline(in);
					}
				}
	
				// If the method is POST, there may be parameters
				// in data section, too, read it:
				if ( method.equalsIgnoreCase( "POST" ))
				{
					long size = 0x7FFFFFFFFFFFFFFFl;
					String contentLength = header.get("content-length");
					if (contentLength != null)
					{
						try { size = Integer.parseInt(contentLength); }
						catch (NumberFormatException ex) {}
					}
					int maxSize = 512*1024*1024;
					if(size>maxSize)
					{
						System.out.println("Max upload file size exceeded");
						size = maxSize;
					}
	
					String contentType = header.get("content-type");
					
					if(contentType==null)
					{
						
					}
					else
					if(contentType.startsWith("multipart/form-data;"))
					{						
						System.out.println("multipart/form-data;");
						parms.put("multipart", null);
						
						saveMultipart("tempMultipartFiles");
					}
					else
					if(contentType.startsWith("application/octet-stream;"))
					{
						System.out.println("application/octet-stream;");
						//Don't parse as parameters
					}
					else
					{
						String postLine = "";
						byte buf[] = new byte[(int)size];
						int read = in.read(buf);
						while ( read >= 0 && size > 0 && !postLine.endsWith("\r\n") )
						{
							size -= read;
							postLine += new String(buf, "UTF-8");//String.valueOf(buf, 0, read);
							if ( size > 0 )
								read = in.read(buf);
						}
						postLine = postLine.trim();
						decodeParms( postLine, parms );
					}
				}
				winterface.queue(this);
			}
			else
			{
				sendResponse( r.status, r.location, r.mimeType, r.header, r.data, r.datacallback, r.datacallbackMetadata, r.range );
				in.close();
			}
		}
		catch ( IOException ioe )
		{
			try
			{
				sendError( WebResponse.HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
			}
			catch ( Throwable t ) {}
		}
		catch ( InterruptedException ie )
		{
			// Thrown by sendError, ignore and exit the thread.
		}
	}


	/**
	 * Decodes parameters in percent-encoded URI-format
	 * ( e.g. "name=Jack%20Daniels&pass=Single%20Malt" ) and
	 * adds them to given Parameters. NOTE: this doesn't support multiple
	 * identical keys due to the simplicity of Parameters -- if you need multiples,
	 * you might want to replace the Parameters with a Hastable of Vectors or such.
	 */
	private void decodeParms( String parms, HashMap<String,String> p )
		throws InterruptedException
	{
		if ( parms == null )
			return;

		StringTokenizer st = new StringTokenizer( parms, "&" );
		while ( st.hasMoreTokens())
		{
			String e = st.nextToken();
			int sep = e.indexOf( '=' );
			if ( sep >= 0 )
				p.put( WebSecurity.decodePercent( e.substring( 0, sep )).trim(),
					   WebSecurity.decodePercent( e.substring( sep+1 )));
		}
	}

	/**
	 * Returns an error message as a HTTP response and
	 * throws InterruptedException to stop further request processing.
	 */
	private void sendError( String status, String msg ) throws InterruptedException
	{
		sendResponse( status, null, WebResponse.MIME_PLAINTEXT, null, new ByteArrayInputStream( msg.getBytes() ), null, null, -1);
		throw new InterruptedException();
	}

	/**
	 * Sends given response to the socket.
	 */
	private void sendResponse( String status, String location, String mime, HashMap<String,String> header, InputStream data, OutputStreamCallback datacallback, Object outputStreamMetadata, long range )
	{
		try
		{
			if ( status == null )
				throw new Error( "sendResponse(): Status can't be null." );

			//System.out.println(status+" "+mime);
			OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter( out );
			pw.print("HTTP/1.0 " + status + " \r\n");

			if ( location != null )
				pw.print("Location: " + location + "\r\n");

			if ( mime != null )
				pw.print("Content-Type: " + mime + "\r\n");

			if ( header == null || header.get( "Date" ) == null )
				pw.print( "Date: " + gmtFrmt.format(new Date()) + "\r\n");

			if ( header != null )
			{
				Iterator<Map.Entry<String,String> > it = header.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
			        //System.out.println(pair.getKey() + " = " + pair.getValue());
					pw.print( pair.getKey() + ": " + pair.getValue() + "\r\n");
			        //it.remove();
			    }
			}

			pw.print("\r\n");
			pw.flush();

			if( datacallback != null)
			{
				datacallback.out(this,outputStreamMetadata,out);
			}
			else
			if ( data != null )
			{
				byte[] buff = new byte[2048];
				if(range==-1)
				{
					while (true)
					{
						int read = data.read( buff, 0, 2048 );
						if (read <= 0)
							break;
						out.write( buff, 0, read );
					}
				}
				else
				{
					long rem = range;
					while (true)
					{
						int read = data.read( buff, 0, 2048 );
						if (read <= 0)
							break;
						if(rem>=read)
						{
							out.write( buff, 0, read );
							rem -= read;
						}
						else
						{
							out.write( buff, 0, (int)rem );
							break;
						}
					}
					
				}
			}

			out.flush();
			out.close();
			
			if ( data != null )
				data.close();
			
			socket.close();
		}
		catch( IOException ioe )
		{
			// Couldn't write? No can do.
			try 
			{ 
				socket.close(); 
			} 
			catch( Throwable t ) 
			{
				
			}
		}
	}
	
	//*
	public void saveMultipart(String to)
	{
//		String test = TextImportExport.readline(in);
//		while(test!=null)
//		{
//			System.out.println(test);
//			test = TextImportExport.readline(in);
//		}

		String boundary = readline(in);
		if((boundary!=null)&&(boundary.length()>3))
		{
			boundary = boundary.substring(2);
			
			/*
			try
			{
				MultipartStream multipartStream = new MultipartStream(in, boundary.getBytes());
				boolean nextPart = true;//multipartStream.skipPreamble();
				 while(nextPart) 
				 {
					 String header = multipartStream.readHeaders();
					 int ind = header.indexOf("name=");
					 String name = null;
					 if(ind!=-1)
					 {
						 String rest = header.substring(ind+"name=\"".length());
						 ind = rest.indexOf("\"");
						 name = rest.substring(0,ind);
					 }
					 
					 ind = header.indexOf("filename=");
					 if(ind!=-1)
					 {
						 String rest = header.substring(ind+"filename=\"".length());
						 ind = rest.indexOf("\"");
						 String filename = rest.substring(0,ind);
						 
						 File root = new File(to);
						 File filenamepath = new File(to+"/"+filename);
						 //only allow saving into this temp folder
						 if(filenamepath.getAbsolutePath().startsWith(root.getAbsolutePath()))
						 {
							 ind = filename.lastIndexOf('.');
							 
							 String uniquename = filename;
							 long t = System.currentTimeMillis();
							 
							 if(ind!=-1)
							 {
								 String ending = filename.substring(ind+1);
								 
								 uniquename = filename.substring(0,ind);

								 uniquename += "_"+t+"."+ending;
							 }
							 else
							 {
								 uniquename += "_"+t;
							 }
							 // process headers
							 OutputStream output = new FileOutputStream(to+"/"+uniquename);
							 // create some output stream
							 multipartStream.readBodyData(output);
							 output.flush();
							 output.close();
							 
							 parms.put(name, to+"/"+uniquename);
						 }
						 else
						 {
							 multipartStream.discardBodyData();						 
						 }
					 }
					 else
					 {
						 if(name!=null)
						 {
							 ByteArrayOutputStream baos = new ByteArrayOutputStream(10*1024*1024);
							 multipartStream.readBodyData(baos);
							 String asString = new String(baos.toByteArray(), StandardCharsets.UTF_8);
							 parms.put(name, asString);
						 }
						 multipartStream.discardBodyData();						 
					 }
					 nextPart = multipartStream.readBoundary();
				 }
			 } 
			catch(Exception e) 
			 {
				System.out.println(e.toString());
			 // the stream failed to follow required syntax
			 }
			 */
		}
	}

    public static String readline(InputStream bis)
    {
    	boolean empty = true;
    	String result = "";
    	try
    	{
	    	while(true)
	    	{
	    		int c = bis.read();
	    		if(c=='\r')
	    		{
	    			//read a line feed
	    			bis.read();
	    			return result;
	    		}
	    		else
	    		if(c=='\n')
	    		{	    			
		    		return result;
	    		}
	    		else
	    		if(c==-1)
	    		{
	    			
	    		}
	    		else
	    		{
	    			empty = false;
	    			result += (char)c;
	    		}
	    		if(c==-1)
	    		{
	    			if(empty)
	    				return null;
	    			else
	    				return result;
	    		}
	    	}
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.toString());
    	}
    	return null;
    }
	
}
