package utils.shapes;

public interface Deformer 
{
	public abstract void deform(double[] vs,int off,int len,double[] dvs,int doff,int dlen);
}
