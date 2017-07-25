package utils;

public class ArrayUtils 
{
	public static String[] add(String[] v, String i)
	{
		String[] nv = new String[v.length+1];
		System.arraycopy(v, 0, nv, 0, v.length);
		nv[v.length] = i;
		return nv;
	}
	public static int[] add(int[] v, int i)
	{
		int[] nv = new int[v.length+1];
		System.arraycopy(v, 0, nv, 0, v.length);
		nv[v.length] = i;
		return nv;
	}
	public static double[] add(double[] v, double[] i)
	{
		double[] nv = new double[v.length+i.length];
		System.arraycopy(v, 0, nv, 0, v.length);
		System.arraycopy(i, 0, nv, v.length, i.length);
		return nv;
	}

	public static String[] remove(String[] v, int ind,int len)
	{
		String[] nv = new String[v.length-len];
		if(ind!=0)
			System.arraycopy(v, 0, nv, 0, ind);
		if((v.length-(ind+len))>=0)
			System.arraycopy(v, ind+len, nv, ind, v.length-(ind+len));
		return nv;
	}
	public static double[] remove(double[] v, int ind,int len)
	{
		double[] nv = new double[v.length-len];
		if(ind!=0)
			System.arraycopy(v, 0, nv, 0, ind);
		if((v.length-(ind+len))>=0)
			System.arraycopy(v, ind+len, nv, ind, v.length-(ind+len));
		return nv;
	}
	public static int[] remove(int[] v, int ind,int len)
	{
		int[] nv = new int[v.length-len];
		if(ind!=0)
			System.arraycopy(v, 0, nv, 0, ind);
		if((v.length-(ind+len))>=0)
			System.arraycopy(v, ind+len, nv, ind, v.length-(ind+len));
		return nv;
	}

	public static int getIndex(String[] within, String w)
	{
		if(within==null)
			return -1;
		for(int i=0;i<within.length;i++)
		{
			if(within[i].equalsIgnoreCase(w))
			{
				return i;
			}
		}
		return -1;
	}

}
