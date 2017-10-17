package utils.shapes;

import utils.GeneralMatrixDouble;
import utils.GeneralMatrixFloat;
import utils.GeneralMatrixString;
import utils.shapes.human.HumanBBmats;
import utils.shapes.human.HumanBones;
import utils.shapes.human.HumanDeformedCuboids;
import utils.shapes.human.HumanJoints;
import utils.shapes.human.HumanPos;
import utils.shapes.human.HumanSkinToBoneMapping;
import utils.shapes.human.HumanSkinToBoneWeights;
import utils.shapes.skinned.Animate;
import utils.shapes.skinned.Skeleton;
import utils.shapes.skinned.Skin;

public class HumanShape extends ParametricShape 
{
	private static final long serialVersionUID = 1L;

	public static final String[] parameternames = 
	{
		"Root_x",
		"Root_y",
		"Root_z",
		"HipstoChest0_rx",
		"HipstoChest0_ry",
		"HipstoChest0_rz",
		"HipstoChest1_rx",
		"HipstoChest1_ry",
		"HipstoChest1_rz",
		"HipstoChest2_rx",
		"HipstoChest2_ry",
		"HipstoChest2_rz",
		"ChesttoNeck_rx",
		"ChesttoNeck_ry",
		"ChesttoNeck_rz",
		"NecktoHead_rx",
		"NecktoHead_ry",
		"NecktoHead_rz",
		"HeadtoHeadend_rx",
		"HeadtoHeadend_ry",
		"HeadtoHeadend_rz",
		"Hip_L_rx",
		"Hip_L_ry",
		"Hip_L_rz",
		"LeftUpLegtoLeftLowLeg_rx",
		"LeftUpLegtoLeftLowLeg_ry",
		"LeftUpLegtoLeftLowLeg_rz",
		"LeftLowLegtoLeftFoot_rx",
		"LeftLowLegtoLeftFoot_ry",
		"LeftLowLegtoLeftFoot_rz",
		"LeftFoottoLeftFootend_rx",
		"LeftFoottoLeftFootend_ry",
		"LeftFoottoLeftFootend_rz",
		"Toe_L_rx",
		"Toe_L_ry",
		"Toe_L_rz",
		"Hip_R_rx",
		"Hip_R_ry",
		"Hip_R_rz",
		"RightUpLegtoRightLowLeg_rx",
		"RightUpLegtoRightLowLeg_ry",
		"RightUpLegtoRightLowLeg_rz",
		"RightLowLegtoRightFoot_rx",
		"RightLowLegtoRightFoot_ry",
		"RightLowLegtoRightFoot_rz",
		"RightFoottoRightFootend_rx",
		"RightFoottoRightFootend_ry",
		"RightFoottoRightFootend_rz",
		"Toe_R_rx",
		"Toe_R_ry",
		"Toe_R_rz",
		"HeadtoMouth_rx",
		"HeadtoMouth_ry",
		"HeadtoMouth_rz",
		"HeadtoLEye_rx",
		"HeadtoLEye_ry",
		"HeadtoLEye_rz",
		"HeadtoREye_rx",
		"HeadtoREye_ry",
		"HeadtoREye_rz",
		"Spine3toLClavicle_rx",
		"Spine3toLClavicle_ry",
		"Spine3toLClavicle_rz",
		"Spine3toRClavicle_rx",
		"Spine3toRClavicle_ry",
		"Spine3toRClavicle_rz",
		"Jaw_rx",
		"Jaw_ry",
		"Jaw_rz",
		"Eye_R_rx",
		"Eye_R_ry",
		"Eye_R_rz",
		"Eye_L_rx",
		"Eye_L_ry",
		"Eye_L_rz",
		"UpLid_R_rx",
		"UpLid_R_ry",
		"UpLid_R_rz",
		"LoLid_R_rx",
		"LoLid_R_ry",
		"LoLid_R_rz",
		"UpLid_L_rx",
		"UpLid_L_ry",
		"UpLid_L_rz",
		"LoLid_L_rx",
		"LoLid_L_ry",
		"LoLid_L_rz",
		"LeftCollartoLeftUpArm_rx",
		"LeftCollartoLeftUpArm_ry",
		"LeftCollartoLeftUpArm_rz",
		"LeftUpArmtoLeftLowArm_rx",
		"LeftUpArmtoLeftLowArm_ry",
		"LeftUpArmtoLeftLowArm_rz",
		"LeftLowArmtoLeftHand_rx",
		"LeftLowArmtoLeftHand_ry",
		"LeftLowArmtoLeftHand_rz",
		"LeftHandtoLeftHandend_rx",
		"LeftHandtoLeftHandend_ry",
		"LeftHandtoLeftHandend_rz",
		"Wrist-1_L_rx",
		"Wrist-1_L_ry",
		"Wrist-1_L_rz",
		"Wrist-2_L_rx",
		"Wrist-2_L_ry",
		"Wrist-2_L_rz",
		"Palm-1_L_rx",
		"Palm-1_L_ry",
		"Palm-1_L_rz",
		"Palm-2_L_rx",
		"Palm-2_L_ry",
		"Palm-2_L_rz",
		"Palm-3_L_rx",
		"Palm-3_L_ry",
		"Palm-3_L_rz",
		"Palm-4_L_rx",
		"Palm-4_L_ry",
		"Palm-4_L_rz",
		"Palm-5_L_rx",
		"Palm-5_L_ry",
		"Palm-5_L_rz",
		"RightCollartoRightUpArm_rx",
		"RightCollartoRightUpArm_ry",
		"RightCollartoRightUpArm_rz",
		"RightUpArmtoRightLowArm_rx",
		"RightUpArmtoRightLowArm_ry",
		"RightUpArmtoRightLowArm_rz",
		"RightLowArmtoRightHand_rx",
		"RightLowArmtoRightHand_ry",
		"RightLowArmtoRightHand_rz",
		"RightHandtoRightHandend_rx",
		"RightHandtoRightHandend_ry",
		"RightHandtoRightHandend_rz",
		"Wrist-1_R_rx",
		"Wrist-1_R_ry",
		"Wrist-1_R_rz",
		"Wrist-2_R_rx",
		"Wrist-2_R_ry",
		"Wrist-2_R_rz",
		"Palm-1_R_rx",
		"Palm-1_R_ry",
		"Palm-1_R_rz",
		"Palm-2_R_rx",
		"Palm-2_R_ry",
		"Palm-2_R_rz",
		"Palm-3_R_rx",
		"Palm-3_R_ry",
		"Palm-3_R_rz",
		"Palm-4_R_rx",
		"Palm-4_R_ry",
		"Palm-4_R_rz",
		"Palm-5_R_rx",
		"Palm-5_R_ry",
		"Palm-5_R_rz",
		"Finger-1-1_L_rx",
		"Finger-1-1_L_ry",
		"Finger-1-1_L_rz",
		"Finger-1-2_L_rx",
		"Finger-1-2_L_ry",
		"Finger-1-2_L_rz",
		"Finger-1-3_L_rx",
		"Finger-1-3_L_ry",
		"Finger-1-3_L_rz",
		"Finger-2-1_L_rx",
		"Finger-2-1_L_ry",
		"Finger-2-1_L_rz",
		"Finger-2-2_L_rx",
		"Finger-2-2_L_ry",
		"Finger-2-2_L_rz",
		"Finger-2-3_L_rx",
		"Finger-2-3_L_ry",
		"Finger-2-3_L_rz",
		"Finger-3-1_L_rx",
		"Finger-3-1_L_ry",
		"Finger-3-1_L_rz",
		"Finger-3-2_L_rx",
		"Finger-3-2_L_ry",
		"Finger-3-2_L_rz",
		"Finger-3-3_L_rx",
		"Finger-3-3_L_ry",
		"Finger-3-3_L_rz",
		"Finger-4-1_L_rx",
		"Finger-4-1_L_ry",
		"Finger-4-1_L_rz",
		"Finger-4-2_L_rx",
		"Finger-4-2_L_ry",
		"Finger-4-2_L_rz",
		"Finger-4-3_L_rx",
		"Finger-4-3_L_ry",
		"Finger-4-3_L_rz",
		"Finger-5-1_L_rx",
		"Finger-5-1_L_ry",
		"Finger-5-1_L_rz",
		"Finger-5-2_L_rx",
		"Finger-5-2_L_ry",
		"Finger-5-2_L_rz",
		"Finger-5-3_L_rx",
		"Finger-5-3_L_ry",
		"Finger-5-3_L_rz",
		"Finger-1-1_R_rx",
		"Finger-1-1_R_ry",
		"Finger-1-1_R_rz",
		"Finger-1-2_R_rx",
		"Finger-1-2_R_ry",
		"Finger-1-2_R_rz",
		"Finger-1-3_R_rx",
		"Finger-1-3_R_ry",
		"Finger-1-3_R_rz",
		"Finger-2-1_R_rx",
		"Finger-2-1_R_ry",
		"Finger-2-1_R_rz",
		"Finger-2-2_R_rx",
		"Finger-2-2_R_ry",
		"Finger-2-2_R_rz",
		"Finger-2-3_R_rx",
		"Finger-2-3_R_ry",
		"Finger-2-3_R_rz",
		"Finger-3-1_R_rx",
		"Finger-3-1_R_ry",
		"Finger-3-1_R_rz",
		"Finger-3-2_R_rx",
		"Finger-3-2_R_ry",
		"Finger-3-2_R_rz",
		"Finger-3-3_R_rx",
		"Finger-3-3_R_ry",
		"Finger-3-3_R_rz",
		"Finger-4-1_R_rx",
		"Finger-4-1_R_ry",
		"Finger-4-1_R_rz",
		"Finger-4-2_R_rx",
		"Finger-4-2_R_ry",
		"Finger-4-2_R_rz",
		"Finger-4-3_R_rx",
		"Finger-4-3_R_ry",
		"Finger-4-3_R_rz",
		"Finger-5-1_R_rx",
		"Finger-5-1_R_ry",
		"Finger-5-1_R_rz",
		"Finger-5-2_R_rx",
		"Finger-5-2_R_ry",
		"Finger-5-2_R_rz",
		"Finger-5-3_R_rx",
		"Finger-5-3_R_ry",
		"Finger-5-3_R_rz",
		"JawtoTongue_rx",
		"JawtoTongue_ry",
		"JawtoTongue_rz",
		"TongueBase_rx",
		"TongueBase_ry",
		"TongueBase_rz",
		"TongueMid_rx",
		"TongueMid_ry",
		"TongueMid_rz",
		"TongueTip_rx",
		"TongueTip_ry",
		"TongueTip_rz",		
	};
	public static final String[] sidenames = {};
	public String[] getParameterNames() { return parameternames; }
	public String[] getSideNames() { return sidenames; }

	public void getShapePaths(String prefix,GeneralMatrixString paths)
	{
		for(int i=0;i<HumanDeformedCuboids.cuboid_names.length;i++)
		{
			paths.push_back(prefix+"/"+HumanDeformedCuboids.cuboid_names[i]+"/DeformedCuboid");
		}
	}

	public GeneralMatrixFloat pos = new GeneralMatrixFloat(3);
	public Skeleton skel = new Skeleton();
	public Skin skin = new Skin();

	public static void createMorphedBody(
//			GeneralMatrixString morphnames,
//			GeneralMatrixFloat morphmagnitudes,
			GeneralMatrixFloat pos,
			GeneralMatrixDouble vpos,
			GeneralMatrixDouble bmats,
			GeneralMatrixDouble bonelengths,
			GeneralMatrixDouble localbindbmats)
	{
		float[] fpos = HumanPos.pos; 
		pos.setDimensions(3,fpos.length/3);
		pos.set(fpos);
		
//		for(int i=0;i<morphnames.height;i++)
//		{
//			GeneralMatrixFloat mpos = new GeneralMatrixFloat();
//			GeneralMatrixInt mind = new GeneralMatrixInt();
//			Makehuman.getMorphIndex(morphnames.value[i], mpos, mind);
//			float mag = morphmagnitudes.value[i];
//			if((mind.value==null)&&(mpos.height>0))
//			{
//				for(int ind=0;ind<pos.height*3;ind+=3)
//				{
//					pos.value[ind+0] += mpos.value[ind+0]*mag;
//					pos.value[ind+1] += mpos.value[ind+1]*mag;
//					pos.value[ind+2] += mpos.value[ind+2]*mag;
//				}
//			}
//			else
//			{
//				for(int ei=0;ei<mind.height;ei++)
//				{
//					int ind = mind.value[ei]*3;
//					int eind = ei*3;
//					pos.value[ind+0] += mpos.value[eind+0]*mag;
//					pos.value[ind+1] += mpos.value[eind+1]*mag;
//					pos.value[ind+2] += mpos.value[eind+2]*mag;
//				}
//			}
//		}

		vpos.setDimensions(3, HumanJoints.names.length);
		//Calc the bind vpos positions
		 for(int vi=0;vi<vpos.height;vi++)
		 {
			 vpos.value[vi*3+0] = 0.0f;
			 vpos.value[vi*3+1] = 0.0f;
			 vpos.value[vi*3+2] = 0.0f;
		
			for(int i=0;i<6;i++)
			{
				int ind = HumanJoints.jointverts[vi*6+i];
				vpos.value[vi*3+0] += pos.value[ind*3+0];
				vpos.value[vi*3+1] += pos.value[ind*3+1];
				vpos.value[vi*3+2] += pos.value[ind*3+2];    			
			}
			vpos.value[vi*3+0] /= 6;
			vpos.value[vi*3+1] /= 6;
			vpos.value[vi*3+2] /= 6;
		 }

		 //Build the bind skeleton
		 GeneralMatrixDouble bmat = new GeneralMatrixDouble(3,3);
    	int bonen = HumanBones.bones.length/2;
		 bmats.setDimensions(9, bonen);
		 bonelengths.setDimensions(1, bonen);
    	for(int bi=0;bi<bonen;bi++)
    	{
    		int v0 = HumanBones.bones[bi*2+0];
    		int v1 = HumanBones.bones[bi*2+1];
	    	bmat.width = 3;
	    	bmat.height = 3;

			double dx = vpos.value[v1*3+0]-vpos.value[v0*3+0];
			double dy = vpos.value[v1*3+1]-vpos.value[v0*3+1];
			double dz = vpos.value[v1*3+2]-vpos.value[v0*3+2];
			double bls = Math.sqrt(dx*dx+dy*dy+dz*dz);
			bonelengths.value[bi] = bls;
	    	bmat.calcBasisFromY(dx, dy, dz,
	    			HumanBBmats.bbmatx[3*bi+0],HumanBBmats.bbmatx[3*bi+1],HumanBBmats.bbmatx[3*bi+2]);
	    	bmat.width = 9;
	    	bmat.height = 1;
	    	bmats.setRow(bi, bmat);
    	}

    	//Now recalculate the localbindbmats and bonelengths for the morphed body shape
		Animate.calcLocalBindMats(HumanBones.bones, HumanBones.bprnts, bmats, localbindbmats);
	}
	
	public HumanShape()
	{
		skel.boneParents.setDimensions(1, HumanBones.names.length);
		skel.boneParents.set(HumanBones.bprnts);
		skel.boneJoints.setDimensions(2,HumanBones.names.length);
		skel.boneJoints.set(HumanBones.bones);
		createMorphedBody(//morphnames,morphmagnitudes, 
				skin.bpos, 
				skel.vpos,skel.bmats,skel.bonelengths,skel.localbindbmats);

		skel.lpos.setDimensions(3,1+(HumanBones.bones.length/2));
		parameters = new double[skel.lpos.width*skel.lpos.height];

		skin.sb = HumanSkinToBoneMapping.sknb;
		skin.sw = HumanSkinToBoneWeights.sknw;

		skel.tvpos.setDimensions(skel.vpos.width,skel.vpos.height);
		skel.tbmats.setDimensions(skel.bmats.width,skel.bmats.height);

		float[] fpos = HumanPos.pos; 
		pos.setDimensions(3,fpos.length/3);
		
		parametersUpdated();
	}
	
	public ParametricShape copy()
	{
		HumanShape c = new HumanShape();
		System.arraycopy(parameters, 0, c.parameters, 0, parameters.length);
		c.parametersUpdated();
		return c;
	}

	public void parametersUpdated()
	{
		if(parameters==null)
		{
			parameters = new double[skel.lpos.width*skel.lpos.height];
			System.arraycopy(skel.lpos.value, 0, parameters, 0, parameters.length);
		}
		final double DEG2RAD = Math.PI/180.0;
		skel.lpos.set(parameters);
		for(int i=3;i<parameters.length;i++)
			skel.lpos.value[i] *= DEG2RAD;
		Animate.transformWithParams(skel.boneJoints.value, skel.boneParents.value,
				skel.bonelengths, skel.localbindbmats,
				skel.tvpos, skel.tbmats, skel.lpos);

    	Animate.updateSkinUsingSkeleton(skel.tvpos, skel.tbmats, skel.vpos, skel.bmats, skin.bpos, HumanBones.bones, skin.sb, skin.sw, pos);
	}
	
	public void setCuboidDimensions(double x,double y,double z)
	{
	}
	
	public void getCuboidDimensions(double[] xyz)
	{
		double minx = Float.MAX_VALUE;
		double miny = Float.MAX_VALUE;
		double minz = Float.MAX_VALUE;
		double maxx = -Float.MAX_VALUE;
		double maxy = -Float.MAX_VALUE;
		double maxz = -Float.MAX_VALUE;
		
		for(int vi=0;vi<pos.height;vi++)
		{
			double vx = pos.value[vi*3+0];
			double vy = pos.value[vi*3+1];
			double vz = pos.value[vi*3+2];
			
			if(vx<minx)
				minx = vx;
			if(vy<miny)
				miny = vy;
			if(vz<minz)
				minz = vz;
			
			if(vx>maxx)
				maxx = vx;
			if(vy>maxy)
				maxy = vy;
			if(vz>maxz)
				maxz = vz;
		}
		
		xyz[0] = maxx-minx;
		xyz[2] = maxy-miny;
		xyz[1] = maxz-minz;
	}

	public void getAABB(double[] xyz)
	{
		double minx = Float.MAX_VALUE;
		double miny = Float.MAX_VALUE;
		double minz = Float.MAX_VALUE;
		double maxx = -Float.MAX_VALUE;
		double maxy = -Float.MAX_VALUE;
		double maxz = -Float.MAX_VALUE;
		
		for(int vi=0;vi<pos.height;vi++)
		{
			double vx = pos.value[vi*3+0];
			double vy = pos.value[vi*3+1];
			double vz = pos.value[vi*3+2];
			
			if(vx<minx)
				minx = vx;
			if(vy<miny)
				miny = vy;
			if(vz<minz)
				minz = vz;
			
			if(vx>maxx)
				maxx = vx;
			if(vy>maxy)
				maxy = vy;
			if(vz>maxz)
				maxz = vz;
		}
		
		xyz[0] = minx;
		xyz[2] = miny;
		xyz[1] = minz;
		xyz[3] = maxx;
		xyz[5] = maxy;
		xyz[4] = maxz;

	}
}
