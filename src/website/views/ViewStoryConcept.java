package website.views;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

import utils.FileStoreInterface;
import utils.GeneralMatrixString;
import web.WebRequest;

public class ViewStoryConcept extends DynamicWebPage
{
	public ViewStoryConcept(FileStoreInterface fs)
	{
		super(fs);
	}
	public boolean process(WebRequest toProcess)
	{
        if(toProcess.path.startsWith("ViewStoryConcept.html"))
        {
        	return true;
        }
        return false;
	}
	
	public void out(WebRequest r,Object metadata,OutputStream s)
	{
		PrintWriter pw = new PrintWriter( s );
		int ind = r.path.indexOf("/");
		String concept = r.path.substring(ind+1);
		//list the concepts
		pw.append("<h1>"+concept+"</h1>");

		pw.append("<h2>Create Instance</h2>");

		//be able to create a new instance of a pose model
		pw.append("<form action='/EditStoryInstance.scgi/createInstance' method='get' role='form'>\n");
		pw.append("<input name=\"concept\" type=\"hidden\" value=\""+concept+"\">");

		pw.append("<input name=\"name\" type=\"txt\" value=\"instance_name\">");
		
		pw.append("<button class=\"btn btn-default\" type='submit'>Create</button>");

		pw.append("</form>");

		pw.append("<h2>Instances</h2>");
		//the default taxonomy
		//view all the maps, objects etc. as lists
		File instsdir = new File("Concepts/"+concept+"/Instances");
		String[] insts = instsdir.list();
		if(insts!=null)
		{
			for(int i=0;i<insts.length;i++)
			{
				String inst = insts[i];
				if(inst.startsWith(".")||inst.endsWith(".md"))
				{
					continue;
				}
				
				pw.append("<a href=\"/EditStoryInstance.html/"+concept+"/"+inst+"\">"+inst+"</a><br>");
			}
		}
		pw.flush();
	}
}