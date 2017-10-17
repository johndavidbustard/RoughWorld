package ai.knowledge_representation.story_instance.changes;

import utils.ArrayUtils;
import utils.GeneralMatrixDouble;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;
import utils.constraints.PhysicalConstraint;
import utils.shapes.ParametricShape;
import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import ai.model_construction.state_model.PoseLanguageModel;

public class ChangePose extends ChangeState
{
	//public static final String[] parameternames = {"X","Y","Z","XR","YR","ZR"};
	public static final String[] constraineenames = {"PoseContainer","Posee0","Posee1","Posee2","Posee3"};
	public static final String[] categoryparameternames = {"change"};
	public static final String[][] categoryparametercategories = {{"add","move","remove"}};
	public static final String[] poseNames = {"Pose"};


	public ChangePose()
	{
		//parameters = new double[]{0.0,0.0,0.0,0.0,0.0,0.0};
		category_parameters = new int[]{0};
		changeePaths = new String[]{null,null,null,null,null};
		posePaths = new String[]{null};
	}
	
	public void change(GeneralMatrixString root,GeneralMatrixString objectpaths,GeneralMatrixObject objects,GeneralMatrixString posepaths,GeneralMatrixObject poses)
	{
		if(posePaths[0]==null)
			return;
		if(changeePaths[0]==null)
			return;
		if(changeePaths[1]==null)
			return;
		
		int poseind = posepaths.find(posePaths[0]);
		int containerind = objectpaths.find(changeePaths[0]);
		
		if(poseind==-1)
			return;
		if(containerind==-1)
			return;

		PoseLanguageModel pm = (PoseLanguageModel)poses.value[poseind];
		ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[containerind];
		
		GeneralMatrixObject pcharacters = new GeneralMatrixObject(1);
		GeneralMatrixObject pobjects = new GeneralMatrixObject(1);
		
		for(int i=0;i<pm.character_concepts.length;i++)
		{
			int containedind = objectpaths.find(changeePaths[1+i]);

			ObjectLanguageInstance r = (ObjectLanguageInstance)objects.value[containedind];
			ParametricShape ps = r.physicalRepresentation.shape;
			for(int pi=0;pi<ps.parameters.length;pi++)
			{
				ps.parameters[pi] = 0.0;
			}
			pcharacters.push_back(r);
		}
		
		for(int i=0;i<pm.object_concepts.length;i++)
		{
			int containedind = objectpaths.find(changeePaths[1+pm.character_concepts.length+i]);

			ObjectLanguageInstance r = (ObjectLanguageInstance)objects.value[containedind];
			pobjects.push_back(r);
		}
		
		//Add
		if(category_parameters[0]==0)
		{
			/*
			//only add the instance if it doesn't already exist
			//int existsind = ArrayUtils.getIndex(o.contains_poseModels, pm.uniqueID);
			o.contains_poseModels = ArrayUtils.add(o.contains_poseModels, pm.uniqueID);
			o.contains_poses_instances = ArrayUtils.add(o.contains_poses_instances, new String[pcharacters.height+pobjects.height]);
			
			for(int i=0;i<pm.character_concepts.length;i++)
			{
				//We should really calculate the path to the instances relative to the object
				//but for this purpose we will assume that all instances are unique and just use their uniqueid
				o.contains_poses_instances[o.contains_poses_instances.length-1][i] = pm.character_concepts[i];
			}
			for(int i=0;i<pm.object_concepts.length;i++)
			{
				//We should really calculate the path to the instances relative to the object
				//but for this purpose we will assume that all instances are unique and just use their uniqueid
				o.contains_poses_instances[o.contains_poses_instances.length-1][i] = pm.object_concepts[i+pm.character_concepts.length];
			}	
			*/
			
			//clear the other pose information
			
			GeneralMatrixDouble oTransforms = new GeneralMatrixDouble(6,pcharacters.height+pobjects.height);
			oTransforms.clear(0.0);

			for(int itr=0;itr<2;itr++)
			{
				for(int i=0;i<pm.physicalRepresentation.constraints.length;i++)
				{
					PhysicalConstraint pc = (PhysicalConstraint)pm.physicalRepresentation.constraints[i];
					pc.minimiseErrors(objectpaths, objects, posepaths, poses, 
							pcharacters, pobjects, oTransforms);
				}
			}
		}
		else
		//Move
		if(category_parameters[0]==1)
		{
			
		}
		else
		//Remove
		if(category_parameters[0]==2)
		{
			
		}
	}

	public String[] getParameterNames()
	{
		return empty;
	}
	public String[] getChangeeNames()
	{
		return constraineenames;
	}
	public String[] getPoseNames()
	{
		return poseNames;
	}
	public String[] getCategoryParameterNames()
	{
		return categoryparameternames;
	}
	public String[][] getCategoryParameterCatgegories()
	{
		return categoryparametercategories;
	}
}
