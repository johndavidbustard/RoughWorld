package utils.constraints;

import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;

public class AbsoluteTransform extends PhysicalConstraint
{
	private static final long serialVersionUID = 1L;

	public static final String[] parameternames = {"X","Y","Z","XR","YR","ZR"};
	public static final String[] constraineenames = {"Constrainee"};

	public String[] getParameterNames() { return parameternames; }
	public String[] getConstraineeNames() { return constraineenames; }
	public int numConstrainees() { return 1; }
	public int numErrorTerms() { return 6; }

	public AbsoluteTransform()
	{
		parameters = new double[]{0.0,0.0,0.0,0.0,0.0,0.0};
	}
	
	public void calcErrors(FileStoreInterface fs,double[] e,int offset,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		if(constrainees[0]<0)
		{
			int oind = (-constrainees[0])-1;
			//ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
			double ex = transforms.value[oind*6+0]-parameters[0];
			double ey = transforms.value[oind*6+1]-parameters[1];
			double ez = transforms.value[oind*6+2]-parameters[2];
			double exr = transforms.value[oind*6+3]-parameters[3];
			double eyr = transforms.value[oind*6+4]-parameters[4];
			double ezr = transforms.value[oind*6+5]-parameters[5];
			e[offset+0] = ex;
			e[offset+1] = ey;
			e[offset+2] = ez;
			e[offset+3] = exr;
			e[offset+4] = eyr;
			e[offset+5] = ezr;
		}
		else
		{
			int cind = constrainees[0];
			//ObjectLanguageInstance o = (ObjectLanguageInstance)characters.value[cind];
			int tind = objects.height+cind;
			double ex = transforms.value[tind*6+0]-parameters[0];
			double ey = transforms.value[tind*6+1]-parameters[1];
			double ez = transforms.value[tind*6+2]-parameters[2];
			double exr = transforms.value[tind*6+3]-parameters[3];
			double eyr = transforms.value[tind*6+4]-parameters[4];
			double ezr = transforms.value[tind*6+5]-parameters[5];
			e[offset+0] = ex;
			e[offset+1] = ey;
			e[offset+2] = ez;
			e[offset+3] = exr;
			e[offset+4] = eyr;
			e[offset+5] = ezr;
		}
	}
	
	public void minimiseErrors(GeneralMatrixString objectpaths,GeneralMatrixObject objects,GeneralMatrixString posepaths,GeneralMatrixObject poses,
			GeneralMatrixObject pcharacters,GeneralMatrixObject pobjects,GeneralMatrixDouble transforms)
	{
		minimiseErrors(null, pcharacters, pobjects, transforms);
	}

	public void minimiseErrors(FileStoreInterface fs,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		if(constrainees[0]<0)
		{
			int oind = (-constrainees[0])-1;
			//ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
			transforms.value[oind*6+0]=parameters[0];
			transforms.value[oind*6+1]=parameters[1];
			transforms.value[oind*6+2]=parameters[2];
			transforms.value[oind*6+3]=parameters[3];
			transforms.value[oind*6+4]=parameters[4];
			transforms.value[oind*6+5]=parameters[5];
		}
		else
		{
			int cind = constrainees[0];
			//ObjectLanguageInstance o = (ObjectLanguageInstance)characters.value[cind];
			int tind = objects.height+cind;
			transforms.value[tind*6+0]=parameters[0];
			transforms.value[tind*6+1]=parameters[1];
			transforms.value[tind*6+2]=parameters[2];
			transforms.value[tind*6+3]=parameters[3];
			transforms.value[tind*6+4]=parameters[4];
			transforms.value[tind*6+5]=parameters[5];
		}
	}

}
