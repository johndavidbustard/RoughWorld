package ai.knowledge_representation.story_instance.changes;

import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import utils.ArrayUtils;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;

public class ChangeContains extends ChangeState
{
	public static final String[] parameternames = {"X","Y","Z","XR","YR","ZR"};
	public static final String[] constraineenames = {"Container","Containee"};
	public static final String[] categoryparameternames = {"change"};
	public static final String[][] categoryparametercategories = {{"add","move","remove"}};
	
	public ChangeContains()
	{
		parameters = new double[]{0.0,0.0,0.0,0.0,0.0,0.0};
		category_parameters = new int[]{0};
		changeePaths = new String[]{null,null};
	}
	
	public void change(GeneralMatrixString root,GeneralMatrixString objectpaths,GeneralMatrixObject objects,GeneralMatrixString posepaths,GeneralMatrixObject poses)
	{
		if(changeePaths[0]==null)
			return;
		if(changeePaths[1]==null)
			return;
		
		int containerind = objectpaths.find(changeePaths[0]);
		int containedind = objectpaths.find(changeePaths[1]);
		
		if(containerind==-1)
			return;
		if(containedind==-1)
			return;
		
		ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[containerind];
		ObjectLanguageInstance r = (ObjectLanguageInstance)objects.value[containedind];
		//Add
		if(category_parameters[0]==0)
		{
			//only add the instance if it doesn't already exist
			int existsind = ArrayUtils.getIndex(o.contains, r.uniqueID);
			if(existsind==-1)
			{	
    			o.add(r);
    			o.physicalRepresentation.add(r.physicalRepresentation, 
    					parameters[0], 
    					parameters[1], 
    					parameters[2], 
    					parameters[3], 
    					parameters[4], 
    					parameters[5]);
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
		return parameternames;
	}
	public String[] getChangeeNames()
	{
		return constraineenames;
	}
	public String[] getPoseNames()
	{
		return empty;
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
