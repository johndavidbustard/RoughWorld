package ai.knowledge_representation.story_instance.changes;

import java.io.Serializable;

import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;

public abstract class ChangeState implements Serializable
{
	public static final String[] changeNames = { "ChangeContains","ChangePose","ChangeRootObject","ChangeTransform"};
	public static final String[] empty = {};

	private static final long serialVersionUID = 1L;

	public double[] parameters;
	public abstract String[] getParameterNames();
	public int[] category_parameters;
	public abstract String[] getCategoryParameterNames();
	public abstract String[][] getCategoryParameterCatgegories();
	public String[] changeePaths;
	public abstract String[] getChangeeNames();
	public String[] posePaths;
	public abstract String[] getPoseNames();

	public abstract void change(GeneralMatrixString root,GeneralMatrixString objectpaths,GeneralMatrixObject objects,GeneralMatrixString posepaths,GeneralMatrixObject poses);

	public static ChangeState get(String name)
	{
		if(name.equalsIgnoreCase("ChangeContains"))
		{
			return new ChangeContains();
		}
		else
		if(name.equalsIgnoreCase("ChangePose"))
		{
			return new ChangePose();
		}
		else
		if(name.equalsIgnoreCase("ChangeRootObject"))
		{
			return new ChangeRootObject();
		}
		
		return null;
	}
	
	public static ChangeState[] add(ChangeState[] v, ChangeState i)
	{
		ChangeState[] nv = new ChangeState[v.length+1];
		System.arraycopy(v, 0, nv, 0, v.length);
		nv[v.length] = i;
		return nv;
	}

	public static ChangeState[] remove(ChangeState[] v, int ind,int len)
	{
		ChangeState[] nv = new ChangeState[v.length-len];
		if(ind!=0)
			System.arraycopy(v, 0, nv, 0, ind);
		if((v.length-(ind+len))>=0)
			System.arraycopy(v, ind+len, nv, ind, v.length-(ind+len));
		return nv;
	}

}
