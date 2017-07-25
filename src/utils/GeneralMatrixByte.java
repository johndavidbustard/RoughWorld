package utils;

public class GeneralMatrixByte 
{
	public int width=0; //columns
	public int height=0; //rows
	public byte[] value=null; //array of values

	public GeneralMatrixByte()
	{
		width = 0;
		height = 0;
	}
	public GeneralMatrixByte(byte[] vals)
	{
		this.width = vals.length;
		this.height = 1;
		this.value = vals;
	}
	public GeneralMatrixByte(int width)
	{
		this.width = width;
	}
	public GeneralMatrixByte(int width,int height)
	{
		this.width = width;
		this.height = height;
		value = new byte[width*height];
	}
	public GeneralMatrixByte(int width,int height,byte[] vals)
	{
		this.width = width;
		this.height = height;
		this.value = vals;
	}
	public GeneralMatrixByte(GeneralMatrixByte o)
	{
		this.width = o.width;
		this.height = o.height;
		value = new byte[width*height];
		set(o);
	}

	public void enumerate()
	{
		for (byte i = 0; i < width*height; i++)
		{
			value[i] = i;
		}
	}
	
	public void clear(byte v)
	{
		for (int i = 0; i < width*height; i++)
		{
			value[i] = v;
		}
	}

	public final byte get(int i,int j)
	{
		return value[j*width+i];
	}
	public void set(int i,int j,byte v)
	{
		value[j*width+i] = v;
	}
	public void set(final GeneralMatrixByte rb)
	{
		if(
				(rb.width==0)||(rb.height==0)
		  )
		{
		}
		else
		if(
				(rb.width==width)&&
				(rb.height==height)
		  )
		{
			System.arraycopy(rb.value,0,value,0,width*height);
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
	public void set(final byte[] vals)
	{
		System.arraycopy(vals, 0, value, 0, width*height);
	}
	  public boolean equals(GeneralMatrixByte m)
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
	  
	  public boolean contains(byte v)
	  {
		  for(int i=(width*height-1);i>=0;i--)
		  {
			  if(value[i]==v)
				  return true;
		  }
		  return false;
	  }
	  
	  public int find(byte v)
	  {
		  for(int i=0;i<(width*height);i++)
		  {
			  if(value[i]==v)
				  return i;
		  }
		  return -1;
	  }
	  public int find(byte[] v)
	  {
		  int off = 0;
		  for(int j=0;j<(height);j++)
		  {
			  boolean found = true;
			  for(int i=0;i<(width);i++)
			  {
				  if(value[off+i]!=v[i])
				  {
					  found = false;
					  break;
				  }
			  }
			  if(found)
			  {
				  return j;
			  }
			  off += width;
		  }
		  return -1;
	  }
	  public int find(byte v0,byte v1)
	  {
		  for(int i=0;i<(width*height);i+=2)
		  {
			  if((value[i]==v0)&&(value[i+1]==v1))
				  return i;
		  }
		  return -1;
	  }
	  
	  public int findinrow(byte v,int row)
	  {
		  for(int i=width*row;i<(width*(row+1));i++)
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
	    		value = new byte[newSize];
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
	    		value = new byte[newSize];
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
	    
	    public int appendRows(int size,byte defaultValue)
	    {
	    	int newSize = width*(height+size);
	    	if(value==null)
	    	{
	    		value = new byte[newSize];
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
	    		value = new byte[mincap];
	    	}
	    	else
	    	if(mincap>value.length)
	    	{
		        int newcap = (value.length * 3)/2 + 1;
		        //int[] olddata = value;
		        value = new byte[newcap < mincap ? mincap : newcap];
	    	}
	    }

	    public void ensureCapacity(int mincap)
	    {
	    	if(value==null)
	    	{
	    		value = new byte[mincap];
	    	}
	    	else
	    	if(mincap>value.length)
	    	{
		        int newcap = (value.length * 3)/2 + 1;
		        byte[] olddata = value;
		        value = new byte[newcap < mincap ? mincap : newcap];
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
	    	byte[] oldv = value;
	    	value = new byte[w*h];
	    	int min = value.length;
	    	if(oldv.length<min)
	    		min = oldv.length;
	    	System.arraycopy(oldv, 0, value, 0, min);
	    	width = w;
	    	height = h;
	    }
	    public void setDimensionsExactNoCopy(int w,int h)
	    {
	    	value = new byte[w*h];
	    	width = w;
	    	height = h;
	    }
	    public int push_back(byte val)
	    {
	    	int ind = appendRow();
	    	value[ind] = val;
	    	return ind;
	    }
	    
	    public void push_back_row(byte val1,byte val2)
	    {
	    	int ind = appendRow();
	    	value[ind*2+0] = val1;
	    	value[ind*2+1] = val2;
	    }
	    public void push_back_row(byte val1,byte val2,byte val3)
	    {
	    	int ind = appendRow();
	    	value[ind*3+0] = val1;
	    	value[ind*3+1] = val2;
	    	value[ind*3+2] = val3;
	    }

	    public int push_back_row(byte val1,byte val2,byte val3,byte val4)
	    {
	    	int ind = appendRow();
	    	value[ind*4+0] = val1;
	    	value[ind*4+1] = val2;
	    	value[ind*4+2] = val3;
	    	value[ind*4+3] = val4;
	    	return ind;
	    }

	    public int push_back_row(byte val1,byte val2,byte val3,byte val4,byte val5,byte val6)
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

	    public int push_back_row(byte val1,byte val2,byte val3,byte val4,byte val5,byte val6,byte val7,byte val8)
	    {
	    	int ind = appendRow();
	    	value[ind*8+0] = val1;
	    	value[ind*8+1] = val2;
	    	value[ind*8+2] = val3;
	    	value[ind*8+3] = val4;
	    	value[ind*8+4] = val5;
	    	value[ind*8+5] = val6;
	    	value[ind*8+6] = val7;
	    	value[ind*8+7] = val8;
	    	return ind;
	    }

	    public void push_back_row(byte[] row)
	    {
	    	appendRow();
	    	System.arraycopy(row, 0, value, width*(height-1), width);
	    }
	    public void push_back_row(GeneralMatrixByte row)
	    {
	    	appendRow();
	    	System.arraycopy(row.value, 0, value, width*(height-1), width);
	    }
	    public void push_back_row_from_block(GeneralMatrixByte row,int yFromFull)
	    {
	    	appendRow();
	    	System.arraycopy(row.value, yFromFull*width, value, width*(height-1), width);
	    }
	    
	    public void push_back_rows(GeneralMatrixByte rows)
	    {
	    	appendRows(rows.height);
	    	System.arraycopy(rows.value, 0, value, width*(height-rows.height), width*rows.height);
	    }
	
		public void appendColumn(byte defaultvalue)
		{
			int newSize = (width+1)*height;
	    	if(value==null)
	    	{
				width++;
				return;
	    	}
	    	//if(newSize>value.length)
	    	{
	    		byte[] oldv = value;
	    		value = new byte[newSize];
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
	    		value = new byte[newSize];
	    	}
	    	else
	    	if(newSize>value.length)
	    	{
	    		ensureCapacity(newSize);
	    	}
	    	height = newSize;
	    	System.arraycopy(value, index, value, index+size, (oldwidth-index));
	    }
}
