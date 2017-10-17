package ai.knowledge_representation.story_instance.changes;

import utils.GeneralMatrixDouble;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;
import utils.constraints.PhysicalConstraint;
import utils.shapes.HumanShape;
import utils.shapes.ParametricShape;
import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import ai.model_construction.state_model.PoseLanguageModel;

public class ChangeRootObject extends ChangeState
{
	//root transform from bone transform

	public static final String[] parameternames = {"dX","dY","dZ","dXR","dYR","dZR"};
	public static final String[] constraineenames = {"Posee"};
	public static final String[] categoryparameternames = {"change"};
	public static final String[][] categoryparametercategories = {{"fromBoneRoot"}};
	public static final String[] poseNames = {};

	public ChangeRootObject()
	{
		parameters = new double[]{0.0,0.0,0.0,0.0,0.0,0.0};
		category_parameters = new int[]{0};
		changeePaths = new String[]{null};
		posePaths = new String[]{};
	}
	
	public void change(GeneralMatrixString root,GeneralMatrixString objectpaths,GeneralMatrixObject objects,GeneralMatrixString posepaths,GeneralMatrixObject poses)
	{
		if(changeePaths[0]==null)
			return;
		
		int containerind = objectpaths.find(changeePaths[0]);
		
		if(containerind==-1)
			return;

		ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[containerind];
				
		//fromBoneRoot
		if(category_parameters[0]==0)
		{
			ParametricShape ps = o.physicalRepresentation.shape;

			if(ps instanceof HumanShape)
			{
				HumanShape hs = (HumanShape)ps;
//				double x = hs.parameters[0];
//				double y = hs.parameters[1];
//				double z = hs.parameters[2];
//				double xr = hs.parameters[3]*Math.PI/180.0;
//				double yr = hs.parameters[4]*Math.PI/180.0;
//				double zr = hs.parameters[5]*Math.PI/180.0;
			
				GeneralMatrixDouble rootTransform = new GeneralMatrixDouble(4,4);
				rootTransform.setIdentity();
//				rootTransform.set3DTransformRotation(xr, yr, zr);
//				rootTransform.set3DTransformPosition(x, y, z);

//				System.arraycopy(hs.skel.tbmats.value, 9*0+0, rootTransform.value, 4*0+0, 3);
//				System.arraycopy(hs.skel.tbmats.value, 9*0+3, rootTransform.value, 4*1+0, 3);
//				System.arraycopy(hs.skel.tbmats.value, 9*0+6, rootTransform.value, 4*2+0, 3);
				System.arraycopy(hs.skel.tvpos.value, 3*0+0, rootTransform.value, 4*3+0, 3);
				
				
				String wid = o.within[0];
				int wind = objectpaths.find(wid);
				ObjectLanguageInstance w = (ObjectLanguageInstance)objects.value[wind];

				int cind = GeneralMatrixString.find(w.contains, o.uniqueID);

				int coff = cind*12;
				double[] ctrans = w.physicalRepresentation.contains_metric_transform;
				
				GeneralMatrixDouble containedRelativeToParenttran = new GeneralMatrixDouble(4,4);
				containedRelativeToParenttran.setIdentity();
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[ti] = ctrans[coff+ti];
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[4+ti] = ctrans[coff+3+ti];
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[8+ti] = ctrans[coff+6+ti];
				containedRelativeToParenttran.set3DTransformPosition(ctrans[coff+9], ctrans[coff+10], ctrans[coff+11]);

				GeneralMatrixDouble rootAsTran = new GeneralMatrixDouble(4,4);
				GeneralMatrixDouble.mult(rootTransform, containedRelativeToParenttran, rootAsTran);
				
				w.moveContainedTo(o,rootAsTran);
				
				for(int i=0;i<3;i++)
					hs.parameters[i] = 0.0;
				
				hs.parametersUpdated();
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
