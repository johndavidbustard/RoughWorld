package website.views;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import utils.FileStoreInterface;
import utils.GeneralMatrixString;
import web.WebRequest;

public class InternalIndex extends DynamicWebPage
{
	public InternalIndex(FileStoreInterface fs)
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
	
	public static void printTaxonomy(PrintWriter pw,String path,int depth)
	{
		File taxdir = new File(path);
		String[] taxs = taxdir.list();
		for(int i=0;i<taxs.length;i++)
		{
			String t = taxs[i];
			if(t.startsWith(".")||t.endsWith(".md"))
			{
				continue;
			}
			
			if(t.startsWith("map_"))
			{
				pw.append("<a href=\"/ViewMapConcept.html/"+t+"\">"+t+"</a><br>");
			}
			else
			if(t.startsWith("room_"))
			{
				pw.append("<a href=\"/ViewRoomConcept.html/"+t+"\">"+t+"</a><br>");
			}
			else
			if(t.startsWith("object_"))
			{
				pw.append("<a href=\"/ViewObjectConcept.html/"+t+"\">"+t+"</a><br>");
			}
			else
			{
				pw.append("<h"+depth+">"+t+"</h"+depth+">");

				printTaxonomy(pw, path+"/"+t, depth+1);
			}
		}
	}
	
	public void out(WebRequest r,Object metadata,OutputStream s)
	{
		PrintWriter pw = new PrintWriter( s );

		//list the concepts
		pw.append("<h1>Rough World</h1>");

		//the default taxonomy
		//view all the maps, objects etc. as lists
		File taxdir = new File("Taxonomies");
		String[] taxs = taxdir.list();
		for(int i=0;i<taxs.length;i++)
		{
			String t = taxs[i];
			if(t.startsWith(".")||t.endsWith(".md"))
			{
				continue;
			}
			
			pw.append("<h2>"+t+"</h2>");
			printTaxonomy(pw, "Taxonomies/"+t,3);
		}
		
		pw.flush();
	}
}
