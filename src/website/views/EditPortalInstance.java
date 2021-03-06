package website.views;

import java.io.OutputStream;
import java.io.PrintWriter;

import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import ai.knowledge_representation.state_instance.ObjectPhysicalInstance;
import tools.visualisation.state_visualisation.ToSVG.StateToSVG;
import utils.ArrayUtils;
import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.shapes.ParametricShape;
import web.WebRequest;
import web.WebResponse;

public class EditPortalInstance extends DynamicWebPage
{
	public EditPortalInstance(FileStoreInterface fs)
	{
		super(fs);
	}
	public boolean process(WebRequest toProcess)
	{
        if(toProcess.path.startsWith("EditPortalInstance.html"))
        {
        	return true;
        }
        else
            if(
            		(toProcess.path.startsWith("EditPortalInstance.cgi"))||
            		(toProcess.path.startsWith("EditportalInstance.cgi"))
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
//    		else
//    		if(function.equalsIgnoreCase("addObject"))
//    		{
//    			//Create a default instance of a room (using a model)
//    			ObjectLanguageInstance r = new ObjectLanguageInstance();
////    			name
////    			type
//    			r.name = toProcess.parms.get("name");
//    			r.name = r.name.replace(' ', '_');
//    			r.type = "object";
//    			String roomConcept = toProcess.parms.get("type");
//    			String roomInstance = instance+"_"+r.name;
//    			r.uniqueID = roomConcept+"/"+roomInstance;
//    			
//    			//only add the instance if it doesn't already exist
//    			int existsind = ArrayUtils.getIndex(o.contains, r.uniqueID);
//    			if(existsind==-1)
//    			{	
//    			
//	    			String rpath = "Concepts/"+roomConcept+"/instances/";
//	    			fs.createDirectory(rpath);
//	    			rpath += roomInstance;
//	    			fs.createDirectory(rpath);
//	    			
//	    			
//	    			
//	    			r.physicalRepresentation = new ObjectPhysicalInstance();
//	    			ParametricShape shape = ParametricShape.get(toProcess.parms.get("shape"));
//	    			r.physicalRepresentation.shape = shape;
//	
//	    			shape.setCuboidDimensions(Double.parseDouble(toProcess.parms.get("width")), 
//	    					Double.parseDouble(toProcess.parms.get("height")),
//	    					Double.parseDouble(toProcess.parms.get("ceiling_height")));
//	
//	//    			x
//	//    			y
//	//    			z
//	//    			width
//	//    			height
//	//    			ceiling_height
//	//    			shape
//	    			
//	    			o.add(r);
//	    			o.physicalRepresentation.add(r.physicalRepresentation, 
//	    					Double.parseDouble(toProcess.parms.get("x")), 
//	    					Double.parseDouble(toProcess.parms.get("y")),
//	    					Double.parseDouble(toProcess.parms.get("z")),
//	    					Double.parseDouble(toProcess.parms.get("zr"))
//	    					);
//	    			rpath += "/data.txt";
//	    			ObjectLanguageInstance.saveObject(r, fs, rpath);
//    			}
//    		}
//    		else
//    		if(function.equalsIgnoreCase("setContainedPosition"))
//    		{
//        		String rid = toProcess.parms.get("cinstance");
//        		int rind = rid.indexOf("/");
//        		
//        		String rconcept = rid.substring(0,rind);
//        		String rinstance = rid.substring(rind+1);
//
//        		String rinstancepath = "Concepts/"+rconcept+"/instances/"+rinstance;    		
//        		String rdatapath = rinstancepath+"/data.txt";
//
//        		ObjectLanguageInstance r = ObjectLanguageInstance.loadObject(fs, rdatapath);
//        		
//        		double x = Double.parseDouble(toProcess.parms.get("X"));
//        		double y = Double.parseDouble(toProcess.parms.get("Y"));
//        		double z = Double.parseDouble(toProcess.parms.get("Z"));
//        		double zr = Double.parseDouble(toProcess.parms.get("Zr"));
//        		
//        		int cind = ArrayUtils.getIndex(o.contains,rid);
//        		
//        		GeneralMatrixDouble zrot = new GeneralMatrixDouble(4,4);
//        		zrot.set3DTransformRotation(0.0, 0.0, zr*Math.PI/180.0);
//
//        		o.physicalRepresentation.contains_metric_transform[12*cind+ 0] = zrot.value[4*0+0];
//        		o.physicalRepresentation.contains_metric_transform[12*cind+ 1] = zrot.value[4*0+1];
//        		o.physicalRepresentation.contains_metric_transform[12*cind+ 2] = zrot.value[4*0+2];
//
//        		o.physicalRepresentation.contains_metric_transform[12*cind+ 3] = zrot.value[4*1+0];
//        		o.physicalRepresentation.contains_metric_transform[12*cind+ 4] = zrot.value[4*1+1];
//        		o.physicalRepresentation.contains_metric_transform[12*cind+ 5] = zrot.value[4*1+2];
//
//        		o.physicalRepresentation.contains_metric_transform[12*cind+ 6] = zrot.value[4*2+0];
//        		o.physicalRepresentation.contains_metric_transform[12*cind+ 7] = zrot.value[4*2+1];
//        		o.physicalRepresentation.contains_metric_transform[12*cind+ 8] = zrot.value[4*2+2];
//
//        		o.physicalRepresentation.contains_metric_transform[12*cind+ 9] = x;
//        		o.physicalRepresentation.contains_metric_transform[12*cind+10] = y;
//        		o.physicalRepresentation.contains_metric_transform[12*cind+11] = z;
//
//        		int wind = ArrayUtils.getIndex(r.within,o.uniqueID);
//        		
//        		System.arraycopy(o.physicalRepresentation.contains_metric_transform, 12*cind, 
//        				r.physicalRepresentation.within_metric_transform, 12*wind, 
//        				12);
////        		r.physicalRepresentation.within_metric_transform[12*wind+ 9] = x;
////        		r.physicalRepresentation.within_metric_transform[12*wind+10] = y;
////        		r.physicalRepresentation.within_metric_transform[12*wind+11] = z;
//        		
//    			ObjectLanguageInstance.saveObject(r, fs, rdatapath);
//    		}
    		EditUtils.processFunction(toProcess, function, o, instance, fs);
    		
			toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML,"Success");

    		ObjectLanguageInstance.saveObject(o, fs, datapath);

    		return true;
        }
       
        return false;
	}
	
	public void printPortal(ObjectLanguageInstance o,PrintWriter pw)
	{
		//Set the parametric shape of this map
		if(o.physicalRepresentation.shape==null)
		{

		}
		else
		{
			
			double ceiling_height = 2.4;
			
			StateToSVG.portalToSVG(pw,fs,o,ceiling_height);
			//create or edit the shape of the map			

			//Adjust the shape parameters as appropriate			
		}
		
		EditUtils.printShape(o, pw, fs);
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
		
		pw.append("<h1>"+concept+"/"+instance+"</h1>");

		printPortal(o,pw);
		
		EditUtils.printObjects(o, pw, fs);
		
		pw.flush();
	}
}