package utils;

import java.io.Serializable;

public class Quaternion implements Serializable 
{
	public static void MatrixtoQuaternion(float[] m,float[] a)
	{
		// Algorithm in Ken Shoemake's article in 1987 SIGGRAPH course notes
		// article "Quaternion Calculus and Fast Animation".

		final float trace = m[3*0+0] + m[3*1+1] + m[3*2+2];

		if (trace>0)
		{
			// |w| > 1/2, may as well choose w > 1/2

			float root = (float)Math.sqrt(trace + 1.0f);  // 2w
			a[3] = 0.5f * root;
			root = 0.5f / root;  // 1/(4w)
			a[0] = (m[3*2+1]-m[3*1+2]) * root;
			a[1] = (m[3*0+2]-m[3*2+0]) * root;
			a[2] = (m[3*1+0]-m[3*0+1]) * root;
		}
		else
		{
			// |w| <= 1/2

			final int[] next = { 1, 2, 0 };
			
			int i = 1;
			if (m[3*1+1]>m[3*0+0])  i = 2;
			if (m[3*2+2]>m[3*i+i]) i = 3;
			int j = next[i];
			int k = next[j];
			
			
			float root = (float)Math.sqrt(m[3*i+i]-m[3*j+j]-m[3*k+k] + 1.0f);
			//float *quaternion[3] = { &x, &y, &z };
			a[i] = 0.5f * root;
			root = 0.5f / root;
			a[3] = (m[3*k+j]-m[3*j+k])*root;
			a[j] = (m[3*j+i]+m[3*i+j])*root;
			a[k] = (m[3*k+i]+m[3*i+k])*root;
		}
	}
	public static void MatrixtoQuaternion(float[] m,int mo,float[] a)
	{
		// Algorithm in Ken Shoemake's article in 1987 SIGGRAPH course notes
		// article "Quaternion Calculus and Fast Animation".

		final float trace = m[mo+3*0+0] + m[mo+3*1+1] + m[mo+3*2+2];

		if (trace>0)
		{
			// |w| > 1/2, may as well choose w > 1/2

			float root = (float)Math.sqrt(trace + 1.0f);  // 2w
			a[3] = 0.5f * root;
			root = 0.5f / root;  // 1/(4w)
			a[0] = (m[mo+3*2+1]-m[mo+3*1+2]) * root;
			a[1] = (m[mo+3*0+2]-m[mo+3*2+0]) * root;
			a[2] = (m[mo+3*1+0]-m[mo+3*0+1]) * root;
		}
		else
		{
			// |w| <= 1/2

			final int[] next = { 1, 2, 0 };
			
			int i = 0;
			if (m[mo+3*1+1]>m[mo+3*0+0])  i = 1;
			if (m[mo+3*2+2]>m[mo+3*i+i])  i = 2;
			int j = next[i];
			int k = next[j];
			
			
			float root = (float)Math.sqrt(m[mo+3*i+i]-m[mo+3*j+j]-m[mo+3*k+k] + 1.0f);
			//float *quaternion[3] = { &x, &y, &z };
			a[i] = 0.5f * root;
			root = 0.5f / root;
			a[3] = (m[mo+3*k+j]-m[mo+3*j+k])*root;
			a[j] = (m[mo+3*j+i]+m[mo+3*i+j])*root;
			a[k] = (m[mo+3*k+i]+m[mo+3*i+k])*root;
		}
	}
	public static void QuaterniontoMatrix(float[] a,float[] m)
	{
		float fTx  = 2.0f*a[0];
		float fTy  = 2.0f*a[1];
		float fTz  = 2.0f*a[2];
		float fTwx = fTx*a[3];
		float fTwy = fTy*a[3];
		float fTwz = fTz*a[3];
		float fTxx = fTx*a[0];
		float fTxy = fTy*a[0];
		float fTxz = fTz*a[0];
		float fTyy = fTy*a[1];
		float fTyz = fTz*a[1];
		float fTzz = fTz*a[2];

		m[0] = 1.0f-(fTyy+fTzz);	m[1] = fTxy-fTwz;			m[2] = fTxz+fTwy;
		m[0+3] = fTxy+fTwz;			m[1+3] = 1.0f-(fTxx+fTzz);	m[2+3] = fTyz-fTwx;
		m[0+6] = fTxz-fTwy;			m[1+6] = fTyz+fTwx;			m[2+6] = 1.0f-(fTxx+fTyy);
	}
	public static void QuaterniontoMatrix(float[] a,float[] m,int mo)
	{
		float fTx  = 2.0f*a[0];
		float fTy  = 2.0f*a[1];
		float fTz  = 2.0f*a[2];
		float fTwx = fTx*a[3];
		float fTwy = fTy*a[3];
		float fTwz = fTz*a[3];
		float fTxx = fTx*a[0];
		float fTxy = fTy*a[0];
		float fTxz = fTz*a[0];
		float fTyy = fTy*a[1];
		float fTyz = fTz*a[1];
		float fTzz = fTz*a[2];

		m[mo+0] = 1.0f-(fTyy+fTzz);		m[mo+1] = fTxy-fTwz;			m[mo+2] = fTxz+fTwy;
		m[mo+0+3] = fTxy+fTwz;			m[mo+1+3] = 1.0f-(fTxx+fTzz);	m[mo+2+3] = fTyz-fTwx;
		m[mo+0+6] = fTxz-fTwy;			m[mo+1+6] = fTyz+fTwx;			m[mo+2+6] = 1.0f-(fTxx+fTyy);
	}
	public static void slerp(float[] a, float[] b, float t, float[] c) 
	{
		assert(t>=0);
		assert(t<=1);
				
		float flip = 1;

		float cosine = a[3]*b[3] + a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
		
		if (cosine<0) 
		{ 
			cosine = -cosine; 
			flip = -1; 
		} 
		
		if ((1-cosine)<GeneralMatrixFloat.EPSILON)
		{
			float it = (1-t);
			float tf = (t*flip);
			c[0] = a[0]*it+b[0]*tf;
			c[1] = a[1]*it+b[1]*tf;
			c[2] = a[2]*it+b[2]*tf;
			c[3] = a[3]*it+b[3]*tf;
			return; 
		}
		
		float theta = (float)Math.acos(cosine); 
		float sine = (float)Math.sin(theta); 
		float beta = (float)Math.sin((1-t)*theta) / sine; 
		float alpha = (float)Math.sin(t*theta) / sine * flip; 
		
		c[0] = a[0]*beta+b[0]*alpha;
		c[1] = a[1]*beta+b[1]*alpha;
		c[2] = a[2]*beta+b[2]*alpha;
		c[3] = a[3]*beta+b[3]*alpha;
	} 

}
