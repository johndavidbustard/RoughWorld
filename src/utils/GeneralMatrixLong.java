/*
Copyright (c) 2008-Present John Bustard  http://johndavidbustard.com

This code is release under the GPL http://www.gnu.org/licenses/gpl.html

To get the latest version of this code goto http://johndavidbustard.com/mmconst.html
*/
package utils;

public class GeneralMatrixLong 
{
	public int width=0; //columns
	public int height=0; //rows
	public long[] value=null; //array of values

	public GeneralMatrixLong()
	{
		width = 0;
		height = 0;
	}
	public GeneralMatrixLong(int width)
	{
		this.width = width;
	}
	public GeneralMatrixLong(int width,int height)
	{
		this.width = width;
		this.height = height;
		value = new long[width*height];
	}
	public GeneralMatrixLong(GeneralMatrixLong a)
	{
		width = a.width;
		height = a.height;
		value = new long[width*height];
		System.arraycopy(a.value,0,value,0,value.length);
	}

	public void clear(long v)
	{
		for (int i = 0; i < width*height; i++)
		{
			value[i] = v;
		}
	}

	public final long get(int i,int j)
	{
		return value[j*width+i];
	}
	public void set(int i,int j,long v)
	{
		value[j*width+i] = v;
	}
	public void set(final GeneralMatrixLong rb)
	{
		if(
				(rb.width==width)&&
				(rb.height==height)
		  )
		{
			System.arraycopy(rb.value,0,value,0,value.length);
		}
		else
		{
			final int minHeight = Math.min(rb.height, height);
			final int minWidth = Math.min(rb.width, width);
			for(int y=0;y<minHeight;y++)
			{
				System.arraycopy(rb.value,y*rb.width,value,y*width,minWidth);				
			}
		}
	}

	  public boolean isequal(GeneralMatrixLong m)
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
	  
	  public boolean contains(long v)
	  {
		  for(int i=(width*height-1);i>=0;i--)
		  {
			  if(value[i]==v)
				  return true;
		  }
		  return false;
	  }
	  
	  public int find(int v)
	  {
		  for(int i=0;i<(width*height);i++)
		  {
			  if(value[i]==v)
				  return i;
		  }
		  return -1;
	  }
	  
	  public int find(long v)
	  {
		  for(int i=0;i<(width*height);i++)
		  {
			  if(value[i]==v)
				  return i;
		  }
		  return -1;
	  }
	  
		//Insertion and deletion
	    public int appendRow()
	    {
	    	int newSize = width*(height+1);
	    	if(value==null)
	    	{
	    		value = new long[newSize];
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

	    public int appendRows(int size)
	    {
	    	int newSize = width*(height+size);
	    	if(value==null)
	    	{
	    		value = new long[newSize];
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
	    
	    public int appendRows(int size,int defaultValue)
	    {
	    	int newSize = width*(height+size);
	    	if(value==null)
	    	{
	    		value = new long[newSize];
				height+=size;
				clear(defaultValue);
				return height-size;
	    	}
	    	if(newSize>value.length)
	    	{
	    		ensureCapacity(newSize);
	    	}
			for(int i=width*height;i<(width*(height+size));i++)
			{
				value[i] = defaultValue;
			}
			height+=size;
			return height-size;
	    }
	    
	    public void removeRow(int index)
	    {
	    	if(index>=height)
	    	{
	    		System.out.println("Row being removed larger than matrix");
	    	}
	    	for(int i=index*width;i<((height-1))*width;i++)
	    	{
	    		value[i] = value[(i+width)];
	    	}
	    	height--;
	    }
	    public void removeRows(int index,int size)
	    {
	    	for(int i=index*width;i<((height-size))*width;i++)
	    	{
	    		value[i] = value[i+width*size];
	    	}
	    	height-=size;
	    }

	    public void insertRowAfter(int index)
	    {
	    	appendRow();
	    	System.arraycopy(value, (index+1)*width, value, (index+2)*width, (height-1-(index+1))*width);
	    }

	    public void insertRowBefore(int index)
	    {
	    	int srcind = (index)*width;
	    	int destind = (index+1)*width;
	    	int length = (height-1-(index))*width;
	    	try{
		    	appendRow();
		    	System.arraycopy(value, srcind, value, destind, length);	    		
	    	}
	    	catch(Exception e)
	    	{
	    		System.out.println("insertRowBefore error");
	    	}
	    }
	    
	    public void ensureCapacityNoCopy(int mincap)
	    {
	    	if(value==null)
	    	{
	    		value = new long[mincap];
	    	}
	    	else
	    	if(mincap>value.length)
	    	{
		        int newcap = (value.length * 3)/2 + 1;
		        //long[] olddata = value;
		        value = new long[newcap < mincap ? mincap : newcap];
	    	}
	    }

	    public void ensureCapacity(int mincap)
	    {
	    	if(value==null)
	    	{
	    		value = new long[mincap];
	    	}
	    	else
	    	if(mincap>value.length)
	    	{
		        int newcap = (value.length * 3)/2 + 1;
		        long[] olddata = value;
		        value = new long[newcap < mincap ? mincap : newcap];
		        System.arraycopy(olddata,0,value,0,olddata.length);
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
	    public void setDimensionsExact(int w,int h)
	    {
	    	long[] oldv = value;
	    	value = new long[w*h];
	    	int min = value.length;
	    	if(oldv.length<min)
	    		min = oldv.length;
	    	System.arraycopy(oldv, 0, value, 0, min);
	    	width = w;
	    	height = h;
	    }
	    public void setDimensionsExactNoCopy(int w,int h)
	    {
	    	value = new long[w*h];
	    	width = w;
	    	height = h;
	    }
	    public void push_back(long val)
	    {
	    	int ind = appendRow();
	    	value[ind] = val;
	    }
	    
	    public void push_back_row(long val1,long val2)
	    {
	    	int ind = appendRow();
	    	value[ind*2+0] = val1;
	    	value[ind*2+1] = val2;
	    }
	    public void push_back_row(int val1,int val2,int val3)
	    {
	    	int ind = appendRow();
	    	value[ind*3+0] = val1;
	    	value[ind*3+1] = val2;
	    	value[ind*3+2] = val3;
	    }

	    public void push_back_row(long[] row)
	    {
	    	appendRow();
	    	System.arraycopy(row, 0, value, width*(height-1), width);
	    }
	    public void push_back_row(GeneralMatrixInt row)
	    {
	    	appendRow();
	    	System.arraycopy(row.value, 0, value, width*(height-1), width);
	    }
	    public void push_back_row_from_block(GeneralMatrixInt row,int yFromFull)
	    {
	    	appendRow();
	    	System.arraycopy(row.value, yFromFull*width, value, width*(height-1), width);
	    }
	    
	    public void push_back_rows(GeneralMatrixInt rows)
	    {
	    	appendRows(rows.height);
	    	System.arraycopy(rows.value, 0, value, width*(height-rows.height), width*rows.height);
	    }
	
		public void appendColumn(int defaultvalue)
		{
			int newSize = (width+1)*height;
	    	if(value==null)
	    	{
				width++;
				return;
	    	}
	    	//if(newSize>value.length)
	    	{
	    		long[] oldv = value;
	    		value = new long[newSize];
	    		width++;
	    		clear(defaultvalue);
	    		for(int i=0;i<height;i++)
	    		{
	    			System.arraycopy(oldv, i*(width-1), value, i*(width), (width-1));
	    		}
	    	}
		}
	
	    public void removeBlock(int column,int size)
	    {
	    	if(width!=1)
	    	{
	    		System.out.println("Block removal only possible on 1d arrays");
	    	}
	    	for(int i=column;i<(height-size);i++)
	    	{
	    		value[i] = value[i+size];
	    	}
	    	height-=size;
	    	if(height<0)
	    	{
	    		System.out.println("Removal from nothing!");
	    	}
	    }
	    
	    public void insertBlockAfter(int index,int size)
	    {
	    	insertBlockBefore(index+1,size);
	    }	    
	    public void insertBlockBefore(int index,int size)
	    {
	    	if(width!=1)
	    	{
	    		System.out.println("Block additions only possible on 1d arrays");
	    	}

	    	int oldwidth = height;
	    	int newSize = height+size;
	    	if(value==null)
	    	{
	    		value = new long[newSize];
	    	}
	    	else
	    	if(newSize>value.length)
	    	{
	    		ensureCapacity(newSize);
	    	}
	    	height = newSize;
	    	System.arraycopy(value, index, value, index+size, (oldwidth-index));
	    }

	    //For rectangles
	    public void scaleAroundCenter(float f)
	    {
			int y = (int)(value[1]-((float)value[3]*f-value[3])/2);
			if (y < 0) y = 0;
			int x = (int)(value[0]-((float)value[2]*f-value[2])/2);
			if (x < 0) x = 0;
			int h = (int)(value[3]*f);
			int w = (int)(value[2]*f);
			value[0] = x;
			value[1] = y;
			value[2] = w;
			value[3] = h;
	    }
}
