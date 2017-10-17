package utils.shapes.skinned;

import java.io.Serializable;

import utils.GeneralMatrixFloat;

public class Skin implements Serializable
{
	private static final long serialVersionUID = 1L;

	public GeneralMatrixFloat bpos = new GeneralMatrixFloat(3);
	public int[] sb = null;
	public float[] sw = null;
}
