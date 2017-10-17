package utils.constraints;

import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;
import utils.shapes.HumanShape;
import utils.shapes.ParametricShape;
import utils.shapes.human.HumanBones;
import utils.shapes.human.HumanJoints;
import utils.shapes.skinned.Animate;
import ai.knowledge_representation.state_instance.ObjectLanguageInstance;

//identify the translation needed by a hand or foot
//calculate the change in bone rotations to reposition

public class IKRelativeToShapeSurface extends PhysicalConstraint
{
	private static final long serialVersionUID = 1L;

	//how is the position determined
	//relative position in ShapeX (on surface, zero is left, 1.0 is right)
	//relative position in ShapeY (on surface)
	//relative position in ShapeZ (away from surface)

	//absolute delta in ShapeX (on surface, zero is left, 1.0 is right)
	//absolute delta in ShapeY (on surface)
	//absolute delta in ShapeZ (away from surface)

	//absolute delta in X
	//absolute delta in Y
	//absolute delta in Z

	//absolute override X weight
	//absolute override X
	//absolute override Y weight
	//absolute override Y
	//absolute override Z weight
	//absolute override Z

	public static final String[] parameternames = {
		"CRelSX","CRelSY","CRelSZ",
		"CAbsDSX","CAbsDSY","CAbsDSZ",
		"CAbsDX","CAbsDY","CAbsDZ",
		"CAbsXw","CAbsYw","CAbsZw",
		"CAbsX","CAbsY","CAbsZ",
		"ORelSX","ORelSY","ORelSZ",
		"OAbsDSX","OAbsDSY","OAbsDSZ",
		"OAbsDX","OAbsDY","OAbsDZ",
		"OAbsXw","OAbsYw","OAbsZw",
		"OAbsX","OAbsY","OAbsZ",
		"Xx","Xy","Xz",
};
	public static final String[] bonename = {"bone"};
	public static final String[] constraineenames = {"Person","Object"};
	public static final String[] shapenames = {"CharacterShape","ObjectShape"};
	public static final String[] sidenames = {"CharacterShapeFace","ObjectShapeFace"};

	public String[] getBoneNames() {return bonename;}

	public String[] getShapeNames() {return shapenames;}
	public String[] getShapeSideNames() {return sidenames;}

	public String[] getParameterNames() { return parameternames; }
	public String[] getConstraineeNames() { return constraineenames; }
	public int numConstrainees() { return 2; }
	public int numErrorTerms() { return 3; }

	public IKRelativeToShapeSurface()
	{
		parameters = new double[]{
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,

				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				
				1.0,0.0,0.0,
			};
		constrainees = new int[]{0,-1};
		shapes = new String[2][1];
		shape_side = new String[2];
		shape_side[0] = ParametricShape.allsidenames[0];
		shape_side[1] = ParametricShape.allsidenames[0];
		bones = new int[1];
		bones[0] = 0;
	}
	
	public void calcErrors(FileStoreInterface fs,double[] e,int offset,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		GeneralMatrixDouble surfaceTransform = new GeneralMatrixDouble(4,4);
		surfaceTransform.setIdentity();
		
		{
			int oind = (-constrainees[1])-1;
			ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
			PhysicalConstraint.getShapeTransform(fs,o,shapes[1],2,shape_side[1],
					parameters[15+0],parameters[15+1],parameters[15+2],
					surfaceTransform);
		}
		
		double tx = surfaceTransform.value[4*3+0];
		double ty = surfaceTransform.value[4*3+1];
		double tz = surfaceTransform.value[4*3+2];
		
//		"AbsDSX","AbsDSY","AbsDSZ",
//		"AbsDX","AbsDY","AbsDZ",
//		"AbsXw","AbsYw","AbsZw",
//		"AbsX","AbsY","AbsZ"};

		
		int cind = constrainees[0];
		ObjectLanguageInstance o = (ObjectLanguageInstance)characters.value[cind];
		if(o.physicalRepresentation.shape instanceof HumanShape)
		{
			HumanShape s = (HumanShape)o.physicalRepresentation.shape;
			s.parametersUpdated();
			PhysicalConstraint.getShapeTransform(fs,o,shapes[0],2,shape_side[0],
					parameters[0],parameters[1],parameters[2],
					surfaceTransform);
			double ex = tx-surfaceTransform.value[4*3+0];//s.parameters[0];
			double ey = ty-surfaceTransform.value[4*3+1];//s.parameters[1];
			double ez = tz-surfaceTransform.value[4*3+2];//s.parameters[2];
			e[offset+0] = ex;
			e[offset+1] = ey;
			e[offset+2] = ez;
		}

	}
	
	
	public void minimiseErrors(FileStoreInterface fs,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		for(int itr=0;itr<2;itr++)
		{
			final double DEG2RAD = Math.PI/180.0;
			GeneralMatrixDouble objectsurfaceTransform = new GeneralMatrixDouble(4,4);
			objectsurfaceTransform.setIdentity();
			
			if(shapes[1][0]!=null)
			{
				int oind = (-constrainees[1])-1;
				ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
				PhysicalConstraint.getShapeTransform(fs,o,shapes[1],2,shape_side[1],
						parameters[15+0],parameters[15+1],parameters[15+2],
						objectsurfaceTransform);
				objectsurfaceTransform.value[4*3+0] += parameters[15+3*2+0];
				objectsurfaceTransform.value[4*3+1] += parameters[15+3*2+1];
				objectsurfaceTransform.value[4*3+2] += parameters[15+3*2+2];
				
				double weightx = parameters[15+3*3+0];
				double wx = parameters[15+3*4+0];
				double iweightx = 1.0-weightx;
				objectsurfaceTransform.value[4*3+0] *= iweightx; 
				objectsurfaceTransform.value[4*3+0] += wx*weightx;
				double weighty = parameters[15+3*3+1];
				double wy = parameters[15+3*4+1];
				double iweighty = 1.0-weighty;
				objectsurfaceTransform.value[4*3+1] *= iweighty; 
				objectsurfaceTransform.value[4*3+1] += wy*weighty;
				double weightz = parameters[15+3*3+2];
				double wz = parameters[15+3*4+2];
				double iweightz = 1.0-weightz;
				objectsurfaceTransform.value[4*3+2] *= iweightz; 
				objectsurfaceTransform.value[4*3+2] += wz*weightz;
			}
			
			double tx = objectsurfaceTransform.value[4*3+0];
			double ty = objectsurfaceTransform.value[4*3+1];
			double tz = objectsurfaceTransform.value[4*3+2];
			
	//		"AbsDSX","AbsDSY","AbsDSZ",
	//		"AbsDX","AbsDY","AbsDZ",
	//		"AbsXw","AbsYw","AbsZw",
	//		"AbsX","AbsY","AbsZ"};
	
			GeneralMatrixDouble charactersurfaceTransform = new GeneralMatrixDouble(4,4);
			charactersurfaceTransform.setIdentity();
	
			
			int cind = constrainees[0];
			ObjectLanguageInstance o = (ObjectLanguageInstance)characters.value[cind];
			if((o.physicalRepresentation.shape instanceof HumanShape)&&(shapes[0][0]!=null))
			{
				HumanShape s = (HumanShape)o.physicalRepresentation.shape;
				s.parametersUpdated();
				PhysicalConstraint.getShapeTransform(fs,o,shapes[0],2,shape_side[0],
						parameters[0],parameters[1],parameters[2],
						charactersurfaceTransform);
				charactersurfaceTransform.value[4*3+0] += parameters[3*2+0];
				charactersurfaceTransform.value[4*3+1] += parameters[3*2+1];
				charactersurfaceTransform.value[4*3+2] += parameters[3*2+2];
				
				double weightx = parameters[3*3+0];
				double wx = parameters[3*4+0];
				double iweightx = 1.0-weightx;
				charactersurfaceTransform.value[4*3+0] *= iweightx; 
				charactersurfaceTransform.value[4*3+0] += wx*weightx;
				double weighty = parameters[3*3+1];
				double wy = parameters[3*4+1];
				double iweighty = 1.0-weighty;
				charactersurfaceTransform.value[4*3+1] *= iweighty; 
				charactersurfaceTransform.value[4*3+1] += wy*weighty;
				double weightz = parameters[3*3+2];
				double wz = parameters[3*4+2];
				double iweightz = 1.0-weightz;
				charactersurfaceTransform.value[4*3+2] *= iweightz; 
				charactersurfaceTransform.value[4*3+2] += wz*weightz;
	
				double ex = tx-charactersurfaceTransform.value[4*3+0];//s.parameters[0];
				double ey = ty-charactersurfaceTransform.value[4*3+1];//s.parameters[1];
				double ez = tz-charactersurfaceTransform.value[4*3+2];//s.parameters[2];
	
				//this delta is the change that needs to apply to the joint at the start of the selected bone
				//under the possibly inaccurate assumption that moving this joint will move the point of interest the same amount
	
				double xX = 1.0;
				double xY = 0.0;
				double xZ = 0.0;
				
				double scaleBend = 1.0;
				
				boolean xNotz = false;
				
				int j0 = 0;
				int j1 = 0;
				int j2 = 0;
				int b0 = 0;
				int b1 = 0;
				if(bones[0]==HumanBones.BONE_LeftFoottoLeftFootend)
				{
					//two bones to rotate are
					b0 = HumanBones.BONE_LeftUpLegtoLeftLowLeg;
					b1 = HumanBones.BONE_LeftLowLegtoLeftFoot;
					
					//Three joints looking at are
					j0 = HumanBones.bones[b0*2+0];
					j1 = HumanBones.bones[b0*2+1];
					j2 = HumanBones.bones[b1*2+1];
				}
				else
				if(bones[0]==HumanBones.BONE_RightFoottoRightFootend)
				{
					//two bones to rotate are
					b0 = HumanBones.BONE_RightUpLegtoRightLowLeg;
					b1 = HumanBones.BONE_RightLowLegtoRightFoot;
					
					//Three joints looking at are
					j0 = HumanBones.bones[b0*2+0];
					j1 = HumanBones.bones[b0*2+1];
					j2 = HumanBones.bones[b1*2+1];
				}
				else
				if(bones[0]==HumanBones.BONE_LeftHandtoLeftHandend)
				{
					//two bones to rotate are
					b0 = HumanBones.BONE_LeftUpArmtoLeftLowArm;
					b1 = HumanBones.BONE_LeftLowArmtoLeftHand;
					
					//Three joints looking at are
					j0 = HumanBones.bones[b0*2+0];
					j1 = HumanBones.bones[b0*2+1];
					j2 = HumanBones.bones[b1*2+1];
					
					xNotz = true;
					scaleBend = -1.0;
				}
				else
				if(bones[0]==HumanBones.BONE_RightHandtoRightHandend)
				{
					//two bones to rotate are
					b0 = HumanBones.BONE_RightUpArmtoRightLowArm;
					b1 = HumanBones.BONE_RightLowArmtoRightHand;
					
					//Three joints looking at are
					j0 = HumanBones.bones[b0*2+0];
					j1 = HumanBones.bones[b0*2+1];
					j2 = HumanBones.bones[b1*2+1];
					xX = -1.0;
					scaleBend = -1.0;
					
					xNotz = true;
				}
				else
				{
					return;
				}
				
				xX = parameters[3*10+0];
				xY = parameters[3*10+1];
				xZ = parameters[3*10+2];

				
				//flip the delta as human is in a different space to the objects
				double j2tx = s.skel.tvpos.value[3*j2+0]+ex;
				double j2ty = s.skel.tvpos.value[3*j2+1]+ey;
				double j2tz = s.skel.tvpos.value[3*j2+2]+ez;
	
				double j0x = s.skel.tvpos.value[3*j0+0];
				double j0y = s.skel.tvpos.value[3*j0+1];
				double j0z = s.skel.tvpos.value[3*j0+2];
				
				double dx = j2tx-j0x;
				double dy = j2ty-j0y;
				double dz = j2tz-j0z;
				
				double c = Math.sqrt(dx*dx+dy*dy+dz*dz);
				double a = s.skel.bonelengths.value[b0];
				double b = s.skel.bonelengths.value[b1];
				
				if(c>(a+b))
				{
					double scale = (a+b)/c;
					dx *= scale;
					dy *= scale;
					dz *= scale;
					c = a+b;
				}
				
				//c^2 = a^2+b^2-2bcCosC
				//CosC = (a^2+b^2-c^2)/2bc
				double cosC = (a*a+b*b-(c*c))/(2.0*a*b);
				if(cosC<-1.0)
					cosC = -1.0;
				if(cosC>1.0)
					cosC = 1.0;
				double C = Math.acos(cosC);
				
				double cosB = (a*a+c*c-(b*b))/(2.0*a*c);
				if(cosB<-1.0)
					cosB = -1.0;
				if(cosB>1.0)
					cosB = 1.0;
				
				double B = Math.acos(cosB);
				
				double A = Math.PI-(C+B);
				
				GeneralMatrixDouble j0Bmat = new GeneralMatrixDouble(3,3);
				GeneralMatrixDouble j1Bmat = new GeneralMatrixDouble(3,3);
	
				GeneralMatrixDouble targetmat = new GeneralMatrixDouble(3,3);
	
				targetmat.calcBasisFromY(dx, dy, dz, 
						xX, xY, xZ);
					
				double b0dx = 0.0;
				double b0dy = cosB*a;
				double b0dz = -Math.sin(B)*a*scaleBend;
				if(xNotz)
				{
					b0dx = b0dz;
					b0dz = 0.0;
				}
	//			if(left)
	//				b0dz = -b0dz;
				
				double b0x = targetmat.value[3*0+0]*b0dx+targetmat.value[3*1+0]*b0dy+targetmat.value[3*2+0]*b0dz;
				double b0y = targetmat.value[3*0+1]*b0dx+targetmat.value[3*1+1]*b0dy+targetmat.value[3*2+1]*b0dz;
				double b0z = targetmat.value[3*0+2]*b0dx+targetmat.value[3*1+2]*b0dy+targetmat.value[3*2+2]*b0dz;
				
				j0Bmat.calcBasisFromY(b0x, b0y, b0z, 
						-targetmat.value[3*2+0], -targetmat.value[3*2+1], -targetmat.value[3*2+2]);
	
				double b1dx = 0.0;
				double b1dy = c-b0dy;
				double b1dz = -b0dz;
				if(xNotz)
				{
					b1dx = b1dz;
					b1dz = 0.0;
				}

				double b1x = targetmat.value[3*0+0]*b1dx+targetmat.value[3*1+0]*b1dy+targetmat.value[3*2+0]*b1dz;
				double b1y = targetmat.value[3*0+1]*b1dx+targetmat.value[3*1+1]*b1dy+targetmat.value[3*2+1]*b1dz;
				double b1z = targetmat.value[3*0+2]*b1dx+targetmat.value[3*1+2]*b1dy+targetmat.value[3*2+2]*b1dz;
	
				j1Bmat.calcBasisFromY(b1x, b1y, b1z, 
						targetmat.value[3*0+0], targetmat.value[3*0+1], targetmat.value[3*0+2]);
				
				System.arraycopy(j0Bmat.value, 0, s.skel.tbmats.value, 9*b0, 9);
				//System.arraycopy(j1Bmat.value, 0, s.skel.tbmats.value, 9*b1, 9);
				
				Animate.calcLocalRotationParams(s.skel.boneJoints, s.skel.boneParents, null, s.skel.tbmats, s.skel.localbindbmats, s.skel.lpos);
	
				for(int i=0;i<3;i++)
					s.parameters[3+b0*3+i] = s.skel.lpos.value[3+b0*3+i]/DEG2RAD;
//				for(int i=0;i<3;i++)
//					s.parameters[3+b1*3+i] = s.skel.lpos.value[3+b1*3+i]/DEG2RAD;
//				for(int i=0;i<3;i++)
					s.parameters[3+b1*3+0] = -(Math.PI-C)*scaleBend/DEG2RAD;
	//			System.arraycopy(s.skel.lpos.value, 0, s.parameters, 0, s.parameters.length);
				s.parametersUpdated();
				
	//			s.parameters[0]+=ex;
	//			s.parameters[1]+=ez;
	//			s.parameters[2]+=ey;
			}
		
		}
	}

	public void minimiseErrors(GeneralMatrixString objectpaths,GeneralMatrixObject wobjects,GeneralMatrixString posepaths,GeneralMatrixObject poses,
			GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		for(int itr=0;itr<2;itr++)
		{
			final double DEG2RAD = Math.PI/180.0;
			GeneralMatrixDouble objectsurfaceTransform = new GeneralMatrixDouble(4,4);
			objectsurfaceTransform.setIdentity();
			
			if(shapes[1][0]!=null)
			{
				int oind = (-constrainees[1])-1;
				ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
				PhysicalConstraint.getShapeTransform(objectpaths,wobjects,
						o,shapes[1],2,shape_side[1],
						parameters[15+0],parameters[15+1],parameters[15+2],
						objectsurfaceTransform);
				objectsurfaceTransform.value[4*3+0] += parameters[15+3*2+0];
				objectsurfaceTransform.value[4*3+1] += parameters[15+3*2+1];
				objectsurfaceTransform.value[4*3+2] += parameters[15+3*2+2];
				
				double weightx = parameters[15+3*3+0];
				double wx = parameters[15+3*4+0];
				double iweightx = 1.0-weightx;
				objectsurfaceTransform.value[4*3+0] *= iweightx; 
				objectsurfaceTransform.value[4*3+0] += wx*weightx;
				double weighty = parameters[15+3*3+1];
				double wy = parameters[15+3*4+1];
				double iweighty = 1.0-weighty;
				objectsurfaceTransform.value[4*3+1] *= iweighty; 
				objectsurfaceTransform.value[4*3+1] += wy*weighty;
				double weightz = parameters[15+3*3+2];
				double wz = parameters[15+3*4+2];
				double iweightz = 1.0-weightz;
				objectsurfaceTransform.value[4*3+2] *= iweightz; 
				objectsurfaceTransform.value[4*3+2] += wz*weightz;
			}
			
			double tx = objectsurfaceTransform.value[4*3+0];
			double ty = objectsurfaceTransform.value[4*3+1];
			double tz = objectsurfaceTransform.value[4*3+2];
			
	//		"AbsDSX","AbsDSY","AbsDSZ",
	//		"AbsDX","AbsDY","AbsDZ",
	//		"AbsXw","AbsYw","AbsZw",
	//		"AbsX","AbsY","AbsZ"};
	
			GeneralMatrixDouble charactersurfaceTransform = new GeneralMatrixDouble(4,4);
			charactersurfaceTransform.setIdentity();
	
			
			int cind = constrainees[0];
			ObjectLanguageInstance o = (ObjectLanguageInstance)characters.value[cind];
			if((o.physicalRepresentation.shape instanceof HumanShape)&&(shapes[0][0]!=null))
			{
				HumanShape s = (HumanShape)o.physicalRepresentation.shape;
				s.parametersUpdated();
				PhysicalConstraint.getShapeTransform(objectpaths,wobjects,
						o,shapes[0],2,shape_side[0],
						parameters[0],parameters[1],parameters[2],
						charactersurfaceTransform);
				charactersurfaceTransform.value[4*3+0] += parameters[3*2+0];
				charactersurfaceTransform.value[4*3+1] += parameters[3*2+1];
				charactersurfaceTransform.value[4*3+2] += parameters[3*2+2];
				
				double weightx = parameters[3*3+0];
				double wx = parameters[3*4+0];
				double iweightx = 1.0-weightx;
				charactersurfaceTransform.value[4*3+0] *= iweightx; 
				charactersurfaceTransform.value[4*3+0] += wx*weightx;
				double weighty = parameters[3*3+1];
				double wy = parameters[3*4+1];
				double iweighty = 1.0-weighty;
				charactersurfaceTransform.value[4*3+1] *= iweighty; 
				charactersurfaceTransform.value[4*3+1] += wy*weighty;
				double weightz = parameters[3*3+2];
				double wz = parameters[3*4+2];
				double iweightz = 1.0-weightz;
				charactersurfaceTransform.value[4*3+2] *= iweightz; 
				charactersurfaceTransform.value[4*3+2] += wz*weightz;
	
				double ex = tx-charactersurfaceTransform.value[4*3+0];//s.parameters[0];
				double ey = ty-charactersurfaceTransform.value[4*3+1];//s.parameters[1];
				double ez = tz-charactersurfaceTransform.value[4*3+2];//s.parameters[2];
	
				//this delta is the change that needs to apply to the joint at the start of the selected bone
				//under the possibly inaccurate assumption that moving this joint will move the point of interest the same amount
	
				double xX = 1.0;
				double xY = 0.0;
				double xZ = 0.0;
				
				double scaleBend = 1.0;
				
				boolean xNotz = false;
				
				int j0 = 0;
				int j1 = 0;
				int j2 = 0;
				int b0 = 0;
				int b1 = 0;
				if(bones[0]==HumanBones.BONE_LeftFoottoLeftFootend)
				{
					//two bones to rotate are
					b0 = HumanBones.BONE_LeftUpLegtoLeftLowLeg;
					b1 = HumanBones.BONE_LeftLowLegtoLeftFoot;
					
					//Three joints looking at are
					j0 = HumanBones.bones[b0*2+0];
					j1 = HumanBones.bones[b0*2+1];
					j2 = HumanBones.bones[b1*2+1];
				}
				else
				if(bones[0]==HumanBones.BONE_RightFoottoRightFootend)
				{
					//two bones to rotate are
					b0 = HumanBones.BONE_RightUpLegtoRightLowLeg;
					b1 = HumanBones.BONE_RightLowLegtoRightFoot;
					
					//Three joints looking at are
					j0 = HumanBones.bones[b0*2+0];
					j1 = HumanBones.bones[b0*2+1];
					j2 = HumanBones.bones[b1*2+1];
				}
				else
				if(bones[0]==HumanBones.BONE_LeftHandtoLeftHandend)
				{
					//two bones to rotate are
					b0 = HumanBones.BONE_LeftUpArmtoLeftLowArm;
					b1 = HumanBones.BONE_LeftLowArmtoLeftHand;
					
					//Three joints looking at are
					j0 = HumanBones.bones[b0*2+0];
					j1 = HumanBones.bones[b0*2+1];
					j2 = HumanBones.bones[b1*2+1];
					
					xNotz = true;
					scaleBend = -1.0;
				}
				else
				if(bones[0]==HumanBones.BONE_RightHandtoRightHandend)
				{
					//two bones to rotate are
					b0 = HumanBones.BONE_RightUpArmtoRightLowArm;
					b1 = HumanBones.BONE_RightLowArmtoRightHand;
					
					//Three joints looking at are
					j0 = HumanBones.bones[b0*2+0];
					j1 = HumanBones.bones[b0*2+1];
					j2 = HumanBones.bones[b1*2+1];
					xX = -1.0;
					scaleBend = -1.0;
					
					xNotz = true;
				}
				else
				{
					return;
				}
				
				xX = parameters[3*10+0];
				xY = parameters[3*10+1];
				xZ = parameters[3*10+2];

				
				//flip the delta as human is in a different space to the objects
				double j2tx = s.skel.tvpos.value[3*j2+0]+ex;
				double j2ty = s.skel.tvpos.value[3*j2+1]+ey;
				double j2tz = s.skel.tvpos.value[3*j2+2]+ez;
	
				double j0x = s.skel.tvpos.value[3*j0+0];
				double j0y = s.skel.tvpos.value[3*j0+1];
				double j0z = s.skel.tvpos.value[3*j0+2];
				
				double dx = j2tx-j0x;
				double dy = j2ty-j0y;
				double dz = j2tz-j0z;
				
				double c = Math.sqrt(dx*dx+dy*dy+dz*dz);
				double a = s.skel.bonelengths.value[b0];
				double b = s.skel.bonelengths.value[b1];
				
				if(c>(a+b))
				{
					double scale = (a+b)/c;
					dx *= scale;
					dy *= scale;
					dz *= scale;
					c = a+b;
				}
				
				//c^2 = a^2+b^2-2bcCosC
				//CosC = (a^2+b^2-c^2)/2bc
				double cosC = (a*a+b*b-(c*c))/(2.0*a*b);
				if(cosC<-1.0)
					cosC = -1.0;
				if(cosC>1.0)
					cosC = 1.0;
				double C = Math.acos(cosC);
				
				double cosB = (a*a+c*c-(b*b))/(2.0*a*c);
				if(cosB<-1.0)
					cosB = -1.0;
				if(cosB>1.0)
					cosB = 1.0;
				
				double B = Math.acos(cosB);
				
				double A = Math.PI-(C+B);
				
				GeneralMatrixDouble j0Bmat = new GeneralMatrixDouble(3,3);
				GeneralMatrixDouble j1Bmat = new GeneralMatrixDouble(3,3);
	
				GeneralMatrixDouble targetmat = new GeneralMatrixDouble(3,3);
	
				targetmat.calcBasisFromY(dx, dy, dz, 
						xX, xY, xZ);
					
				double b0dx = 0.0;
				double b0dy = cosB*a;
				double b0dz = -Math.sin(B)*a*scaleBend;
				if(xNotz)
				{
					b0dx = b0dz;
					b0dz = 0.0;
				}
	//			if(left)
	//				b0dz = -b0dz;
				
				double b0x = targetmat.value[3*0+0]*b0dx+targetmat.value[3*1+0]*b0dy+targetmat.value[3*2+0]*b0dz;
				double b0y = targetmat.value[3*0+1]*b0dx+targetmat.value[3*1+1]*b0dy+targetmat.value[3*2+1]*b0dz;
				double b0z = targetmat.value[3*0+2]*b0dx+targetmat.value[3*1+2]*b0dy+targetmat.value[3*2+2]*b0dz;
				
				j0Bmat.calcBasisFromY(b0x, b0y, b0z, 
						-targetmat.value[3*2+0], -targetmat.value[3*2+1], -targetmat.value[3*2+2]);
	
				double b1dx = 0.0;
				double b1dy = c-b0dy;
				double b1dz = -b0dz;
				if(xNotz)
				{
					b1dx = b1dz;
					b1dz = 0.0;
				}

				double b1x = targetmat.value[3*0+0]*b1dx+targetmat.value[3*1+0]*b1dy+targetmat.value[3*2+0]*b1dz;
				double b1y = targetmat.value[3*0+1]*b1dx+targetmat.value[3*1+1]*b1dy+targetmat.value[3*2+1]*b1dz;
				double b1z = targetmat.value[3*0+2]*b1dx+targetmat.value[3*1+2]*b1dy+targetmat.value[3*2+2]*b1dz;
	
				j1Bmat.calcBasisFromY(b1x, b1y, b1z, 
						targetmat.value[3*0+0], targetmat.value[3*0+1], targetmat.value[3*0+2]);
				
				System.arraycopy(j0Bmat.value, 0, s.skel.tbmats.value, 9*b0, 9);
				//System.arraycopy(j1Bmat.value, 0, s.skel.tbmats.value, 9*b1, 9);
				
				Animate.calcLocalRotationParams(s.skel.boneJoints, s.skel.boneParents, null, s.skel.tbmats, s.skel.localbindbmats, s.skel.lpos);
	
				for(int i=0;i<3;i++)
					s.parameters[3+b0*3+i] = s.skel.lpos.value[3+b0*3+i]/DEG2RAD;
//				for(int i=0;i<3;i++)
//					s.parameters[3+b1*3+i] = s.skel.lpos.value[3+b1*3+i]/DEG2RAD;
//				for(int i=0;i<3;i++)
					s.parameters[3+b1*3+0] = -(Math.PI-C)*scaleBend/DEG2RAD;
	//			System.arraycopy(s.skel.lpos.value, 0, s.parameters, 0, s.parameters.length);
				s.parametersUpdated();
				
	//			s.parameters[0]+=ex;
	//			s.parameters[1]+=ez;
	//			s.parameters[2]+=ey;
			}
		
		}
	}

}