package web;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class WebInterfaceSSL extends WebInterface
{
    private SSLServerSocketFactory sslServerSocketFactory;
    private String[] sslProtocols;

    public ServerSocket createSocket() 
    {
        SSLServerSocket ss = null;
        try
        {
	        ss = (SSLServerSocket) this.sslServerSocketFactory.createServerSocket(port);
	        if (this.sslProtocols != null) {
	            ss.setEnabledProtocols(this.sslProtocols);
	        } else {
	            ss.setEnabledProtocols(ss.getSupportedProtocols());
	        }
	        ss.setUseClientMode(false);
	        ss.setWantClientAuth(false);
	        ss.setNeedClientAuth(false);
        }
        catch(Exception e)
        {
        	System.out.println(e.toString());
        }
        return ss;
    }

    public WebInterfaceSSL(int port)
	{
    	super(port);
   	}

    public static SSLServerSocketFactory makeSSLSocketFactory(KeyStore loadedKeyStore, KeyManager[] keyManagers)
    {
        SSLServerSocketFactory res = null;
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(loadedKeyStore);
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(keyManagers, trustManagerFactory.getTrustManagers(), null);
            res = ctx.getServerSocketFactory();
        } 
        catch (Exception e) 
        {
        	System.out.println(e.toString());
            //throw new IOException(e.getMessage());
        }
        return res;
    }
    /**
     * Creates an SSLSocketFactory for HTTPS. Pass a loaded KeyStore and a
     * loaded KeyManagerFactory. These objects must properly loaded/initialized
     * by the caller.
     */
    public static SSLServerSocketFactory makeSSLSocketFactory(KeyStore loadedKeyStore, KeyManagerFactory loadedKeyFactory) throws IOException {
        try {
            return makeSSLSocketFactory(loadedKeyStore, loadedKeyFactory.getKeyManagers());
        } catch (Exception e) {
        	System.out.println(e.toString());
            //throw new IOException(e.getMessage());
        }
        return null;
    }

    /**
     * Creates an SSLSocketFactory for HTTPS. Pass a KeyStore resource with your
     * certificate and passphrase
     */
    public static SSLServerSocketFactory makeSSLSocketFactory(String keyAndTrustStoreClasspathPath, char[] passphrase)
    {
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            File keystrorefile = new File(keyAndTrustStoreClasspathPath);
            System.out.println(keystrorefile.getAbsolutePath());
            InputStream keystoreStream = new FileInputStream(keystrorefile);//NanoHTTPD.class.getResourceAsStream(keyAndTrustStoreClasspathPath);

//            if (keystoreStream == null) 
//            {
//            	System.out.println("Unable to load keystore from classpath: " + keyAndTrustStoreClasspathPath);
//                //throw new IOException("Unable to load keystore from classpath: " + keyAndTrustStoreClasspathPath);
//            	return null;
//            }

            keystore.load(keystoreStream, passphrase);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, passphrase);
            return makeSSLSocketFactory(keystore, keyManagerFactory);
        } catch (Exception e) {
        	System.out.println(e.toString());
            //throw new IOException(e.getMessage());
        }
        return null;
    }

	public synchronized void getQueue(ArrayList<WebRequest> q)
	{
	    q.addAll(queue);
	    queue.clear();
	}
	public synchronized void queue(WebRequest e)
	{
		queue.add(e);
	}
	
	public void run()
	{
        File f =new File("keystore.jks");
        System.out.println(f.getAbsolutePath());
    	System.setProperty("javax.net.ssl.trustStore", f.getAbsolutePath());
    	
    	sslServerSocketFactory = makeSSLSocketFactory(f.getAbsolutePath(), "csc1009".toCharArray());

    	try
		{
			final ServerSocket ss = createSocket();
			while( true )
				new WebRequest( this, ss.accept() );
		}
		catch ( IOException ioe )
		{}
	}
	

}
