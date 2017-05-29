package web;
import java.net.URLEncoder;
import java.util.StringTokenizer;


public class WebSecurity 
{
	/**
	 * URL-encodes everything between "/"-characters.
	 * Encodes spaces as '%20' instead of '+'.
	 */
	public static String encodeUri( String uri )
	{
		String newUri = "";
		StringTokenizer st = new StringTokenizer( uri, "/ ", true );
		while ( st.hasMoreTokens())
		{
			String tok = st.nextToken();
			if ( tok.equals( "/" ))
				newUri += "/";
			else if ( tok.equals( " " ))
				newUri += "%20";
			else
			{
				newUri += URLEncoder.encode( tok );
				// For Java 1.4 you'll want to use this instead:
				// try { newUri += URLEncoder.encode( tok, "UTF-8" ); } catch ( UnsupportedEncodingException uee )
			}
		}
		return newUri;
	}

	/**
	 * Decodes the percent encoding scheme. <br/>
	 * For example: "an+example%20string" -> "an example string"
	 */
	public static String decodePercent( String str )
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			for( int i=0; i<str.length(); i++ )
			{
			    char c = str.charAt( i );
			    switch ( c )
				{
			        case '+':
			            sb.append( ' ' );
			            break;
			        case '%':
		                sb.append((char)Integer.parseInt( str.substring(i+1,i+3), 16 ));
			            i += 2;
			            break;
			        default:
			            sb.append( c );
			            break;
			    }
			}
			return new String( sb.toString().getBytes());
		}
		catch( Exception e )
		{
			return null;
		}
	}

}
