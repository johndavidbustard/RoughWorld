package web;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.security.cert.X509Certificate;

public class WebClientRequest implements Runnable 
{
	boolean secure = false;
	WebClient wclient;
	String path;
	String parameters;
	File[] fileuploads = null;

	public WebClientRequest(WebClient wclient,String path,String params,boolean secure)
	{
		this.secure = secure;
		this.path = path;
		this.parameters = params;
		Thread t = new Thread( this );
		t.setDaemon( true );
		t.start();
	}
	
	public WebClientRequest(WebClient wclient,String path,String params)
	{
		this.wclient = wclient;
		this.path = path;
		this.parameters = params;
		Thread t = new Thread( this );
		t.setDaemon( true );
		t.start();
	}

	public WebClientRequest(WebClient wclient,String path,String params,File fileupload)
	{
		this.wclient = wclient;
		this.path = path;
		this.parameters = params;
		this.fileuploads = new File[1];
		this.fileuploads[0] = fileupload;
		Thread t = new Thread( this );
		t.setDaemon( true );
		t.start();
	}

	public WebClientRequest(WebClient wclient,String path,String params,File[] fileupload)
	{
		this.wclient = wclient;
		this.path = path;
		this.parameters = params;
		this.fileuploads = fileupload;
		Thread t = new Thread( this );
		t.setDaemon( true );
		t.start();
	}

	protected String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
        buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
   }

	private final static char[] MULTIPART_CHARS =
        "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
             .toCharArray();
	
	public void run()
	{
		try
		{
			if(fileuploads!=null)
			{
				createMultipartPost();
			}
			else
			{
				URL url = new URL(path);
				
				HttpURLConnection con = null;
				
				if(secure)
				{
					SSLSocketFactory sslsocketfactory = (SSLSocketFactory) HttpsURLConnection.getDefaultSSLSocketFactory();
					HttpsURLConnection cons = (HttpsURLConnection)url.openConnection();
					cons.setSSLSocketFactory(sslsocketfactory);	
					con = cons;
				}
				else
				{
					//try
					{
						con = (HttpURLConnection)url.openConnection();
					}
//					catch(Exception e)
					{
	//					aa
					}
				}
				
				final String USER_AGENT = "Mozilla/5.0";
				//add reuqest header
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		 
				String urlParameters = parameters;
		 
				// Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();

				//int responseCode = con.getResponseCode();
				System.out.println("\nSending 'POST' request to URL : " + url);
				System.out.println("Post parameters : " + urlParameters);
				//System.out.println("Response Code : " + responseCode);

				String line;
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
				ArrayList<String> result = new ArrayList<String>();
				while ((line = reader.readLine()) != null) 
				{
					result.add(line);
				}
				
				wclient.queue(result);
				
				reader.close();  
				
				con.disconnect();
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	public void createMultipartPost() 
	{
        String boundary = generateBoundary();
		
	    String lineEnd = "\r\n";
	    String twoHyphens = "--";

	    // generating byte[] boundary here
	    HttpURLConnection conn = null;
	    DataOutputStream outputStream = null;
	    DataInputStream inputStream = null; 

	    int bytesRead, bytesAvailable, bufferSize;
	    byte[] buffer;
	    int maxBufferSize = 1*1024*1024;

	    try
	    {
			URL url = new URL(path);
	        conn = (HttpURLConnection)url.openConnection();
	        conn.setConnectTimeout(30000);
	        conn.setReadTimeout(30000);
	        conn.setDoOutput(true);
	        conn.setDoInput(true);         
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);            

	        String stringForLength = new String();  

	        stringForLength += twoHyphens + boundary + lineEnd;
	        stringForLength += "Content-Disposition: form-data; name=\"params\"" + lineEnd;
	        stringForLength += "Content-Type: text/plain;charset=UTF-8" + lineEnd + "Content-Length: " + parameters.length() + lineEnd + lineEnd;
	        stringForLength += parameters + lineEnd;

	        long flen = 0;
	        if(fileuploads!=null)
	        {
		        for(int i=0;i<fileuploads.length;i++)
		        {
			        File file = fileuploads[i];
			        flen += file.length();
			        
			        stringForLength += twoHyphens + boundary + lineEnd + "Content-Disposition: form-data; filename=\""+file.getName()+"\"" + lineEnd;
			        stringForLength += "Content-Type: application/octet-stream" + lineEnd + "Content-Length: " + file.length() + lineEnd + lineEnd;
		        }
	        }
	        stringForLength += lineEnd + twoHyphens + boundary + twoHyphens + lineEnd;
	        
	        int totalLength = stringForLength.getBytes().length + (int)flen;
	        conn.setFixedLengthStreamingMode(totalLength); 

	        outputStream = new DataOutputStream( conn.getOutputStream() );          
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);

	        outputStream.writeBytes("Content-Disposition: form-data; name=\"params\"" + lineEnd);
	        outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
	        outputStream.writeBytes("Content-Length: " + parameters.length() + lineEnd);
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(parameters + lineEnd);
	        // image
	        if(fileuploads!=null)
	        {
		        for(int i=0;i<fileuploads.length;i++)
		        {
			        File file = fileuploads[i];//new File(imagePath);
			        FileInputStream fileInputStream = new FileInputStream(file);
	
			        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			        outputStream.writeBytes("Content-Disposition: form-data; filename=\""+file.getName()+"\"" + lineEnd);
			        outputStream.writeBytes("Content-Type: application/octet-stream" + lineEnd);
			        outputStream.writeBytes("Content-Length: " + file.length() + lineEnd);
			        outputStream.writeBytes(lineEnd);           
		
			        bytesAvailable = fileInputStream.available();
			        bufferSize = Math.min(bytesAvailable, maxBufferSize);
			        buffer = new byte[bufferSize];
			        // Read file
			        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		
			        while (bytesRead > 0)
			        {
				        outputStream.write(buffer, 0, bufferSize);          
				        bytesAvailable = fileInputStream.available();
				        bufferSize = Math.min(bytesAvailable, maxBufferSize);
				        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			        }
	
			        fileInputStream.close();
		        }
	        }	        
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

	        outputStream.flush();
	        outputStream.close();

			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	
			ArrayList<String> result = new ArrayList<String>();
			while ((line = reader.readLine()) != null) 
			{
				result.add(line);
			}
			
			wclient.queue(result);
			
			reader.close();  		

			conn.disconnect();
	    } 
	    catch(Exception e)
	    {
	    	System.out.println(e.toString());
	    } 
	}
}
