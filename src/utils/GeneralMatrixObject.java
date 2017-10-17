package utils;

import java.io.Serializable;

public class GeneralMatrixObject implements Serializable 
{
	private static final long serialVersionUID = 1L;

	public int width;
	public int height;
	public Object[] value;

    public GeneralMatrixObject()
    {
    	width = 0;
    	height = 0;
    	value = new Object[8];
    }
    public GeneralMatrixObject(Object a)
    {
    	width = 1;
    	height = 1;
    	value = new Object[1];
    	value[0] = a;
    }
    public GeneralMatrixObject(GeneralMatrixObject o)
    {
    	this.width = o.width;
    	this.height = o.height;
    	value = new Object[width*height];
    	if(value.length>0)
    		System.arraycopy(o.value, 0, value, 0, value.length);
    }
    public GeneralMatrixObject(Object[] o)
    {
    	this.width = 1;
    	this.height = o.length;
    	value = new Object[width*height];
    	if(value.length>0)
    		System.arraycopy(o, 0, value, 0, value.length);
    }
    public GeneralMatrixObject(int width)
    {
    	this.width = width;
    	height = 0;
    	value = new Object[8];
    }
    public GeneralMatrixObject(int width,int height)
    {
    	this.width = width;
    	this.height = height;
    	value = new Object[width*height];
    }
    public GeneralMatrixObject(int width,int height,int mincap)
    {
    	this.width = width;
    	this.height = height;
    	value = new Object[mincap];
    }
    
    public Object clone()
    {
    	GeneralMatrixObject o = new GeneralMatrixObject(width,height);
    	for(int i=0;i<(width*height);i++)
    	{
    		Object v = value[i];
    		if(v instanceof GeneralMatrixObject)
    		{
    			v = ((GeneralMatrixObject)v).clone();
    		}
    		else
    		if(v instanceof GeneralMatrixString)
    		{
    			v = ((GeneralMatrixString)v).clone();    			
    		}
    		o.value[i] = v;
    	}
    	return o;
    }

    
    public int push_back(Object o)
    {
    	int ind = appendRow();
    	value[ind] = o;
    	return ind;
    }
    
    public int appendRow()
    {
    	int newSize = width*(height+1);
    	if(newSize>value.length)
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
    		value = new Object[newSize];
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
    	if(value==null)
    	{
    		value = new Object[mincap];
    	}
    	else
    	if(mincap>value.length)
    	{
	        int newcap = (value.length * 3)/2 + 1;
	        Object[] olddata = value;
	        value = new Object[newcap < mincap ? mincap : newcap];
	        System.arraycopy(olddata,0,value,0,olddata.length);
    	}
    }
//    public void ensureCapacity(int mincap)
//    {
//        int newcap = (value.length * 3)/2 + 1;
//        Object[] olddata = value;
//        value = new Object[newcap < mincap ? mincap : newcap];
//        System.arraycopy(olddata,0,value,0,olddata.length);    		
//    }


    public void clear()
	{
		for (int i = 0; i < width*height; i++)
		{
			value[i] = null;
		}
	}

	  public int find(Object v)
	  {
		  for(int i=0;i<(width*height);i++)
		  {
			  if(value[i]==v)
				  return i;
		  }
		  return -1;
	  }
    
	  public void set(GeneralMatrixObject o)
	  {
		  setDimensions(o.width, o.height);
		  int len = o.width*o.height;
    	  if(len>0)
    		  System.arraycopy(o.value, 0, value, 0, len);
	  }
		public void setFromSubset(GeneralMatrixObject full,int ys)
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

    public void setDimensions(int w,int h)
    {
    	ensureCapacity(w*h);
    	width = w;
    	height = h;
    }

    public void setDimensionsAndClearNew(int w,int h)
    {
    	int oldl = width*height;
    	ensureCapacity(w*h);
    	width = w;
    	height = h;
    	int newl = w*h;
    	for(int i=oldl;i<newl;i++)
    	{
    		value[i] = null;
    	}
    }
    
    public int push_back_row(Object val1,Object val2)
    {
    	int ind = appendRow();
    	value[ind*2+0] = val1;
    	value[ind*2+1] = val2;
    	return ind;
    }

    public int push_back_row(Object val1,Object val2,Object val3)
    {
    	int ind = appendRow();
    	value[ind*3+0] = val1;
    	value[ind*3+1] = val2;
    	value[ind*3+2] = val3;
    	return ind;
    }

    public int push_back_row(Object val1,Object val2,Object val3,Object val4)
    {
    	int ind = appendRow();
    	value[ind*4+0] = val1;
    	value[ind*4+1] = val2;
    	value[ind*4+2] = val3;
    	value[ind*4+3] = val4;
    	return ind;
    }

    public int push_back_row(Object val1,Object val2,Object val3,Object val4,Object val5)
    {
    	int ind = appendRow();
    	value[ind*5+0] = val1;
    	value[ind*5+1] = val2;
    	value[ind*5+2] = val3;
    	value[ind*5+3] = val4;
    	value[ind*5+4] = val5;
    	return ind;
    }

    public int push_back_row(Object val1,Object val2,Object val3,Object val4,Object val5,Object val6)
    {
    	int ind = appendRow();
    	value[ind*6+0] = val1;
    	value[ind*6+1] = val2;
    	value[ind*6+2] = val3;
    	value[ind*6+3] = val4;
    	value[ind*6+4] = val5;
    	value[ind*6+5] = val6;
    	return ind;
    }

    public int push_back_row(Object val1,Object val2,Object val3,Object val4,Object val5,Object val6,Object val7,Object val8,Object val9)
    {
    	int ind = appendRow();
    	value[ind*9+0] = val1;
    	value[ind*9+1] = val2;
    	value[ind*9+2] = val3;
    	value[ind*9+3] = val4;
    	value[ind*9+4] = val5;
    	value[ind*9+5] = val6;
    	value[ind*9+6] = val7;
    	value[ind*9+7] = val8;
    	value[ind*9+8] = val9;
    	return ind;
    }

    public int push_back_row(Object val1,Object val2,Object val3,Object val4,Object val5,Object val6,Object val7,Object val8,Object val9,Object val10)
    {
    	int ind = appendRow();
    	value[ind*10+0] = val1;
    	value[ind*10+1] = val2;
    	value[ind*10+2] = val3;
    	value[ind*10+3] = val4;
    	value[ind*10+4] = val5;
    	value[ind*10+5] = val6;
    	value[ind*10+6] = val7;
    	value[ind*10+7] = val8;
    	value[ind*10+8] = val9;
    	value[ind*10+9] = val10;
    	return ind;
    }

    public void push_back_rows(GeneralMatrixObject rows)
    {
    	if(rows.height==0)
    		return;
    	appendRows(rows.height);
    	System.arraycopy(rows.value, 0, value, width*(height-rows.height), width*rows.height);
    }

    public void push_back_row_from_block(GeneralMatrixObject row,int yFromFull)
    {
    	appendRow();
    	System.arraycopy(row.value, yFromFull*width, value, width*(height-1), width);
    }

   }
