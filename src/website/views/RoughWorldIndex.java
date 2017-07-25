package website.views;

import java.io.OutputStream;
import java.io.PrintWriter;

import utils.FileStoreInterface;
import web.WebRequest;

public class RoughWorldIndex extends DynamicWebPage
{
	public RoughWorldIndex(FileStoreInterface fs)
	{
		super(fs);
	}
	public boolean process(WebRequest toProcess)
	{
        if(toProcess.path.equalsIgnoreCase("Index.html"))
        {
        	return true;
        }
        return false;
	}
	
	public void out(WebRequest r,Object metadata,OutputStream s)
	{
		PrintWriter pw = new PrintWriter( s );
		pw.append("Hello World");
		pw.flush();
	}
}
