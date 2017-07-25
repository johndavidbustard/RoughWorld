package utils;

public class GeneralMatrixInt 
{
	public int width=0; //columns
	public int height=0; //rows
	public int[] value=null; //array of values

	public GeneralMatrixInt()
	{
		width = 0;
		height = 0;
	}	
	public GeneralMatrixInt(int val0,int val1,int val2,int val3)
	{
		this.width = 1;
		this.height = 4;
		this.value = new int[4];
		this.value[0] = val0;
		this.value[1] = val1;
		this.value[2] = val2;
		this.value[3] = val3;
	}

	public GeneralMatrixInt(int[] vals)
	{
		this.width = vals.length;
		this.height = 1;
		this.value = vals;
	}
	public GeneralMatrixInt(int[] vals,int h)
	{
		this.width = 1;
		this.height = h;
		this.value = vals;
	}
	public GeneralMatrixInt(int width)
	{
		this.width = width;
	}
	public GeneralMatrixInt(int width,int height)
	{
		this.width = width;
		this.height = height;
		value = new int[width*height];
	}
	public GeneralMatrixInt(int width,int height,int[] vals)
	{
		this.width = width;
		this.height = height;
		this.value = vals;
	}
	public GeneralMatrixInt(int width,int height,int val)
	{
		this.width = width;
		this.height = height;
		this.value = new int[width*height];
		for(int i=0;i<value.length;i++)
			value[i] = val;
	}
	public GeneralMatrixInt(GeneralMatrixInt o)
	{
		this.width = o.width;
		this.height = o.height;
		value = new int[width*height];
		set(o);
	}

	  public boolean isequal(GeneralMatrixInt m)
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

	  public boolean isequal(int[] m)
	  {
		  if(width!=1)
			  return false;
		  if(height!=m.length)
			  return false;
		  for (int i = 0; i < width*height; i++) 
		  {
			  if(value[i]!=m[i])
				  return false;
		  }		  
		  return true;
	  }
//	public boolean isequal(GeneralMatrixInt o)
//	{
//		boolean is = true;
//		is = is && (width==o.width);
//		is = is && (height==o.height);
//		if(is)
//		{
//			for(int i=0;i<width*height;i++)
//			{
//				is = is && (value[i]==o.value[i]);
//			}
//		}
//		if(!is)
//			System.out.println("diff!");
//		return is;
//	}
	
		public void enumerate()
		{
			for (int i = 0; i < width*height; i++)
			{
				value[i] = i;
			}
		}
		
		public void enumerate(int from)
		{
			for (int i = 0; i < width*height; i++)
			{
				value[i] = i+from;
			}
		}
		public void denumerate(int from)
		{			
			for (int i = 0; i < width*height; i++)
			{
				int v = (width*height-1)-i;
				value[i] = i+from;
			}
		}
		
	public void clear(int v)
	{
		for (int i = 0; i < width*height; i++)
		{
			value[i] = v;
		}
	}

	public final int get(int i,int j)
	{
		return value[j*width+i];
	}
	public void set(int i,int j,int v)
	{
		value[j*width+i] = v;
	}
	public void set(final GeneralMatrixInt rb)
	{
		setDimensionsNoCopy(rb.width, rb.height);
		int s = width*height;
		if(s>0)
			System.arraycopy(rb.value,0,value,0,s);
	}
	public void setInto(final GeneralMatrixInt rb)
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
	public void set(final int[] vals)
	{
		System.arraycopy(vals, 0, value, 0, width*height);
	}
	  public boolean equals(GeneralMatrixInt m)
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
	  
	  public boolean contains(int v)
	  {
		  for(int i=(width*height-1);i>=0;i--)
		  {
			  if(value[i]==v)
				  return true;
		  }
		  return false;
	  }
	  
	  public int find(int[] v)
	  {
		  int ind = 0;
		  for(int j=0;j<(height);j++)
		  {
			  boolean found = true;
			  for(int i=0;i<(width);i++)
			  {
				  if(value[ind+i]!=v[i])
				  {
					  found = false;
					  break;
				  }
			  }
			  if(found)
				  return j;
			  ind+=width;
		  }
		  return -1;
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
	  public int find(int v0,int v1)
	  {
		  for(int i=0;i<(width*height);i+=2)
		  {
			  if((value[i]==v0)&&(value[i+1]==v1))
				  return i/2;
		  }
		  return -1;
	  }

	  public int find_thresh(int v0,int v1,int t)
	  {
		  for(int i=0;i<(width*height);i+=2)
		  {
			  if((Math.abs(value[i]-v0)<=t)&&(Math.abs(value[i+1]-v1)<=t))
				  return i/2;
		  }
		  return -1;
	  }

	  public int find(int v0,int v1,int v2,int v3)
	  {
		  for(int i=0;i<(width*height);i+=4)
		  {
			  if((value[i]==v0)&&(value[i+1]==v1)&&(value[i+2]==v2)&&(value[i+3]==v3))
				  return i/4;
		  }
		  return -1;
	  }
	  
	  public int findincolumn(int c,int v)
	  {
		  for(int i=0;i<(height);i++)
		  {
			  if(value[width*i+c]==v)
				  return i;
		  }
		  return -1;
	  }
	  public int findinrow(int v,int row)
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
	    		value = new int[newSize];
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
	    		value = new int[newSize];
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
	    		value = new int[newSize];
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
	    	int length = (height-(index))*width;
	    	try{
		    	appendRow();
		    	System.arraycopy(value, srcind, value, destind, length);	    		
	    	}
	    	catch(Exception e)
	    	{
	    		System.out.println("insertRowBefore error");
	    	}
	    }
	    
	    public void insertRowsBefore(int index,GeneralMatrixInt rows)
	    {
	    	int srcind = (index)*width;
	    	int destind = (index+rows.height)*width;
	    	int length = (height-(index))*width;
	    	try{
		    	appendRows(rows.height);
		    	System.arraycopy(value, srcind, value, destind, length);	    		
		    	System.arraycopy(rows.value, 0, value, srcind, rows.width*rows.height);	    		
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
	    		value = new int[mincap];
	    	}
	    	else
	    	if(mincap>value.length)
	    	{
		        int newcap = (value.length * 3)/2 + 1;
		        //int[] olddata = value;
		        value = new int[newcap < mincap ? mincap : newcap];
	    	}
	    }

	    public void ensureCapacity(int mincap)
	    {
	    	if(value==null)
	    	{
	    		value = new int[mincap];
	    	}
	    	else
	    	if(mincap>value.length)
	    	{
		        int newcap = (value.length * 3)/2 + 1;
		    	if(newcap>100000000)
		    	{
		    		newcap = mincap+1000000;
		    	}
		        int[] olddata = value;
		        value = new int[newcap < mincap ? mincap : newcap];
		        System.arraycopy(olddata,0,value,0,olddata.length);
	    	}
	    }
	    
	    public void setDimensions(int w,int h)
	    {
	    	ensureCapacity(w*h);
	    	width = w;
	    	height = h;
	    }
	    public void setDimensionsAndClearNew(int w,int h,int init)
	    {
	    	int oldl = width*height;
	    	ensureCapacity(w*h);
	    	width = w;
	    	height = h;
	    	int newl = w*h;
	    	for(int i=oldl;i<newl;i++)
	    	{
	    		value[i] = init;
	    	}
	    }
	    public void setDimensionsNoCopy(int w,int h)
	    {
	    	ensureCapacityNoCopy(w*h);
	    	width = w;
	    	height = h;
	    }
	    public void setDimensionsExact(int w,int h)
	    {
	    	int[] oldv = value;
	    	value = new int[w*h];
	    	int min = value.length;
	    	if(oldv.length<min)
	    		min = oldv.length;
	    	System.arraycopy(oldv, 0, value, 0, min);
	    	width = w;
	    	height = h;
	    }
	    public void setDimensionsExactNoCopy(int w,int h)
	    {
	    	value = new int[w*h];
	    	width = w;
	    	height = h;
	    }
	    public int pop_back()
	    {
	    	height--;
	    	return value[height];
	    }
	    public int push_back(int val)
	    {
	    	int ind = appendRow();
	    	value[ind] = val;
	    	return ind;
	    }
	    
	    public void set_row(int row,int val1,int val2)
	    {
	    	int ind = row*width;
	    	value[ind+0] = val1;
	    	value[ind+1] = val2;    	
	    }    
	    public int push_back_row(int val1,int val2)
	    {
	    	int ind = appendRow();
	    	value[ind*2+0] = val1;
	    	value[ind*2+1] = val2;
	    	return ind;
	    }
	    public int push_back_row(int val1,int val2,int val3)
	    {
	    	int ind = appendRow();
	    	value[ind*3+0] = val1;
	    	value[ind*3+1] = val2;
	    	value[ind*3+2] = val3;
	    	return ind;
	    }

	    public int push_back_row(int val1,int val2,int val3,int val4)
	    {
	    	int ind = appendRow();
	    	value[ind*4+0] = val1;
	    	value[ind*4+1] = val2;
	    	value[ind*4+2] = val3;
	    	value[ind*4+3] = val4;
	    	return ind;
	    }

	    public int push_back_row(int val1,int val2,int val3,int val4,int val5)
	    {
	    	int ind = appendRow();
	    	value[ind*5+0] = val1;
	    	value[ind*5+1] = val2;
	    	value[ind*5+2] = val3;
	    	value[ind*5+3] = val4;
	    	value[ind*5+4] = val5;
	    	return ind;
	    }

	    public int push_back_row(int val1,int val2,int val3,int val4,int val5,int val6)
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

	    public int push_back_row(int val1,int val2,int val3,int val4,int val5,int val6,int val7,int val8)
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

	    public int push_back_row(int[] row)
	    {
	    	int ind = appendRow();
	    	System.arraycopy(row, 0, value, width*(height-1), width);
	    	return ind;
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
	    	if(rows.height==0)
	    		return;
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
	    		int[] oldv = value;
	    		value = new int[newSize];
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
	    		value = new int[newSize];
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
