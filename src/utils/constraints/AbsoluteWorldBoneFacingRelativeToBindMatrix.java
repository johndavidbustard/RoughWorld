package utils.constraints;

import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.GeneralMatrixFloat;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;
import utils.shapes.HumanShape;
import utils.shapes.skinned.Animate;
import ai.knowledge_representation.state_instance.ObjectLanguageInstance;

public class AbsoluteWorldBoneFacingRelativeToBindMatrix extends PhysicalConstraint
{
	private static final long serialVersionUID = 1L;

	public static final String[] bonename = {"bone"};
	public static final String[] parameternames = {"yX","yY","yZ","xX","xY","xZ"};
	public static final String[] constraineenames = {"Constrainee"};

	public String[] getBoneNames() {return bonename;}

	public String[] getParameterNames() { return parameternames; }
	public String[] getConstraineeNames() { return constraineenames; }
	public int numConstrainees() { return 1; }
	public int numErrorTerms() { return 3; }

	public AbsoluteWorldBoneFacingRelativeToBindMatrix()
	{
		parameters = new double[]{0.0,1.0,0.0,1.0,0.0,0.0};
		bones = new int[1];
	}
	
	public void calcErrors(FileStoreInterface fs,double[] e,int offset,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		final double DEG2RAD = Math.PI/180.0;

		int bone = bones[0];
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
				
				//what is the binding bmat
				GeneralMatrixDouble bmat = new GeneralMatrixDouble(3,3);
				bmat.setFromSubset(s.skel.bmats, bone);
				
				GeneralMatrixDouble postBindRot = new GeneralMatrixDouble(3,3);
				postBindRot.calcBasisFromY(parameters[0], parameters[1], parameters[2], 
						parameters[3], parameters[4], parameters[5]);
				
				GeneralMatrixDouble tbmat = new GeneralMatrixDouble(3,3);
				
				//GeneralMatrixDouble.mult(postBindRot, bmat, tbmat);
				GeneralMatrixDouble.mult(bmat,postBindRot, tbmat);
				
				System.arraycopy(tbmat.value, 0, s.skel.tbmats.value, 9*bone, 9);
				
				Animate.calcLocalRotationParams(s.skel.boneJoints, s.skel.boneParents, null, s.skel.tbmats, s.skel.localbindbmats, s.skel.lpos);

//				for(int i=0;i<3;i++)
//					s.parameters[3+bone*3+i] = s.skel.lpos.value[3+bone*3+i]/DEG2RAD;
				for(int i=0;i<3;i++)
					e[offset+i] = s.parameters[3+bone*3+i]-s.skel.lpos.value[3+bone*3+i]/DEG2RAD;
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
		final double DEG2RAD = Math.PI/180.0;
		int bone = bones[0];

		if(constrainees[0]<0)
		{
			int oind = (-constrainees[0])-1;
			ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
			if(o.physicalRepresentation.shape instanceof HumanShape)
			{
				HumanShape s = (HumanShape)o.physicalRepresentation.shape;
				s.parameters[(bone+1)*3+0]=parameters[0];
				s.parameters[(bone+1)*3+1]=parameters[1];
				s.parameters[(bone+1)*3+2]=parameters[2];
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
				
				//what is the binding bmat
				GeneralMatrixDouble bmat = new GeneralMatrixDouble(3,3);
				//bmat.setFromSubset(s.skel.bmats, bone);
				System.arraycopy(s.skel.bmats.value, bone*9, bmat.value, 0, 9);
				
				GeneralMatrixDouble postBindRot = new GeneralMatrixDouble(3,3);
				postBindRot.calcBasisFromY(parameters[0], parameters[1], parameters[2], 
						parameters[3], parameters[4], parameters[5]);
				
				GeneralMatrixDouble tbmat = new GeneralMatrixDouble(3,3);
				
				//GeneralMatrixDouble.mult(postBindRot, bmat, tbmat);
				GeneralMatrixDouble.mult(bmat,postBindRot, tbmat);
				
				System.arraycopy(tbmat.value, 0, s.skel.tbmats.value, 9*bone, 9);
				
				Animate.calcLocalRotationParams(s.skel.boneJoints, s.skel.boneParents, null, s.skel.tbmats, s.skel.localbindbmats, s.skel.lpos);

				for(int i=0;i<3;i++)
					s.parameters[3+bone*3+i] = s.skel.lpos.value[3+bone*3+i]/DEG2RAD;
			}
		}
	}

}
