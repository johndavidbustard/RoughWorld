package utils.constraints;

import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;
import utils.shapes.HumanShape;
import ai.knowledge_representation.state_instance.ObjectLanguageInstance;

public class AbsoluteRootTranslation extends PhysicalConstraint
{
	private static final long serialVersionUID = 1L;

	public static final String[] parameternames = {"X","Y","Z"};
	public static final String[] constraineenames = {"Constrainee"};


	public String[] getParameterNames() { return parameternames; }
	public String[] getConstraineeNames() { return constraineenames; }
	public int numConstrainees() { return 1; }
	public int numErrorTerms() { return 3; }

	public AbsoluteRootTranslation()
	{
		parameters = new double[]{0.0,0.0,0.0};
		bones = new int[1];
	}
	
	public void calcErrors(FileStoreInterface fs,double[] e,int offset,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		int bone = 0;
		if(constrainees[0]<0)
		{
			int oind = (-constrainees[0])-1;
			ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
			if(o.physicalRepresentation.shape instanceof HumanShape)
			{
				HumanShape s = (HumanShape)o.physicalRepresentation.shape;
				double ex = parameters[0]-s.parameters[(bone+1)*3+0];
				double ey = parameters[1]-s.parameters[(bone+1)*3+1];
				double ez = parameters[2]-s.parameters[(bone+1)*3+2];
				e[offset+0] = ex;
				e[offset+1] = ey;
				e[offset+2] = ez;
			}
		}
		else
		{
			int cind = constrainees[0];
			ObjectLanguageInstance o = (ObjectLanguageInstance)characters.value[cind];
			int tind = objects.height+cind;
			if(o.physicalRepresentation.shape instanceof HumanShape)
			{
				HumanShape s = (HumanShape)o.physicalRepresentation.shape;
				double ex = parameters[0]-s.parameters[(bone+1)*3+0];
				double ey = parameters[1]-s.parameters[(bone+1)*3+1];
				double ez = parameters[2]-s.parameters[(bone+1)*3+2];
				e[offset+0] = ex;
				e[offset+1] = ey;
				e[offset+2] = ez;
			}
		}
	}
	
	public void minimiseErrors(GeneralMatrixString objectpaths,GeneralMatrixObject objects,GeneralMatrixString posepaths,GeneralMatrixObject poses,
			GeneralMatrixObject pcharacters,GeneralMatrixObject pobjects,GeneralMatrixDouble transforms)
	{
		minimiseErrors(null, pcharacters, pobjects, transforms);
	}

	public void minimiseErrors(FileStoreInterface fs,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		int bone = 0;

		if(constrainees[0]<0)
		{
			int oind = (-constrainees[0])-1;
			ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
			if(o.physicalRepresentation.shape instanceof HumanShape)
			{
				HumanShape s = (HumanShape)o.physicalRepresentation.shape;
				s.parameters[0]=parameters[0];
				s.parameters[1]=parameters[1];
				s.parameters[2]=parameters[2];
			}
		}
		else
		{
			int cind = constrainees[0];
			ObjectLanguageInstance o = (ObjectLanguageInstance)characters.value[cind];
			int tind = objects.height+cind;
			if(o.physicalRepresentation.shape instanceof HumanShape)
			{
				HumanShape s = (HumanShape)o.physicalRepresentation.shape;
				s.parameters[0]=parameters[0];
				s.parameters[1]=parameters[1];
				s.parameters[2]=parameters[2];
			}
		}
	}

}
