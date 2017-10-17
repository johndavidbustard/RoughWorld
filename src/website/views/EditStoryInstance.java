package website.views;

import java.io.OutputStream;
import java.io.PrintWriter;

import tools.visualisation.state_visualisation.ToSVG.StateToSVG;
import utils.ArrayUtils;
import utils.FileStoreInterface;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;
import utils.shapes.ParametricShape;
import web.WebRequest;
import web.WebResponse;
import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import ai.knowledge_representation.state_instance.ObjectPhysicalInstance;
import ai.knowledge_representation.story_instance.StoryInstance;
import ai.knowledge_representation.story_instance.changes.ChangePose;
import ai.knowledge_representation.story_instance.changes.ChangeState;
import ai.model_construction.state_model.PoseLanguageModel;
import ai.model_construction.state_model.PosePhysicalModel;

public class EditStoryInstance extends DynamicWebPage
{
	public EditStoryInstance(FileStoreInterface fs)
	{
		super(fs);
	}
	public boolean process(WebRequest toProcess)
	{
        if(toProcess.path.startsWith("EditStoryInstance.html"))
        {
        	return true;
        }
        else
        if(
        		(toProcess.path.startsWith("EditStoryInstance.scgi"))||
        		(toProcess.path.startsWith("EditStoryInstance.scgi"))
        		)
    {
    		int ind = toProcess.path.indexOf("/");
    		String function = toProcess.path.substring(ind+1);

    		StoryInstance o = null;
    		if(function.equalsIgnoreCase("createInstance"))
    		{
    			String concept = toProcess.parms.get("concept");
    			String instance = toProcess.parms.get("name");
    			o = new StoryInstance();
    			o.uniqueID = concept+"/"+instance;
    			o.name = instance;
    			o.type = "pose";
    			
        		String instancepath = "Concepts/"+concept+"/instances/"+instance;
    			fs.createDirectory("Concepts/"+concept+"/instances/");
    			fs.createDirectory("Concepts/"+concept+"/instances/"+instance);
        		String datapath = instancepath+"/data.txt";
        		StoryInstance.saveObject(o, fs, datapath);
    		}
    		
    		String result = "<a href='/EditStoryInstance.html/"+o.uniqueID+"'>"+o.uniqueID+"</a>";
    		
			toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML,result);
			return true;
    }
        else
            if(
            		(toProcess.path.startsWith("EditStoryInstance.cgi"))||
            		(toProcess.path.startsWith("EditStoryInstance.cgi"))
            		)
        {
    		String id = toProcess.parms.get("instance");
    		int ind = id.indexOf("/");
    		
    		String concept = id.substring(0,ind);
    		String instance = id.substring(ind+1);

    		String instancepath = "Concepts/"+concept+"/instances/"+instance;    		
    		String datapath = instancepath+"/data.txt";

    		StoryInstance o = StoryInstance.loadObject(fs, datapath);
    		if(o==null)
    		{
    			o = new StoryInstance();
    			o.uniqueID = concept+"/"+instance;
    			o.name = instance;
    			o.type = "activity";
       		}

    		ind = toProcess.path.indexOf("/");
    		String function = toProcess.path.substring(ind+1);

    		if(function.equalsIgnoreCase("addMap"))
    		{
    			String mapname = toProcess.parms.get("map");
    			o.objects = ArrayUtils.add(o.objects, mapname);
    			if(o.initialRootObject==null)
    				o.initialRootObject = mapname;
    		}
    		else
    		if(function.equalsIgnoreCase("addCharacter"))
    		{
    			String charname = toProcess.parms.get("character");
    			o.objects = ArrayUtils.add(o.objects, charname);
    		}
    		else
    		if(function.equalsIgnoreCase("addPose"))
    		{
    			String posename = toProcess.parms.get("pose");
    			o.poses = ArrayUtils.add(o.poses, posename);
    		}
    		else
    		if(function.equalsIgnoreCase("addChange"))
    		{
    			String time = toProcess.parms.get("time");
    			String change = toProcess.parms.get("change");
    			
    			ChangeState cs = ChangeState.get(change);
    			double t = Double.parseDouble(time);
    			o.changes = ChangeState.add(o.changes, cs);
    			o.timeOfChange = ArrayUtils.add(o.timeOfChange, t);
    		}
    		else
    		if(function.equalsIgnoreCase("setChangeParameters"))
    		{
    			String cinds = toProcess.parms.get("cinstance");
    			int ci = Integer.parseInt(cinds);
    			
    			ChangeState cs = o.changes[ci];
    			
    			String[] pnames = cs.getParameterNames();
    			String[] pcatnames = cs.getCategoryParameterNames();
    			String[] changeenames = cs.getChangeeNames();
    			String[] posenames = cs.getPoseNames();
    			
    			for(int i=0;i<pnames.length;i++)
    			{
    				String pval = toProcess.parms.get(pnames[i]);
    				double v = Double.parseDouble(pval);
    				cs.parameters[i] = v;
    			}
    			for(int i=0;i<pcatnames.length;i++)
    			{
    				String pval = toProcess.parms.get(pcatnames[i]);
    				int v = Integer.parseInt(pval);
    				cs.category_parameters[i] = v;
    			}    			
    			for(int i=0;i<changeenames.length;i++)
    			{
    				String pval = toProcess.parms.get(changeenames[i]);
    				cs.changeePaths[i] = pval;
    			}
    			for(int i=0;i<posenames.length;i++)
    			{
    				String pval = toProcess.parms.get(posenames[i]);
    				cs.posePaths[i] = pval;
    			}
    		}
    		
    		//EditUtils.processFunction(toProcess, function, o, instance, fs);

			toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML,"Success");

			StoryInstance.saveObject(o, fs, datapath);

    		return true;
        }
       
        return false;
	}
	

	public void loadContainedObjects(ObjectLanguageInstance o,GeneralMatrixObject objects, GeneralMatrixString objectpaths)
	{
		for(int i=0;i<o.contains.length;i++)
		{
			String cid = o.contains[i];
			String cpath = ObjectLanguageInstance.uniqueIdToPath(cid);
			ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, cpath+"/data.txt");
			objects.push_back(c);
			objectpaths.push_back(cid);
			loadContainedObjects(c, objects, objectpaths);
		}
	}
	
	public void printChange(PrintWriter pw,String oid,ChangeState cs,int cind,
			GeneralMatrixString root,GeneralMatrixString objectpaths,GeneralMatrixObject objects,GeneralMatrixString posepaths,GeneralMatrixObject poses)
	{
		pw.append(cs.getClass().getSimpleName()+"<br>");

		pw.append("<form action='/EditStoryInstance.cgi/setChangeParameters' method='get' role='form'>\n");
		pw.append("<input name=\"instance\" type=\"hidden\" value=\""+oid+"\">");
		pw.append("<input name=\"cinstance\" type=\"hidden\" value=\""+cind+"\">");
		String[] pnames = cs.getParameterNames();
		String[] pcatnames = cs.getCategoryParameterNames();
		String[][] pcatvalues = cs.getCategoryParameterCatgegories();
		String[] changeenames = cs.getChangeeNames();
		String[] posenames = cs.getPoseNames();
		
		pw.append("<table>");
		for(int i=0;i<pnames.length;i++)
		{
			pw.append("<tr>");
			pw.append("<td>");
			pw.append(pnames[i]);
			pw.append("</td>");
			pw.append("<td>");
			pw.append("<input name=\""+pnames[i]+"\" type=\"number\" step=\"0.001\" value=\""+String.format( "%.3f", cs.parameters[i])+"\">");
			
			pw.append("</td>");
			pw.append("</tr>");
		}
		for(int i=0;i<pcatnames.length;i++)
		{
			pw.append("<tr>");
			pw.append("<td>");
			pw.append(pcatnames[i]);
			pw.append("</td>");
			pw.append("<td>");

			pw.append("<select name=\""+pcatnames[i]+"\">");

			String[] cvals = pcatvalues[i];
			for(int si=0;si<cvals.length;si++)
			{
				if(cs.category_parameters[i]==si)
					pw.append("<option value=\""+si+"\" selected>"+cvals[si]+"</option>");
				else
					pw.append("<option value=\""+si+"\">"+cvals[si]+"</option>");
			}
			
			pw.append("</select>");
			
			pw.append("</td>");
			pw.append("</tr>");
		}

		for(int i=0;i<changeenames.length;i++)
		{
			pw.append("<tr>");
			pw.append("<td>");
			pw.append(changeenames[i]);
			pw.append("</td>");
			pw.append("<td>");

			pw.append("<select name=\""+changeenames[i]+"\">");

			for(int si=0;si<objectpaths.height;si++)
			{
				if((cs.changeePaths[i]!=null)&&(objectpaths.value[si].equalsIgnoreCase(cs.changeePaths[i])))
					pw.append("<option value=\""+objectpaths.value[si]+"\" selected>"+objectpaths.value[si]+"</option>");
				else
					pw.append("<option value=\""+objectpaths.value[si]+"\">"+objectpaths.value[si]+"</option>");
			}
			
			pw.append("</select>");
			
			pw.append("</td>");
			pw.append("</tr>");
		}

		for(int i=0;i<posenames.length;i++)
		{
			pw.append("<tr>");
			pw.append("<td>");
			pw.append(posenames[i]);
			pw.append("</td>");
			pw.append("<td>");

			pw.append("<select name=\""+posenames[i]+"\">");

			for(int si=0;si<posepaths.height;si++)
			{
				if((cs.posePaths[i]!=null)&&(posepaths.value[si].equalsIgnoreCase(cs.posePaths[i])))
					pw.append("<option value=\""+posepaths.value[si]+"\" selected>"+posepaths.value[si]+"</option>");
				else
					pw.append("<option value=\""+posepaths.value[si]+"\">"+posepaths.value[si]+"</option>");
			}
			
			pw.append("</select>");
			
			pw.append("</td>");
			pw.append("</tr>");
		}
		pw.append("</table>");
		pw.append("<button class=\"btn btn-default\" type='submit'>Set</button>");
		pw.append("</form>");
	}
	
	public void out(WebRequest r,Object metadata,OutputStream s)
	{
		PrintWriter pw = new PrintWriter( s );
		int ind = r.path.indexOf("/");
		String concept = r.path.substring(ind+1);
		String thisid = ""+concept;
		ind = concept.indexOf("/");
		String instance = concept.substring(ind+1);
		concept = concept.substring(0,ind);

		String instancepath = "Concepts/"+concept+"/instances/"+instance;
		
		String datapath = instancepath+"/data.txt";
		
		StoryInstance o = StoryInstance.loadObject(fs, datapath);
		if(o==null)
		{
			o = new StoryInstance();
			o.uniqueID = concept+"/"+instance;
			o.name = instance;
			o.type = "map";
			
			StoryInstance.saveObject(o, fs, datapath);
		}
		
//		{
//			o.changes = ChangeState.remove(o.changes,o.changes.length-1,1);
//			o.timeOfChange = ArrayUtils.remove(o.timeOfChange, o.timeOfChange.length-1, 1);
//			StoryInstance.saveObject(o, fs, datapath);
//		}
		
		//load up the objects and pose models
		//go through the changes until the target time

		GeneralMatrixObject objects = new GeneralMatrixObject(1);
		GeneralMatrixString objectpaths = new GeneralMatrixString(1);
		GeneralMatrixObject poses = new GeneralMatrixObject(1);
		GeneralMatrixString posepaths = new GeneralMatrixString(1);

		//load up all the objects and poses and put in the lookup table
		for(int i=0;i<o.objects.length;i++)
		{
			String oid = o.objects[i];
			String opath = ObjectLanguageInstance.uniqueIdToPath(oid);
			ObjectLanguageInstance oi = ObjectLanguageInstance.loadObject(fs, opath+"/data.txt");
			objects.push_back(oi);
			objectpaths.push_back(oid);
			
			//remove the within information for the object (so that it can be placed)
			oi.within = new String[]{};
			oi.within_type = new String[]{};
			oi.physicalRepresentation.within_metric_transform = new double[]{};
			
			loadContainedObjects(oi,objects,objectpaths);
		}
		
		for(int i=0;i<o.poses.length;i++)
		{
			String pid = o.poses[i];
			String ppath = PoseLanguageModel.uniqueIdToPath(pid);
			PoseLanguageModel pi = PoseLanguageModel.loadObject(fs, ppath+"/data.txt");
			poses.push_back(pi);
			posepaths.push_back(pid);
		}
		
		pw.append("<h1>"+concept+"/"+instance+"</h1>");

		pw.append("<h2>Maps</h2>");
		for(int i=0;i<o.objects.length;i++)
		{
			String obid = o.objects[i];
			if(obid.startsWith("map"))
				pw.append(o.objects[i]+"<br>");
		}

		pw.append("<h3>Add Map</h3>");
		GeneralMatrixString mapconcepts = new GeneralMatrixString(1);
		EditUtils.getConcepts("map", mapconcepts);

		for(int mi=0;mi<mapconcepts.height;mi++)
		{
			GeneralMatrixString instances = new GeneralMatrixString(1);
			EditUtils.findInstances(mapconcepts.value[mi], instances);

			if(instances.height==0)
				continue;
			
			pw.append("<h4>"+mapconcepts.value[mi]+"</h4>");

			pw.append("<form action='/EditStoryInstance.cgi/addMap' method='get' role='form'>\n");
			pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
			
			pw.append("<select name=\"map\">");


			for(int si=0;si<instances.height;si++)
			{
				if((o.initialRootObject!=null)&&(o.initialRootObject.equalsIgnoreCase(instances.value[si])))
					pw.append("<option value=\""+instances.value[si]+"\" selected>"+instances.value[si]+"</option>");
				else
					pw.append("<option value=\""+instances.value[si]+"\">"+instances.value[si]+"</option>");
			}
			
			pw.append("</select>");
			
			pw.append("<button class=\"btn btn-default\" type='submit'>Add Map</button>");

			pw.append("</form>");

		}

		pw.append("<h2>Characters</h2>");
		for(int i=0;i<o.objects.length;i++)
		{
			String obid = o.objects[i];
			if(obid.startsWith("character"))
				pw.append(o.objects[i]+"<br>");
		}

		pw.append("<h3>Add Character</h3>");
		GeneralMatrixString charconcepts = new GeneralMatrixString(1);
		EditUtils.getConcepts("character", charconcepts);

		for(int mi=0;mi<charconcepts.height;mi++)
		{
			GeneralMatrixString instances = new GeneralMatrixString(1);
			EditUtils.findInstances(charconcepts.value[mi], instances);

			if(instances.height==0)
				continue;
			
			pw.append("<h4>"+charconcepts.value[mi]+"</h4>");

			pw.append("<form action='/EditStoryInstance.cgi/addCharacter' method='get' role='form'>\n");
			pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
			
			pw.append("<select name=\"character\">");


			for(int si=0;si<instances.height;si++)
			{
//				int charind = GeneralMatrixString.find(o.objects,instances.value[si]);
//				if(charind!=-1)
//					pw.append("<option value=\""+instances.value[si]+"\" selected>"+instances.value[si]+"</option>");
//				else
					pw.append("<option value=\""+instances.value[si]+"\">"+instances.value[si]+"</option>");
			}
			
			pw.append("</select>");
			
			pw.append("<button class=\"btn btn-default\" type='submit'>Add Character</button>");

			pw.append("</form>");

		}

		pw.append("<h2>Poses</h2>");
		for(int i=0;i<o.poses.length;i++)
		{
			pw.append(o.poses[i]+"<br>");
		}
		
		pw.append("<h3>Add Pose</h3>");
		GeneralMatrixString poseconcepts = new GeneralMatrixString(1);
		EditUtils.getConcepts("pose", poseconcepts);

		for(int mi=0;mi<poseconcepts.height;mi++)
		{
			GeneralMatrixString models = new GeneralMatrixString(1);
			EditUtils.findModels(poseconcepts.value[mi], models);

			if(models.height==0)
				continue;
			
			pw.append("<h4>"+poseconcepts.value[mi]+"</h4>");

			pw.append("<form action='/EditStoryInstance.cgi/addPose' method='get' role='form'>\n");
			pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
			
			pw.append("<select name=\"pose\">");


			for(int si=0;si<models.height;si++)
			{
//				int charind = GeneralMatrixString.find(o.objects,instances.value[si]);
//				if(charind!=-1)
//					pw.append("<option value=\""+instances.value[si]+"\" selected>"+instances.value[si]+"</option>");
//				else
					pw.append("<option value=\""+models.value[si]+"\">"+models.value[si]+"</option>");
			}
			
			pw.append("</select>");
			
			pw.append("<button class=\"btn btn-default\" type='submit'>Add Pose</button>");

			pw.append("</form>");

		}
		
		pw.append("<h2>Changes</h2>");
		
		GeneralMatrixString root = new GeneralMatrixString(o.initialRootObject);

		double time = 0.0;
		pw.append("<h3>"+time+"</h3>");
		for(int i=0;i<o.changes.length;i++)
		{
			ChangeState cid = o.changes[i];
			double ctime = o.timeOfChange[i];
			
			if(ctime>time)
			{
				time = ctime;
				pw.append("<h3>"+time+"</h3>");
			}
			printChange(pw,o.uniqueID,cid,i,
					root, objectpaths, objects, posepaths, poses);
		}

		//add change
		pw.append("<h3>Add Change</h3>");

		pw.append("<form action='/EditStoryInstance.cgi/addChange' method='get' role='form'>\n");
		pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
		
		pw.append("<table>");
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Time:");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"time\" type=\"number\" step=\"0.001\" value=\""+String.format( "%.3f", time)+"\">");
		pw.append("</td>");
		pw.append("</tr>");
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Change:");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<select name=\"change\">");

		String[] changeNames = ChangeState.changeNames;
		for(int si=0;si<changeNames.length;si++)
		{
//			int charind = GeneralMatrixString.find(o.objects,instances.value[si]);
//			if(charind!=-1)
//				pw.append("<option value=\""+instances.value[si]+"\" selected>"+instances.value[si]+"</option>");
//			else
				pw.append("<option value=\""+changeNames[si]+"\">"+changeNames[si]+"</option>");
		}
		
		pw.append("</select>");
		pw.append("</td>");
		pw.append("</tr>");
		pw.append("</table>");
		
		pw.append("<button class=\"btn btn-default\" type='submit'>Add Change</button>");

		pw.append("</form>");		
		if(o.initialRootObject==null)
		{
		}
		else
		//if(o.initialRootObject!=null)
		{
		
		String times = r.parms.get("time");
		time = 0.0;
		if(times!=null)
		{
			time = Double.parseDouble(times);
		}
		for(int i=0;i<o.changes.length;i++)
		{
			double ct = o.timeOfChange[i];
			if(ct>time)
			{
				break;
			}
			
			ChangeState cs = o.changes[i];
//			if(cs instanceof ChangePose)
//			{
//				cs.posePaths = new String[]{null};
//				StoryInstance.saveObject(o, fs, datapath);
//			}
			cs.change(root, objectpaths, objects, posepaths, poses);
		}
		
		pw.append("<form action='/EditStoryInstance.html/"+thisid+"' method='get' role='form'>\n");

		pw.append("<table>");
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Time:");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"time\" type=\"number\" step=\"0.001\" value=\""+String.format( "%.3f", time)+"\">");
		pw.append("</td>");
		pw.append("</tr>");
		pw.append("</table>");
		
		pw.append("<button class=\"btn btn-default\" type='submit'>At time</button>");

		pw.append("</form>");		

		
		int rootind = objectpaths.find(root.value[0]);

		ObjectLanguageInstance rooto = (ObjectLanguageInstance)objects.value[rootind];
		//printObject(o,pw);
		
		//EditUtils.printObjects(o, pw, fs);
		//Set the parametric shape of this map
		if(rooto.physicalRepresentation.shape==null)
		{

		}
		else
		{
			
			double ceiling_height = 2.4;
						
			StateToSVG.mapToSVG(pw,
					objectpaths,objects,posepaths,poses,rooto);
			//create or edit the shape of the map			

			//Adjust the shape parameters as appropriate			
		}
		
		//EditUtils.printShape(rooto, pw, fs);
		}
		
		pw.flush();
	}
	
	
}