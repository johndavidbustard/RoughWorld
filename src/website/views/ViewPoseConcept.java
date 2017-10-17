package website.views;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

import utils.FileStoreInterface;
import web.WebRequest;

public class ViewPoseConcept extends DynamicWebPage
{
	public ViewPoseConcept(FileStoreInterface fs)
	{
		super(fs);
	}
	
	public boolean process(WebRequest toProcess)
	{
        if(toProcess.path.startsWith("ViewPoseConcept.html"))
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

		pw.append("<h2>Instances</h2>");
		//the default taxonomy
		//view all the maps, objects etc. as lists
		File instsdir = new File("Concepts/"+concept+"/instances");
		if(instsdir.exists())
		{
			String[] insts = instsdir.list();
			for(int i=0;i<insts.length;i++)
			{
				String inst = insts[i];
				if(inst.startsWith(".")||inst.endsWith(".md"))
				{
					continue;
				}
				
				pw.append("<a href=\"/EditPoseInstance.html/"+concept+"/"+inst+"\">"+inst+"</a><br>");
			}
		}
		
		pw.append("<h2>Models</h2>");
		//the default taxonomy
		//view all the maps, objects etc. as lists
		File modelsdir = new File("Concepts/"+concept+"/models");
		if(modelsdir.exists())
		{
			String[] insts = modelsdir.list();
			for(int i=0;i<insts.length;i++)
			{
				String inst = insts[i];
				if(inst.startsWith(".")||inst.endsWith(".md"))
				{
					continue;
				}
				
				pw.append("<a href=\"/EditPoseModel.html/"+concept+"/"+inst+"\">"+inst+"</a><br>");
			}
		}
		pw.flush();
	}
}