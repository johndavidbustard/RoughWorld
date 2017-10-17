package website.views;

import java.io.OutputStream;
import java.io.PrintWriter;

import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import ai.knowledge_representation.state_instance.ObjectPhysicalInstance;
import ai.knowledge_representation.state_instance.PoseLanguageInstance;
import ai.model_construction.state_model.PoseLanguageModel;
import ai.model_construction.state_model.PosePhysicalModel;
import tools.visualisation.state_visualisation.ToSVG.StateToSVG;
import utils.ArrayUtils;
import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.GeneralMatrixInt;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;
import utils.constraints.IKRelativeToShapeSurface;
import utils.constraints.PhysicalConstraint;
import utils.shapes.ParametricShape;
import utils.shapes.human.HumanBones;
import web.WebRequest;
import web.WebResponse;

public class EditPoseModel extends DynamicWebPage
{
	public EditPoseModel(FileStoreInterface fs)
	{
		super(fs);
	}
	public boolean process(WebRequest toProcess)
	{
        if(toProcess.path.startsWith("EditPoseModel.html"))
        {
        	return true;
        }
        else
            if(
            		(toProcess.path.startsWith("EditPoseModel.scgi"))||
            		(toProcess.path.startsWith("EditposeModel.scgi"))
            		)
        {
        		int ind = toProcess.path.indexOf("/");
        		String function = toProcess.path.substring(ind+1);

        		PoseLanguageModel o = null;
        		if(function.equalsIgnoreCase("createModel"))
        		{
        			String concept = toProcess.parms.get("concept");
        			String instance = toProcess.parms.get("name");
        			o = new PoseLanguageModel();
        			o.uniqueID = concept+"/"+instance;
        			o.name = instance;
        			o.type = "pose";
        			
        			o.physicalRepresentation = new PosePhysicalModel();
        			
            		String instancepath = "Concepts/"+concept+"/models/"+instance;
        			fs.createDirectory("Concepts/"+concept+"/models/");
        			fs.createDirectory("Concepts/"+concept+"/models/"+instance);
            		String datapath = instancepath+"/data.txt";
        			PoseLanguageModel.saveObject(o, fs, datapath);
        		}
        		
        		String result = "<a href='/EditPoseModel.html/"+o.uniqueID+"'>"+o.uniqueID+"</a>";
        		
    			toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML,result);
    			return true;
        }
        else
            if(
            		(toProcess.path.startsWith("EditPoseModel.cgi"))||
            		(toProcess.path.startsWith("EditposeModel.cgi"))
            		)
        {
    		String id = toProcess.parms.get("instance");
    		
    		String concept = id;
    		int ind = concept.indexOf("/");
    		String instance = concept.substring(ind+1);
    		concept = concept.substring(0,ind);

    		String instancepath = "Concepts/"+concept+"/models/"+instance;
    		
    		String datapath = instancepath+"/data.txt";
    		
    		PoseLanguageModel o = PoseLanguageModel.loadObject(fs, datapath);

    		ind = toProcess.path.indexOf("/");
    		String function = toProcess.path.substring(ind+1);

    		if(function.equalsIgnoreCase("addCharacter"))
    		{
    			String cconcept = toProcess.parms.get("concept");
    			o.character_concepts = ArrayUtils.add(o.character_concepts, cconcept);
    			PoseLanguageModel.saveObject(o, fs, datapath);
    		}
    		else
    		if(function.equalsIgnoreCase("addObject"))
    		{
    			String cconcept = toProcess.parms.get("concept");
    			o.object_concepts = ArrayUtils.add(o.object_concepts, cconcept);
    		}
    		else
    		if(function.equalsIgnoreCase("addConstraint"))
    		{
    			String type = toProcess.parms.get("type");
    			GeneralMatrixInt constrainees = new GeneralMatrixInt(1);
    			int cind = 0;
    			while(true)
    			{
    				String constrainee = toProcess.parms.get("constrainee"+cind);
    				if(constrainee==null)
    					break;
    				if(!constrainee.equalsIgnoreCase("unused"))
    				{
    					int ival = Integer.parseInt(constrainee);
    					constrainees.push_back(ival);
    				}
    				cind++;
    			}
    			PhysicalConstraint constraint = PhysicalConstraint.get(type);
    			constraint.constrainees = new int[constrainees.height];
    			System.arraycopy(constrainees.value,0, constraint.constrainees, 0, constraint.constrainees.length);

    			o.physicalRepresentation.constraints = PhysicalConstraint.add(o.physicalRepresentation.constraints, constraint);
    		}
    		else
    		if(function.equalsIgnoreCase("removeConstraint"))
    		{
    			String cinds = toProcess.parms.get("constraint");
    			int cind = Integer.parseInt(cinds);
    			o.physicalRepresentation.constraints = PhysicalConstraint.remove(o.physicalRepresentation.constraints, cind, 1);
    		}
    		else
    		if(function.equalsIgnoreCase("moveConstraintUp"))
    		{
    			String cinds = toProcess.parms.get("constraint");
    			int cind = Integer.parseInt(cinds);
    			if(cind>0)
    			{
    				PhysicalConstraint p = o.physicalRepresentation.constraints[cind-1];
    				PhysicalConstraint n = o.physicalRepresentation.constraints[cind];
    				o.physicalRepresentation.constraints[cind-1] = n;
    				o.physicalRepresentation.constraints[cind] = p;
    			}
    		}
    		else
    		if(function.equalsIgnoreCase("moveConstraintFirst"))
    		{
    			String cinds = toProcess.parms.get("constraint");
    			int cind = Integer.parseInt(cinds);
    			if(cind>0)
    			{
    				PhysicalConstraint p = o.physicalRepresentation.constraints[0];
    				PhysicalConstraint n = o.physicalRepresentation.constraints[cind];
    				o.physicalRepresentation.constraints[0] = n;
    				o.physicalRepresentation.constraints[cind] = p;
    			}
    		}
    		else
    		if(function.equalsIgnoreCase("moveConstraintDown"))
    		{
    			String cinds = toProcess.parms.get("constraint");
    			int cind = Integer.parseInt(cinds);
    			if((cind+1)<o.physicalRepresentation.constraints.length)
    			{
    				PhysicalConstraint p = o.physicalRepresentation.constraints[cind];
    				PhysicalConstraint n = o.physicalRepresentation.constraints[cind+1];
    				o.physicalRepresentation.constraints[cind] = n;
    				o.physicalRepresentation.constraints[cind+1] = p;
    			}
    		}
    		else
    		if(function.equalsIgnoreCase("moveConstraintLast"))
    		{
    			String cinds = toProcess.parms.get("constraint");
    			int cind = Integer.parseInt(cinds);
    			if((cind+1)<o.physicalRepresentation.constraints.length)
    			{
    				PhysicalConstraint p = o.physicalRepresentation.constraints[cind];
    				PhysicalConstraint n = o.physicalRepresentation.constraints[o.physicalRepresentation.constraints.length-1];
    				o.physicalRepresentation.constraints[cind] = n;
    				o.physicalRepresentation.constraints[o.physicalRepresentation.constraints.length-1] = p;
    			}
    		}
    		else
    		if(function.equalsIgnoreCase("setConstraintParameters"))
    		{
    			String cinds = toProcess.parms.get("constraint");
    			int cind = Integer.parseInt(cinds);
    			
    			PhysicalConstraint constraint = o.physicalRepresentation.constraints[cind];
    			
    			String[] pnames = constraint.getParameterNames();    			
    			for(int i=0;i<pnames.length;i++)
    			{
    				String v = toProcess.parms.get(pnames[i]);
    				constraint.parameters[i] = Double.parseDouble(v);
    			}
    			String[] bnames = constraint.getBoneNames();    			
    			for(int i=0;i<bnames.length;i++)
    			{
    				String v = toProcess.parms.get(bnames[i]);
    				constraint.bones[i] = Integer.parseInt(v);
    			}
    			String[] snames = constraint.getShapeNames();    			
    			for(int i=0;i<snames.length;i++)
    			{
    				String v = toProcess.parms.get(snames[i]);
    				constraint.shapes[i] = v.split("/");
    			}
    			String[] ssides = constraint.getShapeSideNames();    			
    			for(int i=0;i<ssides.length;i++)
    			{
    				String v = toProcess.parms.get(ssides[i]);
    				constraint.shape_side[i] = v;
    			}
    			
    			constraint.parametersUpdated();
    		}
    		
    		String result = "<a href='/EditPoseModel.html/"+o.uniqueID+"'>"+o.uniqueID+"</a>";
    		
			PoseLanguageModel.saveObject(o, fs, datapath);

			toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML,result);
			return true;
        }
		return false;
	} 
	
	public void out(WebRequest r,Object metadata,OutputStream s)
	{
		PrintWriter pw = new PrintWriter( s );
		
		//load up a model
		
		int ind = r.path.indexOf("/");
		
		if(ind==-1)
		{
			//be able to create a new instance of a pose model
			pw.append("<form action='/EditPoseModel.scgi/createModel' method='get' role='form'>\n");
		
			GeneralMatrixString pconcepts = new GeneralMatrixString(1);
			EditUtils.findConcepts("pose", pconcepts);
			pw.append("<select name=\"concept\">");
			for(int ci=0;ci<pconcepts.height;ci++)
			{
				pw.append("<option value=\""+pconcepts.value[ci]+"\">"+pconcepts.value[ci]+"</option>");
			}
			pw.append("</select>");
			
			pw.append("<input name=\"name\" type=\"txt\" value=\"model_name\">");
			
			pw.append("<button class=\"btn btn-default\" type='submit'>Add</button>");

			pw.append("</form>");

			pw.flush();
			return;
		}
		
		String concept = r.path.substring(ind+1);
		ind = concept.indexOf("/");
		String instance = concept.substring(ind+1);
		concept = concept.substring(0,ind);

		String instancepath = "Concepts/"+concept+"/models/"+instance;
		
		String datapath = instancepath+"/data.txt";
		
		PoseLanguageModel o = PoseLanguageModel.loadObject(fs, datapath);
//		
//		ObjectLanguageInstance o = ObjectLanguageInstance.loadObject(fs, datapath);
		if(o==null)
		{
			o = new PoseLanguageModel();
			o.uniqueID = concept+"/"+instance;
			o.name = instance;
			o.type = "pose";
			
			o.physicalRepresentation = new PosePhysicalModel();
			
			PoseLanguageModel.saveObject(o, fs, datapath);
		}

//		if(o.object_concepts[0]==null)
//		{
//			o.object_concepts = new String[]{};
//			PoseLanguageModel.saveObject(o, fs, datapath);			
//		}
		
//		o.physicalRepresentation.constraints = new PhysicalConstraint[0];
//		PoseLanguageModel.saveObject(o, fs, datapath);
		
		//one or more character concepts
		//none or more object concepts
		
		//a model is evaluated with a specific collection of instances
		//which can be selected through get parameters

		if((o.character_concepts.length>0)||(o.object_concepts.length>0))
		{
			pw.append("<form action='/EditPoseModel.html/"+o.uniqueID+"' method='get' role='form'>\n");
	
			for(int i=0;i<o.character_concepts.length;i++)
			{
				GeneralMatrixString instances = new GeneralMatrixString(1);
				EditUtils.findInstances(o.character_concepts[i], instances);
				pw.append("<h4>Character Instance"+i);
				pw.append("<select name=\"cinst"+i+"\">");
				String selected = r.parms.get("cinst"+i);
				for(int ci=0;ci<instances.height;ci++)
				{
					if((selected!=null)&&(instances.value[i].equalsIgnoreCase(selected)))					
						pw.append("<option selected=\"selected\" value=\""+instances.value[ci]+"\">"+instances.value[ci]+"</option>");
					else
						pw.append("<option value=\""+instances.value[ci]+"\">"+instances.value[ci]+"</option>");
				}
				pw.append("</select>");
				pw.append("</h4>");
			}		
	
			for(int i=0;i<o.object_concepts.length;i++)
			{
				GeneralMatrixString instances = new GeneralMatrixString(1);
				EditUtils.findInstances(o.object_concepts[i], instances);
				pw.append("<h4>Object Instance"+i);
				pw.append("<select name=\"oinst"+i+"\">");
				String selected = r.parms.get("oinst"+i);
				for(int ci=0;ci<instances.height;ci++)
				{
					if((selected!=null)&&(instances.value[i].equalsIgnoreCase(selected)))					
						pw.append("<option selected=\"selected\" value=\""+instances.value[ci]+"\">"+instances.value[ci]+"</option>");
					else
						pw.append("<option value=\""+instances.value[ci]+"\">"+instances.value[ci]+"</option>");
				}
				pw.append("</select>");
				pw.append("</h4>");
			}		
	
			pw.append("<button class=\"btn btn-default\" type='submit'>Test</button>");
	
			pw.append("</form>");
		}
		
		//add
		{
			pw.append("<h4>Add Character</h4>");
			pw.append("<form action='/EditPoseModel.cgi/addCharacter' method='get' role='form'>\n");
			pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
			GeneralMatrixString cconcepts = new GeneralMatrixString(1);
			EditUtils.findConcepts("character", cconcepts);
			pw.append("<select name=\"concept\">");
			for(int ci=0;ci<cconcepts.height;ci++)
			{
				pw.append("<option value=\""+cconcepts.value[ci]+"\">"+cconcepts.value[ci]+"</option>");
			}
			pw.append("</select>");

			pw.append("<button class=\"btn btn-default\" type='submit'>Add</button>");	
			pw.append("</form>");
		}
		
		{
			pw.append("<h4>Add Object</h4>");
			pw.append("<form action='/EditPoseModel.cgi/addObject' method='get' role='form'>\n");
			pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
			GeneralMatrixString cconcepts = new GeneralMatrixString(1);
			EditUtils.findConcepts("object", cconcepts);
			pw.append("<select name=\"concept\">");
			for(int ci=0;ci<cconcepts.height;ci++)
			{
				pw.append("<option value=\""+cconcepts.value[ci]+"\">"+cconcepts.value[ci]+"</option>");
			}
			pw.append("</select>");
			pw.append("<button class=\"btn btn-default\" type='submit'>Add</button>");	
			pw.append("</form>");
		}


		{
			pw.append("<h4>Add Constraint</h4>");
			pw.append("<form action='/EditPoseModel.cgi/addConstraint' method='get' role='form'>\n");
			pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
			
			pw.append("<select name=\"type\">");
			for(int ci=0;ci<PhysicalConstraint.constraintnames.length;ci++)
			{
				pw.append("<option value=\""+PhysicalConstraint.constraintnames[ci]+"\">"+PhysicalConstraint.constraintnames[ci]+"</option>");
			}
			pw.append("</select>");
			
			//can constrain with self so +1
			for(int i=0;i<(o.object_concepts.length+o.character_concepts.length+1);i++)
			{
				pw.append("<select name=\"constrainee"+i+"\">");
				pw.append("<option value=\"unused\">unused</option>");
				for(int ci=0;ci<o.object_concepts.length;ci++)
				{
					pw.append("<option value=\""+(-(ci+1))+"\">"+o.object_concepts[ci]+"</option>");
				}
				for(int ci=0;ci<o.character_concepts.length;ci++)
				{
					pw.append("<option value=\""+ci+"\">"+o.character_concepts[ci]+"</option>");
				}
				pw.append("</select>");
			}
			
			
			pw.append("<button class=\"btn btn-default\" type='submit'>Add</button>");	
			pw.append("</form>");
		}

		//given the selected instances, and the constraints 
		//produce a model instance
		GeneralMatrixObject allObjects = new GeneralMatrixObject(1,o.character_concepts.length+o.object_concepts.length);
		GeneralMatrixDouble oTransforms = new GeneralMatrixDouble(6,allObjects.height);
		oTransforms.clear(0.0);
		
		//when rendering the pose, show instance examples of the characters and objects associated with the pose
		GeneralMatrixObject characters = new GeneralMatrixObject(1,o.character_concepts.length);
		for(int i=0;i<characters.height;i++)
		{
			String iuid = r.parms.get("cinst"+i);
			if(iuid==null)
			{
				GeneralMatrixString instances = new GeneralMatrixString(1);
				EditUtils.findInstances(o.character_concepts[i], instances);
				iuid = instances.value[0];
			}
			String path = ObjectLanguageInstance.uniqueIdToPath(iuid);
			ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, path+"/data.txt");
			characters.value[i] = c;
			allObjects.value[i] = c;
		}

		GeneralMatrixObject objects = new GeneralMatrixObject(1,o.object_concepts.length);
		for(int i=0;i<objects.height;i++)
		{
			String iuid = r.parms.get("oinst"+i);
			if(iuid==null)
			{
				GeneralMatrixString instances = new GeneralMatrixString(1);
				EditUtils.findInstances(o.object_concepts[i], instances);
				iuid = instances.value[0];
			}
			String path = ObjectLanguageInstance.uniqueIdToPath(iuid);
			ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, path+"/data.txt");
			objects.value[i] = c;
			allObjects.value[i+characters.height] = c;
		}

		
		//some of the constraints are parameterised so a pose instance is created 
		//by selecting a set of instances and some parameters
		//the value of these parameters can be selected just like the instances
		for(int i=0;i<o.physicalRepresentation.constraints.length;i++)
		{
			PhysicalConstraint constraint = o.physicalRepresentation.constraints[i];

			if(constraint instanceof IKRelativeToShapeSurface)
			{
				IKRelativeToShapeSurface ik = (IKRelativeToShapeSurface)constraint;
				if(ik.parameters.length<ik.getParameterNames().length)
				{
					double[] np = new double[ik.getParameterNames().length];
					System.arraycopy(ik.parameters, 0, np, 0, ik.parameters.length);
					np[ik.parameters.length+0] = 1.0;
					np[ik.parameters.length+1] = 0.0;
					np[ik.parameters.length+2] = 0.0;
					ik.parameters = np;
					PoseLanguageModel.saveObject(o, fs, datapath);
				}
			}
			
			pw.append("<h2>"+constraint.getClass().getName()+"</h2>");

			pw.append("<form action='/EditPoseModel.cgi/removeConstraint' method='get' role='form'>\n");
			pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
			pw.append("<input name=\"constraint\" type=\"hidden\" value=\""+i+"\">");
			pw.append("<button class=\"btn btn-default\" type='submit'>Remove</button>");
			pw.append("</form>");

			if(i!=0)
			{
				pw.append("<form action='/EditPoseModel.cgi/moveConstraintUp' method='get' role='form'>\n");
				pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
				pw.append("<input name=\"constraint\" type=\"hidden\" value=\""+i+"\">");
				pw.append("<button class=\"btn btn-default\" type='submit'>Move Up</button>");
				pw.append("</form>");

				pw.append("<form action='/EditPoseModel.cgi/moveConstraintFirst' method='get' role='form'>\n");
				pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
				pw.append("<input name=\"constraint\" type=\"hidden\" value=\""+i+"\">");
				pw.append("<button class=\"btn btn-default\" type='submit'>Move First</button>");
				pw.append("</form>");
			}
			if(i!=(o.physicalRepresentation.constraints.length-1))
			{
				pw.append("<form action='/EditPoseModel.cgi/moveConstraintDown' method='get' role='form'>\n");
				pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
				pw.append("<input name=\"constraint\" type=\"hidden\" value=\""+i+"\">");
				pw.append("<button class=\"btn btn-default\" type='submit'>Move Down</button>");
				pw.append("</form>");
				pw.append("<form action='/EditPoseModel.cgi/moveConstraintLast' method='get' role='form'>\n");
				pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
				pw.append("<input name=\"constraint\" type=\"hidden\" value=\""+i+"\">");
				pw.append("<button class=\"btn btn-default\" type='submit'>Move Last</button>");
				pw.append("</form>");
			}
			
			pw.append("<h3>Set Parameters</h3>");
			pw.append("<form action='/EditPoseModel.cgi/setConstraintParameters' method='get' role='form'>\n");
			pw.append("<input name=\"instance\" type=\"hidden\" value=\""+o.uniqueID+"\">");
			pw.append("<input name=\"constraint\" type=\"hidden\" value=\""+i+"\">");
			String[] pnames = constraint.getParameterNames();
			pw.append("<table>");
			for(int pi=0;pi<pnames.length;pi++)
			{
				pw.append("<tr>");
				pw.append("<td>");
				pw.append(pnames[pi]);
				pw.append("</td>");
				pw.append("<td>");
				pw.append("<input name=\""+pnames[pi]+"\" type=\"number\" step=\"0.001\" value=\""+String.format( "%.3f", constraint.parameters[pi])+"\">");
				
				pw.append("</td>");
				pw.append("</tr>");
			}
			pw.append("</table>");

			
			String[] bnames = constraint.getBoneNames();
			if(bnames.length!=0)
			{
				pw.append("<h3>Bones</h3>");
				pw.append("<table>");
				for(int pi=0;pi<bnames.length;pi++)
				{
//					if(constraint.bones==null)
//					{
//						constraint.bones = new int[1];
//						PoseLanguageModel.saveObject(o, fs, datapath);
//					}
					pw.append("<tr>");
					pw.append("<td>");
					pw.append(bnames[pi]);
					pw.append("</td>");
					pw.append("<td>");
					
					pw.append("<select name=\""+bnames[pi]+"\">");
					for(int ci=0;ci<HumanBones.names.length;ci++)
					{
						if(ci==constraint.bones[pi])
							pw.append("<option selected=\"selected\" value=\""+ci+"\">"+HumanBones.names[ci]+"</option>");
						else
							pw.append("<option value=\""+ci+"\">"+HumanBones.names[ci]+"</option>");
					}
					pw.append("</select>");

					pw.append("</td>");
					pw.append("</tr>");
				}
				pw.append("</table>");
			}
			
			String[] snames = constraint.getShapeNames();
			if(snames.length!=0)
			{
				GeneralMatrixString possibleShapes = new GeneralMatrixString(1);
				for(int oi=0;oi<allObjects.height;oi++)
				{
					ObjectLanguageInstance oo = (ObjectLanguageInstance)allObjects.value[oi];
					oo.physicalRepresentation.shape.getShapePaths(""+oi+"/"+oo.name, possibleShapes);
					for(int ci=0;ci<oo.contains.length;ci++)
					{
						String cid = oo.contains[ci];
						
						String cpath = ObjectLanguageInstance.uniqueIdToPath(cid);
						ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, cpath+"/data.txt");
						
						ParametricShape cshape = c.physicalRepresentation.shape;
						cshape.getShapePaths(""+oi+"/"+oo.name+"/contains/"+c.name+"/", possibleShapes);
					}
				}

				pw.append("<h3>Shapes</h3>");
				pw.append("<table>");
				for(int pi=0;pi<snames.length;pi++)
				{
					String currentshapepath = "";
					String[] currentshapestrings = constraint.shapes[pi];
					for(int si=0;si<currentshapestrings.length;si++)
					{
						if(si!=0)
							currentshapepath += "/";
						currentshapepath += currentshapestrings[si];
					}
					pw.append("<tr>");
					pw.append("<td>");
					pw.append(snames[pi]);
					pw.append("</td>");
					pw.append("<td>");
					
					pw.append("<select name=\""+snames[pi]+"\">");
					
					for(int si=0;si<possibleShapes.height;si++)
					{
						if(possibleShapes.value[si].equalsIgnoreCase(currentshapepath))
							pw.append("<option selected=\"selected\" value=\""+possibleShapes.value[si]+"\">"+possibleShapes.value[si]+"</option>");
						else
							pw.append("<option value=\""+possibleShapes.value[si]+"\">"+possibleShapes.value[si]+"</option>");
					}
					//
					
					pw.append("</select>");
					
					pw.append("</td>");
					pw.append("</tr>");

				}
				pw.append("</table>");
			}
			
			String[] sinames = constraint.getShapeSideNames();
			if(sinames.length!=0)
			{
				pw.append("<h3>Side</h3>");
				pw.append("<table>");

				for(int pi=0;pi<sinames.length;pi++)
				{
					pw.append("<tr>");
					pw.append("<td>");
					pw.append(sinames[pi]);
					pw.append("</td>");
					pw.append("<td>");
					
					pw.append("<select name=\""+sinames[pi]+"\">");

					for(int si=0;si<ParametricShape.allsidenames.length;si++)
					{
						if(constraint.shape_side[pi].equalsIgnoreCase(ParametricShape.allsidenames[si]))
							pw.append("<option selected=\"selected\" value=\""+ParametricShape.allsidenames[si]+"\">"+ParametricShape.allsidenames[si]+"</option>");
						else
							pw.append("<option value=\""+ParametricShape.allsidenames[si]+"\">"+ParametricShape.allsidenames[si]+"</option>");
					}

					pw.append("</select>");
					
					pw.append("</td>");
					pw.append("</tr>");
				}
				pw.append("</table>");
			}

			pw.append("<button class=\"btn btn-default\" type='submit'>Set</button>");
			pw.append("</form>");

		}
		
		//initial conditions can also be set
		//these will then be used to see if it helps convergence of the constraints
		//if they are effective they can be saved
		
		//
		
		//for each initial state
		
		//first try to solve the constraints in turn
		for(int itr=0;itr<2;itr++)
		{
			for(int i=0;i<o.physicalRepresentation.constraints.length;i++)
			{
				PhysicalConstraint pc = (PhysicalConstraint)o.physicalRepresentation.constraints[i];
				pc.minimiseErrors(fs,characters, objects, oTransforms);
			}
		}
		
		for(int i=0;i<allObjects.height;i++)
		{
			ObjectLanguageInstance oo = (ObjectLanguageInstance)allObjects.value[i];
			if(oo!=null)
				oo.physicalRepresentation.shape.parametersUpdated();
		}
		//in some cases this may be sufficient to solve them

		//this is the initial state
		
		//iterate through the constraints calculating their cost
		
		//potentially attempt to solve the constraints e.g. using levenberg marquart
		
		
		//visualise the result
		
		//
		int[] origins = new int[3*2];
		int inset = 40;
		double scale = 400;

		StateToSVG.objectsToSVG(pw, fs, allObjects, oTransforms, 2.4,
				origins,inset,scale);
		
		StateToSVG.addConstraints(fs, o, characters, objects, oTransforms, scale, origins, pw);
		
		pw.append("</svg>");

		//list the parameters of the posed characters
		
		for(int i=0;i<characters.height;i++)
		{
			ObjectLanguageInstance c = (ObjectLanguageInstance)characters.value[i];
			pw.append("<h2>"+c.name+"</h2>");
			String[] pnames = c.physicalRepresentation.shape.getParameterNames();
			double[] parameters = c.physicalRepresentation.shape.parameters;
			pw.append("<table>");

			for(int pi=0;pi<parameters.length;pi++)
			{
				pw.append("<tr>");
				pw.append("<td>");
				pw.append(pnames[pi]);
				pw.append("</td>");
				pw.append("<td>"+parameters[pi]);
				pw.append("</td>");
				pw.append("</tr>");
			}
			pw.append("</table>");
		}
		pw.flush();
	}
}
