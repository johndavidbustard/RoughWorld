package website.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import ai.knowledge_representation.state_instance.ObjectPhysicalInstance;
import tools.validation.SpatialValidation;
import tools.visualisation.state_visualisation.ToSVG.StateToSVG;
import utils.ArrayUtils;
import utils.FileStoreInterface;
import utils.GeneralMatrixInt;
import utils.GeneralMatrixString;
import utils.shapes.ParametricShape;
import utils.shapes.RectangularBuildingFloor;
import utils.shapes.RectangularPortal;
import utils.shapes.ReverseRBuildingFloor;
import web.WebRequest;
import web.WebResponse;
import web.WebSecurity;

public class EditMapInstance extends DynamicWebPage
{
	public EditMapInstance(FileStoreInterface fs)
	{
		super(fs);
	}
	public boolean process(WebRequest toProcess)
	{
        if(toProcess.path.startsWith("EditMapInstance.html"))
        {
        	return true;
        }
        else
        if(toProcess.path.startsWith("EditMapInstance.cgi"))
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
    		if(function.equalsIgnoreCase("setShape"))
    		{
    			String shape = toProcess.parms.get("shape");
    			ParametricShape oldshape = o.physicalRepresentation.shape;
    			o.physicalRepresentation.shape = ParametricShape.get(shape);
    			if(oldshape!=null)
    			{
    				o.physicalRepresentation.shape.makeSimilarSize(oldshape);
    			}
    		}
    		else
    		if(function.equalsIgnoreCase("setShapeParameters"))
    		{
    			ParametricShape shape = o.physicalRepresentation.shape;
    			String[] pnames = shape.getParameterNames();
    			
    			for(int i=0;i<pnames.length;i++)
    			{
    				String v = toProcess.parms.get(pnames[i]);
    				shape.parameters[i] = Double.parseDouble(v);
    			}
    			shape.parametersUpdated();
    		}
    		else
        	if(function.equalsIgnoreCase("addExternalPortal"))
    		{
    			//Create a default instance of a portal
    			ObjectLanguageInstance r = new ObjectLanguageInstance();
//    			name
//    			type
    			r.name = toProcess.parms.get("name");
    			r.name = r.name.replace(' ', '_');
    			r.type = "portal";
    			String roomConcept = toProcess.parms.get("type");
    			String roomInstance = instance+"_"+r.name;
    			r.uniqueID = roomConcept+"/"+roomInstance;
    			
    			//only add the instance if it doesn't already exist
    			int existsind = ArrayUtils.getIndex(o.contains, r.uniqueID);
    			if(existsind==-1)
    			{	
    			
	    			String rpath = "Concepts/"+roomConcept+"/instances/";
	    			fs.createDirectory(rpath);
	    			rpath += roomInstance;
	    			fs.createDirectory(rpath);
	    			
	    			
	    			
	    			r.physicalRepresentation = new ObjectPhysicalInstance();
	    			ParametricShape shape = ParametricShape.get(toProcess.parms.get("shape"));
	    			//ParametricShape shape = new RectangularPortal();
	    			r.physicalRepresentation.shape = shape;
	
	    			shape.setCuboidDimensions(Double.parseDouble(toProcess.parms.get("width")), 
	    					Double.parseDouble(toProcess.parms.get("height")),
	    					Double.parseDouble(toProcess.parms.get("ceiling_height")));
	
	    			double x = Double.parseDouble(toProcess.parms.get("x"));
	    			double y = Double.parseDouble(toProcess.parms.get("y"));
	    			double z = Double.parseDouble(toProcess.parms.get("z"));
	    			o.add(r);
	    			o.physicalRepresentation.add(r.physicalRepresentation, 
	    					x,y,z);
	    			rpath += "/data.txt";
	    			ObjectLanguageInstance.saveObject(r, fs, rpath);
	    			
	    			String connectingRoom = toProcess.parms.get("connectTo");
	    			if(connectingRoom!=null)
	    			{
		    			String crpath = ObjectLanguageInstance.uniqueIdToPath(connectingRoom);
		    			crpath += "/data.txt";
		    			ObjectLanguageInstance cr = ObjectLanguageInstance.loadObject(fs, crpath);
		    			cr.add(r);
		    			int crind = ArrayUtils.getIndex(o.contains,connectingRoom);
		    			double crx = o.physicalRepresentation.contains_metric_transform[crind*12+9];
		    			double cry = o.physicalRepresentation.contains_metric_transform[crind*12+10];
		    			double crz = o.physicalRepresentation.contains_metric_transform[crind*12+11];
		    			//relative position of portal to room
		    			cr.physicalRepresentation.add(r.physicalRepresentation, x-crx, y-cry, z-crz);
		    			ObjectLanguageInstance.saveObject(cr, fs, crpath);
	    			}

	    			connectingRoom = toProcess.parms.get("connectTo2");
	    			if(connectingRoom!=null)
	    			{
		    			String crpath = ObjectLanguageInstance.uniqueIdToPath(connectingRoom);
		    			crpath += "/data.txt";
		    			ObjectLanguageInstance cr = ObjectLanguageInstance.loadObject(fs, crpath);
		    			cr.add(r);
		    			int crind = ArrayUtils.getIndex(o.contains,connectingRoom);
		    			double crx = o.physicalRepresentation.contains_metric_transform[crind*12+9];
		    			double cry = o.physicalRepresentation.contains_metric_transform[crind*12+10];
		    			double crz = o.physicalRepresentation.contains_metric_transform[crind*12+11];
		    			//relative position of portal to room
		    			cr.physicalRepresentation.add(r.physicalRepresentation, x-crx, y-cry, z-crz);
		    			ObjectLanguageInstance.saveObject(cr, fs, crpath);
	    			}
    			}
    		}
    		else
    		if(function.equalsIgnoreCase("setContainedPosition"))
    		{
        		String rid = toProcess.parms.get("cinstance");
        		int rind = rid.indexOf("/");
        		
        		String rconcept = rid.substring(0,rind);
        		String rinstance = rid.substring(rind+1);

        		String rinstancepath = "Concepts/"+rconcept+"/instances/"+rinstance;    		
        		String rdatapath = rinstancepath+"/data.txt";

        		ObjectLanguageInstance r = ObjectLanguageInstance.loadObject(fs, rdatapath);
        		
        		double x = Double.parseDouble(toProcess.parms.get("x"));
        		double y = Double.parseDouble(toProcess.parms.get("y"));
        		double z = Double.parseDouble(toProcess.parms.get("z"));
        		
        		int cind = ArrayUtils.getIndex(o.contains,rid);
        		o.physicalRepresentation.contains_metric_transform[12*cind+ 9] = x;
        		o.physicalRepresentation.contains_metric_transform[12*cind+10] = y;
        		o.physicalRepresentation.contains_metric_transform[12*cind+11] = z;

        		int wind = ArrayUtils.getIndex(r.within,o.uniqueID);
        		r.physicalRepresentation.within_metric_transform[12*wind+ 9] = x;
        		r.physicalRepresentation.within_metric_transform[12*wind+10] = y;
        		r.physicalRepresentation.within_metric_transform[12*wind+11] = z;
        		
    			ObjectLanguageInstance.saveObject(r, fs, rdatapath);
    		}
    		else
    		if(function.equalsIgnoreCase("removeContained"))
    		{
    			//don't modify the room data at this point
        		String rid = toProcess.parms.get("cinstance");
        		int cind = ArrayUtils.getIndex(o.contains,rid);
        		o.physicalRepresentation.contains_metric_transform = ArrayUtils.remove(o.physicalRepresentation.contains_metric_transform,cind*12,12);
        		o.contains = ArrayUtils.remove(o.contains,cind,1);
        		o.contains_type = ArrayUtils.remove(o.contains_type,cind,1);
    		}
    		else
    		if(function.equalsIgnoreCase("addRoom"))
    		{
    			//Create a default instance of a room (using a model)
    			ObjectLanguageInstance r = new ObjectLanguageInstance();
//    			name
//    			type
    			r.name = toProcess.parms.get("name");
    			r.name = r.name.replace(' ', '_');
    			r.type = "room";
    			String roomConcept = toProcess.parms.get("type");
    			String roomInstance = instance+"_"+r.name;
    			r.uniqueID = roomConcept+"/"+roomInstance;
    			
    			//only add the instance if it doesn't already exist
    			int existsind = ArrayUtils.getIndex(o.contains, r.uniqueID);
    			if(existsind==-1)
    			{	
    			
	    			String rpath = "Concepts/"+roomConcept+"/instances/";
	    			fs.createDirectory(rpath);
	    			rpath += roomInstance;
	    			fs.createDirectory(rpath);
	    			
	    			
	    			
	    			r.physicalRepresentation = new ObjectPhysicalInstance();
	    			ParametricShape shape = ParametricShape.get(toProcess.parms.get("shape"));
	    			r.physicalRepresentation.shape = shape;
	
	    			shape.setCuboidDimensions(Double.parseDouble(toProcess.parms.get("width")), 
	    					Double.parseDouble(toProcess.parms.get("height")),
	    					Double.parseDouble(toProcess.parms.get("ceiling_height")));
	
	//    			x
	//    			y
	//    			z
	//    			width
	//    			height
	//    			ceiling_height
	//    			shape
	    			
	    			o.add(r);
	    			o.physicalRepresentation.add(r.physicalRepresentation, 
	    					Double.parseDouble(toProcess.parms.get("x")), 
	    					Double.parseDouble(toProcess.parms.get("y")),
	    					Double.parseDouble(toProcess.parms.get("z")));
	    			rpath += "/data.txt";
	    			ObjectLanguageInstance.saveObject(r, fs, rpath);
    			}
    		}
    		ObjectLanguageInstance.saveObject(o, fs, datapath);
    		
			toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML,"Success");
			
        	return true;
        }
        return false;
	}

	
	
	public void printMap(String concept,String instance,String instancepath,PrintWriter pw)
	{
		pw.append("<h1>"+concept+"/"+instance+"</h1>");
		
		
		boolean hasfloorplan = false;
		String floorplanpath = instancepath+"/floorplan.jpg";
		if(fs.exists(floorplanpath))
		{
			pw.append("<img src=\"/"+WebSecurity.encodeUri(floorplanpath)+"\">");
			hasfloorplan = true;
		}
		if(!hasfloorplan)
		{
			floorplanpath = instancepath+"/floorplan.png";
			if(fs.exists(floorplanpath))
			{
				pw.append("<img src=\"/"+WebSecurity.encodeUri(floorplanpath)+"\">");
				hasfloorplan = true;
			}
		}
		
		boolean hassatellite = false;
		String satellitepath = instancepath+"/satellite.jpg";
		if(fs.exists(satellitepath))
		{
			pw.append("<img src=\"/"+WebSecurity.encodeUri(satellitepath)+"\">");
			hassatellite = true;
		}
		if(!hassatellite)
		{
			satellitepath = instancepath+"/satellite.png";
			if(fs.exists(satellitepath))
			{
				pw.append("<img src=\"/"+WebSecurity.encodeUri(satellitepath)+"\">");
				hassatellite = true;
			}
		}
		if(!hassatellite)
		{
			//<div id="map" style="width:400px;height:400px">>My map will go here</div>
		}
		
		if(!hasfloorplan)
		{
			//create a form for uploading a floorplan image
		}
		if(!hassatellite)
		{
			//create a form for uploading a satellite image
		}

	}
	
	public void printShape(ObjectLanguageInstance o,PrintWriter pw)
	{
		//Set the parametric shape of this map
		if(o.physicalRepresentation.shape==null)
		{

		}
		else
		{
			
			StateToSVG.mapToSVG(pw,fs,o);
			//create or edit the shape of the map			

			//Adjust the shape parameters as appropriate			
		}

		pw.append("<h3>Set Shape:</h3>");
		pw.append("<form action='/EditMapInstance.cgi/setShape' method='get' role='form'>\n");
		
		pw.append("<select name=\"shape\">");
		
		for(int i=0;i<ParametricShape.buildingShapes.length;i++)
		{
			if((o.physicalRepresentation.shape!=null)&&(o.physicalRepresentation.shape.getClass().getName().equalsIgnoreCase(ParametricShape.buildingShapes[i])))
				pw.append("<option value=\""+ParametricShape.buildingShapes[i]+"\" selected>"+ParametricShape.buildingShapes[i]+"</option>");
			else
				pw.append("<option value=\""+ParametricShape.buildingShapes[i]+"\">"+ParametricShape.buildingShapes[i]+"</option>");
		}
		
		pw.append("</select>");

		pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
		
		//pw.append("<a class=\"btn btn-primary btn-block\" href='http://www.google.com' onclick='return checkLogin()'>Login</a>");
		pw.append("<button class=\"btn btn-default\" type='submit'>Set</button>");
		pw.append("</form>");
		
		ParametricShape shape = o.physicalRepresentation.shape;
		if(shape!=null)
		{
			pw.append("<form action='/EditMapInstance.cgi/setShapeParameters' method='get' role='form'>\n");
			pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
			String[] pnames = shape.getParameterNames();
			
			pw.append("<table>");
			for(int i=0;i<pnames.length;i++)
			{
				pw.append("<tr>");
				pw.append("<td>");
				pw.append(pnames[i]);
				pw.append("</td>");
				pw.append("<td>");
				pw.append("<input name=\""+pnames[i]+"\" type=\"number\" step=\"0.001\" value=\""+String.format( "%.3f", shape.parameters[i])+"\">");
				
				pw.append("</td>");
				pw.append("</tr>");
			}
			pw.append("</table>");
			pw.append("<button class=\"btn btn-default\" type='submit'>Set</button>");
			pw.append("</form>");
		}		
	}
	
	public void printExternalPortals(ObjectLanguageInstance o,PrintWriter pw)
	{
		//Go through the rooms in the map
		pw.append("<h2>Portals</h2>");

		printType(o, "Portal", "Map", pw, fs);

		pw.append("<h3>Add Internal Portal</h3>");

		//add a (new) external portal instance
		pw.append("<form action='/EditMapInstance.cgi/addExternalPortal' method='get' role='form'>\n");
		
		pw.append("<table>");
		
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Name");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"name\" type=\"text\" value=\"front_door\">");
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("</table>");

		GeneralMatrixString portalconcepts = new GeneralMatrixString(1);
		EditUtils.getConcepts("portal", portalconcepts);
		
		pw.append("<h3>Portal Type</h3>");
		pw.append("<select name=\"type\">");
		for(int i=0;i<portalconcepts.height;i++)
		{
			pw.append("<option value=\""+portalconcepts.value[i]+"\">"+portalconcepts.value[i]+"</option>");
		}
		pw.append("</select>");

//		ParametricShape shape = o.physicalRepresentation.shape;
//
//		if(shape!=null)
//		{
//			String[] shapesides = shape.getSideNames();
//			
//			pw.append("<h3>Map Side</h3>");
//			pw.append("<select name=\"shapeside\">");
//			for(int i=0;i<shapesides.length;i++)
//			{
//				pw.append("<option value=\""+shapesides[i]+"\">"+shapesides[i]+"</option>");
//			}
//			pw.append("</select>");
//		}

		pw.append("<h4>Room Position and Dimensions</h4>");
		pw.append("<table>");
		
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("X");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"x\" type=\"number\" step=\"0.001\" value=\"0.0\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Y");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"y\" type=\"number\" step=\"0.001\" value=\"0.36\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Z");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"z\" type=\"number\" step=\"0.001\" value=\"0.0\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Plan Width");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"width\" type=\"number\" step=\"0.001\" value=\"0.36\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Plan Height");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"height\" type=\"number\" step=\"0.001\" value=\"0.823\">");
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Ceiling Height");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"ceiling_height\" type=\"number\" step=\"0.001\" value=\"2.0145\">");
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("</table>");
		
		pw.append("<h4>Portal Shape</h4>");
		pw.append("<select name=\"shape\">");
		for(int i=0;i<ParametricShape.portalShapes.length;i++)
		{
			pw.append("<option value=\""+ParametricShape.portalShapes[i]+"\">"+ParametricShape.portalShapes[i]+"</option>");
		}
		pw.append("</select>");
		
		GeneralMatrixInt roomsOffset = new GeneralMatrixInt(1);
		GeneralMatrixString roomsWithin = new GeneralMatrixString(1);
		EditUtils.getContains(o,"room", roomsWithin,roomsOffset);
		if(roomsWithin.height>0)
		{
			pw.append("<h3>Connect between:</h3>");
			pw.append("<select name=\"connectTo\">");
			for(int i=0;i<roomsWithin.height;i++)
			{
				pw.append("<option value=\""+roomsWithin.value[i]+"\">"+roomsWithin.value[i]+"</option>");
			}
			pw.append("</select>");
			pw.append("<select name=\"connectTo2\">");
			for(int i=0;i<roomsWithin.height;i++)
			{
				pw.append("<option value=\""+roomsWithin.value[i]+"\">"+roomsWithin.value[i]+"</option>");
			}
			pw.append("</select>");
		}
		
		pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
		
		//pw.append("<a class=\"btn btn-primary btn-block\" href='http://www.google.com' onclick='return checkLogin()'>Login</a>");
		pw.append("<button class=\"btn btn-default\" type='submit'>Add</button>");
		pw.append("</form>");
		
		pw.append("<h3>Add External Portal</h3>");
		//add a (new) external portal instance
		pw.append("<form action='/EditMapInstance.cgi/addExternalPortal' method='get' role='form'>\n");
		
		pw.append("<table>");
		
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Name");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"name\" type=\"text\" value=\"front_door\">");
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("</table>");

		//GeneralMatrixString portalconcepts = new GeneralMatrixString(1);
		EditUtils.getConcepts("portal", portalconcepts);
		
		pw.append("<h3>Portal Type</h3>");
		pw.append("<select name=\"type\">");
		for(int i=0;i<portalconcepts.height;i++)
		{
			pw.append("<option value=\""+portalconcepts.value[i]+"\">"+portalconcepts.value[i]+"</option>");
		}
		pw.append("</select>");

//		ParametricShape shape = o.physicalRepresentation.shape;
//
//		if(shape!=null)
//		{
//			String[] shapesides = shape.getSideNames();
//			
//			pw.append("<h3>Map Side</h3>");
//			pw.append("<select name=\"shapeside\">");
//			for(int i=0;i<shapesides.length;i++)
//			{
//				pw.append("<option value=\""+shapesides[i]+"\">"+shapesides[i]+"</option>");
//			}
//			pw.append("</select>");
//		}

		pw.append("<h4>Room Position and Dimensions</h4>");
		pw.append("<table>");
		
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("X");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"x\" type=\"number\" step=\"0.001\" value=\"0.0\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Y");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"y\" type=\"number\" step=\"0.001\" value=\"0.36\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Z");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"z\" type=\"number\" step=\"0.001\" value=\"0.0\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Plan Width");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"width\" type=\"number\" step=\"0.001\" value=\"0.36\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Plan Height");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"height\" type=\"number\" step=\"0.001\" value=\"0.823\">");
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Ceiling Height");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"ceiling_height\" type=\"number\" step=\"0.001\" value=\"2.0145\">");
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("</table>");
		
		pw.append("<h4>Portal Shape</h4>");
		pw.append("<select name=\"shape\">");
		for(int i=0;i<ParametricShape.portalShapes.length;i++)
		{
			pw.append("<option value=\""+ParametricShape.portalShapes[i]+"\">"+ParametricShape.portalShapes[i]+"</option>");
		}
		pw.append("</select>");
		
		//GeneralMatrixString roomsWithin = new GeneralMatrixString(1);
//		GeneralMatrixInt roomsOffset = new GeneralMatrixInt(1);
//		GeneralMatrixString roomsWithin = new GeneralMatrixString(1);
//		getContains(o,"room", roomsWithin,roomsOffset);
		if(roomsWithin.height>0)
		{
			pw.append("<h3>Connect to internal room:</h3>");
			pw.append("<select name=\"connectTo\">");
			for(int i=0;i<roomsWithin.height;i++)
			{
				pw.append("<option value=\""+roomsWithin.value[i]+"\">"+roomsWithin.value[i]+"</option>");
			}
			pw.append("</select>");
		}
		
		pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
		
		//pw.append("<a class=\"btn btn-primary btn-block\" href='http://www.google.com' onclick='return checkLogin()'>Login</a>");
		pw.append("<button class=\"btn btn-default\" type='submit'>Add</button>");
		pw.append("</form>");
	}
	

	public static void printType(ObjectLanguageInstance o,String type,String withintype,PrintWriter pw,FileStoreInterface fs)
	{
		if(o.contains!=null)
		{
			double[] ctrans = o.physicalRepresentation.contains_metric_transform;
			GeneralMatrixInt roomsOffset = new GeneralMatrixInt(1);
			GeneralMatrixString roomsWithin = new GeneralMatrixString(1);
//			GeneralMatrixString roomsWithin = new GeneralMatrixString(1);
//			getContains(o,"room", roomsWithin,roomsOffset);
			EditUtils.getContains(o,type, roomsWithin,roomsOffset);
			for(int i=0;i<roomsWithin.height;i++)
			{
				String rpath = ObjectLanguageInstance.uniqueIdToPath(roomsWithin.value[i]);
				ObjectLanguageInstance r = ObjectLanguageInstance.loadObject(fs, rpath+"/data.txt");

				pw.append("<h3><a href=\"/Edit"+type+"Instance.html/"+roomsWithin.value[i]+"\">"+roomsWithin.value[i]+"</a><br></h3>");

				pw.append("<form action='/Edit"+withintype+"Instance.cgi/removeContained' method='get' role='form'>\n");
				pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
				pw.append("<input name=\"cinstance\" type=\"hidden\" value=\""+r.uniqueID+"\">");
				pw.append("<button class=\"btn btn-default\" type='submit'>Remove "+type+"</button>");

				pw.append("</form>");

				//position the room relative to the map
				pw.append("<form action='/Edit"+withintype+"Instance.cgi/setContainedPosition' method='get' role='form'>\n");
				pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
				pw.append("<input name=\"cinstance\" type=\"hidden\" value=\""+r.uniqueID+"\">");
				
				int cind = roomsOffset.value[i];
				double x = ctrans[cind*12+ 9];
				double y = ctrans[cind*12+10];
				double z = ctrans[cind*12+11];
				
				pw.append("<table>");
					pw.append("<tr>");
						pw.append("<td>");
							pw.append("X");
						pw.append("</td>");
						pw.append("<td>");
							pw.append("<input name=\"x\" type=\"number\" step=\"0.001\" value=\""+String.format( "%.3f", x)+"\">");
						pw.append("</td>");
					pw.append("</tr>");
					pw.append("<tr>");
					pw.append("<td>");
						pw.append("Y");
					pw.append("</td>");
					pw.append("<td>");
					pw.append("<input name=\"y\" type=\"number\" step=\"0.001\" value=\""+String.format( "%.3f", y)+"\">");
					pw.append("</td>");
				pw.append("</tr>");
				pw.append("<tr>");
				pw.append("<td>");
					pw.append("Z");
				pw.append("</td>");
				pw.append("<td>");
				pw.append("<input name=\"z\" type=\"number\" step=\"0.001\" value=\""+String.format( "%.3f", z)+"\">");
				pw.append("</td>");
			pw.append("</tr>");

			
			
				pw.append("</table>");
				pw.append("<button class=\"btn btn-default\" type='submit'>Position</button>");

				pw.append("</form>");

				
				pw.append("<h4>Set Shape:</h4>");
				pw.append("<form action='/Edit"+type+"Instance.cgi/setShape' method='get' role='form'>\n");
				
				pw.append("<select name=\"shape\">");
				
				String[] shapes = ParametricShape.getShapesByType(type);
				for(int si=0;si<shapes.length;si++)
				{
					if((r.physicalRepresentation.shape!=null)&&(r.physicalRepresentation.shape.getClass().getName().equalsIgnoreCase(shapes[si])))
						pw.append("<option value=\""+shapes[si]+"\" selected>"+shapes[si]+"</option>");
					else
						pw.append("<option value=\""+shapes[si]+"\">"+shapes[si]+"</option>");
				}
				
				pw.append("</select>");

				pw.append("<input name=\"instance\" type=\"hidden\" value=\""+r.uniqueID+"\">");
				
				//pw.append("<a class=\"btn btn-primary btn-block\" href='http://www.google.com' onclick='return checkLogin()'>Login</a>");
				pw.append("<button class=\"btn btn-default\" type='submit'>Set</button>");
				pw.append("</form>");
				

				pw.append("<h4>Set Shape Dimensions:</h4>");

				pw.append("<form action='/Edit"+type+"Instance.cgi/setShapeParameters' method='get' role='form'>\n");
				pw.append("<input name=\"instance\" type=\"hidden\" value=\""+r.uniqueID+"\">");
				
				ParametricShape rshape = r.physicalRepresentation.shape;

				String[] pnames = rshape.getParameterNames();
				
				pw.append("<table>");
				for(int pi=0;pi<pnames.length;pi++)
				{
					pw.append("<tr>");
					pw.append("<td>");
					pw.append(pnames[pi]);
					pw.append("</td>");
					pw.append("<td>");
					pw.append("<input name=\""+pnames[pi]+"\" type=\"number\" step=\"0.001\" value=\""+String.format( "%.3f", rshape.parameters[pi])+"\">");
					
					pw.append("</td>");
					pw.append("</tr>");
				}
				pw.append("</table>");
				pw.append("<button class=\"btn btn-default\" type='submit'>Set</button>");
				pw.append("</form>");
				//remove a room
			}
		}
	}
	
	public void printRooms(ObjectLanguageInstance o,PrintWriter pw)
	{
		pw.append("<h2>Rooms</h2>");
		
		printType(o, "Room", "Map", pw, fs);
		
		pw.append("<h3>Add New Room</h3>");

		//add a (new) room instance to the map
		pw.append("<form action='/EditMapInstance.cgi/addRoom' method='get' role='form'>\n");
		
		pw.append("<table>");
		
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Name");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"name\" type=\"text\" value=\"bedroom\">");
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("</table>");

		GeneralMatrixString roomconcepts = new GeneralMatrixString(1);
		EditUtils.getConcepts("room", roomconcepts);
		
		pw.append("<h4>Room Type</h4>");
		pw.append("<select name=\"type\">");
		for(int i=0;i<roomconcepts.height;i++)
		{
			pw.append("<option value=\""+roomconcepts.value[i]+"\">"+roomconcepts.value[i]+"</option>");
		}
		pw.append("</select>");

		pw.append("<h4>Room Position and Dimensions</h4>");
		pw.append("<table>");
		
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("X");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"x\" type=\"number\" step=\"0.001\" value=\"0.36\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Y");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"y\" type=\"number\" step=\"0.001\" value=\"0.36\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Z");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"z\" type=\"number\" step=\"0.001\" value=\"0.0\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Plan Width");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"width\" type=\"number\" step=\"0.001\" value=\"3.0\">");		
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Plan Height");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"height\" type=\"number\" step=\"0.001\" value=\"3.0\">");
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Ceiling Height");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"ceiling_height\" type=\"number\" step=\"0.001\" value=\"2.4\">");
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("</table>");
		
		pw.append("<h4>Room Shape</h4>");
		pw.append("<select name=\"shape\">");
		for(int i=0;i<ParametricShape.roomShapes.length;i++)
		{
			pw.append("<option value=\""+ParametricShape.roomShapes[i]+"\">"+ParametricShape.roomShapes[i]+"</option>");
		}
		pw.append("</select>");

		pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
		
		//pw.append("<a class=\"btn btn-primary btn-block\" href='http://www.google.com' onclick='return checkLogin()'>Login</a>");
		pw.append("<button class=\"btn btn-default\" type='submit'>Add</button>");
		pw.append("</form>");
	}
	
	public void out(WebRequest r,Object metadata,OutputStream s)
	{
		PrintWriter pw = new PrintWriter( s );
		int ind = r.path.indexOf("/");
		String concept = r.path.substring(ind+1);
		ind = concept.indexOf("/");
		String instance = concept.substring(ind+1);
		concept = concept.substring(0,ind);
		//identify the path to the instance of this map
		
		/*
		<script>
		function myMap() {
		    var mapOptions = {
		        center: new google.maps.LatLng(51.5, -0.12),
		        zoom: 10,
		        mapTypeId: google.maps.MapTypeId.HYBRID
		    }
		var map = new google.maps.Map(document.getElementById("map"), mapOptions);
		}
		</script>
		
		<script src="https://maps.googleapis.com/maps/api/js?callback=myMap"></script>
		*/

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

		SpatialValidation.validateMap(o, fs);
		
		printMap(concept,instance,instancepath,pw);

		printShape(o, pw);

		printExternalPortals(o, pw);
		
		printRooms(o, pw);
		
		pw.flush();
	}
}