package website.views;

import java.io.File;
import java.io.PrintWriter;

import utils.ArrayUtils;
import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.GeneralMatrixInt;
import utils.GeneralMatrixString;
import utils.shapes.CSGShape;
import utils.shapes.ModifiedCopyShape;
import utils.shapes.ParametricShape;
import web.WebRequest;
import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import ai.knowledge_representation.state_instance.ObjectPhysicalInstance;

public class EditUtils 
{
	public static GeneralMatrixString shapesToCopy = new GeneralMatrixString(1);
	public static GeneralMatrixString shapesToPaths = new GeneralMatrixString(1);
	
	public static void findShapesToCopy(FileStoreInterface fs)
	{
		File conceptsdir = new File("Concepts");
		String[] concepts = conceptsdir.list();
		for(int i=0;i<concepts.length;i++)
		{
			String c = concepts[i];
			
			System.out.println(c);
			
			String cInstPath = "Concepts/"+c+"/instances";
			File cInstdir = new File(cInstPath);
			if(!cInstdir.exists())
			{
				continue;
			}
			
			String[] insts = cInstdir.list();
			for(int ii=0;ii<insts.length;ii++)
			{
				String instdata = cInstPath+"/"+insts[ii]+"/data.txt";
				File instf = new File(instdata);
				if(instf.exists())
				{
					ObjectLanguageInstance o = ObjectLanguageInstance.loadObject(fs, instdata);
					if(o.physicalRepresentation.shape instanceof ModifiedCopyShape)
						continue;
					
					System.out.println(insts[ii]);
					
					shapesToPaths.push_back(instdata);
					shapesToCopy.push_back(c+"/"+insts[ii]);
				}
			}
		}
	}
	
	public static void getContains(ObjectLanguageInstance o,String type,GeneralMatrixString result,GeneralMatrixInt offset)
	{
		for(int i=0;i<o.contains_type.length;i++)
		{
			if(o.contains_type[i].equalsIgnoreCase(type))
			{
				result.push_back(o.contains[i]);
				offset.push_back(i);
			}
		}
	}
	
	public static void getConcepts(String type,GeneralMatrixString result)
	{
		File conceptsdir = new File("Concepts");
		String[] concepts = conceptsdir.list();
		for(int i=0;i<concepts.length;i++)
		{
			String c = concepts[i];
			if(c.startsWith(".")||c.endsWith(".md"))
			{
				continue;
			}

			if(c.startsWith(type+"_"))
			{
				result.push_back(c);
			}
		}
		
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
			getContains(o,type, roomsWithin,roomsOffset);
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
				
				int coff = cind*12;
				GeneralMatrixDouble containedRelativeToParenttran = new GeneralMatrixDouble(4,4);
				containedRelativeToParenttran.setIdentity();
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[ti] = ctrans[coff+ti];
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[4+ti] = ctrans[coff+3+ti];
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[8+ti] = ctrans[coff+6+ti];
				containedRelativeToParenttran.set3DTransformPosition(ctrans[coff+9], ctrans[coff+10], ctrans[coff+11]);

				GeneralMatrixDouble euler = new GeneralMatrixDouble(3,1);
				GeneralMatrixDouble.getEuler(containedRelativeToParenttran, euler);
				double xr = 180.0*(euler.value[0]/Math.PI);
				double yr = 180.0*(euler.value[1]/Math.PI);
				double zr = 180.0*(euler.value[2]/Math.PI);
				
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
			
			
			pw.append("<tr>");
			pw.append("<td>");
				pw.append("Xr");
			pw.append("</td>");
			pw.append("<td>");
			pw.append("<input name=\"xr\" type=\"number\" step=\"1.0\" value=\""+String.format( "%.1f", xr)+"\">");
			pw.append("</td>");
		pw.append("</tr>");
		pw.append("<tr>");
		pw.append("<td>");
			pw.append("Yr");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"yr\" type=\"number\" step=\"1.0\" value=\""+String.format( "%.1f", yr)+"\">");
		pw.append("</td>");
	pw.append("</tr>");
	pw.append("<tr>");
	pw.append("<td>");
		pw.append("Zr");
	pw.append("</td>");
	pw.append("<td>");
	pw.append("<input name=\"zr\" type=\"number\" step=\"1.0\" value=\""+String.format( "%.1f", zr)+"\">");
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
				
				if(rshape instanceof CSGShape)
				{
					addSubShapeForm(pw, type, r, shapes);
//					pw.append("<h4>Add Sub Shape:</h4>");
//
//					pw.append("<form action='/Edit"+type+"Instance.cgi/addSubShape' method='get' role='form'>\n");
//					pw.append("<input name=\"instance\" type=\"hidden\" value=\""+r.uniqueID+"\">");
//
//					pw.append("<table>");
//					
//					pw.append("<tr>");
//					pw.append("<td>");
//					pw.append("Name");
//					pw.append("</td>");
//					pw.append("<td>");
//					pw.append("<input name=\"name\" type=\"text\" value=\"objectname\">");
//					pw.append("</td>");
//					pw.append("</tr>");
//
//					pw.append("</table>");
//
//					pw.append("<select name=\"shape\">");
//					
//					for(int si=0;si<shapes.length;si++)
//					{
//						if(shapes[si].equalsIgnoreCase("CSGShape"))
//							continue;
//						
//						if((r.physicalRepresentation.shape!=null)&&(r.physicalRepresentation.shape.getClass().getName().equalsIgnoreCase(shapes[si])))
//							pw.append("<option value=\""+shapes[si]+"\" selected>"+shapes[si]+"</option>");
//						else
//							pw.append("<option value=\""+shapes[si]+"\">"+shapes[si]+"</option>");
//					}
//					
//					pw.append("</select>");
//					
//					pw.append("<h5>Sub Shape Position and Dimensions</h5>");
//					pw.append("<table>");
//					
//					pw.append("<tr>");
//					pw.append("<td>");
//					pw.append("X");
//					pw.append("</td>");
//					pw.append("<td>");
//					pw.append("<input name=\"x\" type=\"number\" step=\"0.001\" value=\"0.0\">");		
//					pw.append("</td>");
//					pw.append("</tr>");
//
//					pw.append("<tr>");
//					pw.append("<td>");
//					pw.append("Y");
//					pw.append("</td>");
//					pw.append("<td>");
//					pw.append("<input name=\"y\" type=\"number\" step=\"0.001\" value=\"0.0\">");		
//					pw.append("</td>");
//					pw.append("</tr>");
//
//					pw.append("<tr>");
//					pw.append("<td>");
//					pw.append("Z");
//					pw.append("</td>");
//					pw.append("<td>");
//					pw.append("<input name=\"z\" type=\"number\" step=\"0.001\" value=\"0.0\">");		
//					pw.append("</td>");
//					pw.append("</tr>");
//
//
//					pw.append("<tr>");
//					pw.append("<td>");
//					pw.append("X Rotation");
//					pw.append("</td>");
//					pw.append("<td>");
//					pw.append("<input name=\"xr\" type=\"number\" step=\"1.0\" value=\"0.0\">");		
//					pw.append("</td>");
//					pw.append("</tr>");
//
//					pw.append("<tr>");
//					pw.append("<td>");
//					pw.append("Y Rotation");
//					pw.append("</td>");
//					pw.append("<td>");
//					pw.append("<input name=\"yr\" type=\"number\" step=\"1.0\" value=\"0.0\">");		
//					pw.append("</td>");
//					pw.append("</tr>");
//
//					pw.append("<tr>");
//					pw.append("<td>");
//					pw.append("Z Rotation");
//					pw.append("</td>");
//					pw.append("<td>");
//					pw.append("<input name=\"zr\" type=\"number\" step=\"1.0\" value=\"0.0\">");		
//					pw.append("</td>");
//					pw.append("</tr>");
//
//					pw.append("<tr>");
//					pw.append("<td>");
//					pw.append("Plan Width");
//					pw.append("</td>");
//					pw.append("<td>");
//					pw.append("<input name=\"width\" type=\"number\" step=\"0.001\" value=\"1.0\">");		
//					pw.append("</td>");
//					pw.append("</tr>");
//
//					pw.append("<tr>");
//					pw.append("<td>");
//					pw.append("Plan Height");
//					pw.append("</td>");
//					pw.append("<td>");
//					pw.append("<input name=\"height\" type=\"number\" step=\"0.001\" value=\"1.0\">");
//					pw.append("</td>");
//					pw.append("</tr>");
//
//					pw.append("<tr>");
//					pw.append("<td>");
//					pw.append("Ceiling Height");
//					pw.append("</td>");
//					pw.append("<td>");
//					pw.append("<input name=\"ceiling_height\" type=\"number\" step=\"0.001\" value=\"1.0\">");
//					pw.append("</td>");
//					pw.append("</tr>");
//
//					pw.append("</table>");					
//					pw.append("<button class=\"btn btn-default\" type='submit'>Set</button>");
//					pw.append("</form>");

				}
				//remove a room
			}
		}
	}
	
	public static void addSubShapeForm(PrintWriter pw, String type,ObjectLanguageInstance r,String[] shapes)
	{
		CSGShape csg = (CSGShape)r.physicalRepresentation.shape;
		if(csg.shapes.length>0)
		{
			pw.append("<h4>Remove Sub Shape:</h4>");
			
			pw.append("<form action='/Edit"+type+"Instance.cgi/removeSubShape' method='get' role='form'>\n");
			pw.append("<input name=\"instance\" type=\"hidden\" value=\""+r.uniqueID+"\">");
			
			pw.append("<select name=\"shape\">");
			
			for(int si=0;si<csg.shapes.length;si++)
			{
				pw.append("<option value=\""+si+"\">"+csg.shapeNames[si]+"</option>");
			}
			
			pw.append("</select>");
			
			pw.append("<button class=\"btn btn-default\" type='submit'>Remove</button>");
			pw.append("</form>");
		}
		
		pw.append("<h4>Add Sub Shape:</h4>");

	pw.append("<form action='/Edit"+type+"Instance.cgi/addSubShape' method='get' role='form'>\n");
	pw.append("<input name=\"instance\" type=\"hidden\" value=\""+r.uniqueID+"\">");

	pw.append("<table>");
	
	pw.append("<tr>");
	pw.append("<td>");
	pw.append("Name");
	pw.append("</td>");
	pw.append("<td>");
	pw.append("<input name=\"name\" type=\"text\" value=\"objectname\">");
	pw.append("</td>");
	pw.append("</tr>");

	pw.append("</table>");

	pw.append("<select name=\"shape\">");
	
	for(int si=0;si<shapes.length;si++)
	{
		if(shapes[si].equalsIgnoreCase("CSGShape"))
			continue;
		
		if((r.physicalRepresentation.shape!=null)&&(r.physicalRepresentation.shape.getClass().getName().equalsIgnoreCase(shapes[si])))
			pw.append("<option value=\""+shapes[si]+"\" selected>"+shapes[si]+"</option>");
		else
			pw.append("<option value=\""+shapes[si]+"\">"+shapes[si]+"</option>");
	}
	
	pw.append("</select>");
	
	pw.append("<h5>Sub Shape Position and Dimensions</h5>");
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
	pw.append("<input name=\"y\" type=\"number\" step=\"0.001\" value=\"0.0\">");		
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
	pw.append("X Rotation");
	pw.append("</td>");
	pw.append("<td>");
	pw.append("<input name=\"xr\" type=\"number\" step=\"1.0\" value=\"0.0\">");		
	pw.append("</td>");
	pw.append("</tr>");

	pw.append("<tr>");
	pw.append("<td>");
	pw.append("Y Rotation");
	pw.append("</td>");
	pw.append("<td>");
	pw.append("<input name=\"yr\" type=\"number\" step=\"1.0\" value=\"0.0\">");		
	pw.append("</td>");
	pw.append("</tr>");

	pw.append("<tr>");
	pw.append("<td>");
	pw.append("Z Rotation");
	pw.append("</td>");
	pw.append("<td>");
	pw.append("<input name=\"zr\" type=\"number\" step=\"1.0\" value=\"0.0\">");		
	pw.append("</td>");
	pw.append("</tr>");

	pw.append("<tr>");
	pw.append("<td>");
	pw.append("Plan Width");
	pw.append("</td>");
	pw.append("<td>");
	pw.append("<input name=\"width\" type=\"number\" step=\"0.001\" value=\"1.0\">");		
	pw.append("</td>");
	pw.append("</tr>");

	pw.append("<tr>");
	pw.append("<td>");
	pw.append("Plan Height");
	pw.append("</td>");
	pw.append("<td>");
	pw.append("<input name=\"height\" type=\"number\" step=\"0.001\" value=\"1.0\">");
	pw.append("</td>");
	pw.append("</tr>");

	pw.append("<tr>");
	pw.append("<td>");
	pw.append("Ceiling Height");
	pw.append("</td>");
	pw.append("<td>");
	pw.append("<input name=\"ceiling_height\" type=\"number\" step=\"0.001\" value=\"1.0\">");
	pw.append("</td>");
	pw.append("</tr>");

	pw.append("</table>");					
	pw.append("<button class=\"btn btn-default\" type='submit'>Add Sub Shape</button>");
	pw.append("</form>");
}
	
	public static void printObjects(ObjectLanguageInstance o,PrintWriter pw,FileStoreInterface fs)
	{
		pw.append("<h2>Object</h2>");
		
		printType(o, "Object", o.type, pw, fs);
		
		pw.append("<h3>Add New Object</h3>");

		//add a (new) room instance to the map
		pw.append("<form action='/Edit"+o.type+"Instance.cgi/addObject' method='get' role='form'>\n");
		
		pw.append("<table>");
		
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Name");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"name\" type=\"text\" value=\"objectname\">");
		pw.append("</td>");
		pw.append("</tr>");

		pw.append("</table>");

		GeneralMatrixString roomconcepts = new GeneralMatrixString(1);
		EditUtils.getConcepts("object", roomconcepts);
		
		pw.append("<h4>Object Type</h4>");
		pw.append("<select name=\"type\">");
		for(int i=0;i<roomconcepts.height;i++)
		{
			pw.append("<option value=\""+roomconcepts.value[i]+"\">"+roomconcepts.value[i]+"</option>");
		}
		pw.append("</select>");

		pw.append("<h4>Object Position and Dimensions</h4>");
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
		pw.append("X Rotation");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"xr\" type=\"number\" step=\"1.0\" value=\"0.0\">");		
		pw.append("</td>");
		pw.append("</tr>");
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Y Rotation");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"yr\" type=\"number\" step=\"1.0\" value=\"0.0\">");		
		pw.append("</td>");
		pw.append("</tr>");
		pw.append("<tr>");
		pw.append("<td>");
		pw.append("Z Rotation");
		pw.append("</td>");
		pw.append("<td>");
		pw.append("<input name=\"zr\" type=\"number\" step=\"1.0\" value=\"0.0\">");		
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
		
		pw.append("<h4>Object Shape</h4>");
		pw.append("<select name=\"shape\">");
		for(int i=0;i<ParametricShape.objectShapes.length;i++)
		{
			pw.append("<option value=\""+ParametricShape.objectShapes[i]+"\">"+ParametricShape.objectShapes[i]+"</option>");
		}
		pw.append("</select>");

		pw.append("<select name=\"shapeToCopyFrom\">");
		for(int i=0;i<shapesToCopy.height;i++)
		{
			pw.append("<option value=\""+shapesToPaths.value[i]+"\">"+shapesToCopy.value[i]+"</option>");
		}
		pw.append("</select>");

		pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
		
		//pw.append("<a class=\"btn btn-primary btn-block\" href='http://www.google.com' onclick='return checkLogin()'>Login</a>");
		pw.append("<button class=\"btn btn-default\" type='submit'>Add</button>");
		pw.append("</form>");
	}

	public static void printShape(ObjectLanguageInstance o,PrintWriter pw,FileStoreInterface fs)
	{
//		//Set the parametric shape of this map
//		if(o.physicalRepresentation.shape==null)
//		{
//
//		}
//		else
//		{
//			
//			StateToSVG.mapToSVG(pw,fs,o);
//			//create or edit the shape of the map			
//
//			//Adjust the shape parameters as appropriate			
//		}

		pw.append("<h3>Set Shape:</h3>");
		pw.append("<form action='/Edit"+o.type+"Instance.cgi/setShape' method='get' role='form'>\n");
		
		pw.append("<select name=\"shape\">");
		
		String[] shapes = ParametricShape.getShapesByType(o.type);
		for(int i=0;i<shapes.length;i++)
		{
			if((o.physicalRepresentation.shape!=null)&&(o.physicalRepresentation.shape.getClass().getName().equalsIgnoreCase(shapes[i])))
				pw.append("<option value=\""+shapes[i]+"\" selected>"+shapes[i]+"</option>");
			else
				pw.append("<option value=\""+shapes[i]+"\">"+shapes[i]+"</option>");
		}
		
		pw.append("</select>");

		pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
		
		//pw.append("<a class=\"btn btn-primary btn-block\" href='http://www.google.com' onclick='return checkLogin()'>Login</a>");
		pw.append("<button class=\"btn btn-default\" type='submit'>Set</button>");
		pw.append("</form>");
		
		ParametricShape shape = o.physicalRepresentation.shape;
		if(shape!=null)
		{
			pw.append("<form action='/Edit"+o.type+"Instance.cgi/setShapeParameters' method='get' role='form'>\n");
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
			
			if(shape instanceof CSGShape)
			{
				addSubShapeForm(pw, o.type, o, shapes);
			}
		}		
	}
	
	public static void processFunction(WebRequest toProcess,String function,ObjectLanguageInstance o,String instance,FileStoreInterface fs)
	{
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
		if(function.equalsIgnoreCase("addObject"))
		{
			//Create a default instance of a room (using a model)
			ObjectLanguageInstance r = new ObjectLanguageInstance();
//			name
//			type
			r.name = toProcess.parms.get("name");
			r.name = r.name.replace(' ', '_');
			r.type = "object";
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
    			ParametricShape shape = null;
    			if(toProcess.parms.get("shape").equalsIgnoreCase("ModifiedCopyShape"))
    			{
    				String ocopypath = toProcess.parms.get("shapeToCopyFrom");
    				ObjectLanguageInstance ocopy = ObjectLanguageInstance.loadObject(fs, ocopypath);
    				ParametricShape shapeToCopy = ocopy.physicalRepresentation.shape;
    				ModifiedCopyShape mshape = new ModifiedCopyShape();
    				mshape.baseCopy = shapeToCopy;
    				mshape.baseUpdated();
    				shape = mshape;
    			}
    			else
    			{
    				shape = ParametricShape.get(toProcess.parms.get("shape"));
    			}
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
    					Double.parseDouble(toProcess.parms.get("z")),
    					Double.parseDouble(toProcess.parms.get("xr")),
    					Double.parseDouble(toProcess.parms.get("yr")),
    					Double.parseDouble(toProcess.parms.get("zr"))
    					);
    			rpath += "/data.txt";
    			ObjectLanguageInstance.saveObject(r, fs, rpath);
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
    		double xr = Double.parseDouble(toProcess.parms.get("xr"));
    		double yr = Double.parseDouble(toProcess.parms.get("yr"));
    		double zr = Double.parseDouble(toProcess.parms.get("zr"));
    		
    		int cind = ArrayUtils.getIndex(o.contains,rid);
    		
    		GeneralMatrixDouble zrot = new GeneralMatrixDouble(4,4);
    		zrot.set3DTransformRotation(xr*Math.PI/180.0, yr*Math.PI/180.0, zr*Math.PI/180.0);

    		o.physicalRepresentation.contains_metric_transform[12*cind+ 0] = zrot.value[4*0+0];
    		o.physicalRepresentation.contains_metric_transform[12*cind+ 1] = zrot.value[4*0+1];
    		o.physicalRepresentation.contains_metric_transform[12*cind+ 2] = zrot.value[4*0+2];

    		o.physicalRepresentation.contains_metric_transform[12*cind+ 3] = zrot.value[4*1+0];
    		o.physicalRepresentation.contains_metric_transform[12*cind+ 4] = zrot.value[4*1+1];
    		o.physicalRepresentation.contains_metric_transform[12*cind+ 5] = zrot.value[4*1+2];

    		o.physicalRepresentation.contains_metric_transform[12*cind+ 6] = zrot.value[4*2+0];
    		o.physicalRepresentation.contains_metric_transform[12*cind+ 7] = zrot.value[4*2+1];
    		o.physicalRepresentation.contains_metric_transform[12*cind+ 8] = zrot.value[4*2+2];

    		o.physicalRepresentation.contains_metric_transform[12*cind+ 9] = x;
    		o.physicalRepresentation.contains_metric_transform[12*cind+10] = y;
    		o.physicalRepresentation.contains_metric_transform[12*cind+11] = z;

    		int wind = ArrayUtils.getIndex(r.within,o.uniqueID);
    		
    		System.arraycopy(o.physicalRepresentation.contains_metric_transform, 12*cind, 
    				r.physicalRepresentation.within_metric_transform, 12*wind, 
    				12);
//    		r.physicalRepresentation.within_metric_transform[12*wind+ 9] = x;
//    		r.physicalRepresentation.within_metric_transform[12*wind+10] = y;
//    		r.physicalRepresentation.within_metric_transform[12*wind+11] = z;
    		
			ObjectLanguageInstance.saveObject(r, fs, rdatapath);
		}
		else
		if(function.equalsIgnoreCase("removeSubShape"))
		{
			int shapeind = Integer.parseInt(toProcess.parms.get("shape"));
			CSGShape csg = (CSGShape)o.physicalRepresentation.shape;
			csg.removeSubShape(shapeind);
		}
		else
		if(function.equalsIgnoreCase("addSubShape"))
		{
			CSGShape csg = (CSGShape)o.physicalRepresentation.shape;

			ParametricShape shape = ParametricShape.get(toProcess.parms.get("shape"));
			
			shape.setCuboidDimensions(Double.parseDouble(toProcess.parms.get("width")), 
					Double.parseDouble(toProcess.parms.get("height")),
					Double.parseDouble(toProcess.parms.get("ceiling_height")));

    		double x = Double.parseDouble(toProcess.parms.get("x"));
    		double y = Double.parseDouble(toProcess.parms.get("y"));
    		double z = Double.parseDouble(toProcess.parms.get("z"));
    		double xr = Double.parseDouble(toProcess.parms.get("xr"));
    		double yr = Double.parseDouble(toProcess.parms.get("yr"));
    		double zr = Double.parseDouble(toProcess.parms.get("zr"));

    		String name = toProcess.parms.get("name");
			
    		csg.addShape(shape, name, CSGShape.ADD, x, y, z, xr, yr, zr);
		}
	}
}
