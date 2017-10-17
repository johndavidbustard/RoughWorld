package website.views;

import java.io.OutputStream;
import java.io.PrintWriter;

import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import ai.knowledge_representation.state_instance.ObjectPhysicalInstance;
import tools.visualisation.state_visualisation.ToSVG.StateToSVG;
import utils.ArrayUtils;
import utils.FileStoreInterface;
import utils.shapes.ParametricShape;
import web.WebRequest;
import web.WebResponse;

public class EditRoomInstance extends DynamicWebPage
{
	public EditRoomInstance(FileStoreInterface fs)
	{
		super(fs);
	}

	public boolean process(WebRequest toProcess)
	{
        if(toProcess.path.startsWith("EditRoomInstance.html"))
        {
        	return true;
        }
        else
            if(
            		(toProcess.path.startsWith("EditRoomInstance.cgi"))||
            		(toProcess.path.startsWith("EditroomInstance.cgi"))
            		)
        {
    		String id = toProcess.parms.get("instance");
    		int ind = id.indexOf("/");
    		
    		String concept = id.substring(0,ind);
    		String instance = id.substring(ind+1);

    		String instancepath = "Concepts/"+concept+"/instances/"+instance;    		
    		String datapath = instancepath+"/data.txt";

    		ObjectLanguageInstance o = ObjectLanguageInstance.loadObject(fs, datapath);
    		if(o==null)
    		{
    			o = new ObjectLanguageInstance();
    			o.uniqueID = concept+"/"+instance;
    			o.name = instance;
    			o.type = "map";
    			
    			o.physicalRepresentation = new ObjectPhysicalInstance();
    		}

    		ind = toProcess.path.indexOf("/");
    		String function = toProcess.path.substring(ind+1);
    		
    		EditUtils.processFunction(toProcess, function, o, instance, fs);

//    		if(function.equalsIgnoreCase("setShape"))
//    		{
//    			String shape = toProcess.parms.get("shape");
//    			ParametricShape oldshape = o.physicalRepresentation.shape;
//    			o.physicalRepresentation.shape = ParametricShape.get(shape);
//    			if(oldshape!=null)
//    			{
//    				o.physicalRepresentation.shape.makeSimilarSize(oldshape);
//    			}
//    		}
//    		else
//    		if(function.equalsIgnoreCase("setShapeParameters"))
//    		{
//    			ParametricShape shape = o.physicalRepresentation.shape;
//    			String[] pnames = shape.getParameterNames();
//    			
//    			for(int i=0;i<pnames.length;i++)
//    			{
//    				String v = toProcess.parms.get(pnames[i]);
//    				shape.parameters[i] = Double.parseDouble(v);
//    			}
//    		}
    		
    		ObjectLanguageInstance.saveObject(o, fs, datapath);

			toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML,"Success");

    		return true;
        }
        
        return false;
	}
	
	public void printRoom(ObjectLanguageInstance o,PrintWriter pw)
	{
		//Set the parametric shape of this map
		if(o.physicalRepresentation.shape==null)
		{

		}
		else
		{
			
			StateToSVG.roomToSVG(pw,fs,o);
			//create or edit the shape of the map			

			//Adjust the shape parameters as appropriate			
			
			EditUtils.printShape(o, pw, fs);
		}
	}

	public void out(WebRequest r,Object metadata,OutputStream s)
	{
		PrintWriter pw = new PrintWriter( s );
		int ind = r.path.indexOf("/");
		String concept = r.path.substring(ind+1);
		ind = concept.indexOf("/");
		String instance = concept.substring(ind+1);
		concept = concept.substring(0,ind);

		String instancepath = "Concepts/"+concept+"/instances/"+instance;
		
		String datapath = instancepath+"/data.txt";
		
		ObjectLanguageInstance o = ObjectLanguageInstance.loadObject(fs, datapath);
		if(o==null)
		{
			o = new ObjectLanguageInstance();
			o.uniqueID = concept+"/"+instance;
			o.name = instance;
			o.type = "map";
			
			o.physicalRepresentation = new ObjectPhysicalInstance();
			
			ObjectLanguageInstance.saveObject(o, fs, datapath);
		}
		
//		for(int i=0;i<o.contains.length;i++)
//		{
//			String cid = o.contains[i];
//			
//			String cpath = ObjectLanguageInstance.uniqueIdToPath(cid);
//			ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, cpath+"/data.txt");
//			
//			if(c==null)
//			{
//				int cind = i;
//        		o.physicalRepresentation.contains_metric_transform = ArrayUtils.remove(o.physicalRepresentation.contains_metric_transform,cind*12,12);
//        		o.contains = ArrayUtils.remove(o.contains,cind,1);
//        		o.contains_type = ArrayUtils.remove(o.contains_type,cind,1);
//
//        		ObjectLanguageInstance.saveObject(o, fs, datapath);
//				break;
//			}
//		}
		
		pw.append("<h1>"+concept+"/"+instance+"</h1>");

		printRoom(o,pw);
		
		EditUtils.printCharacters(o, pw, fs);
		EditUtils.printObjects(o, pw, fs);
		
		pw.flush();	}
}