/*
Copyright (c) 2008-Present John Bustard  http://johndavidbustard.com

This code is release under the GPL http://www.gnu.org/licenses/gpl.html

To get the latest version of this code goto http://johndavidbustard.com/mmconst.html
*/
package utils;

import java.io.Serializable;

public class GeneralMatrixDouble  implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final double EPSILON=5.9e-8f;
	public int width; //columns
	public int height; //rows
	public double[] value; //array of values

	public static void main(String[] list) throws Exception 
    {
		GeneralMatrixDouble pose = new GeneralMatrixDouble(3,3);
		GeneralMatrixDouble vpose = new GeneralMatrixDouble(3,3);
		GeneralMatrixDouble euler = new GeneralMatrixDouble(3,1);
		
		GeneralMatrixDouble tests = new GeneralMatrixDouble(3);

		double v = Math.PI;
		tests.push_back_row(0.0,0.0,0.0);
		tests.push_back_row(v,0.0,0.0);
		tests.push_back_row(0.0,v,0.0);
		tests.push_back_row(v,v,0.0);
		tests.push_back_row(0.0,0.0,v);
		tests.push_back_row(v,0.0,v);
		tests.push_back_row(0.0,v,v);
		tests.push_back_row(v,v,v);

		for(int i=0;i<tests.height;i++)
		{
			pose.set3DTransformRotationYXZ(tests.value[i*3+0], tests.value[i*3+1], tests.value[i*3+2]);
			GeneralMatrixDouble.getEulerYXZ(pose, euler);
			double errx = euler.value[0]-tests.value[i*3+0];
			double erry = euler.value[1]-tests.value[i*3+1];
			double errz = euler.value[2]-tests.value[i*3+2];
			System.out.println("err "+i+" x:"+errx+" y:"+erry+" z:"+errz);
			vpose.set3DTransformRotationYXZ(euler.value[0], euler.value[1], euler.value[2]);
			
		}
    }
	
	public void printTransform2()
	{
		System.out.println("Xx:"+value[0+0]+ "\tXy:"+value[1+0]);
		System.out.println("Yx:"+value[0+2]+ "\tYy:"+value[1+2]);
	}
	public void printTransform()
	{
		System.out.println("Xx:"+value[0+0]+ "\tXy:"+value[1+0]+ "\tXz:"+value[2+0]);
		System.out.println("Yx:"+value[0+4]+ "\tYy:"+value[1+4]+ "\tYz:"+value[2+4]);
		System.out.println("Zx:"+value[0+8]+ "\tZy:"+value[1+8]+ "\tZz:"+value[2+8]);
		System.out.println("Tx:"+value[0+12]+"\tTy:"+value[1+12]+"\tTz:"+value[2+12]);
	}
	public void printProjection()
	{
		System.out.println("Xx:"+value[0+0]+ "\tXy:"+value[1+0]+ "\tXz:"+value[2+0]);
		System.out.println("Yx:"+value[0+4]+ "\tYy:"+value[1+4]+ "\tYz:"+value[2+4]);
		System.out.println("Zx:"+value[0+8]+ "\tZy:"+value[1+8]+ "\tZz:"+value[2+8]);
		System.out.println("Tx:"+value[0+12]+"\tTy:"+value[1+12]+"\tTz:"+value[2+12]);
		System.out.println("Hx:"+value[3+0]+ "\tHy:"+value[3+4]+ "\tHz:"+value[3+8]+"\tHt:"+value[3+12]);
	}
	
	public static double calculateMachineEpsilonDouble() 
	{
        double machEps = 1.0f;
 
        do {
           machEps /= 2.0f;
        }
        while ((double)(1.0 + (machEps/2.0)) != 1.0);
 
        return machEps;
    }
	
	public GeneralMatrixDouble(double[] v)
	{
		this.width = 1;
		this.height = v.length;
		value = v;
	}
	public GeneralMatrixDouble(double a)
	{
		this.width = 1;
		this.height = 1;
		value = new double[1];
		value[0] = a;
	}
	public GeneralMatrixDouble(double a,double b)
	{
		this.width = 1;
		this.height = 2;
		value = new double[2];
		value[0] = a;
		value[1] = b;
	}
	public GeneralMatrixDouble(GeneralMatrixDouble a)
	{
		this.width = a.width;
		this.height = a.height;
		value = new double[a.width*a.height];
		set(a);
	}
	public GeneralMatrixDouble(GeneralMatrixFloat a)
	{
		this.width = a.width;
		this.height = a.height;
		value = new double[a.width*a.height];
		set(a);
	}
	public GeneralMatrixDouble(int width,int height)
	{
		this.width = width;
		this.height = height;
		value = new double[width*height];
	}
	public GeneralMatrixDouble(int width,int height,double[] v)
	{
		this.width = width;
		this.height = height;
		value = v;
	}
	public GeneralMatrixDouble()
	{
		width = 0;
		height = 0;
	}

	  public boolean isequal(GeneralMatrixDouble m)
	  {
		  if(width!=m.width)
			  return false;
		  if(height!=m.height)
			  return false;
		  for (int i = 0; i < width*height; i++) 
		  {
			  if(value[i]!=m.value[i])
				  return false;
		  }		  
		  return true;
	  }

	  
	public void clear(double v)
	{
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = v;
		  }
	}

	public final double get(int i,int j)
	{
		return value[j*width+i];
	}

	public void reshape(int nw,int nh)
	{
		ensureCapacity(nw*nh);
		if(nw<width)
		{
			for(int j=0;j<nh;j++)
			{
				for(int i=0;i<nw;i++)
				{
					value[i+j*nw] = value[i+j*width];
				}
			}
		}
		else
		{
			for(int j=(nh-1);j>=0;j--)
			{
				for(int i=(nw-1);i>=0;i--)
				{
					value[i+j*nw] = value[i+j*width];
				}
			}
		}
		width = nw;
		height = nh;
	}
	
	public void reshapeleft(int nw,int nh)
	{
		ensureCapacity(nw*nh);
		if(nw<width)
		{
			for(int j=0;j<nh;j++)
			{
				for(int i=0;i<nw;i++)
				{
					value[i+j*nw] = value[i+(width-nw)+j*width];
				}
			}
		}
		else
		{
			for(int j=(nh-1);j>=0;j--)
			{
				for(int i=(nw-1);i>=0;i--)
				{
					value[i+(nw-width)+j*nw] = value[i+j*width];
				}
			}
		}
		width = nw;
		height = nh;
	}

	public double getMirror(int x, int y)
    {
        if (x >= width)
            x = width - (x - width + 2);

        if (y >= height)
            y = height - (y - height + 2);

        if (x < 0)
        {
            int tmp = 0;
            int dir = 1;

            while (x < 0)
            {
                tmp += dir;
                if (tmp == width - 1 || tmp == 0)
                    dir *= -1;
                x++;
            }
            x = tmp;
        }

        if (y < 0)
        {
            int tmp = 0;
            int dir = 1;

            while (y < 0)
            {
                tmp += dir;
                if (tmp == height - 1 || tmp == 0)
                    dir *= -1;
                y++;
            }
            y = tmp;
        }

       return value[x+y*width];
    }

	public void set(int i,int j,double v)
	{
		value[j*width+i] = v;
	}

	public void add(int i,int j,double v)
	{
		value[j*width+i] += v;
	}

	public void set(float[] a)
	{
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = a[i];
		  }
	}
	public void set(GeneralMatrixFloat a)
	{
		ensureCapacityNoCopy(a.width*a.height);
		width = a.width;
		height = a.height;
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = a.value[i];
		  }
	}
	public void set(GeneralMatrixDouble a)
	{
		ensureCapacityNoCopy(a.width*a.height);
		width = a.width;
		height = a.height;
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = a.value[i];
		  }
	}
	public void set(double[] a)
	{
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = (double)a[i];
		  }
	}
	public void set(double[][] a)
	{
		  for (int j = 0; j < height; j++)
		  {
			  for (int i = 0; i < width; i++)
			  {
				  value[i+j*width] = (double)a[j][i];
			  }
		  }
	}

	public void setDiagonal(double v)
	{
		  for (int i = 0; i < width; i++)
		  {
			  value[i+i*width] = v;
		  }
	}

	public static double determinant(GeneralMatrixDouble m)
	{
		int n = m.width; 
		if(n==1)
			return m.value[0];
		else
		if(n==2)
		{
			return m.value[0*2+0] * m.value[1*2+1] - m.value[1*2+0] * m.value[0*2+1];
		}
		else
		{
			float det = 0.0f;
			for (int j1=0;j1<n;j1++) 
			{
		         if(m.value[0*n+j1]==0.0f)
		        	 continue;

		         GeneralMatrixDouble subm = new GeneralMatrixDouble(n-1,n-1);

		         for (int i=1;i<n;i++) 
		         {
		            int j2 = 0;
		            for (int j=0;j<n;j++) 
		            {
		               if (j == j1)
		                  continue;
		               subm.value[(i-1)*(n-1)+j2] = m.value[i*n+j];
		               j2++;
		            }
		         }
		         int ind = 1+j1+1;
		         
		         if((ind%2)==0)
		        	 det += m.value[0*n+j1] * determinant(subm);
		         else
		        	 det -= m.value[0*n+j1] * determinant(subm);
			}
			return det;
		}
	}
	
	public void set3DTransformPosition(double x,double y,double z)
	{
		value[4*3+0] = x;
		value[4*3+1] = y;
		value[4*3+2] = z;
	}
	public void set3DTransformZRotation(double z)
	{
		double sin=(double)Math.sin(z);
		double cos=(double)Math.cos(z);
		value[width*0+0] = cos;
		value[width*0+1] = -sin;
		value[width*1+0] = sin;
		value[width*1+1] = cos;
		value[width*2+2] = 1.0f;
	}
	public void set3DTransformYRotation(double y)
	{
		double sin=(double)Math.sin(y);
		double cos=(double)Math.cos(y);
		value[width*0+0] = cos;
		value[width*0+2] = sin;
		value[width*2+0] = -sin;
		value[width*2+2] = cos;
		value[width*1+1] = 1.0f;
	}
	public void set3DTransformXRotation(double x)
	{
		double sin=(double)Math.sin(x);
		double cos=(double)Math.cos(x);
		value[width*1+1] = cos;
		value[width*1+2] = -sin;
		value[width*2+1] = sin;
		value[width*2+2] = cos;
		value[width*0+0] = 0.0f;
	}

	public void set3DTransformRotationYFirst(double x,double y,double z)
	{
		//Normal result
		double sx=(double)Math.sin(x);
		double cx=(double)Math.cos(x);
		double sy=(double)Math.sin(y);
		double cy=(double)Math.cos(y);
		double sz=(double)Math.sin(z);
		double cz=(double)Math.cos(z);
		
		//times this
		//0,  1, 0
		//-1, 0, 0
		//0,  0, 1

//		value[4*0+0] = cz*cy;
//		value[4*0+1] = (sy*sx*cz-cx*sz);
//		value[4*0+2] = (sy*cx*cz+sz*sx);
//
//		value[4*1+0] = sz*cy;
//		value[4*1+1] = (sy*sx*sz+cx*cz);
//		value[4*1+2] = (sy*cx*sz-cz*sx);
//
//		value[4*2+0] = -sy;
//		value[4*2+1] = cy*sx;
//		value[4*2+2] = cy*cx;

		
		//by this 
//		value[4*0+0] = sz*cy;
//		value[4*0+1] = (sy*sx*sz+cx*cz);
//		value[4*0+2] = (sy*cx*sz-cz*sx);
//
//		value[4*1+0] = -cz*cy;
//		value[4*1+1] = -(sy*sx*cz-cx*sz);
//		value[4*1+2] = -(sy*cx*cz+sz*sx);
//
//		value[4*2+0] = -sy;
//		value[4*2+1] = cy*sx;
//		value[4*2+2] = cy*cx;
		
		//times this
		//0,  -1, 0
		//1, 0, 0
		//0,  0, 1

		//First rotate by -90 z 		
		//x axis becomes y
		//y becomes -x
		
//		for (int k=0; k<3; k++) 
//		{
//		for (int j=0; j<3; j++) 
//		{
//			rbmat.value[k*3+j] = 
//				bmat.value[k*3+0] * procrustesTransform.value[0*4+j] +
//				bmat.value[k*3+1] * procrustesTransform.value[1*4+j] +
//				bmat.value[k*3+2] * procrustesTransform.value[2*4+j];
//		}
//		}
		
		if(width==4)
		{
			value[4*0+0] = (sy*sx*sz+cx*cz);
			value[4*0+1] = -sz*cy;
			value[4*0+2] = (sy*cx*sz-cz*sx);
	
			value[4*1+0] = -(sy*sx*cz-cx*sz);
			value[4*1+1] = cz*cy;
			value[4*1+2] = -(sy*cx*cz+sz*sx);
	
			value[4*2+0] = cy*sx;
			value[4*2+1] = sy;
			value[4*2+2] = cy*cx;
		}
		else
		{
			value[3*0+0] = (sy*sx*sz+cx*cz);
			value[3*0+1] = -sz*cy;
			value[3*0+2] = (sy*cx*sz-cz*sx);

			value[3*1+0] = -(sy*sx*cz-cx*sz);
			value[3*1+1] = cz*cy;
			value[3*1+2] = -(sy*cx*cz+sz*sx);

			value[3*2+0] = cy*sx;
			value[3*2+1] = sy;
			value[3*2+2] = cy*cx;
		}
		
		
		
		
		//then rotate back +90 z
		//x axis becomes -y
		//y becomes x
	}

	public void set3DTransformRotation(double x,double y,double z)
	{
		double sx=(double)Math.sin(x);
		double cx=(double)Math.cos(x);
		double sy=(double)Math.sin(y);
		double cy=(double)Math.cos(y);
		double sz=(double)Math.sin(z);
		double cz=(double)Math.cos(z);

	    if(width==4)
		{
			value[4*0+0] = cz*cy;
			value[4*0+1] = (sy*sx*cz-cx*sz);
			value[4*0+2] = (sy*cx*cz+sz*sx);
	
			value[4*1+0] = sz*cy;
			value[4*1+1] = (sy*sx*sz+cx*cz);
			value[4*1+2] = (sy*cx*sz-cz*sx);
	
			value[4*2+0] = -sy;
			value[4*2+1] = cy*sx;
			value[4*2+2] = cy*cx;
		}
		else
		{
			value[3*0+0] = cz*cy;
			value[3*0+1] = (sy*sx*cz-cx*sz);
			value[3*0+2] = (sy*cx*cz+sz*sx);
	
			value[3*1+0] = sz*cy;
			value[3*1+1] = (sy*sx*sz+cx*cz);
			value[3*1+2] = (sy*cx*sz-cz*sx);
	
			value[3*2+0] = -sy;
			value[3*2+1] = cy*sx;
			value[3*2+2] = cy*cx;
		}
	}
//	   [  (cy*cz)+(sy*sx*sz)   ,  (cy*(-(sz)))+(sy*sx*cz)   ,sy*cx]
//	   [                                                          ]
//	   [         cx*sz         ,           cx*cz            ,-(sx)]
//	   [                                                          ]
//	   [((-(sy))*cz)+(cy*sx*sz),((-(sy))*(-(sz)))+(cy*sx*cz),cy*cx]
	public void set3DTransformRotationYXZ(double x,double y,double z)
	{
		double sx=(double)Math.sin(x);
		double cx=(double)Math.cos(x);
		double sy=(double)Math.sin(y);
		double cy=(double)Math.cos(y);
		double sz=(double)Math.sin(z);
		double cz=(double)Math.cos(z);

		if(width==4)
		{
			value[4*0+0] = (cy*cz)+(sy*sx*sz);
			value[4*0+1] = (cy*(-(sz)))+(sy*sx*cz);
			value[4*0+2] = sy*cx;
	
			value[4*1+0] = cx*sz;
			value[4*1+1] = cx*cz;
			value[4*1+2] = -(sx);
	
			value[4*2+0] = ((-(sy))*cz)+(cy*sx*sz);
			value[4*2+1] = ((-(sy))*(-(sz)))+(cy*sx*cz);
			value[4*2+2] = cy*cx;
		}
		else
		{
			value[3*0+0] = (cy*cz)+(sy*sx*sz);
			value[3*0+1] = (cy*(-(sz)))+(sy*sx*cz);
			value[3*0+2] = sy*cx;
	
			value[3*1+0] = cx*sz;
			value[3*1+1] = cx*cz;
			value[3*1+2] = -(sx);
	
			value[3*2+0] = ((-(sy))*cz)+(cy*sx*sz);
			value[3*2+1] = ((-(sy))*(-(sz)))+(cy*sx*cz);
			value[3*2+2] = cy*cx;
		}
	}

	public static final void getEulerYXZ(GeneralMatrixFloat  m, GeneralMatrixFloat euler) 
	{
		final int i=0;
		final int j=1;
		final int k=2;

		final int w=m.width;
		
		double cx = Math.sqrt(m.value[j*w+i]*m.value[j*w+i] + m.value[j*w+j]*m.value[j*w+j]);
		if (cx > 16*EPSILON) {
		    euler.value[0] = (float)Math.atan2(-m.value[j*w+k], cx);
		    euler.value[1] = (float)Math.atan2(m.value[i*w+k], m.value[k*w+k]);
		    euler.value[2] = (float)Math.atan2(m.value[j*w+i], m.value[j*w+j]);
		} else {
			euler.value[0] = (float)Math.atan2(-m.value[j*w+k], cx);
		    euler.value[1] = 0;
		    euler.value[2] = (float)Math.atan2(m.value[i*w+i], m.value[i*w+j]);
		}
	}
	public static final void getEulerYXZ(GeneralMatrixDouble  m, GeneralMatrixDouble euler) 
	{
		final int i=0;
		final int j=1;
		final int k=2;

		final int w=m.width;
		
		double cx = Math.sqrt(m.value[j*w+i]*m.value[j*w+i] + m.value[j*w+j]*m.value[j*w+j]);
		if (cx > 16*EPSILON) {
		    euler.value[0] = (double)Math.atan2(-m.value[j*w+k], cx);
		    euler.value[1] = (double)Math.atan2(m.value[i*w+k], m.value[k*w+k]);
		    euler.value[2] = (double)Math.atan2(m.value[j*w+i], m.value[j*w+j]);
		} else {
			euler.value[0] = (double)Math.atan2(-m.value[j*w+k], cx);
		    euler.value[1] = 0;
		    euler.value[2] = (double)Math.atan2(m.value[i*w+i], m.value[i*w+j]);
		}
	}

	public static final void getEuler(GeneralMatrixFloat  m, GeneralMatrixDouble euler) 
	{
		final int i=0;
		final int j=1;
		final int k=2;

		final int w=m.width;

		double cy = Math.sqrt(m.value[i*w+i]*m.value[i*w+i] + m.value[j*w+i]*m.value[j*w+i]);
		if (cy > 16*EPSILON) {
		    euler.value[0] = (double)Math.atan2(m.value[k*w+j], m.value[k*w+k]);
		    euler.value[1] = (double)Math.atan2(-m.value[k*w+i], cy);
		    euler.value[2] = (double)Math.atan2(m.value[j*w+i], m.value[i*w+i]);
		} else {
		    euler.value[0] = (double)Math.atan2(-m.value[i*w+k], m.value[j*w+j]);
		    euler.value[1] = (double)Math.atan2(-m.value[k*w+i], cy);
		    euler.value[2] = 0;
		}
	}
	
	public static final void getEuler(GeneralMatrixDouble  m, GeneralMatrixDouble euler) 
	{
		final int i=0;
		final int j=1;
		final int k=2;

		final int w=m.width;

		double cy = Math.sqrt(m.value[i*w+i]*m.value[i*w+i] + m.value[j*w+i]*m.value[j*w+i]);
		if (cy > 16*EPSILON) {
		    euler.value[0] = (double)Math.atan2(m.value[k*w+j], m.value[k*w+k]);
		    euler.value[1] = (double)Math.atan2(-m.value[k*w+i], cy);
		    euler.value[2] = (double)Math.atan2(m.value[j*w+i], m.value[i*w+i]);
		} else {
		    euler.value[0] = (double)Math.atan2(-m.value[i*w+k], m.value[j*w+j]);
		    euler.value[1] = (double)Math.atan2(-m.value[k*w+i], cy);
		    euler.value[2] = 0;
		}
	}
	
	public static final void getEuler(GeneralMatrixDouble  m, GeneralMatrixFloat euler) 
	{
		final int i=0;
		final int j=1;
		final int k=2;

		final int w=m.width;

		double cy = Math.sqrt(m.value[i*w+i]*m.value[i*w+i] + m.value[j*w+i]*m.value[j*w+i]);
		if (cy > 16*EPSILON) {
		    euler.value[0] = (float)Math.atan2(m.value[k*w+j], m.value[k*w+k]);
		    euler.value[1] = (float)Math.atan2(-m.value[k*w+i], cy);
		    euler.value[2] = (float)Math.atan2(m.value[j*w+i], m.value[i*w+i]);
		} else {
		    euler.value[0] = (float)Math.atan2(-m.value[i*w+k], m.value[j*w+j]);
		    euler.value[1] = (float)Math.atan2(-m.value[k*w+i], cy);
		    euler.value[2] = 0;
		}
	}
	
	public void calcBasisFromY(double x,double y,double z,
			double Xx,double Xy,double Xz)
	{
		value[width*1+0] = x;
		value[width*1+1] = y;
		value[width*1+2] = z;
		double total = x*x;
		total += y*y;
		total += z*z;
		if(total<=EPSILON)
		{
			value[width*1+0] = 1.0f;
		}
		else
		{
			total = Math.sqrt(total);
		}
		value[width*1+0] /= total;
		value[width*1+1] /= total;
		value[width*1+2] /= total;
		
		total = Xx*Xx;
		total += Xy*Xy;
		total += Xz*Xz;
		if(total<=EPSILON)
		{
			value[width*0+0] = 1.0f;
		}
		else
		{
			total = Math.sqrt(total);
			value[width*0+0] = Xx/total;
			value[width*0+1] = Xy/total;
			value[width*0+2] = Xz/total;
		}

		
		value[width*2+0] = -y*value[width*0+2]+value[width*0+1]*z;
		value[width*2+1] = -z*value[width*0+0]+value[width*0+2]*x;
		value[width*2+2] = -x*value[width*0+1]+value[width*0+0]*y;

		//Normalise
		total = value[width*2+0]*value[width*2+0];
		total += value[width*2+1]*value[width*2+1];
		total += value[width*2+2]*value[width*2+2];
		if(total<=EPSILON)
		{
			value[width*2+0] = 1.0f;
		}
		else
		{
			total = Math.sqrt(total);
			value[width*2+0] /= total;
			value[width*2+1] /= total;
			value[width*2+2] /= total;
		}
		
		value[width*0+0] = y*value[width*2+2]-value[width*2+1]*z;
		value[width*0+1] = z*value[width*2+0]-value[width*2+2]*x;
		value[width*0+2] = x*value[width*2+1]-value[width*2+0]*y;

		total = value[width*0+0]*value[width*0+0];
		total += value[width*0+1]*value[width*0+1];
		total += value[width*0+2]*value[width*0+2];
		if(total<=EPSILON)
		{
			value[width*0+0] = 1.0f;
		}
		else
		{
			total = Math.sqrt(total);
			value[width*0+0] /= total;
			value[width*0+1] /= total;
			value[width*0+2] /= total;
		}
	}

	public void calcBasisFromY(double x,double y,double z,
			double Xx,double Xy,double Xz,
			int width,int off)
	{
		value[width*1+0+off] = x;
		value[width*1+1+off] = y;
		value[width*1+2+off] = z;
		double total = x*x;
		total += y*y;
		total += z*z;
		if(total<=EPSILON)
		{
			value[width*1+0+off] = 1.0f;
		}
		else
		{
			total = Math.sqrt(total);
		}
		value[width*1+0+off] /= total;
		value[width*1+1+off] /= total;
		value[width*1+2+off] /= total;
		
		total = Xx*Xx;
		total += Xy*Xy;
		total += Xz*Xz;
		if(total<=EPSILON)
		{
			value[width*0+0+off] = 1.0f;
		}
		else
		{
			total = Math.sqrt(total);
			value[width*0+0+off] = Xx/total;
			value[width*0+1+off] = Xy/total;
			value[width*0+2+off] = Xz/total;
		}

		
		value[width*2+0+off] = -y*value[width*0+2+off]+value[width*0+1+off]*z;
		value[width*2+1+off] = -z*value[width*0+0+off]+value[width*0+2+off]*x;
		value[width*2+2+off] = -x*value[width*0+1+off]+value[width*0+0+off]*y;

		//Normalise
		total = value[width*2+0+off]*value[width*2+0+off];
		total += value[width*2+1+off]*value[width*2+1+off];
		total += value[width*2+2+off]*value[width*2+2+off];
		if(total<=EPSILON)
		{
			value[width*2+0+off] = 1.0f;
		}
		else
		{
			total = Math.sqrt(total);
			value[width*2+0+off] /= total;
			value[width*2+1+off] /= total;
			value[width*2+2+off] /= total;
		}
		
		value[width*0+0+off] = y*value[width*2+2+off]-value[width*2+1+off]*z;
		value[width*0+1+off] = z*value[width*2+0+off]-value[width*2+2+off]*x;
		value[width*0+2+off] = x*value[width*2+1+off]-value[width*2+0+off]*y;

		total = value[width*0+0+off]*value[width*0+0+off];
		total += value[width*0+1+off]*value[width*0+1+off];
		total += value[width*0+2+off]*value[width*0+2+off];
		if(total<=EPSILON)
		{
			value[width*0+0+off] = 1.0f;
		}
		else
		{
			total = Math.sqrt(total);
			value[width*0+0+off] /= total;
			value[width*0+1+off] /= total;
			value[width*0+2+off] /= total;
		}
	}

	public void setSubset(GeneralMatrixDouble subset,int xs,int ys)
	{
		int maxy = ys+subset.height;
		int maxx = xs+subset.width;
		for(int y=ys;y<maxy;y++)
		{
			for(int x=xs;x<maxx;x++)
			{
				value[x+y*width] = subset.value[(x-xs)+(y-ys)*subset.width];
			}
		}
	}
	public void clearSubset(int xs,int ys,int sw,int sh,double v)
	{
		int maxy = ys+sh;
		int maxx = xs+sw;
		for(int y=ys;y<maxy;y++)
		{
			for(int x=xs;x<maxx;x++)
			{
				value[x+y*width] = v;
			}
		}
	}

	//For setting a vec or mat from an array of vecs/mats
	public void setFromSubset(GeneralMatrixDouble full,int ys)
	{
		int i=0;
		int fi = ys*width;
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				value[i] = full.value[fi];
				i++;
				fi++;
			}
		}
	}
	public void setFromSubset(GeneralMatrixFloat full,int ys)
	{
		int i=0;
		int fi = ys*width;
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				value[i] = full.value[fi];
				i++;
				fi++;
			}
		}
	}

    public void setRow(int r,GeneralMatrixDouble row)
    {
    	System.arraycopy(row.value, 0, value, width*(r), width);
    }
	
	public static double norm(GeneralMatrixDouble a)
	{
		double total = 0.0f;
		for (int i = 0; i < a.width*a.height; i++)
		{
			total += a.value[i]*a.value[i];
		}
		total = (double)Math.sqrt(total);
		return total;
	}

	public void normalise()
	{
		double total = 0.0f;
		for (int i = 0; i < width*height; i++)
		{
			total += value[i]*value[i];
		}
		if(total<=EPSILON)
		{
			value[0] = 1.0f;
			return;
		}
		total = (double)Math.sqrt(total);
		for (int i = 0; i < width*height; i++)
		{
			value[i] /= total;
		}
	}

	public void rowNormalise(int row)
	{
		double total = 0.0f;
		for (int i = 0; i < width; i++)
		{
			total += value[i+row*width]*value[i+row*width];
		}
		if(total<=EPSILON)
		{
			value[0+row*width] = 1.0f;
			return;
		}
		total = (double)Math.sqrt(total);
		for (int i = 0; i < width; i++)
		{
			value[i+row*width] /= total;
		}
	}
	
	public void reverse()
	{
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = -value[i];
		  }
	}

	public void scale(double s)
	{
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = value[i]*s;
		  }
	}

	public void setIdentity()
	{
		int i=0;
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				if(x==y)
					value[i] = 1.0f;
				else
					value[i] = 0.0f;
				i++;
			}
		}
	}

	//Must be square
	public static final boolean invert(GeneralMatrixDouble a,GeneralMatrixDouble ia)
	{
		GeneralMatrixDouble temp = new GeneralMatrixDouble(a.width,a.height);
		temp.set(a);

		ia.setIdentity();
		int i,j,k,swap;
		double t;
		for(i=0;i<a.width;i++)
		{
			swap = i;
			for (j = i + 1; j < a.height; j++) {
			    if (Math.abs(a.get(i, j)) > Math.abs(a.get(i, j)))
			    {
			    	swap = j;
			    }
			}

			if (swap != i)
			{
			    /*
			    ** Swap rows.
			    */
			    for (k = 0; k < a.width; k++)
			    {
					t = temp.get(k,i);
					temp.set(k,i,temp.get(k, swap));
					temp.set(k, swap,t);

					t = ia.get(k,i);
					ia.set(k,i,ia.get(k, swap));
					ia.set(k, swap,t);
			    }
			}

			t = temp.get(i,i);
			if (t == 0.0f)
			{
			    /*
			    ** No non-zero pivot.  The matrix is singular, which shouldn't
			    ** happen.  This means the user gave us a bad matrix.
			    */
			    return false;
			}

			for (k = 0; k < a.width; k++)
			{
				temp.set(k,i,temp.get(k,i) / t);
				ia.set(k,i,ia.get(k,i) / t);
			}
			for (j = 0; j < a.width; j++)
			{
			    if (j != i)
			    {
			    	t = temp.get(i,j);
			    	for (k = 0; k < a.width; k++)
			    	{
			    		temp.set(k,j, temp.get(k,j) - temp.get(k,i)*t);
			    		ia.set(k,j, ia.get(k,j) - ia.get(k,i)*t);
			    	}
			    }
			}
	    }
	    return true;
	}

	public static final void invertTransform(GeneralMatrixDouble cameraTransformMatrix,GeneralMatrixDouble modelMatrix)
	{
		//GeneralMatrixFloat.invert(cameraTransformMatrix,modelMatrix);
		//*
		modelMatrix.value[0*4+0] = cameraTransformMatrix.value[0*4+0];
		modelMatrix.value[1*4+0] = cameraTransformMatrix.value[0*4+1];
		modelMatrix.value[2*4+0] = cameraTransformMatrix.value[0*4+2];

		modelMatrix.value[0*4+1] = cameraTransformMatrix.value[1*4+0];
		modelMatrix.value[1*4+1] = cameraTransformMatrix.value[1*4+1];
		modelMatrix.value[2*4+1] = cameraTransformMatrix.value[1*4+2];

		modelMatrix.value[0*4+2] = cameraTransformMatrix.value[2*4+0];
		modelMatrix.value[1*4+2] = cameraTransformMatrix.value[2*4+1];
		modelMatrix.value[2*4+2] = cameraTransformMatrix.value[2*4+2];

		double x = -cameraTransformMatrix.value[3*4+0];
		double y = -cameraTransformMatrix.value[3*4+1];
		double z = -cameraTransformMatrix.value[3*4+2];
		
		//needs to be rotated like the rest of the points in space
		double tx = modelMatrix.value[0*4+0]*x+modelMatrix.value[1*4+0]*y+modelMatrix.value[2*4+0]*z;
		double ty = modelMatrix.value[0*4+1]*x+modelMatrix.value[1*4+1]*y+modelMatrix.value[2*4+1]*z;
		double tz = modelMatrix.value[0*4+2]*x+modelMatrix.value[1*4+2]*y+modelMatrix.value[2*4+2]*z;
		modelMatrix.value[3*4+0] = tx;
		modelMatrix.value[3*4+1] = ty;
		modelMatrix.value[3*4+2] = tz;		
		//*/
	}

	
	public static final void crossProduct3(GeneralMatrixDouble a,GeneralMatrixDouble b,GeneralMatrixDouble c)
	{
		c.value[0] = a.value[1]*b.value[2]-b.value[1]*a.value[2];
		c.value[1] = a.value[2]*b.value[0]-b.value[2]*a.value[0];
		c.value[2] = a.value[0]*b.value[1]-b.value[0]*a.value[1];
	}

	public static final void mult(GeneralMatrixDouble a,GeneralMatrixDouble b,GeneralMatrixDouble c)
	{
		int n = a.height;
		int p = a.width;
		int r = b.width;

		if(p!=b.height)
		{
			System.out.println("Mult error, matricies sizes dont match");
			return;
		}
		
	  double vdot;
	  int i,j,k,m;


      int r5 = r*5;
	  int rowI = 0;
	  int crowI = 0;
	  for (i = 0; i < n; i++) {

	     for (j = 0; j < r; j++) {

	        vdot = 0.0f;

	        m = p%5;

	        int rowK = 0;
	        for (k = 0; k < m; k++) {

	           vdot += a.value[rowI+k]*b.value[rowK+j];
	           rowK += r;
	        }

	        for (k = m; k < p; k += 5) {

	           vdot += a.value[rowI+k]*b.value[rowK+j] +
	                   a.value[rowI+k+1]*b.value[rowK+r+j] +
	                   a.value[rowI+k+2]*b.value[rowK+r*2+j] +
	                   a.value[rowI+k+3]*b.value[rowK+r*3+j] +
	                   a.value[rowI+k+4]*b.value[rowK+r*4+j];

	           rowK += r5;
	        }

	        c.value[crowI+j] = vdot;

	     }
	     rowI += p;
	     crowI += r;
	  }

	}

	public static final void mult(GeneralMatrixDouble a,double[] x,double[] b)
	{
		int n = a.height;
		int p = a.width;
		int r = 1;

		if(p!=x.length)
		{
			System.out.println("Mult error, matricies sizes dont match");
			return;
		}
		
	  double vdot;
	  int i,j,k,m;


      int r5 = r*5;
	  int rowI = 0;
	  int crowI = 0;
	  for (i = 0; i < n; i++) {

	     for (j = 0; j < r; j++) {

	        vdot = 0.0f;

	        m = p%5;

	        int rowK = 0;
	        for (k = 0; k < m; k++) {

	           vdot += a.value[rowI+k]*x[rowK+j];
	           rowK += r;
	        }

	        for (k = m; k < p; k += 5) {

	           vdot += a.value[rowI+k]*x[rowK+j] +
	                   a.value[rowI+k+1]*x[rowK+r+j] +
	                   a.value[rowI+k+2]*x[rowK+r*2+j] +
	                   a.value[rowI+k+3]*x[rowK+r*3+j] +
	                   a.value[rowI+k+4]*x[rowK+r*4+j];

	           rowK += r5;
	        }

	        b[crowI+j] = vdot;

	     }
	     rowI += p;
	     crowI += r;
	  }

	}

	public static final void rowmult(GeneralMatrixDouble a,GeneralMatrixDouble b,GeneralMatrixDouble c)
	{
		double vdot;
		  for (int i = 0; i < a.height; i++)
		  {
			  //Apply the matrix to this vector

			  for (int j = 0; j < a.width; j++)
			  {
				  vdot = 0.0f;
				  for (int k = 0; k < b.height; k++)
				  {
			           vdot += a.value[i*a.width+k]*b.value[k*b.width+j];
				  }
				  c.value[i*a.width+j] = vdot;
			  }
		  }
	}

	public static final void rowtransform(GeneralMatrixDouble a,GeneralMatrixDouble b,GeneralMatrixDouble c)
	{
		//     wxh
		//a is 3xn (h value of 1.0 is implied)
		//B is 3x4 (or at least the other values are unused)
		double vdot;
		  for (int i = 0; i < a.height; i++)
		  {
			  //Apply the matrix to this vector

			  for (int j = 0; j < a.width; j++)
			  {
				  vdot = 0.0f;
				  for (int k = 0; k < a.width; k++)
				  {
			           vdot += a.value[i*a.width+k]*b.value[k*b.width+j];
				  }
				  //Add what would be the homogenious value to translate correctly
				  vdot += b.value[a.width*b.width+j];
				  c.value[i*a.width+j] = vdot;
			  }
		  }
	}

	public static final void rowproject(GeneralMatrixDouble a,GeneralMatrixDouble b,GeneralMatrixDouble c)
	{
		//     wxh
		//a is 3xn (h value of 1.0 is implied)
		//B is 4x4 (or at least the other values are unused)
		double vdot;
		double h;
		  for (int i = 0; i < a.height; i++)
		  {
			  //Apply the matrix to this vector

			  for (int j = 0; j < a.width; j++)
			  {
				  vdot = 0.0f;
				  for (int k = 0; k < a.width; k++)
				  {
			           vdot += a.value[i*a.width+k]*b.value[k*b.width+j];
				  }
				  //Add what would be the homogenious value to translate correctly
				  vdot += b.value[a.width*b.width+j];
				  c.value[i*a.width+j] = vdot;
			  }
			  h = 0.0f;
			  for (int k = 0; k < a.width; k++)
			  {
		           h += a.value[i*a.width+k]*b.value[k*b.width+3];
			  }
			  //Add implicit h
			  h += b.value[a.width*b.width+3];
			  //Divide by h
			  for (int j = 0; j < a.width; j++)
			  {
				  c.value[i*a.width+j] /= h;
			  }
		  }
	}

	public static final double rowdot(GeneralMatrixDouble a,int ai,GeneralMatrixDouble b,int bi)
	{
		double result = 0.0f;
		for(int i=0;i<a.width;i++)
		{
			result += a.value[i+ai*a.width]*b.value[i+bi*b.width];
		}
		return result;
	}

	public static final void add(GeneralMatrixDouble a,GeneralMatrixDouble b,GeneralMatrixDouble c)
	{
		  for (int i = 0; i < a.width*a.height; i++)
		  {
			  c.value[i] = a.value[i]+b.value[i];
		  }
	}

	public static final void addWithScale(GeneralMatrixDouble a,GeneralMatrixDouble b,double s,GeneralMatrixDouble c)
	{
		  for (int i = 0; i < a.width*a.height; i++)
		  {
			  c.value[i] = a.value[i]+b.value[i]*s;
		  }
	}

	
	
	public static final void rowadd(GeneralMatrixDouble a,GeneralMatrixDouble b,GeneralMatrixDouble c)
	{
		int ai = 0;
		  for (int i = 0; i < a.height; i++)
		  {
			  for (int j = 0; j < a.width; j++)
			  {
				  c.value[ai] = a.value[ai]+b.value[j];
				  ai++;
			  }
		  }
	}

	public static final void sub(GeneralMatrixDouble a,GeneralMatrixDouble b,GeneralMatrixDouble c)
	{
		  for (int i = 0; i < a.width*a.height; i++)
		  {
			  c.value[i] = a.value[i]-b.value[i];
		  }
	}

	public static final void rowsub(GeneralMatrixDouble a,GeneralMatrixDouble b,GeneralMatrixDouble c)
	{
		int ai = 0;
		  for (int i = 0; i < a.height; i++)
		  {
			  for (int j = 0; j < a.width; j++)
			  {
				  c.value[ai] = a.value[ai]-b.value[j];
				  ai++;
			  }
		  }
	}

	public static final void scale(GeneralMatrixDouble a,double b,GeneralMatrixDouble c)
	{
		  for (int i = 0; i < a.width*a.height; i++)
		  {
			  c.value[i] = a.value[i]*b;
		  }
	}

	public static final void negate(GeneralMatrixDouble a,GeneralMatrixDouble b)
	{
		  for (int i = 0; i < a.width*a.height; i++)
		  {
			  b.value[i] = -a.value[i];
		  }
	}

	public static final void transpose(GeneralMatrixDouble a,GeneralMatrixDouble at)
	{
		int i,j;

		int ai = 0;
		int atjR;
	      for (i = 0; i < a.height; i++) {

	  		atjR = 0;
	         for (j = 0; j < a.width; j++) {

	            at.value[atjR+i] = a.value[ai];
	            ai++;
	            atjR += a.height;
	         }

	      }
	}

	public static final double trace(GeneralMatrixDouble a)
	{
		double sum = 0.0f;
		int index = 0;
		for (int i = 0; i < a.height; i++)
		{
			sum += a.value[index];
			index += a.height+1;
		}
		return sum;
	}

	public double[][] getAs2DArray()
	{
		double[][] result = new double[height][width];
		int i=0;
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				result[y][x]=value[i];
				i++;
			}
		}
		return result;
	}

	public void setFrom2DArray(double[][] a)
	{
		int i=0;
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				value[i]=a[y][x];
				i++;
			}
		}
	}
	  
	  public GeneralMatrixDouble(int width)
	  {
		  this.width = width;
	  }

	    public int appendRows(int numRows)
	    {
	    	int newSize = width*(height+numRows);
	    	if(value==null)
	    	{
	    		value = new double[newSize];
				height+=numRows;
				return height-numRows;
	    	}
	    	if(newSize>value.length)
	    	{
	    		ensureCapacity(newSize);
	    	}
			height+=numRows;
			return height-numRows;
	    }

	    public int appendRow()
	    {
	    	int newSize = width*(height+1);
	    	if(value==null)
	    	{
	    		value = new double[newSize];
				height++;
				return height-1;
	    	}
	    	if(newSize>value.length)
	    	{
	    		ensureCapacity(newSize);
	    	}
			height++;
			return height-1;
	    }

	    public void ensureCapacityNoCopy(int mincap)
	    {
	    	if(value==null)
	    	{
	    		value = new double[mincap];
	    	}
	    	else
	    	if(mincap>value.length)
	    	{
		        int newcap = (value.length * 3)/2 + 1;
		        double[] olddata = value;
		        value = new double[newcap < mincap ? mincap : newcap];
	    	}
	    }

	    public void ensureCapacity(int mincap)
	    {
	    	if(value==null)
	    	{
	    		value = new double[mincap];
	    	}
	    	else
	    	if(mincap>value.length)
	    	{
		        int newcap = (value.length * 3)/2 + 1;
		        double[] olddata = value;
		        value = new double[newcap < mincap ? mincap : newcap];
		        System.arraycopy(olddata,0,value,0,width*height);
	    	}
	    }

	    public void setDimensions(int w,int h)
	    {
	    	ensureCapacity(w*h);
	    	width = w;
	    	height = h;
	    }
	    public void setDimensionsNoCopy(int w,int h)
	    {
	    	ensureCapacityNoCopy(w*h);
	    	width = w;
	    	height = h;
	    }

	    public void push_back(double val)
	    {
	    	int ind = appendRow();
	    	value[ind] = val;
	    }
	    public void push_back_row(double val1,double val2)
	    {
	    	int ind = appendRow();
	    	value[ind*2+0] = val1;
	    	value[ind*2+1] = val2;
	    }
	    public void push_back_row(double val1,double val2,double val3)
	    {
	    	int ind = appendRow();
	    	value[ind*3+0] = val1;
	    	value[ind*3+1] = val2;
	    	value[ind*3+2] = val3;
	    }
	    public void push_back_row(double val1,double val2,double val3,double val4)
	    {
	    	int ind = appendRow();
	    	value[ind*4+0] = val1;
	    	value[ind*4+1] = val2;
	    	value[ind*4+2] = val3;
	    	value[ind*4+3] = val4;
	    }
	    public void push_back_row(double val1,double val2,double val3,double val4,double val5,double val6)
	    {
	    	int ind = appendRow();
	    	value[ind*6+0] = val1;
	    	value[ind*6+1] = val2;
	    	value[ind*6+2] = val3;
	    	value[ind*6+3] = val4;
	    	value[ind*6+4] = val5;
	    	value[ind*6+5] = val6;
	    }

	    public int push_back_row(double[] row)
	    {
	    	int ind = appendRow();
	    	System.arraycopy(row, 0, value, width*(height-1), width);
	    	return ind;
	    }
	    public int push_back_row(float[] row)
	    {
	    	int ind = appendRow();
	    	int off = width*ind;
	    	for(int i=0;i<width;i++)
	    		value[off+i] = row[i];
	    	return ind;
	    }

	    public int push_back_row(double[] row, int off)
	    {
	    	int ind = appendRow();
	    	System.arraycopy(row, off, value, width*(height-1), width);
	    	return ind;
	    }
	    public void push_back_rows(GeneralMatrixDouble rows)
	    {
	    	if(rows.height==0)
	    		return;
	    	appendRows(rows.height);
	    	System.arraycopy(rows.value, 0, value, width*(height-rows.height), width*rows.height);
	    }
}
