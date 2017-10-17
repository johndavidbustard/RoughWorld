package utils.shapes.skinned;

import utils.GeneralMatrixDouble;
import utils.GeneralMatrixFloat;
import utils.GeneralMatrixInt;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;
import utils.Quaternion;
import utils.shapes.human.HumanBones;

public class Animate 
{
	public static GeneralMatrixDouble bmat = new GeneralMatrixDouble(3,3);
	public static GeneralMatrixDouble pbmat = new GeneralMatrixDouble(9,1);
	public static GeneralMatrixDouble bmatl = new GeneralMatrixDouble(3,3);
	public static GeneralMatrixDouble bmatbw = new GeneralMatrixDouble(3,3);
	public static GeneralMatrixDouble bmatw = new GeneralMatrixDouble(3,3);
	public static GeneralMatrixDouble pbmati = new GeneralMatrixDouble(3,3);
	public static GeneralMatrixDouble euler = new GeneralMatrixDouble(3,1);

	public static GeneralMatrixFloat bmatf = new GeneralMatrixFloat(3,3);
	public static GeneralMatrixFloat pbmatf = new GeneralMatrixFloat(9,1);
	public static GeneralMatrixFloat bmatlf = new GeneralMatrixFloat(3,3);
	public static GeneralMatrixFloat bmatbwf = new GeneralMatrixFloat(3,3);
	public static GeneralMatrixFloat bmatwf = new GeneralMatrixFloat(3,3);
	public static GeneralMatrixFloat pbmatif = new GeneralMatrixFloat(3,3);
	public static GeneralMatrixFloat eulerf = new GeneralMatrixFloat(3,1);

	public static void animatea(float apos,GeneralMatrixFloat frames,GeneralMatrixFloat lposes,
			GeneralMatrixFloat lpos)
	{
		int pPose = (int)apos;
		int nPose = pPose+1;
		float f1 = apos-pPose;
		float f0 = 1.0f-f1;
		apos = pPose+f1;

		if(f1==0.0f)
		{
			for(int li=0;li<lposes.width;li++)
			{
				lpos.value[li] = lposes.value[pPose*lposes.width+li];
			}
		}
		else
		{
			for(int li=0;li<lposes.width;li++)
			{
				lpos.value[li] = lposes.value[pPose*lposes.width+li]*f0+lposes.value[nPose*lposes.width+li]*f1;
			}
		}
	}
	
	public static float animate(float times,GeneralMatrixFloat frames,GeneralMatrixFloat lposes,
			GeneralMatrixFloat lpos)
	{
		float apos = 0.0f;
		int olw = lpos.width;
		int olh = lpos.height;
		lpos.setDimensions(lposes.width, 1);
		float pFrame = 0.0f;
		float nFrame = 0.0f;
		int pPose = -1;
		int nPose = -1;
		for(int i=0;i<frames.height;i++)
		{
			float f = frames.value[i];
			if(f<times)
			{
				pFrame = f;
				pPose = i;				
			}
			else
			{
				nFrame = f;
				nPose = i;
				break;
			}
		}
		if(pPose==-1)
		{
			apos = nPose;
			for(int li=0;li<lposes.width;li++)
			{
				lpos.value[li] = lposes.value[nPose*lposes.width+li];
			}
		}
		else
		if(nPose==-1)
		{
			apos = pPose;
			for(int li=0;li<lposes.width;li++)
			{
				lpos.value[li] = lposes.value[pPose*lposes.width+li];
			}
		}
		else
		{
			float dp = times-pFrame;
			float dn = nFrame-times;
			float d = nFrame-pFrame;
			
			if(d<GeneralMatrixFloat.EPSILON)
			{					
				if(dp<dn)
				{
					apos = pPose;
					for(int li=0;li<lposes.width;li++)
					{
						lpos.value[li] = lposes.value[pPose*lposes.width+li];
					}
				}
				else
				{
					apos = nPose;
					for(int li=0;li<lposes.width;li++)
					{
						lpos.value[li] = lposes.value[nPose*lposes.width+li];
					}
				}
			}
			//*
			else
			{
				float f1 = dp/d;
				float f0 = 1.0f-f1;
				apos = pPose+f1;

				for(int li=0;li<lposes.width;li++)
				{
					lpos.value[li] = lposes.value[pPose*lposes.width+li]*f0+lposes.value[nPose*lposes.width+li]*f1;
				}
			}
		}
		lpos.width = olw;
		lpos.height = olh;
		return apos;
	}	
	public static void blend(float f,GeneralMatrixFloat pbmats,GeneralMatrixFloat nbmats,GeneralMatrixFloat bmats)
	{
		float[] qa = new float[4];
		float[] qb = new float[4];
		float[] qc = new float[4];

		for(int i=0;i<nbmats.height;i++)
		{
			int mo = i*9;
			Quaternion.MatrixtoQuaternion(pbmats.value,mo, qa);
			Quaternion.MatrixtoQuaternion(nbmats.value,mo, qb);
			Quaternion.slerp(qa, qb, f, qc);
			Quaternion.QuaterniontoMatrix(qc, bmats.value, mo);
		}
	}
	
	public static void updateVposFromBmats(GeneralMatrixInt bones,
			GeneralMatrixFloat vpos,GeneralMatrixFloat nbmat,GeneralMatrixFloat nvpos)
	{
		 for(int bi=0;bi<bones.height;bi++)
		 {
			 int v0 = bones.value[bi*2+0];
			 int v1 = bones.value[bi*2+1];
			 float bx = vpos.value[v1*3+0]-vpos.value[v0*3+0];
			 float by = vpos.value[v1*3+1]-vpos.value[v0*3+1];
			 float bz = vpos.value[v1*3+2]-vpos.value[v0*3+2];
			 float bl = (float)(Math.sqrt(bx*bx+by*by+bz*bz));
			 nvpos.value[v1*3+0] = nvpos.value[v0*3+0]+nbmat.value[9*bi+3+0]*bl; 
			 nvpos.value[v1*3+1] = nvpos.value[v0*3+1]+nbmat.value[9*bi+3+1]*bl; 
			 nvpos.value[v1*3+2] = nvpos.value[v0*3+2]+nbmat.value[9*bi+3+2]*bl;
		 }
	}
	
	public static void updateVposFromBmats(GeneralMatrixInt boneUpdateOrder,GeneralMatrixInt bones,
			GeneralMatrixFloat vpos,GeneralMatrixFloat nbmat,GeneralMatrixFloat nvpos)
	{
		 for(int ubi=0;ubi<boneUpdateOrder.height;ubi++)
		 {
			 int bi = boneUpdateOrder.value[ubi];
			 int v0 = bones.value[bi*2+0];
			 int v1 = bones.value[bi*2+1];
			 float bx = vpos.value[v1*3+0]-vpos.value[v0*3+0];
			 float by = vpos.value[v1*3+1]-vpos.value[v0*3+1];
			 float bz = vpos.value[v1*3+2]-vpos.value[v0*3+2];
			 float bl = (float)(Math.sqrt(bx*bx+by*by+bz*bz));
			 nvpos.value[v1*3+0] = nvpos.value[v0*3+0]+nbmat.value[9*bi+3+0]*bl; 
			 nvpos.value[v1*3+1] = nvpos.value[v0*3+1]+nbmat.value[9*bi+3+1]*bl; 
			 nvpos.value[v1*3+2] = nvpos.value[v0*3+2]+nbmat.value[9*bi+3+2]*bl;
		 }
	}
	
	public static void updateVposFromBmatsAndBoneLengths(GeneralMatrixInt boneUpdateOrder,GeneralMatrixInt bones,
			GeneralMatrixFloat blengths,GeneralMatrixFloat nbmat,GeneralMatrixFloat nvpos)
	{
		 for(int ubi=0;ubi<boneUpdateOrder.height;ubi++)
		 {
			 int bi = boneUpdateOrder.value[ubi];
			 int v0 = bones.value[bi*2+0];
			 int v1 = bones.value[bi*2+1];
			 float bl = blengths.value[bi];
			 nvpos.value[v1*3+0] = nvpos.value[v0*3+0]+nbmat.value[9*bi+3+0]*bl; 
			 nvpos.value[v1*3+1] = nvpos.value[v0*3+1]+nbmat.value[9*bi+3+1]*bl; 
			 nvpos.value[v1*3+2] = nvpos.value[v0*3+2]+nbmat.value[9*bi+3+2]*bl;
		 }
	}
	
	public static void calculateInverseSkeletonTransform(GeneralMatrixFloat vpos,GeneralMatrixFloat pbmat, //animated position
			GeneralMatrixFloat bvpos,GeneralMatrixFloat bbmat,GeneralMatrixInt bones,
			GeneralMatrixFloat animtransforms)
	{
		GeneralMatrixFloat ftrans = new GeneralMatrixFloat(4,4);
		GeneralMatrixFloat iftrans = new GeneralMatrixFloat(4,4);
		//now calculate the transform matricies for each bone (ones which take a point and translate and rotate it with the new values)
		animtransforms.setDimensions(12,bones.height);
		for(int i=0;i<animtransforms.height;i++)
		{
			//Invert the old transform and then apply the new one
			int v0 = bones.value[i*2];
			
			//invert trans
			float ix = -bvpos.value[v0*3+0];
			float iy = -bvpos.value[v0*3+1];
			float iz = -bvpos.value[v0*3+2];
			
			//rotate with transpose of bindmat (i.e. i and j are flipped)
			
			float rix = ix*bbmat.value[i*9+0*3+0]+iy*bbmat.value[i*9+0*3+1]+iz*bbmat.value[i*9+0*3+2];
			float riy = ix*bbmat.value[i*9+1*3+0]+iy*bbmat.value[i*9+1*3+1]+iz*bbmat.value[i*9+1*3+2];
			float riz = ix*bbmat.value[i*9+2*3+0]+iy*bbmat.value[i*9+2*3+1]+iz*bbmat.value[i*9+2*3+2];
			
			ftrans.setIdentity();
			
			//the new transform is a composition of the inverse and then the new bmat
			ftrans.value[0] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+0];
			ftrans.value[1] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+1];
			ftrans.value[2] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+2];

			ftrans.value[4+0] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+0];
			ftrans.value[4+1] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+1];
			ftrans.value[4+2] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+2];
			
			ftrans.value[8+0] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+0];
			ftrans.value[8+1] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+1];
			ftrans.value[8+2] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+2];
			
			//translation is bmat rotation of ri then vpos				
			ftrans.value[12+0] = rix*pbmat.value[i*9+0*3+0]+riy*pbmat.value[i*9+1*3+0]+riz*pbmat.value[i*9+2*3+0]+vpos.value[v0*3+0];
			ftrans.value[12+1] = rix*pbmat.value[i*9+0*3+1]+riy*pbmat.value[i*9+1*3+1]+riz*pbmat.value[i*9+2*3+1]+vpos.value[v0*3+1];
			ftrans.value[12+2] = rix*pbmat.value[i*9+0*3+2]+riy*pbmat.value[i*9+1*3+2]+riz*pbmat.value[i*9+2*3+2]+vpos.value[v0*3+2];			
			
			GeneralMatrixFloat.invert(ftrans, iftrans);

			animtransforms.value[12*i+0+0] = ftrans.value[0];
			animtransforms.value[12*i+0+1] = ftrans.value[1];
			animtransforms.value[12*i+0+2] = ftrans.value[2];
			animtransforms.value[12*i+3+0] = ftrans.value[4+0];
			animtransforms.value[12*i+3+1] = ftrans.value[4+1];
			animtransforms.value[12*i+3+2] = ftrans.value[4+2];
			animtransforms.value[12*i+6+0] = ftrans.value[8+0];
			animtransforms.value[12*i+6+1] = ftrans.value[8+1];
			animtransforms.value[12*i+6+2] = ftrans.value[8+2];
			animtransforms.value[12*i+9+0] = ftrans.value[12+0];
			animtransforms.value[12*i+9+1] = ftrans.value[12+1];
			animtransforms.value[12*i+9+2] = ftrans.value[12+2];
		}

	}
	
	public static void updateSkinUsingSkeleton(GeneralMatrixFloat vpos,GeneralMatrixFloat pbmat, //animated position
			GeneralMatrixFloat bvpos,GeneralMatrixFloat bbmat, GeneralMatrixFloat pos, //bind position and skin			
			GeneralMatrixInt bones,GeneralMatrixInt sb, GeneralMatrixFloat sw,
			GeneralMatrixFloat dpos)
	{
		//now calculate the transform matricies for each bone (ones which take a point and translate and rotate it with the new values)
		GeneralMatrixFloat animtransforms = new GeneralMatrixFloat(12,bones.height);
		for(int i=0;i<animtransforms.height;i++)
		{
			//Invert the old transform and then apply the new one
			int v0 = bones.value[i*2];
			
			//invert trans
			float ix = -bvpos.value[v0*3+0];
			float iy = -bvpos.value[v0*3+1];
			float iz = -bvpos.value[v0*3+2];
			
			//rotate with transpose of bindmat (i.e. i and j are flipped)
			
			float rix = ix*bbmat.value[i*9+0*3+0]+iy*bbmat.value[i*9+0*3+1]+iz*bbmat.value[i*9+0*3+2];
			float riy = ix*bbmat.value[i*9+1*3+0]+iy*bbmat.value[i*9+1*3+1]+iz*bbmat.value[i*9+1*3+2];
			float riz = ix*bbmat.value[i*9+2*3+0]+iy*bbmat.value[i*9+2*3+1]+iz*bbmat.value[i*9+2*3+2];
			
			//the new transform is a composition of the inverse and then the new bmat
			animtransforms.value[12*i+0] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+0];
			animtransforms.value[12*i+1] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+1];
			animtransforms.value[12*i+2] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+2];

			animtransforms.value[12*i+3+0] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+0];
			animtransforms.value[12*i+3+1] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+1];
			animtransforms.value[12*i+3+2] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+2];
			
			animtransforms.value[12*i+6+0] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+0];
			animtransforms.value[12*i+6+1] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+1];
			animtransforms.value[12*i+6+2] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+2];
			
			//translation is bmat rotation of ri then vpos				
			animtransforms.value[12*i+9] = rix*pbmat.value[i*9+0*3+0]+riy*pbmat.value[i*9+1*3+0]+riz*pbmat.value[i*9+2*3+0]+vpos.value[v0*3+0];
			animtransforms.value[12*i+10] = rix*pbmat.value[i*9+0*3+1]+riy*pbmat.value[i*9+1*3+1]+riz*pbmat.value[i*9+2*3+1]+vpos.value[v0*3+1];
			animtransforms.value[12*i+11] = rix*pbmat.value[i*9+0*3+2]+riy*pbmat.value[i*9+1*3+2]+riz*pbmat.value[i*9+2*3+2]+vpos.value[v0*3+2];
		}

		//now go through the skin
		dpos.clear(0.0f);
		for(int si=0;si<sw.height;si++)
		{
			int i = sb.value[si*2+0];
			int i0 = sb.value[si*2+1];
			float w = sw.value[si];
			
			float x = pos.value[i*3+0];
			float y = pos.value[i*3+1];
			float z = pos.value[i*3+2];
			
			//transform using the matrices as labelled in the skin matrix
			float x0 = x*animtransforms.value[i0*12+0*3+0]+y*animtransforms.value[i0*12+1*3+0]+z*animtransforms.value[i0*12+2*3+0]+animtransforms.value[i0*12+3*3+0];
			float y0 = x*animtransforms.value[i0*12+0*3+1]+y*animtransforms.value[i0*12+1*3+1]+z*animtransforms.value[i0*12+2*3+1]+animtransforms.value[i0*12+3*3+1];
			float z0 = x*animtransforms.value[i0*12+0*3+2]+y*animtransforms.value[i0*12+1*3+2]+z*animtransforms.value[i0*12+2*3+2]+animtransforms.value[i0*12+3*3+2];

			dpos.value[i*3+0] += w*x0;
			dpos.value[i*3+1] += w*y0;
			dpos.value[i*3+2] += w*z0;
		}
	}
	
	//(GeneralMatrixFloat, GeneralMatrixFloat, GeneralMatrixFloat, GeneralMatrixFloat, GeneralMatrixFloat, int[], int[], float[], GeneralMatrixFloat)
	public static void updateSkinUsingSkeleton(GeneralMatrixDouble vpos,GeneralMatrixDouble pbmat, //animated position
			GeneralMatrixDouble bvpos,GeneralMatrixDouble bbmat, GeneralMatrixFloat pos, //bind position and skin			
			int[] bones,int[] sb, float[] sw,
			GeneralMatrixFloat dpos)
	{
		int bonesn = bones.length/2;
		//now calculate the transform matricies for each bone (ones which take a point and translate and rotate it with the new values)
		GeneralMatrixDouble animtransforms = new GeneralMatrixDouble(12,bonesn);
		for(int i=0;i<animtransforms.height;i++)
		{
			//Invert the old transform and then apply the new one
			int v0 = bones[i*2];
			
			//invert trans
			double ix = -bvpos.value[v0*3+0];
			double iy = -bvpos.value[v0*3+1];
			double iz = -bvpos.value[v0*3+2];
			
			//rotate with transpose of bindmat (i.e. i and j are flipped)
			
			double rix = ix*bbmat.value[i*9+0*3+0]+iy*bbmat.value[i*9+0*3+1]+iz*bbmat.value[i*9+0*3+2];
			double riy = ix*bbmat.value[i*9+1*3+0]+iy*bbmat.value[i*9+1*3+1]+iz*bbmat.value[i*9+1*3+2];
			double riz = ix*bbmat.value[i*9+2*3+0]+iy*bbmat.value[i*9+2*3+1]+iz*bbmat.value[i*9+2*3+2];
			
			//the new transform is a composition of the inverse and then the new bmat
			animtransforms.value[12*i+0] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+0];
			animtransforms.value[12*i+1] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+1];
			animtransforms.value[12*i+2] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+2];

			animtransforms.value[12*i+3+0] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+0];
			animtransforms.value[12*i+3+1] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+1];
			animtransforms.value[12*i+3+2] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+2];
			
			animtransforms.value[12*i+6+0] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+0];
			animtransforms.value[12*i+6+1] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+1];
			animtransforms.value[12*i+6+2] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+2];
			
			//translation is bmat rotation of ri then vpos				
			animtransforms.value[12*i+9] = rix*pbmat.value[i*9+0*3+0]+riy*pbmat.value[i*9+1*3+0]+riz*pbmat.value[i*9+2*3+0]+vpos.value[v0*3+0];
			animtransforms.value[12*i+10] = rix*pbmat.value[i*9+0*3+1]+riy*pbmat.value[i*9+1*3+1]+riz*pbmat.value[i*9+2*3+1]+vpos.value[v0*3+1];
			animtransforms.value[12*i+11] = rix*pbmat.value[i*9+0*3+2]+riy*pbmat.value[i*9+1*3+2]+riz*pbmat.value[i*9+2*3+2]+vpos.value[v0*3+2];

//			animtransforms.value[12*i+0] = 1.0f;
//			animtransforms.value[12*i+1] = 0.0f;
//			animtransforms.value[12*i+2] = 0.0f;
//
//			animtransforms.value[12*i+3+0] = 0.0f;
//			animtransforms.value[12*i+3+1] = 1.0f;
//			animtransforms.value[12*i+3+2] = 0.0f;
//			
//			animtransforms.value[12*i+6+0] = 0.0f;
//			animtransforms.value[12*i+6+1] = 0.0f;
//			animtransforms.value[12*i+6+2] = 1.0f;
//			
//			//translation is bmat rotation of ri then vpos				
//			animtransforms.value[12*i+9] = 0.0f;
//			animtransforms.value[12*i+10] = 0.0f;
//			animtransforms.value[12*i+11] = 0.0f;
		}

		//now go through the skin
		dpos.clear(0.0f);
		int swn = sw.length;
		for(int si=0;si<swn;si++)
		{
			int i = sb[si*2+0];
			int i0 = sb[si*2+1];
			float w = sw[si];
			
			float x = pos.value[i*3+0];
			float y = pos.value[i*3+1];
			float z = pos.value[i*3+2];
			
			//transform using the matrices as labelled in the skin matrix
			float x0 = (float)(x*animtransforms.value[i0*12+0*3+0]+y*animtransforms.value[i0*12+1*3+0]+z*animtransforms.value[i0*12+2*3+0]+animtransforms.value[i0*12+3*3+0]);
			float y0 = (float)(x*animtransforms.value[i0*12+0*3+1]+y*animtransforms.value[i0*12+1*3+1]+z*animtransforms.value[i0*12+2*3+1]+animtransforms.value[i0*12+3*3+1]);
			float z0 = (float)(x*animtransforms.value[i0*12+0*3+2]+y*animtransforms.value[i0*12+1*3+2]+z*animtransforms.value[i0*12+2*3+2]+animtransforms.value[i0*12+3*3+2]);

			dpos.value[i*3+0] += w*x0;
			dpos.value[i*3+1] += w*y0;
			dpos.value[i*3+2] += w*z0;
		}
	}
	
	public static void updateSkinUsingSkeleton(GeneralMatrixFloat vpos,GeneralMatrixFloat pbmat, //animated position
			GeneralMatrixFloat bvpos,GeneralMatrixFloat bbmat, GeneralMatrixFloat pos, //bind position and skin			
			int[] bones,int[] sb, float[] sw,
			GeneralMatrixFloat dpos)
	{
		int bonesn = bones.length/2;
		//now calculate the transform matricies for each bone (ones which take a point and translate and rotate it with the new values)
		GeneralMatrixFloat animtransforms = new GeneralMatrixFloat(12,bonesn);
		for(int i=0;i<animtransforms.height;i++)
		{
			//Invert the old transform and then apply the new one
			int v0 = bones[i*2];
			
			//invert trans
			float ix = -bvpos.value[v0*3+0];
			float iy = -bvpos.value[v0*3+1];
			float iz = -bvpos.value[v0*3+2];
			
			//rotate with transpose of bindmat (i.e. i and j are flipped)
			
			float rix = ix*bbmat.value[i*9+0*3+0]+iy*bbmat.value[i*9+0*3+1]+iz*bbmat.value[i*9+0*3+2];
			float riy = ix*bbmat.value[i*9+1*3+0]+iy*bbmat.value[i*9+1*3+1]+iz*bbmat.value[i*9+1*3+2];
			float riz = ix*bbmat.value[i*9+2*3+0]+iy*bbmat.value[i*9+2*3+1]+iz*bbmat.value[i*9+2*3+2];
			
			//the new transform is a composition of the inverse and then the new bmat
			animtransforms.value[12*i+0] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+0];
			animtransforms.value[12*i+1] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+1];
			animtransforms.value[12*i+2] = bbmat.value[i*9+0*3+0]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+0]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+0]*pbmat.value[i*9+2*3+2];

			animtransforms.value[12*i+3+0] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+0];
			animtransforms.value[12*i+3+1] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+1];
			animtransforms.value[12*i+3+2] = bbmat.value[i*9+0*3+1]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+1]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+1]*pbmat.value[i*9+2*3+2];
			
			animtransforms.value[12*i+6+0] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+0]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+0]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+0];
			animtransforms.value[12*i+6+1] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+1]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+1]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+1];
			animtransforms.value[12*i+6+2] = bbmat.value[i*9+0*3+2]*pbmat.value[i*9+0*3+2]+bbmat.value[i*9+1*3+2]*pbmat.value[i*9+1*3+2]+bbmat.value[i*9+2*3+2]*pbmat.value[i*9+2*3+2];
			
			//translation is bmat rotation of ri then vpos				
			animtransforms.value[12*i+9] = rix*pbmat.value[i*9+0*3+0]+riy*pbmat.value[i*9+1*3+0]+riz*pbmat.value[i*9+2*3+0]+vpos.value[v0*3+0];
			animtransforms.value[12*i+10] = rix*pbmat.value[i*9+0*3+1]+riy*pbmat.value[i*9+1*3+1]+riz*pbmat.value[i*9+2*3+1]+vpos.value[v0*3+1];
			animtransforms.value[12*i+11] = rix*pbmat.value[i*9+0*3+2]+riy*pbmat.value[i*9+1*3+2]+riz*pbmat.value[i*9+2*3+2]+vpos.value[v0*3+2];

//			animtransforms.value[12*i+0] = 1.0f;
//			animtransforms.value[12*i+1] = 0.0f;
//			animtransforms.value[12*i+2] = 0.0f;
//
//			animtransforms.value[12*i+3+0] = 0.0f;
//			animtransforms.value[12*i+3+1] = 1.0f;
//			animtransforms.value[12*i+3+2] = 0.0f;
//			
//			animtransforms.value[12*i+6+0] = 0.0f;
//			animtransforms.value[12*i+6+1] = 0.0f;
//			animtransforms.value[12*i+6+2] = 1.0f;
//			
//			//translation is bmat rotation of ri then vpos				
//			animtransforms.value[12*i+9] = 0.0f;
//			animtransforms.value[12*i+10] = 0.0f;
//			animtransforms.value[12*i+11] = 0.0f;
		}

		//now go through the skin
		dpos.clear(0.0f);
		int swn = sw.length;
		for(int si=0;si<swn;si++)
		{
			int i = sb[si*2+0];
			int i0 = sb[si*2+1];
			float w = sw[si];
			
			float x = pos.value[i*3+0];
			float y = pos.value[i*3+1];
			float z = pos.value[i*3+2];
			
			//transform using the matrices as labelled in the skin matrix
			float x0 = x*animtransforms.value[i0*12+0*3+0]+y*animtransforms.value[i0*12+1*3+0]+z*animtransforms.value[i0*12+2*3+0]+animtransforms.value[i0*12+3*3+0];
			float y0 = x*animtransforms.value[i0*12+0*3+1]+y*animtransforms.value[i0*12+1*3+1]+z*animtransforms.value[i0*12+2*3+1]+animtransforms.value[i0*12+3*3+1];
			float z0 = x*animtransforms.value[i0*12+0*3+2]+y*animtransforms.value[i0*12+1*3+2]+z*animtransforms.value[i0*12+2*3+2]+animtransforms.value[i0*12+3*3+2];

			dpos.value[i*3+0] += w*x0;
			dpos.value[i*3+1] += w*y0;
			dpos.value[i*3+2] += w*z0;
		}
	}
	
	public static void updateSkinUsingSkeleton(
			GeneralMatrixFloat vpos0,GeneralMatrixFloat pbmat0, //animated position
			float f0,
			GeneralMatrixFloat vpos1,GeneralMatrixFloat pbmat1, //animated position
			float f1,
			GeneralMatrixFloat bvpos,GeneralMatrixFloat bbmat, GeneralMatrixFloat pos, //bind position and skin			
			GeneralMatrixInt bones,GeneralMatrixInt sb, GeneralMatrixFloat sw,
			GeneralMatrixFloat dpos)
	{
		//now calculate the transform matricies for each bone (ones which take a point and translate and rotate it with the new values)
		GeneralMatrixFloat animtransforms0 = new GeneralMatrixFloat(12,bones.height);
		GeneralMatrixFloat animtransforms1 = new GeneralMatrixFloat(12,bones.height);
		for(int i=0;i<animtransforms0.height;i++)
		{
			//Invert the old transform and then apply the new one
			int v0 = bones.value[i*2];
			
			//invert trans
			float ix = -bvpos.value[v0*3+0];
			float iy = -bvpos.value[v0*3+1];
			float iz = -bvpos.value[v0*3+2];
			
			//rotate with transpose of bindmat (i.e. i and j are flipped)
			
			float rix = ix*bbmat.value[i*9+0*3+0]+iy*bbmat.value[i*9+0*3+1]+iz*bbmat.value[i*9+0*3+2];
			float riy = ix*bbmat.value[i*9+1*3+0]+iy*bbmat.value[i*9+1*3+1]+iz*bbmat.value[i*9+1*3+2];
			float riz = ix*bbmat.value[i*9+2*3+0]+iy*bbmat.value[i*9+2*3+1]+iz*bbmat.value[i*9+2*3+2];
			
			//the new transform is a composition of the inverse and then the new bmat
			animtransforms0.value[12*i+0] = bbmat.value[i*9+0*3+0]*pbmat0.value[i*9+0*3+0]+bbmat.value[i*9+1*3+0]*pbmat0.value[i*9+1*3+0]+bbmat.value[i*9+2*3+0]*pbmat0.value[i*9+2*3+0];
			animtransforms0.value[12*i+1] = bbmat.value[i*9+0*3+0]*pbmat0.value[i*9+0*3+1]+bbmat.value[i*9+1*3+0]*pbmat0.value[i*9+1*3+1]+bbmat.value[i*9+2*3+0]*pbmat0.value[i*9+2*3+1];
			animtransforms0.value[12*i+2] = bbmat.value[i*9+0*3+0]*pbmat0.value[i*9+0*3+2]+bbmat.value[i*9+1*3+0]*pbmat0.value[i*9+1*3+2]+bbmat.value[i*9+2*3+0]*pbmat0.value[i*9+2*3+2];

			animtransforms0.value[12*i+3+0] = bbmat.value[i*9+0*3+1]*pbmat0.value[i*9+0*3+0]+bbmat.value[i*9+1*3+1]*pbmat0.value[i*9+1*3+0]+bbmat.value[i*9+2*3+1]*pbmat0.value[i*9+2*3+0];
			animtransforms0.value[12*i+3+1] = bbmat.value[i*9+0*3+1]*pbmat0.value[i*9+0*3+1]+bbmat.value[i*9+1*3+1]*pbmat0.value[i*9+1*3+1]+bbmat.value[i*9+2*3+1]*pbmat0.value[i*9+2*3+1];
			animtransforms0.value[12*i+3+2] = bbmat.value[i*9+0*3+1]*pbmat0.value[i*9+0*3+2]+bbmat.value[i*9+1*3+1]*pbmat0.value[i*9+1*3+2]+bbmat.value[i*9+2*3+1]*pbmat0.value[i*9+2*3+2];
			
			animtransforms0.value[12*i+6+0] = bbmat.value[i*9+0*3+2]*pbmat0.value[i*9+0*3+0]+bbmat.value[i*9+1*3+2]*pbmat0.value[i*9+1*3+0]+bbmat.value[i*9+2*3+2]*pbmat0.value[i*9+2*3+0];
			animtransforms0.value[12*i+6+1] = bbmat.value[i*9+0*3+2]*pbmat0.value[i*9+0*3+1]+bbmat.value[i*9+1*3+2]*pbmat0.value[i*9+1*3+1]+bbmat.value[i*9+2*3+2]*pbmat0.value[i*9+2*3+1];
			animtransforms0.value[12*i+6+2] = bbmat.value[i*9+0*3+2]*pbmat0.value[i*9+0*3+2]+bbmat.value[i*9+1*3+2]*pbmat0.value[i*9+1*3+2]+bbmat.value[i*9+2*3+2]*pbmat0.value[i*9+2*3+2];
			
			//translation is bmat rotation of ri then vpos				
			animtransforms0.value[12*i+9] = rix*pbmat0.value[i*9+0*3+0]+riy*pbmat0.value[i*9+1*3+0]+riz*pbmat0.value[i*9+2*3+0]+vpos0.value[v0*3+0];
			animtransforms0.value[12*i+10] = rix*pbmat0.value[i*9+0*3+1]+riy*pbmat0.value[i*9+1*3+1]+riz*pbmat0.value[i*9+2*3+1]+vpos0.value[v0*3+1];
			animtransforms0.value[12*i+11] = rix*pbmat0.value[i*9+0*3+2]+riy*pbmat0.value[i*9+1*3+2]+riz*pbmat0.value[i*9+2*3+2]+vpos0.value[v0*3+2];

			//the new transform is a composition of the inverse and then the new bmat
			animtransforms1.value[12*i+0] = bbmat.value[i*9+0*3+0]*pbmat1.value[i*9+0*3+0]+bbmat.value[i*9+1*3+0]*pbmat1.value[i*9+1*3+0]+bbmat.value[i*9+2*3+0]*pbmat1.value[i*9+2*3+0];
			animtransforms1.value[12*i+1] = bbmat.value[i*9+0*3+0]*pbmat1.value[i*9+0*3+1]+bbmat.value[i*9+1*3+0]*pbmat1.value[i*9+1*3+1]+bbmat.value[i*9+2*3+0]*pbmat1.value[i*9+2*3+1];
			animtransforms1.value[12*i+2] = bbmat.value[i*9+0*3+0]*pbmat1.value[i*9+0*3+2]+bbmat.value[i*9+1*3+0]*pbmat1.value[i*9+1*3+2]+bbmat.value[i*9+2*3+0]*pbmat1.value[i*9+2*3+2];

			animtransforms1.value[12*i+3+0] = bbmat.value[i*9+0*3+1]*pbmat1.value[i*9+0*3+0]+bbmat.value[i*9+1*3+1]*pbmat1.value[i*9+1*3+0]+bbmat.value[i*9+2*3+1]*pbmat1.value[i*9+2*3+0];
			animtransforms1.value[12*i+3+1] = bbmat.value[i*9+0*3+1]*pbmat1.value[i*9+0*3+1]+bbmat.value[i*9+1*3+1]*pbmat1.value[i*9+1*3+1]+bbmat.value[i*9+2*3+1]*pbmat1.value[i*9+2*3+1];
			animtransforms1.value[12*i+3+2] = bbmat.value[i*9+0*3+1]*pbmat1.value[i*9+0*3+2]+bbmat.value[i*9+1*3+1]*pbmat1.value[i*9+1*3+2]+bbmat.value[i*9+2*3+1]*pbmat1.value[i*9+2*3+2];
			
			animtransforms1.value[12*i+6+0] = bbmat.value[i*9+0*3+2]*pbmat1.value[i*9+0*3+0]+bbmat.value[i*9+1*3+2]*pbmat1.value[i*9+1*3+0]+bbmat.value[i*9+2*3+2]*pbmat1.value[i*9+2*3+0];
			animtransforms1.value[12*i+6+1] = bbmat.value[i*9+0*3+2]*pbmat1.value[i*9+0*3+1]+bbmat.value[i*9+1*3+2]*pbmat1.value[i*9+1*3+1]+bbmat.value[i*9+2*3+2]*pbmat1.value[i*9+2*3+1];
			animtransforms1.value[12*i+6+2] = bbmat.value[i*9+0*3+2]*pbmat1.value[i*9+0*3+2]+bbmat.value[i*9+1*3+2]*pbmat1.value[i*9+1*3+2]+bbmat.value[i*9+2*3+2]*pbmat1.value[i*9+2*3+2];
			
			//translation is bmat rotation of ri then vpos				
			animtransforms1.value[12*i+9] = rix*pbmat1.value[i*9+0*3+0]+riy*pbmat1.value[i*9+1*3+0]+riz*pbmat1.value[i*9+2*3+0]+vpos1.value[v0*3+0];
			animtransforms1.value[12*i+10] = rix*pbmat1.value[i*9+0*3+1]+riy*pbmat1.value[i*9+1*3+1]+riz*pbmat1.value[i*9+2*3+1]+vpos1.value[v0*3+1];
			animtransforms1.value[12*i+11] = rix*pbmat1.value[i*9+0*3+2]+riy*pbmat1.value[i*9+1*3+2]+riz*pbmat1.value[i*9+2*3+2]+vpos1.value[v0*3+2];
		}

		//now go through the skin
		dpos.clear(0.0f);
		for(int si=0;si<sw.height;si++)
		{
			int i = sb.value[si*2+0];
			int i0 = sb.value[si*2+1];
			float w = sw.value[si];
			
			float w0 = w*f0;
			float w1 = w*f1;
			
			float x = pos.value[i*3+0];
			float y = pos.value[i*3+1];
			float z = pos.value[i*3+2];
			
			//transform using the matrices as labelled in the skin matrix
			float x0 = x*animtransforms0.value[i0*12+0*3+0]+y*animtransforms0.value[i0*12+1*3+0]+z*animtransforms0.value[i0*12+2*3+0]+animtransforms0.value[i0*12+3*3+0];
			float y0 = x*animtransforms0.value[i0*12+0*3+1]+y*animtransforms0.value[i0*12+1*3+1]+z*animtransforms0.value[i0*12+2*3+1]+animtransforms0.value[i0*12+3*3+1];
			float z0 = x*animtransforms0.value[i0*12+0*3+2]+y*animtransforms0.value[i0*12+1*3+2]+z*animtransforms0.value[i0*12+2*3+2]+animtransforms0.value[i0*12+3*3+2];
			float x1 = x*animtransforms1.value[i0*12+0*3+0]+y*animtransforms1.value[i0*12+1*3+0]+z*animtransforms1.value[i0*12+2*3+0]+animtransforms1.value[i0*12+3*3+0];
			float y1 = x*animtransforms1.value[i0*12+0*3+1]+y*animtransforms1.value[i0*12+1*3+1]+z*animtransforms1.value[i0*12+2*3+1]+animtransforms1.value[i0*12+3*3+1];
			float z1 = x*animtransforms1.value[i0*12+0*3+2]+y*animtransforms1.value[i0*12+1*3+2]+z*animtransforms1.value[i0*12+2*3+2]+animtransforms1.value[i0*12+3*3+2];

			dpos.value[i*3+0] += w0*x0+w1*x1;
			dpos.value[i*3+1] += w0*y0+w1*y1;
			dpos.value[i*3+2] += w0*z0+w1*z1;
		}
	}
	
	public static void calculateBoneUpdateOrder(GeneralMatrixInt boneParents,GeneralMatrixInt bones,int numVerts,
			GeneralMatrixInt boneUpdateOrder)
	{
		GeneralMatrixInt vertSet = new GeneralMatrixInt(1,numVerts);
		vertSet.clear(0);
		GeneralMatrixInt boneSet = new GeneralMatrixInt(1,boneParents.height);
		boneSet.clear(0);
		boneUpdateOrder.width = 1;
		boneUpdateOrder.height = 0;
		
		boolean allset = false;
		while(!allset)
		{
			allset = true;
			for(int i=0;i<boneParents.height;i++)
			{
				if(boneSet.value[i]==1)
					continue;
				
				int v0 = bones.value[i*2+0];
				int v1 = bones.value[i*2+1];
				
				int p = boneParents.value[i];
				if(p==-1)
				{
					boneUpdateOrder.push_back(i);
					boneSet.value[i] = 1;
					vertSet.value[v0] = 1;
					vertSet.value[v1] = 1;
					continue;
				}
				
				if(
						(boneSet.value[p]==1)&&
						(vertSet.value[v0]==1)
				  )
				{
					boneUpdateOrder.push_back(i);
					boneSet.value[i] = 1;
					vertSet.value[v1] = 1;
				}
				else
				{
					allset = false;
				}
			}
		}
	}
	
	public static void calcLocalRotationParams(
			GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixInt boneUpdateOrder,
			GeneralMatrixFloat bmats,
			GeneralMatrixDouble localbindbmats,
			GeneralMatrixDouble params
			)
	{
		params.setDimensions(3, bones.height+1);
		//Go through the bones and calculate their starting local mats (deformations are relative to this)
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = ubi;
			if(boneUpdateOrder!=null)
				bi = boneUpdateOrder.value[ubi];
			int pbi = boneParents.value[bi];
			
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(bmats, bi);
			bmat.width = 3;
			bmat.height = 3;
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(bmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}
			
			 //Calc inverse
			 GeneralMatrixDouble.transpose(pbmat, pbmati);
			//calc the local matrix
			 //calc the local bmat
			 bmatl.width = 3;
			 bmatl.height = 3;
			 GeneralMatrixDouble.mult(bmat, pbmati, bmatl);
			 
			 //Now need to calc the inverse of the localbindmat
			 //and compose to get the relative localmat
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(localbindbmats, bi);
			bmat.width = 3;
			bmat.height = 3;

			 GeneralMatrixDouble.transpose(bmat, pbmati);
			 GeneralMatrixDouble.mult(bmatl, pbmati, bmat);
			 
			 GeneralMatrixDouble.getEulerYXZ(bmat, euler);
			 //now lets get the props from the params
			 params.setRow(bi+1,euler);
		}
	}
	
	public static void calcLocalRotationParams(
			GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixInt boneUpdateOrder,
			GeneralMatrixDouble bmats,
			GeneralMatrixDouble localbindbmats,
			GeneralMatrixDouble params
			)
	{
		params.setDimensions(3, bones.height+1);
		//Go through the bones and calculate their starting local mats (deformations are relative to this)
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = ubi;
			if(boneUpdateOrder!=null)
				bi = boneUpdateOrder.value[ubi];
			int pbi = boneParents.value[bi];
			
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(bmats, bi);
			bmat.width = 3;
			bmat.height = 3;
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(bmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}
			
			 //Calc inverse
			 GeneralMatrixDouble.transpose(pbmat, pbmati);
			//calc the local matrix
			 //calc the local bmat
			 bmatl.width = 3;
			 bmatl.height = 3;
			 GeneralMatrixDouble.mult(bmat, pbmati, bmatl);
			 
			 //Now need to calc the inverse of the localbindmat
			 //and compose to get the relative localmat
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(localbindbmats, bi);
			bmat.width = 3;
			bmat.height = 3;

			 GeneralMatrixDouble.transpose(bmat, pbmati);
			 GeneralMatrixDouble.mult(bmatl, pbmati, bmat);
			 
			 GeneralMatrixDouble.getEulerYXZ(bmat, euler);
			 //now lets get the props from the params
			 params.setRow(bi+1,euler);
		}
	}
	
	public static void calcLocalRotationParams(
			GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixInt boneUpdateOrder,
			GeneralMatrixFloat bmats,
			GeneralMatrixDouble localbindbmats,
			GeneralMatrixFloat params
			)
	{
		params.setDimensions(3, bones.height+1);
		//Go through the bones and calculate their starting local mats (deformations are relative to this)
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = boneUpdateOrder.value[ubi];
			int pbi = boneParents.value[bi];
			
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(bmats, bi);
			bmat.width = 3;
			bmat.height = 3;
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(bmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}
			
			 //Calc inverse
			 GeneralMatrixDouble.transpose(pbmat, pbmati);
			//calc the local matrix
			 //calc the local bmat
			 bmatl.width = 3;
			 bmatl.height = 3;
			 GeneralMatrixDouble.mult(bmat, pbmati, bmatl);
			 
			 //Now need to calc the inverse of the localbindmat
			 //and compose to get the relative localmat
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(localbindbmats, bi);
			bmat.width = 3;
			bmat.height = 3;

			 GeneralMatrixDouble.transpose(bmat, pbmati);
			 GeneralMatrixDouble.mult(bmatl, pbmati, bmat);
			 
			 GeneralMatrixDouble.getEulerYXZ(bmat, euler);
			 //now lets get the props from the params
			 params.setRow(bi+1,euler);
		}
	}
	
	public static void calcLocalRotationParams(
			GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixInt boneUpdateOrder,
			GeneralMatrixFloat bmats,
			GeneralMatrixFloat localbindbmats,
			GeneralMatrixFloat params
			)
	{
		params.setDimensions(3, bones.height+1);
		//Go through the bones and calculate their starting local mats (deformations are relative to this)
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = ubi;
			if(boneUpdateOrder!=null)
				bi = boneUpdateOrder.value[ubi];
			int pbi = boneParents.value[bi];
			
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(bmats, bi);
			bmat.width = 3;
			bmat.height = 3;
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(bmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}
			
			 //Calc inverse
			 GeneralMatrixDouble.transpose(pbmat, pbmati);
			//calc the local matrix
			 //calc the local bmat
			 bmatl.width = 3;
			 bmatl.height = 3;
			 GeneralMatrixDouble.mult(bmat, pbmati, bmatl);
			 
			 //Now need to calc the inverse of the localbindmat
			 //and compose to get the relative localmat
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(localbindbmats, bi);
			bmat.width = 3;
			bmat.height = 3;

			 GeneralMatrixDouble.transpose(bmat, pbmati);
			 GeneralMatrixDouble.mult(bmatl, pbmati, bmat);
			 
			 GeneralMatrixDouble.getEulerYXZ(bmat, euler);
			 //now lets get the props from the params
			 params.setRow(bi+1,euler);
		}
	}	
	public static float calcLocalRotationParams(
			GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixInt boneUpdateOrder,
			GeneralMatrixFloat bmats,
			GeneralMatrixFloat localbindbmats,
			GeneralMatrixFloat localboneangleLimits,
			GeneralMatrixFloat params
			)
	{
		float totalerror = 0.0f;
		params.setDimensions(3, bones.height+1);
		//Go through the bones and calculate their starting local mats (deformations are relative to this)
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = ubi;
			if(boneUpdateOrder!=null)
				bi = boneUpdateOrder.value[ubi];
			int pbi = boneParents.value[bi];
			
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(bmats, bi);
			bmat.width = 3;
			bmat.height = 3;
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(bmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}
			
			 //Calc inverse
			 GeneralMatrixDouble.transpose(pbmat, pbmati);
			//calc the local matrix
			 //calc the local bmat
			 bmatl.width = 3;
			 bmatl.height = 3;
			 GeneralMatrixDouble.mult(bmat, pbmati, bmatl);
			 
			 //Now need to calc the inverse of the localbindmat
			 //and compose to get the relative localmat
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(localbindbmats, bi);
			bmat.width = 3;
			bmat.height = 3;

			 GeneralMatrixDouble.transpose(bmat, pbmati);
			 GeneralMatrixDouble.mult(bmatl, pbmati, bmat);
			 
			 GeneralMatrixDouble.getEulerYXZ(bmat, euler);
			 //now lets get the props from the params
			 params.setRow(bi+1,euler);

			for(int li=0;li<3;li++)
			{
				int lind = bi*3+3+li;
				if(params.value[lind]<localboneangleLimits.value[6*bi+li*2])
				{
					float dl = Math.abs(params.value[lind]-localboneangleLimits.value[6*bi+li*2+0]);
					totalerror += dl;
					params.value[lind] = localboneangleLimits.value[6*bi+li*2];
				}
				if(params.value[lind]>localboneangleLimits.value[6*bi+li*2+1])
				{
					float dl = Math.abs(params.value[lind]-localboneangleLimits.value[6*bi+li*2+1]);
					totalerror += dl;
					params.value[lind] = localboneangleLimits.value[6*bi+li*2+1];
				}
			}
		}
		return totalerror;
	}	
	public static String calcLocalRotationParams(
			GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixInt boneUpdateOrder,
			GeneralMatrixFloat bmats,
			GeneralMatrixFloat localbindbmats,
			GeneralMatrixFloat localboneangleLimits,
			GeneralMatrixFloat params,
			GeneralMatrixString plog
			)
	{
		float totalerror = 0.0f;
		params.setDimensions(3, bones.height+1);
		//Go through the bones and calculate their starting local mats (deformations are relative to this)
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = ubi;
			if(boneUpdateOrder!=null)
				bi = boneUpdateOrder.value[ubi];
			int pbi = boneParents.value[bi];
			
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(bmats, bi);
			bmat.width = 3;
			bmat.height = 3;
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(bmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}
			
			 //Calc inverse
			 GeneralMatrixDouble.transpose(pbmat, pbmati);
			//calc the local matrix
			 //calc the local bmat
			 bmatl.width = 3;
			 bmatl.height = 3;
			 GeneralMatrixDouble.mult(bmat, pbmati, bmatl);
			 
			 //Now need to calc the inverse of the localbindmat
			 //and compose to get the relative localmat
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(localbindbmats, bi);
			bmat.width = 3;
			bmat.height = 3;

			 GeneralMatrixDouble.transpose(bmat, pbmati);
			 GeneralMatrixDouble.mult(bmatl, pbmati, bmat);
			 
			 GeneralMatrixDouble.getEulerYXZ(bmat, euler);
			 //now lets get the props from the params
			 params.setRow(bi+1,euler);

			for(int li=0;li<3;li++)
			{
				int lind = bi*3+3+li;
				if(params.value[lind]<localboneangleLimits.value[6*bi+li*2])
				{
					float dl = Math.abs(params.value[lind]-localboneangleLimits.value[6*bi+li*2+0]);
					if(dl>0.01f)
						plog.value[0] += ""+HumanBones.snames[bi]+":"+li+":"+dl+":"+params.value[lind]/HumanBones.DEG2RAD+"\n";
					params.value[lind] = localboneangleLimits.value[6*bi+li*2];
				}
				if(params.value[lind]>localboneangleLimits.value[6*bi+li*2+1])
				{
					float dl = Math.abs(params.value[lind]-localboneangleLimits.value[6*bi+li*2+1]);
					if(dl>0.01f)
						plog.value[0] += ""+HumanBones.snames[bi]+":"+li+":"+dl+":"+params.value[lind]/HumanBones.DEG2RAD+"\n";
					params.value[lind] = localboneangleLimits.value[6*bi+li*2+1];
				}
			}
		}
		return plog.value[0];
	}	
	public static void calcLocalBindMats(
			GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixInt boneUpdateOrder,
			GeneralMatrixFloat bmats,
			GeneralMatrixDouble localbindbmats)
	{
		localbindbmats.setDimensions(9,bones.height);
		//Go through the bones and calculate their starting local mats (deformations are relative to this)
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = ubi;
			if(boneUpdateOrder!=null)
				bi=boneUpdateOrder.value[ubi];
			int pbi = boneParents.value[bi];
			
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(bmats, bi);
			bmat.width = 3;
			bmat.height = 3;
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(bmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}
			
			 //Calc inverse
			 GeneralMatrixDouble.transpose(pbmat, pbmati);
			//calc the local matrix
			 //calc the local bmat
			 bmatl.width = 3;
			 bmatl.height = 3;
			 GeneralMatrixDouble.mult(bmat, pbmati, bmatl);
			 
			 localbindbmats.setRow(bi,bmatl);
		}
	}

	public static void calcLocalBindMats(
			GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixFloat bmats,
			GeneralMatrixDouble localbindbmats)
	{
		localbindbmats.setDimensions(9,bones.height);
		//Go through the bones and calculate their starting local mats (deformations are relative to this)
		for(int bi=0;bi<bones.height;bi++)
		{
			int pbi = boneParents.value[bi];
			
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(bmats, bi);
			bmat.width = 3;
			bmat.height = 3;
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(bmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}
			
			 //Calc inverse
			 GeneralMatrixDouble.transpose(pbmat, pbmati);
			//calc the local matrix
			 //calc the local bmat
			 bmatl.width = 3;
			 bmatl.height = 3;
			 GeneralMatrixDouble.mult(bmat, pbmati, bmatl);
			 
			 localbindbmats.setRow(bi,bmatl);
		}
	}

	public static void calcLocalBindMats(
			int[] bones,
			int[] boneParents,
			GeneralMatrixDouble bmats,
			GeneralMatrixDouble localbindbmats)
	{
		int bonen = bones.length/2;
		localbindbmats.setDimensions(9,bonen);
		//Go through the bones and calculate their starting local mats (deformations are relative to this)
		for(int bi=0;bi<bonen;bi++)
		{
			int pbi = boneParents[bi];
			
			bmat.width = 9;
			bmat.height = 1;
			bmat.setFromSubset(bmats, bi);
			bmat.width = 3;
			bmat.height = 3;
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(bmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}
			
			 //Calc inverse
			 GeneralMatrixDouble.transpose(pbmat, pbmati);
			//calc the local matrix
			 //calc the local bmat
			 bmatl.width = 3;
			 bmatl.height = 3;
			 GeneralMatrixDouble.mult(bmat, pbmati, bmatl);
			 
			 localbindbmats.setRow(bi,bmatl);
		}
	}

	public static void calcLocalBindMats(
			int[] bones,
			int[] boneParents,
			GeneralMatrixFloat bmats,
			GeneralMatrixFloat localbindbmats)
	{
		int bonen = bones.length/2;
		localbindbmats.setDimensions(9,bonen);
		//Go through the bones and calculate their starting local mats (deformations are relative to this)
		for(int bi=0;bi<bonen;bi++)
		{
			int pbi = boneParents[bi];
			
			bmatf.width = 9;
			bmatf.height = 1;
			bmatf.setFromSubset(bmats, bi);
			bmatf.width = 3;
			bmatf.height = 3;
			if(pbi==-1)
			{
				pbmatf.width = 3;
				pbmatf.height = 3;
				pbmatf.setIdentity();
			}
			else
			{
				pbmatf.width = 9;
				pbmatf.height = 1;
				pbmatf.setFromSubset(bmats, pbi);
				pbmatf.width = 3;
				pbmatf.height = 3;
			}
			
			 //Calc inverse
			 GeneralMatrixFloat.transpose(pbmatf, pbmatif);
			//calc the local matrix
			 //calc the local bmat
			 bmatlf.width = 3;
			 bmatlf.height = 3;
			 GeneralMatrixFloat.mult(bmatf, pbmatif, bmatlf);
			 
			 localbindbmats.setRow(bi,bmatlf);
		}
	}

	public static void transformWithParams(	int[] bones,
			int[] boneParents,
			GeneralMatrixFloat bonelengths,
			GeneralMatrixFloat localbindbmats,
			GeneralMatrixFloat tvpos,
			GeneralMatrixFloat tbmats,
			GeneralMatrixFloat lpos)
	{
		//Build the transforms
		int rv = bones[0];
		tvpos.value[rv*3+0] = lpos.value[0];
		tvpos.value[rv*3+1] = lpos.value[1];
		tvpos.value[rv*3+2] = lpos.value[2];

		int bonesn = bones.length/2;
		bmatf.width = 3;
		bmatf.height = 3;
		for(int bi=0;bi<bonesn;bi++)
		{
			int v0 = bones[bi*2+0];
			int v1 = bones[bi*2+1];

			//world orientation of parent
			int pbi = boneParents[bi];
			if(pbi==-1)
			{
				pbmatf.width = 3;
				pbmatf.height = 3;
				pbmatf.setIdentity();
			}
			else
			{
				pbmatf.width = 9;
				pbmatf.height = 1;
				pbmatf.setFromSubset(tbmats, pbi);
				pbmatf.width = 3;
				pbmatf.height = 3;
			}

			bmatlf.width = 9;
			bmatlf.height = 1;
			bmatlf.setFromSubset(localbindbmats, bi);
			bmatlf.width = 3;
			bmatlf.height = 3;
			
			//Now compose with the parent matrix if it exists and the local bind matrix
			GeneralMatrixFloat.mult(bmatlf, pbmatf, bmatbwf);
			
			float xrot = lpos.value[bi*3+0+3];
			float yrot = lpos.value[bi*3+1+3];
			float zrot = lpos.value[bi*3+2+3];
			
			bmatf.set3DTransformRotationYXZ(xrot,yrot,zrot);
			GeneralMatrixFloat.mult(bmatf, bmatbwf, bmatwf);
			
			tbmats.setRow(bi, bmatwf);
			tvpos.value[v1*3+0] = (float)(tvpos.value[v0*3+0]+bonelengths.value[bi]*bmatwf.value[3*1+0]);
			tvpos.value[v1*3+1] = (float)(tvpos.value[v0*3+1]+bonelengths.value[bi]*bmatwf.value[3*1+1]);
			tvpos.value[v1*3+2] = (float)(tvpos.value[v0*3+2]+bonelengths.value[bi]*bmatwf.value[3*1+2]);	
		}
	}
	
	public static void transformWithParams(	int[] bones,
			int[] boneParents,
			GeneralMatrixFloat bonelengths,
			GeneralMatrixFloat localbindbmats,
			GeneralMatrixFloat tvpos,
			GeneralMatrixFloat tbmats,
			GeneralMatrixFloat lpos,
			GeneralMatrixObject bindtransOverwrite,
			GeneralMatrixObject wtransOverwrite)
	{
		//Build the transforms
		int rv = bones[0];
		tvpos.value[rv*3+0] = lpos.value[0];
		tvpos.value[rv*3+1] = lpos.value[1];
		tvpos.value[rv*3+2] = lpos.value[2];

		int bonesn = bones.length/2;
		bmatf.width = 3;
		bmatf.height = 3;
		for(int bi=0;bi<bonesn;bi++)
		{
			int v0 = bones[bi*2+0];
			int v1 = bones[bi*2+1];

			if((wtransOverwrite!=null)&&(wtransOverwrite.value[bi]!=null))
			{
				bmatwf.set((GeneralMatrixFloat)wtransOverwrite.value[bi]);
			}
			else
			if((bindtransOverwrite!=null)&&(bindtransOverwrite.value[bi]!=null))
			{
				bmatlf.width = 9;
				bmatlf.height = 1;
				bmatlf.setFromSubset(localbindbmats, bi);
				bmatlf.width = 3;
				bmatlf.height = 3;
				GeneralMatrixFloat.mult(bmatlf, (GeneralMatrixFloat)bindtransOverwrite.value[bi], bmatwf);
			}
			else
			{
				//world orientation of parent
				int pbi = boneParents[bi];
				if(pbi==-1)
				{
					pbmatf.width = 3;
					pbmatf.height = 3;
					pbmatf.setIdentity();
				}
				else
				{
					pbmatf.width = 9;
					pbmatf.height = 1;
					pbmatf.setFromSubset(tbmats, pbi);
					pbmatf.width = 3;
					pbmatf.height = 3;
				}

				bmatlf.width = 9;
				bmatlf.height = 1;
				bmatlf.setFromSubset(localbindbmats, bi);
				bmatlf.width = 3;
				bmatlf.height = 3;
				
				//Now compose with the parent matrix if it exists and the local bind matrix
				GeneralMatrixFloat.mult(bmatlf, pbmatf, bmatbwf);
				
				float xrot = lpos.value[bi*3+0+3];
				float yrot = lpos.value[bi*3+1+3];
				float zrot = lpos.value[bi*3+2+3];
				
				bmatf.set3DTransformRotationYXZ(xrot,yrot,zrot);
				GeneralMatrixFloat.mult(bmatf, bmatbwf, bmatwf);
				
			}
			tbmats.setRow(bi, bmatwf);
			tvpos.value[v1*3+0] = (float)(tvpos.value[v0*3+0]+bonelengths.value[bi]*bmatwf.value[3*1+0]);
			tvpos.value[v1*3+1] = (float)(tvpos.value[v0*3+1]+bonelengths.value[bi]*bmatwf.value[3*1+1]);
			tvpos.value[v1*3+2] = (float)(tvpos.value[v0*3+2]+bonelengths.value[bi]*bmatwf.value[3*1+2]);	
		}
	}

	public static void transformWithParams(	int[] bones,
			int[] boneParents,
			GeneralMatrixDouble bonelengths,
			GeneralMatrixDouble localbindbmats,
//			boolean calcingJacobian,
//			boolean onlyApplyRigidConstraints,
			GeneralMatrixDouble tvpos,
			GeneralMatrixDouble tbmats,
			GeneralMatrixDouble params)
	{
		transformWithParams(bones, boneParents, bonelengths, localbindbmats, null, tvpos, tbmats, params);
	}
	
	public static void transformWithParams(	int[] bones,
			int[] boneParents,
			GeneralMatrixDouble bonelengths,
			GeneralMatrixDouble localbindbmats,
			GeneralMatrixFloat localboneangleLimits,
//			boolean calcingJacobian,
//			boolean onlyApplyRigidConstraints,
			GeneralMatrixDouble tvpos,
			GeneralMatrixDouble tbmats,
			GeneralMatrixDouble params)
	{
//		boolean oldCalcingJacobian = calcingJacobian; 
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = true;
		//Build the transforms
		int rv = bones[0];
		tvpos.value[rv*3+0] = params.value[0];
		tvpos.value[rv*3+1] = params.value[1];
		tvpos.value[rv*3+2] = params.value[2];

		int bonesn = bones.length/2;
		bmat.width = 3;
		bmat.height = 3;
		for(int ubi=0;ubi<bonesn;ubi++)
		{
			int bi = ubi;
//			if(boneUpdateOrder!=null)
//				bi = boneUpdateOrder.value[ubi];
			int v0 = bones[bi*2+0];
			int v1 = bones[bi*2+1];

			//world orientation of parent
			int pbi = boneParents[bi];
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(tbmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}

			bmatl.width = 9;
			bmatl.height = 1;
			bmatl.setFromSubset(localbindbmats, bi);
			bmatl.width = 3;
			bmatl.height = 3;
			
			//Now compose with the parent matrix if it exists and the local bind matrix
			GeneralMatrixDouble.mult(bmatl, pbmat, bmatbw);
			
			double xrot = params.value[bi*3+0+3];
			double yrot = params.value[bi*3+1+3];
			double zrot = params.value[bi*3+2+3];
//*
			if(localboneangleLimits!=null)
			{
				int ind = 6*bi;
				
//				if(calcingJacobian)
				{
					float xrange = localboneangleLimits.value[ind+1]-localboneangleLimits.value[ind+0]; 
					float yrange = localboneangleLimits.value[ind+3]-localboneangleLimits.value[ind+2]; 
					float zrange = localboneangleLimits.value[ind+5]-localboneangleLimits.value[ind+4]; 
					if(xrange<GeneralMatrixFloat.EPSILON)
						xrot = 0.0;
					if(yrange<GeneralMatrixFloat.EPSILON)
						yrot = 0.0;
					if(zrange<GeneralMatrixFloat.EPSILON)
						zrot = 0.0;
				}
//				else
//				{
//					if(localboneangleLimits.value[ind+0]>xrot)
//						xrot = localboneangleLimits.value[ind+0];
//					if(localboneangleLimits.value[ind+1]<xrot)
//						xrot = localboneangleLimits.value[ind+1];
//					if(localboneangleLimits.value[ind+2]>yrot)
//						yrot = localboneangleLimits.value[ind+2];
//					if(localboneangleLimits.value[ind+3]<yrot)
//						yrot = localboneangleLimits.value[ind+3];
//					if(localboneangleLimits.value[ind+4]>zrot)
//						zrot = localboneangleLimits.value[ind+4];
//					if(localboneangleLimits.value[ind+5]<zrot)
//						zrot = localboneangleLimits.value[ind+5];
//				}
			}
	//*/		
			bmat.set3DTransformRotationYXZ(xrot,yrot,zrot);
			GeneralMatrixDouble.mult(bmat, bmatbw, bmatw);
			
//			for(int biv=0;biv<9;biv++)
//			{
//				double d = bmatw.value[biv]-tbmats.value[bi*9+biv];
//				if(Math.abs(d)>0.000001)
//				{
//					System.out.println("Eh");
//				}
//			}
			
			tbmats.setRow(bi, bmatw);
			tvpos.value[v1*3+0] = (tvpos.value[v0*3+0]+bonelengths.value[bi]*bmatw.value[3*1+0]);
			tvpos.value[v1*3+1] = (tvpos.value[v0*3+1]+bonelengths.value[bi]*bmatw.value[3*1+1]);
			tvpos.value[v1*3+2] = (tvpos.value[v0*3+2]+bonelengths.value[bi]*bmatw.value[3*1+2]);	
		}
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = oldCalcingJacobian;
	}

	public static void transformWithParams(	int[] bones,
			int[] boneParents,
			GeneralMatrixDouble bonelengths,
			GeneralMatrixDouble localbindbmats,
			GeneralMatrixFloat localboneangleLimits,
//			boolean calcingJacobian,
//			boolean onlyApplyRigidConstraints,
			GeneralMatrixFloat tvpos,
			GeneralMatrixFloat tbmats,
			GeneralMatrixDouble params)
	{
//		boolean oldCalcingJacobian = calcingJacobian; 
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = true;
		//Build the transforms
		int rv = bones[0];
		tvpos.value[rv*3+0] = (float)params.value[0];
		tvpos.value[rv*3+1] = (float)params.value[1];
		tvpos.value[rv*3+2] = (float)params.value[2];

		int bonesn = bones.length/2;
		bmat.width = 3;
		bmat.height = 3;
		for(int ubi=0;ubi<bonesn;ubi++)
		{
			int bi = ubi;
//			if(boneUpdateOrder!=null)
//				bi = boneUpdateOrder.value[ubi];
			int v0 = bones[bi*2+0];
			int v1 = bones[bi*2+1];

			//world orientation of parent
			int pbi = boneParents[bi];
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(tbmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}

			bmatl.width = 9;
			bmatl.height = 1;
			bmatl.setFromSubset(localbindbmats, bi);
			bmatl.width = 3;
			bmatl.height = 3;
			
			//Now compose with the parent matrix if it exists and the local bind matrix
			GeneralMatrixDouble.mult(bmatl, pbmat, bmatbw);
			
			double xrot = params.value[bi*3+0+3];
			double yrot = params.value[bi*3+1+3];
			double zrot = params.value[bi*3+2+3];
//*
			if(localboneangleLimits!=null)
			{
				int ind = 6*bi;
				
//				if(calcingJacobian)
				{
					float xrange = localboneangleLimits.value[ind+1]-localboneangleLimits.value[ind+0]; 
					float yrange = localboneangleLimits.value[ind+3]-localboneangleLimits.value[ind+2]; 
					float zrange = localboneangleLimits.value[ind+5]-localboneangleLimits.value[ind+4]; 
					if(xrange<GeneralMatrixFloat.EPSILON)
						xrot = 0.0;
					if(yrange<GeneralMatrixFloat.EPSILON)
						yrot = 0.0;
					if(zrange<GeneralMatrixFloat.EPSILON)
						zrot = 0.0;
				}
//				else
//				{
//					if(localboneangleLimits.value[ind+0]>xrot)
//						xrot = localboneangleLimits.value[ind+0];
//					if(localboneangleLimits.value[ind+1]<xrot)
//						xrot = localboneangleLimits.value[ind+1];
//					if(localboneangleLimits.value[ind+2]>yrot)
//						yrot = localboneangleLimits.value[ind+2];
//					if(localboneangleLimits.value[ind+3]<yrot)
//						yrot = localboneangleLimits.value[ind+3];
//					if(localboneangleLimits.value[ind+4]>zrot)
//						zrot = localboneangleLimits.value[ind+4];
//					if(localboneangleLimits.value[ind+5]<zrot)
//						zrot = localboneangleLimits.value[ind+5];
//				}
			}
	//*/		
			bmat.set3DTransformRotationYXZ(xrot,yrot,zrot);
			GeneralMatrixDouble.mult(bmat, bmatbw, bmatw);
			
//			for(int biv=0;biv<9;biv++)
//			{
//				double d = bmatw.value[biv]-tbmats.value[bi*9+biv];
//				if(Math.abs(d)>0.000001)
//				{
//					System.out.println("Eh");
//				}
//			}
			
			tbmats.setRow(bi, bmatw);
			tvpos.value[v1*3+0] = (float)(tvpos.value[v0*3+0]+bonelengths.value[bi]*bmatw.value[3*1+0]);
			tvpos.value[v1*3+1] = (float)(tvpos.value[v0*3+1]+bonelengths.value[bi]*bmatw.value[3*1+1]);
			tvpos.value[v1*3+2] = (float)(tvpos.value[v0*3+2]+bonelengths.value[bi]*bmatw.value[3*1+2]);	
		}
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = oldCalcingJacobian;
	}

	/*
	public static void transformWithParams(int[] bones,
			int[] boneParents,
			int[] boneUpdateOrder,
			GeneralMatrixDouble bonelengths,
			GeneralMatrixDouble localbindbmats,
			GeneralMatrixFloat localboneangleLimits,
//			boolean calcingJacobian,
//			boolean onlyApplyRigidConstraints,
			GeneralMatrixDouble tvpos,
			GeneralMatrixDouble tbmats,
			GeneralMatrixDouble params)
	{
//		boolean oldCalcingJacobian = calcingJacobian; 
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = true;
		//Build the transforms
		int rv = bones.value[0];
		tvpos.value[rv*3+0] = params.value[0];
		tvpos.value[rv*3+1] = params.value[1];
		tvpos.value[rv*3+2] = params.value[2];

		bmat.width = 3;
		bmat.height = 3;
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = ubi;
			if(boneUpdateOrder!=null)
				bi = boneUpdateOrder.value[ubi];
			int v0 = bones.value[bi*2+0];
			int v1 = bones.value[bi*2+1];

			//world orientation of parent
			int pbi = boneParents.value[bi];
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(tbmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}

			bmatl.width = 9;
			bmatl.height = 1;
			bmatl.setFromSubset(localbindbmats, bi);
			bmatl.width = 3;
			bmatl.height = 3;
			
			//Now compose with the parent matrix if it exists and the local bind matrix
			GeneralMatrixDouble.mult(bmatl, pbmat, bmatbw);
			double xrot = params.value[bi*3+0+3];
			double yrot = params.value[bi*3+1+3];
			double zrot = params.value[bi*3+2+3];
			if(localboneangleLimits!=null)
			{
				int ind = 6*bi;
//				if(calcingJacobian)
				{
					float xrange = localboneangleLimits.value[ind+1]-localboneangleLimits.value[ind+0]; 
					float yrange = localboneangleLimits.value[ind+3]-localboneangleLimits.value[ind+2]; 
					float zrange = localboneangleLimits.value[ind+5]-localboneangleLimits.value[ind+4]; 
					if(xrange<GeneralMatrixFloat.EPSILON)
						xrot = 0.0;
					if(yrange<GeneralMatrixFloat.EPSILON)
						yrot = 0.0;
					if(zrange<GeneralMatrixFloat.EPSILON)
						zrot = 0.0;
				}
//				else
//				{
//					if(localboneangleLimits.value[ind+0]>xrot)
//						xrot = localboneangleLimits.value[ind+0];
//					if(localboneangleLimits.value[ind+1]<xrot)
//						xrot = localboneangleLimits.value[ind+1];
//					if(localboneangleLimits.value[ind+2]>yrot)
//						yrot = localboneangleLimits.value[ind+2];
//					if(localboneangleLimits.value[ind+3]<yrot)
//						yrot = localboneangleLimits.value[ind+3];
//					if(localboneangleLimits.value[ind+4]>zrot)
//						zrot = localboneangleLimits.value[ind+4];
//					if(localboneangleLimits.value[ind+5]<zrot)
//						zrot = localboneangleLimits.value[ind+5];
//				}
			}
			
			bmat.set3DTransformRotationYXZ(xrot,yrot,zrot);
			//bmat.set3DTransformRotationYFirst(params.value[bi*3+0+3], params.value[bi*3+1+3], params.value[bi*3+2+3]);
			GeneralMatrixDouble.mult(bmat, bmatbw, bmatw);
			
			tbmats.setRow(bi, bmatw);
			tvpos.value[v1*3+0] = tvpos.value[v0*3+0]+bonelengths.value[bi]*bmatw.value[3*1+0];
			tvpos.value[v1*3+1] = tvpos.value[v0*3+1]+bonelengths.value[bi]*bmatw.value[3*1+1];
			tvpos.value[v1*3+2] = tvpos.value[v0*3+2]+bonelengths.value[bi]*bmatw.value[3*1+2];	
		}
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = oldCalcingJacobian;
	}
	*/
	
	public static void constrainParams(GeneralMatrixDouble params,GeneralMatrixDouble cparams,
			GeneralMatrixFloat localboneangleLimits,
			boolean calcingJacobian,
			boolean onlyApplyRigidConstraints)
	{
		if(onlyApplyRigidConstraints)
			calcingJacobian = true;

		int numbones = (params.height*params.width/3)-1;
		for(int bi=0;bi<(numbones);bi++)
		{
		double xrot = params.value[bi*3+0+3];
		double yrot = params.value[bi*3+1+3];
		double zrot = params.value[bi*3+2+3];
		if(localboneangleLimits!=null)
		{
			int ind = 6*bi;
			if(calcingJacobian)
			{
				float xrange = localboneangleLimits.value[ind+1]-localboneangleLimits.value[ind+0]; 
				float yrange = localboneangleLimits.value[ind+3]-localboneangleLimits.value[ind+2]; 
				float zrange = localboneangleLimits.value[ind+5]-localboneangleLimits.value[ind+4]; 
				if(xrange<GeneralMatrixFloat.EPSILON)
					xrot = 0.0;
				if(yrange<GeneralMatrixFloat.EPSILON)
					yrot = 0.0;
				if(zrange<GeneralMatrixFloat.EPSILON)
					zrot = 0.0;
			}
			else
			{
				if(localboneangleLimits.value[ind+0]>xrot)
					xrot = localboneangleLimits.value[ind+0];
				if(localboneangleLimits.value[ind+1]<xrot)
					xrot = localboneangleLimits.value[ind+1];
				if(localboneangleLimits.value[ind+2]>yrot)
					yrot = localboneangleLimits.value[ind+2];
				if(localboneangleLimits.value[ind+3]<yrot)
					yrot = localboneangleLimits.value[ind+3];
				if(localboneangleLimits.value[ind+4]>zrot)
					zrot = localboneangleLimits.value[ind+4];
				if(localboneangleLimits.value[ind+5]<zrot)
					zrot = localboneangleLimits.value[ind+5];
			}
		}
		cparams.value[bi*3+0+3] = xrot;
		cparams.value[bi*3+1+3] = yrot;
		cparams.value[bi*3+2+3] = zrot;
		}
	}
	
	public static void transformWithParams(GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixInt boneUpdateOrder,
			GeneralMatrixDouble bonelengths,
			GeneralMatrixDouble localbindbmats,
			GeneralMatrixFloat localboneangleLimits,
//			boolean calcingJacobian,
//			boolean onlyApplyRigidConstraints,
			GeneralMatrixFloat tvpos,
			GeneralMatrixFloat tbmats,
			GeneralMatrixFloat params)
	{
//		boolean oldCalcingJacobian = calcingJacobian; 
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = true;
		//Build the transforms
		int rv = bones.value[0];
		tvpos.value[rv*3+0] = params.value[0];
		tvpos.value[rv*3+1] = params.value[1];
		tvpos.value[rv*3+2] = params.value[2];

		bmat.width = 3;
		bmat.height = 3;
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = boneUpdateOrder.value[ubi];
			int v0 = bones.value[bi*2+0];
			int v1 = bones.value[bi*2+1];

			//world orientation of parent
			int pbi = boneParents.value[bi];
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(tbmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}

			bmatl.width = 9;
			bmatl.height = 1;
			bmatl.setFromSubset(localbindbmats, bi);
			bmatl.width = 3;
			bmatl.height = 3;
			
			//Now compose with the parent matrix if it exists and the local bind matrix
			GeneralMatrixDouble.mult(bmatl, pbmat, bmatbw);
			double xrot = params.value[bi*3+0+3];
			double yrot = params.value[bi*3+1+3];
			double zrot = params.value[bi*3+2+3];
			if(localboneangleLimits!=null)
			{
				int ind = 6*bi;
//				if(calcingJacobian)
				{
					float xrange = localboneangleLimits.value[ind+1]-localboneangleLimits.value[ind+0]; 
					float yrange = localboneangleLimits.value[ind+3]-localboneangleLimits.value[ind+2]; 
					float zrange = localboneangleLimits.value[ind+5]-localboneangleLimits.value[ind+4]; 
					if(xrange<GeneralMatrixFloat.EPSILON)
						xrot = 0.0;
					if(yrange<GeneralMatrixFloat.EPSILON)
						yrot = 0.0;
					if(zrange<GeneralMatrixFloat.EPSILON)
						zrot = 0.0;
				}
//				else
//				{
//					if(localboneangleLimits.value[ind+0]>xrot)
//						xrot = localboneangleLimits.value[ind+0];
//					if(localboneangleLimits.value[ind+1]<xrot)
//						xrot = localboneangleLimits.value[ind+1];
//					if(localboneangleLimits.value[ind+2]>yrot)
//						yrot = localboneangleLimits.value[ind+2];
//					if(localboneangleLimits.value[ind+3]<yrot)
//						yrot = localboneangleLimits.value[ind+3];
//					if(localboneangleLimits.value[ind+4]>zrot)
//						zrot = localboneangleLimits.value[ind+4];
//					if(localboneangleLimits.value[ind+5]<zrot)
//						zrot = localboneangleLimits.value[ind+5];
//				}
			}
			
			bmat.set3DTransformRotationYXZ(xrot,yrot,zrot);
			//bmat.set3DTransformRotationYFirst(params.value[bi*3+0+3], params.value[bi*3+1+3], params.value[bi*3+2+3]);
			GeneralMatrixDouble.mult(bmat, bmatbw, bmatw);
			
			tbmats.setRow(bi, bmatw);
			tvpos.value[v1*3+0] = (float)(tvpos.value[v0*3+0]+bonelengths.value[bi]*bmatw.value[3*1+0]);
			tvpos.value[v1*3+1] = (float)(tvpos.value[v0*3+1]+bonelengths.value[bi]*bmatw.value[3*1+1]);
			tvpos.value[v1*3+2] = (float)(tvpos.value[v0*3+2]+bonelengths.value[bi]*bmatw.value[3*1+2]);	
		}
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = oldCalcingJacobian;
	}

	public static void transformWithParams(GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixInt boneUpdateOrder,
			GeneralMatrixDouble bonelengths,
			GeneralMatrixDouble localbindbmats,
			GeneralMatrixFloat localboneangleLimits,
//			boolean calcingJacobian,
//			boolean onlyApplyRigidConstraints,
			GeneralMatrixFloat tvpos,
			GeneralMatrixFloat tbmats,
			GeneralMatrixDouble params)
	{
//		boolean oldCalcingJacobian = calcingJacobian; 
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = true;
		//Build the transforms
		int rv = bones.value[0];
		tvpos.value[rv*3+0] = (float)params.value[0];
		tvpos.value[rv*3+1] = (float)params.value[1];
		tvpos.value[rv*3+2] = (float)params.value[2];

		bmat.width = 3;
		bmat.height = 3;
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = boneUpdateOrder.value[ubi];
			int v0 = bones.value[bi*2+0];
			int v1 = bones.value[bi*2+1];

			//world orientation of parent
			int pbi = boneParents.value[bi];
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(tbmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}

			bmatl.width = 9;
			bmatl.height = 1;
			bmatl.setFromSubset(localbindbmats, bi);
			bmatl.width = 3;
			bmatl.height = 3;
			
			//Now compose with the parent matrix if it exists and the local bind matrix
			GeneralMatrixDouble.mult(bmatl, pbmat, bmatbw);
			double xrot = params.value[bi*3+0+3];
			double yrot = params.value[bi*3+1+3];
			double zrot = params.value[bi*3+2+3];
			if(localboneangleLimits!=null)
			{
				int ind = 6*bi;
//				if(calcingJacobian)
				{
					float xrange = localboneangleLimits.value[ind+1]-localboneangleLimits.value[ind+0]; 
					float yrange = localboneangleLimits.value[ind+3]-localboneangleLimits.value[ind+2]; 
					float zrange = localboneangleLimits.value[ind+5]-localboneangleLimits.value[ind+4]; 
					if(xrange<GeneralMatrixFloat.EPSILON)
						xrot = 0.0;
					if(yrange<GeneralMatrixFloat.EPSILON)
						yrot = 0.0;
					if(zrange<GeneralMatrixFloat.EPSILON)
						zrot = 0.0;
				}
//				else
//				{
//					if(localboneangleLimits.value[ind+0]>xrot)
//						xrot = localboneangleLimits.value[ind+0];
//					if(localboneangleLimits.value[ind+1]<xrot)
//						xrot = localboneangleLimits.value[ind+1];
//					if(localboneangleLimits.value[ind+2]>yrot)
//						yrot = localboneangleLimits.value[ind+2];
//					if(localboneangleLimits.value[ind+3]<yrot)
//						yrot = localboneangleLimits.value[ind+3];
//					if(localboneangleLimits.value[ind+4]>zrot)
//						zrot = localboneangleLimits.value[ind+4];
//					if(localboneangleLimits.value[ind+5]<zrot)
//						zrot = localboneangleLimits.value[ind+5];
//				}
			}
			
			bmat.set3DTransformRotationYXZ(xrot,yrot,zrot);
			//bmat.set3DTransformRotationYFirst(params.value[bi*3+0+3], params.value[bi*3+1+3], params.value[bi*3+2+3]);
			GeneralMatrixDouble.mult(bmat, bmatbw, bmatw);
			
			tbmats.setRow(bi, bmatw);
			tvpos.value[v1*3+0] = (float)(tvpos.value[v0*3+0]+bonelengths.value[bi]*bmatw.value[3*1+0]);
			tvpos.value[v1*3+1] = (float)(tvpos.value[v0*3+1]+bonelengths.value[bi]*bmatw.value[3*1+1]);
			tvpos.value[v1*3+2] = (float)(tvpos.value[v0*3+2]+bonelengths.value[bi]*bmatw.value[3*1+2]);	
		}
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = oldCalcingJacobian;
	}

	public static void transformWithParams(GeneralMatrixInt bones,
			GeneralMatrixInt boneParents,
			GeneralMatrixInt boneUpdateOrder,
			GeneralMatrixDouble bonelengths,
			GeneralMatrixDouble localbindbmats,
			GeneralMatrixFloat localboneangleLimits,
//			boolean calcingJacobian,
//			boolean onlyApplyRigidConstraints,
			GeneralMatrixDouble tvpos,
			GeneralMatrixDouble tbmats,
			GeneralMatrixDouble params)
	{
//		boolean oldCalcingJacobian = calcingJacobian; 
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = true;
		//Build the transforms
		int rv = bones.value[0];
		tvpos.value[rv*3+0] = params.value[0];
		tvpos.value[rv*3+1] = params.value[1];
		tvpos.value[rv*3+2] = params.value[2];

		bmat.width = 3;
		bmat.height = 3;
		for(int ubi=0;ubi<bones.height;ubi++)
		{
			int bi = boneUpdateOrder.value[ubi];
			int v0 = bones.value[bi*2+0];
			int v1 = bones.value[bi*2+1];

			//world orientation of parent
			int pbi = boneParents.value[bi];
			if(pbi==-1)
			{
				pbmat.width = 3;
				pbmat.height = 3;
				pbmat.setIdentity();
			}
			else
			{
				pbmat.width = 9;
				pbmat.height = 1;
				pbmat.setFromSubset(tbmats, pbi);
				pbmat.width = 3;
				pbmat.height = 3;
			}

			bmatl.width = 9;
			bmatl.height = 1;
			bmatl.setFromSubset(localbindbmats, bi);
			bmatl.width = 3;
			bmatl.height = 3;
			
			//Now compose with the parent matrix if it exists and the local bind matrix
			GeneralMatrixDouble.mult(bmatl, pbmat, bmatbw);
			double xrot = params.value[bi*3+0+3];
			double yrot = params.value[bi*3+1+3];
			double zrot = params.value[bi*3+2+3];
			if(localboneangleLimits!=null)
			{
				int ind = 6*bi;
//				if(calcingJacobian)
				{
					float xrange = localboneangleLimits.value[ind+1]-localboneangleLimits.value[ind+0]; 
					float yrange = localboneangleLimits.value[ind+3]-localboneangleLimits.value[ind+2]; 
					float zrange = localboneangleLimits.value[ind+5]-localboneangleLimits.value[ind+4]; 
					if(xrange<GeneralMatrixFloat.EPSILON)
						xrot = 0.0;
					if(yrange<GeneralMatrixFloat.EPSILON)
						yrot = 0.0;
					if(zrange<GeneralMatrixFloat.EPSILON)
						zrot = 0.0;
				}
//				else
//				{
//					if(localboneangleLimits.value[ind+0]>xrot)
//						xrot = localboneangleLimits.value[ind+0];
//					if(localboneangleLimits.value[ind+1]<xrot)
//						xrot = localboneangleLimits.value[ind+1];
//					if(localboneangleLimits.value[ind+2]>yrot)
//						yrot = localboneangleLimits.value[ind+2];
//					if(localboneangleLimits.value[ind+3]<yrot)
//						yrot = localboneangleLimits.value[ind+3];
//					if(localboneangleLimits.value[ind+4]>zrot)
//						zrot = localboneangleLimits.value[ind+4];
//					if(localboneangleLimits.value[ind+5]<zrot)
//						zrot = localboneangleLimits.value[ind+5];
//				}
			}
			
			bmat.set3DTransformRotationYXZ(xrot,yrot,zrot);
			//bmat.set3DTransformRotationYFirst(params.value[bi*3+0+3], params.value[bi*3+1+3], params.value[bi*3+2+3]);
			GeneralMatrixDouble.mult(bmat, bmatbw, bmatw);
			
			tbmats.setRow(bi, bmatw);
			tvpos.value[v1*3+0] = (tvpos.value[v0*3+0]+bonelengths.value[bi]*bmatw.value[3*1+0]);
			tvpos.value[v1*3+1] = (tvpos.value[v0*3+1]+bonelengths.value[bi]*bmatw.value[3*1+1]);
			tvpos.value[v1*3+2] = (tvpos.value[v0*3+2]+bonelengths.value[bi]*bmatw.value[3*1+2]);	
		}
//		if(onlyApplyRigidConstraints)
//			calcingJacobian = oldCalcingJacobian;
	}

//	public static void pose(GeneralMatrixFloat bvpos,GeneralMatrixFloat bbmats,
//			GeneralMatrixInt bones,GeneralMatrixInt boneParents,GeneralMatrixInt boneUpdateOrder,
//			GeneralMatrixDouble lpos,
//			GeneralMatrixFloat vpos,GeneralMatrixFloat bmats)
//	{
//		 EvalLocalBones ebones;
//		 ebones = new EvalLocalBones(bvpos, bbmats, bones, 
//			 boneUpdateOrder, boneParents, 
//			 null, 
//			 null, null);
//	
//	ebones.transformWithParams(vpos, bmats, lpos);		
//	}

//	public static void pose(GeneralMatrixFloat bvpos,GeneralMatrixFloat bbmats,
//							GeneralMatrixInt bones,GeneralMatrixInt boneParents,GeneralMatrixInt boneUpdateOrder,
//							GeneralMatrixFloat lpos,
//							GeneralMatrixFloat vpos,GeneralMatrixFloat bmats)
//	{
//		 EvalLocalBones ebones;
//			 ebones = new EvalLocalBones(bvpos, bbmats, bones, 
//				 boneUpdateOrder, boneParents, 
//				 null, 
//				 null, null);
//		 
//		ebones.transformWithParams(vpos, bmats, lpos);		
//	}
	
}
