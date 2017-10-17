package utils;

import java.io.Serializable;


public class GeneralMatrixString implements Serializable 
{
	private static final long serialVersionUID = 1L;

	public int width = 0;
	public int height = 0;
	public String[] value;

    public GeneralMatrixString()
    {
    }
    public GeneralMatrixString(int width)
    {
    	this.width = width;
    	height = 0;
    	value = new String[8];
    }
    public GeneralMatrixString(int width,int height)
    {
    	this.width = width;
    	this.height = height;
    	value = new String[height*width];
    }
    public GeneralMatrixString(int width,int height,int mincap)
    {
    	this.width = width;
    	this.height = height;
    	value = new String[mincap];
    }
    public GeneralMatrixString(String val)
    {
    	this.width = 1;
    	this.height = 1;
    	value = new String[1];
    	value[0] = val;
    }
    public GeneralMatrixString(String val0,String val1)
    {
    	this.width = 1;
    	this.height = 2;
    	value = new String[2];
    	value[0] = val0;
    	value[1] = val1;
    }
    public GeneralMatrixString(String val0,String val1,String val2)
    {
    	this.width = 1;
    	this.height = 3;
    	value = new String[3];
    	value[0] = val0;
    	value[1] = val1;
    	value[2] = val2;
    }
    public GeneralMatrixString(String val0,String val1,String val2,String val3)
    {
    	this.width = 1;
    	this.height = 4;
    	value = new String[4];
    	value[0] = val0;
    	value[1] = val1;
    	value[2] = val2;
    	value[3] = val3;
    }
    public GeneralMatrixString(String val0,String val1,String val2,String val3,String val4)
    {
    	this.width = 1;
    	this.height = 5;
    	value = new String[5];
    	value[0] = val0;
    	value[1] = val1;
    	value[2] = val2;
    	value[3] = val3;
    	value[4] = val4;
    }
    public GeneralMatrixString(String val0,String val1,String val2,String val3,String val4,String val5)
    {
    	this.width = 1;
    	this.height = 6;
    	value = new String[6];
    	value[0] = val0;
    	value[1] = val1;
    	value[2] = val2;
    	value[3] = val3;
    	value[4] = val4;
    	value[5] = val5;
    }
    public GeneralMatrixString(String val0,String val1,String val2,String val3,String val4,String val5,String val6)
    {
    	this.width = 1;
    	this.height = 7;
    	value = new String[7];
    	value[0] = val0;
    	value[1] = val1;
    	value[2] = val2;
    	value[3] = val3;
    	value[4] = val4;
    	value[5] = val5;
    	value[6] = val6;
    }
    public GeneralMatrixString(String val0,String val1,String val2,String val3,String val4,String val5,String val6,String val7)
    {
    	this.width = 1;
    	this.height = 8;
    	value = new String[8];
    	value[0] = val0;
    	value[1] = val1;
    	value[2] = val2;
    	value[3] = val3;
    	value[4] = val4;
    	value[5] = val5;
    	value[6] = val6;
    	value[7] = val7;
    }
    public GeneralMatrixString(String val0,String val1,String val2,String val3,String val4,String val5,String val6,String val7,String val8)
    {
    	this.width = 1;
    	this.height = 9;
    	value = new String[9];
    	value[0] = val0;
    	value[1] = val1;
    	value[2] = val2;
    	value[3] = val3;
    	value[4] = val4;
    	value[5] = val5;
    	value[6] = val6;
    	value[7] = val7;
    	value[8] = val8;
    }
    public GeneralMatrixString(String val0,String val1,String val2,String val3,String val4,String val5,String val6,String val7,String val8,String val9,String val10,String val11,String val12)
    {
    	this.width = 1;
    	this.height = 13;
    	value = new String[13];
    	value[0] = val0;
    	value[1] = val1;
    	value[2] = val2;
    	value[3] = val3;
    	value[4] = val4;
    	value[5] = val5;
    	value[6] = val6;
    	value[7] = val7;
    	value[8] = val8;
    	value[9] = val9;
    	value[10] = val10;
    	value[11] = val11;
    	value[12] = val12;
    }
    public GeneralMatrixString(final GeneralMatrixString val)
    {
    	this.width = val.width;
    	this.height = val.height;
    	if(val.value!=null)
    		value = val.value.clone();
    }
    public GeneralMatrixString(int width,int height,String[] val)
    {
    	this.width = width;
    	this.height = height;
    	value = val;
    }
    public GeneralMatrixString(int width,int height,String val)
    {
    	this.width = width;
    	this.height = height;
    	int mincap = width*height;
    	value = new String[mincap];
    	for(int i=0;i<mincap;i++)
    		value[i] = ""+val;
    }
    public GeneralMatrixString(String[] val)
    {
    	this.width = 1;
    	if(val==null)
    	{
    		this.height = 0;
    		value = null;
    		return;
    	}
    	this.height = val.length;
    	value = val;
    }

    public Object clone()
    {
    	return new GeneralMatrixString(this);
    }
    
	public boolean isequal(GeneralMatrixString o)
	{
		if(!(width==o.width))
			return false;
		if(!(height==o.height))
			return false;
		for(int i=0;i<width*height;i++)
		{
			if(!(value[i]).contentEquals(o.value[i]))
				return false;
		}
		return true;

//		boolean is = true;
//		is = is && (width==o.width);
//		is = is && (height==o.height);
//		if(is)
//		{
//			for(int i=0;i<width*height;i++)
//			{
//				is = is && (value[i].contentEquals(o.value[i]));
//			}
//		}
//		if(!is)
//			System.out.println("diff!");
//		return is;
	}
	
    public void clear(String s)
    {
    	for(int i=0;i<(width*height);i++)
    	{
    		value[i] = ""+s;
    	}    	
    }
    
    public int contains(String v)
    {
    	for(int i=0;i<(width*height);i++)
    	{
    		if(value[i].contains(v))
    			return i;
    	}
    	return -1;    	
    }
    public int find(String v)
    {
    	for(int i=0;i<(width*height);i++)
    	{
    		if(value[i].equalsIgnoreCase(v))
    			return i;
    	}
    	return -1;
    }
    public boolean includes(GeneralMatrixString subset)
    {
    	for(int j=0;j<(subset.width*subset.height);j++)
    	{
    		boolean found = false;
	    	for(int i=0;i<(width*height);i++)
	    	{
	    		if(value[i].equalsIgnoreCase(subset.value[j]))
	    		{
	    			found = true;
	    			break;
	    		}
	    	}
			if(!found)
				return false;
    	}
    	return true;
    }
    public int findContaining(String v)
    {
    	for(int i=0;i<(width*height);i++)
    	{
    		if(value[i].contains(v))
    			return i;
    	}
    	return -1;
    }
    
    public void set(GeneralMatrixString o)
    {
    	width = o.width;
		height = o.height;
		ensureCapacity(height*width);	
		System.arraycopy(o.value, 0, value, 0, height*width);
    }
    public void set(String[] o,int h)
    {
		ensureCapacity(h);	
    	width = 1;
		height = h;
		System.arraycopy(o, 0, value, 0, height*width);
    }
    public int push_back(String o)
    {
    	int ind = appendRow();
    	value[ind] = o;
    	return ind;
    }
    
    public int appendRow()
    {
    	
    	int newSize = width*(height+1);
    	if((value == null)||(newSize>value.length))
    	{
    		ensureCapacity(newSize);
    	}
		height++;
		return height-1;
    }
    
    public int appendRows(int size)
    {
    	int newSize = width*(height+size);
    	if(value==null)
    	{
    		value = new String[newSize];
			height+=size;
			return height-size;
    	}
    	if(newSize>value.length)
    	{
    		ensureCapacity(newSize);
    	}
		height+=size;
		return height-size;
    }

    public void removeRow(int index)
    {
    	for(int i=index*width;i<((height-1))*width;i++)
    	{
    		value[i] = value[i+width];
    	}
    	height--;
    }

    public void ensureCapacity(int mincap)
    {
    	if(value == null)
    	{
    		value = new String[mincap];
    		return;
    	}
    	if(mincap<=value.length)
    		return;
        int newcap = (value.length * 3)/2 + 1;
        String[] olddata = value;
        value = new String[newcap < mincap ? mincap : newcap];
        System.arraycopy(olddata,0,value,0,olddata.length);    		
    }
    

	public static int find(String[] value,String v)
    {
		int height = value.length;
    	for(int i=0;i<(height);i++)
    	{
    		if(value[i].equalsIgnoreCase(v))
    			return i;
    	}
    	return -1;
    }
	public static int find(String[] value,int height,String v)
    {
    	for(int i=0;i<(height);i++)
    	{
    		if(value[i].equalsIgnoreCase(v))
    			return i;
    	}
    	return -1;
    }

    public void push_back_row(String o1,String o2)
    {
    	int ind = appendRow();
    	value[ind*2+0] = o1;
    	value[ind*2+1] = o2;
    }
    
    public void push_back_row(String o1,String o2,String o3)
    {
    	int ind = appendRow();
    	value[ind*3+0] = o1;
    	value[ind*3+1] = o2;
    	value[ind*3+2] = o3;
    }
    
    public void push_back_row(String o1,String o2,String o3,String o4)
    {
    	int ind = appendRow();
    	value[ind*4+0] = o1;
    	value[ind*4+1] = o2;
    	value[ind*4+2] = o3;
    	value[ind*4+3] = o4;
    }
    
    public void push_back_row(String o1,String o2,String o3,String o4,String o5)
    {
    	int ind = appendRow();
    	value[ind*5+0] = o1;
    	value[ind*5+1] = o2;
    	value[ind*5+2] = o3;
    	value[ind*5+3] = o4;
    	value[ind*5+4] = o5;
    }
    
    public void push_back_rows(GeneralMatrixString rows)
    {
    	appendRows(rows.height);
    	System.arraycopy(rows.value, 0, value, width*(height-rows.height), width*rows.height);
    }

    public void setDimensions(int w,int h)
    {
    	ensureCapacity(w*h);
    	width = w;
    	height = h;
    }

    public void insertRowAfter(int index)
    {
    	appendRow();
    	System.arraycopy(value, (index+1)*width, value, (index+2)*width, (height-1-(index+1))*width);
    }
    
  public void insertRowBefore(int index)
  {
  	int srcind = (index);
  	int destind = (index+1);
  	int length = (height-1-(index));
  	try{
	    	appendRow();
	    	System.arraycopy(value, srcind, value, destind, length);	    		
  	}
  	catch(Exception e)
  	{
  		System.out.println("insertRowBefore error");
  	}
  }
}
