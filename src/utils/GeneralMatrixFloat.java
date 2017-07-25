package utils;

public class GeneralMatrixFloat
{
	public static final float EPSILON = 0.000000001f;
	//public static final float EPSILON=5.9e-8f;
	public int width = 0; //columns
	public int height = 0; //rows
	public float[] value = null; //array of values

	public GeneralMatrixFloat()
	{
		width = 0;
		height = 0;
	}
	public GeneralMatrixFloat(float val)
	{
		this.width = 1;
		this.height = 1;
		this.value = new float[1];
		this.value[0] = val;
	}
	public GeneralMatrixFloat(float val0,float val1)
	{
		this.width = 1;
		this.height = 2;
		this.value = new float[2];
		this.value[0] = val0;
		this.value[1] = val1;
	}
	public GeneralMatrixFloat(float val0,float val1,float val2)
	{
		this.width = 1;
		this.height = 3;
		this.value = new float[3];
		this.value[0] = val0;
		this.value[1] = val1;
		this.value[2] = val2;
	}
	public GeneralMatrixFloat(float val0,float val1,float val2,float val3)
	{
		this.width = 1;
		this.height = 4;
		this.value = new float[4];
		this.value[0] = val0;
		this.value[1] = val1;
		this.value[2] = val2;
		this.value[3] = val3;
	}
	public GeneralMatrixFloat(float[] vals)
	{
		this.width = vals.length;
		this.height = 1;
		this.value = vals;
	}
	public GeneralMatrixFloat(int width)
	{
		this.width = width;
	}
	public GeneralMatrixFloat(int width,int height)
	{
		this.width = width;
		this.height = height;
		value = new float[width*height];
	}
	public GeneralMatrixFloat(int width,int height,float[] vals)
	{
		this.width = width;
		this.height = height;
		value = vals;
	}
	public GeneralMatrixFloat(GeneralMatrixFloat o)
	{
		width = o.width;
		height = o.height;
		value = new float[width*height];
		set(o);
	}
	public GeneralMatrixFloat(GeneralMatrixDouble o)
	{
		width = o.width;
		height = o.height;
		value = new float[width*height];
		set(o);
	}

	public boolean isequal(GeneralMatrixFloat o)
	{
		boolean is = true;
		is = is && (width==o.width);
		is = is && (height==o.height);
		if(is)
		{
			for(int i=0;i<width*height;i++)
			{
				is = is && (value[i]==o.value[i]);
			}
		}
		if(!is)
			System.out.println("diff!");
		return is;
	}
	
	public boolean includesNAN()
	{
		for (int i = 0; i < width*height; i++)
		{
			if(Float.isNaN(value[i]))
				return true;
		}
		return false;
	}
	
	public void clear(float v)
	{
		for (int i = 0; i < width*height; i++)
		{
			value[i] = v;
		}
	}
	public void clearSubset(int xs,int ys,int sw,int sh,float v)
	{
		int maxy = ys+sh;
		int maxx = xs+sw;
		for(int y=ys;y<maxy;y++)
		{
			for(int x=xs;x<maxx;x++)
			{
				value[x+y*width] = v;
			}
		}
	}

	public final float get(int i,int j)
	{
		return value[j*width+i];
	}
	
	public float getMirror(int x, int y)
    {
        if (x >= width)
            x = width - (x - width + 2);

        if (y >= height)
            y = height - (y - height + 2);

        if (x < 0)
        {
            int tmp = 0;
            int dir = 1;

            while (x < 0)
            {
                tmp += dir;
                if (tmp == width - 1 || tmp == 0)
                    dir *= -1;
                x++;
            }
            x = tmp;
        }

        if (y < 0)
        {
            int tmp = 0;
            int dir = 1;

            while (y < 0)
            {
                tmp += dir;
                if (tmp == height - 1 || tmp == 0)
                    dir *= -1;
                y++;
            }
            y = tmp;
        }

       return value[x+y*width];
    }	
	public void set(int i,int j,float v)
	{
		value[j*width+i] = v;
	}

	public void set(GeneralMatrixFloat a)
	{
		ensureCapacityNoCopy(a.width*a.height);
		width = a.width;
		height = a.height;
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = a.value[i];
		  }
	}
	public void set(GeneralMatrixInt a)
	{
		  for (int i = 0; i < width*height; i++) 
		  {
			  value[i] = a.value[i];
		  }
	}
	public void set(GeneralMatrixDouble a)
	{
		  for (int i = 0; i < width*height; i++) 
		  {
			  value[i] = (float)a.value[i];
		  }
	}
	
	public void set(float[] a)
	{
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = a[i];
		  }
	}
	public void set(double[] a)
	{
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = (float)a[i];
		  }
	}
	public void set(double[][] a)
	{
		  for (int j = 0; j < height; j++)
		  {
			  for (int i = 0; i < width; i++)
			  {
				  value[i+j*width] = (float)a[j][i];
			  }
		  }
	}

	  public int find(float v0,float v1,float eps)
	  {
		  for(int i=0;i<(width*height);i+=2)
		  {
			  if((Math.abs(value[i]-v0)<=eps)&&(Math.abs(value[i+1]-v1)<=eps))
				  return i/2;
		  }
		  return -1;
	  }
	  public int find(float v0,float v1,float v2,float eps)
	  {
		  for(int i=0;i<(width*height);i+=3)
		  {
			  if((Math.abs(value[i]-v0)<=eps)&&(Math.abs(value[i+1]-v1)<=eps)&&(Math.abs(value[i+2]-v2)<=eps))
				  return i/3;
		  }
		  return -1;
	  }
	  public int find(float v0,float v1,float v2,float v3,float v4,float eps)
	  {
		  for(int i=0;i<(width*height);i+=5)
		  {
			  if((Math.abs(value[i]-v0)<=eps)&&(Math.abs(value[i+1]-v1)<=eps)&&(Math.abs(value[i+2]-v2)<=eps)&&(Math.abs(value[i+3]-v3)<=eps)&&(Math.abs(value[i+4]-v4)<=eps))
				  return i/5;
		  }
		  return -1;
	  }

	//Insertion and deletion
    public int appendRow()
    {
    	int newSize = width*(height+1);
    	if(value==null)
    	{
    		value = new float[newSize];
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
    		value = new float[newSize];
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
    
    public int appendRows(int size,float defaultValue)
    {
    	int newSize = width*(height+size);
    	if(value==null)
    	{
    		value = new float[newSize];
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
    		value = new float[mincap];
    	}
    	else
    	if(mincap>value.length)
    	{
	        int newcap = mincap;//(value.length * 3)/2 + 1;
	        //float[] olddata = value;
	        value = new float[newcap < mincap ? mincap : newcap];
    	}
    }

    public void ensureCapacity(int mincap)
    {
    	if(value==null)
    	{
    		value = new float[mincap];
    	}
    	else
    	if(mincap>value.length)
    	{
	        int newcap = (value.length * 3)/2 + 1;
	        float[] olddata = value;
	        value = new float[newcap < mincap ? mincap : newcap];
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
    public int push_back(float val)
    {
    	int ind = appendRow();
    	value[ind] = val;
    	return ind;
    }
    
    public void set_row(int row,float val1,float val2)
    {
    	int ind = row*width;
    	value[ind+0] = val1;
    	value[ind+1] = val2;    	
    }    
    public int push_back_row(float val1,float val2)
    {
    	int ind = appendRow();
    	value[ind*2+0] = val1;
    	value[ind*2+1] = val2;
    	return ind;
    }
    public void set_row(int row,float val1,float val2,float val3)
    {
    	int ind = row*width;
    	value[ind+0] = val1;
    	value[ind+1] = val2;    	
    	value[ind+2] = val3;    	
    }    
    public int push_back_row(float val1,float val2,float val3)
    {
    	int ind = appendRow();
    	value[ind*width+0] = val1;
    	value[ind*width+1] = val2;
    	value[ind*width+2] = val3;
    	return ind;
    }
    public void set_row(int row,float val1,float val2,float val3,float val4)
    {
    	int ind = row*width;
    	value[ind+0] = val1;
    	value[ind+1] = val2;    	
    	value[ind+2] = val3;    	
    	value[ind+3] = val4;    	
    }    
    public int push_back_row(float val1,float val2,float val3,float val4)
    {
    	int ind = appendRow();
    	value[ind*4+0] = val1;
    	value[ind*4+1] = val2;
    	value[ind*4+2] = val3;
    	value[ind*4+3] = val4;
    	return ind;
    }
    public void set_row(int row,float val1,float val2,float val3,float val4,float val5)
    {
    	int ind = row*width;
    	value[ind+0] = val1;
    	value[ind+1] = val2;    	
    	value[ind+2] = val3;    	
    	value[ind+3] = val4;    	
    	value[ind+4] = val5;    	
    }    
    public void push_back_row(float val1,float val2,float val3,float val4,float val5)
    {
    	int ind = appendRow();
    	value[ind*5+0] = val1;
    	value[ind*5+1] = val2;
    	value[ind*5+2] = val3;
    	value[ind*5+3] = val4;
    	value[ind*5+4] = val5;
    }
    public void set_row(int row,float val1,float val2,float val3,float val4,float val5,float val6)
    {
    	int ind = row*width;
    	value[ind+0] = val1;
    	value[ind+1] = val2;    	
    	value[ind+2] = val3;    	
    	value[ind+3] = val4;    	
    	value[ind+4] = val5;    	
    	value[ind+5] = val6;    	
    }    
    public int push_back_row(float val1,float val2,float val3,float val4,float val5,float val6)
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
    public int push_back_row(float val1,float val2,float val3,float val4,float val5,float val6,float val7)
    {
    	int ind = appendRow();
    	value[ind*7+0] = val1;
    	value[ind*7+1] = val2;
    	value[ind*7+2] = val3;
    	value[ind*7+3] = val4;
    	value[ind*7+4] = val5;
    	value[ind*7+5] = val6;
    	value[ind*7+6] = val7;
    	return ind;
    }
    public int push_back_row(float val1,float val2,float val3,float val4,float val5,float val6,float val7,float val8)
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
    public void push_back_row(float val1,float val2,float val3,float val4,float val5,float val6,float val7,float val8,float val9)
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
    }
    public void push_back_row(float val1,float val2,float val3,float val4,float val5,float val6,float val7,float val8,float val9,float val10,float val11)
    {
    	int ind = appendRow();
    	value[ind*11+0] = val1;
    	value[ind*11+1] = val2;
    	value[ind*11+2] = val3;
    	value[ind*11+3] = val4;
    	value[ind*11+4] = val5;
    	value[ind*11+5] = val6;
    	value[ind*11+6] = val7;
    	value[ind*11+7] = val8;
    	value[ind*11+8] = val9;
    	value[ind*11+9] = val10;
    	value[ind*11+10] = val11;
    }
    public void push_back_row(float val1,float val2,float val3,float val4,float val5,float val6,float val7,float val8,float val9,float val10,float val11,float val12,float val13,float val14)
    {
    	int ind = appendRow();
    	value[ind*14+0] = val1;
    	value[ind*14+1] = val2;
    	value[ind*14+2] = val3;
    	value[ind*14+3] = val4;
    	value[ind*14+4] = val5;
    	value[ind*14+5] = val6;
    	value[ind*14+6] = val7;
    	value[ind*14+7] = val8;
    	value[ind*14+8] = val9;
    	value[ind*14+9] = val10;
    	value[ind*14+10] = val11;
    	value[ind*14+11] = val12;
    	value[ind*14+12] = val13;
    	value[ind*14+13] = val14;
    }

    public void push_back_row(float val1,float val2,float val3,float val4,float val5,float val6,float val7,float val8,float val9,float val10,float val11,float val12,float val13,float val14,float val15,float val16)
    {
    	int ind = appendRow();
    	value[ind*16+0] = val1;
    	value[ind*16+1] = val2;
    	value[ind*16+2] = val3;
    	value[ind*16+3] = val4;
    	value[ind*16+4] = val5;
    	value[ind*16+5] = val6;
    	value[ind*16+6] = val7;
    	value[ind*16+7] = val8;
    	value[ind*16+8] = val9;
    	value[ind*16+9] = val10;
    	value[ind*16+10] = val11;
    	value[ind*16+11] = val12;
    	value[ind*16+12] = val13;
    	value[ind*16+13] = val14;
    	value[ind*16+14] = val15;
    	value[ind*16+15] = val16;
    }

    public void push_back_row(GeneralMatrixFloat row)
    {
    	appendRow();
    	System.arraycopy(row.value, 0, value, width*(height-1), width);
    }

    public void push_back_row_from_matrix(GeneralMatrixFloat row,int i)
    {
    	appendRow();
    	System.arraycopy(row.value, i*width, value, width*(height-1), width);
    }

    public void push_back_row(GeneralMatrixDouble row)
    {
    	appendRow();
    	int off = width*(height-1); 
    	for(int i=0;i<width;i++)
    	{
    		value[off+i] = (float)row.value[i];
    	}
    }

    public int push_back_row(float[] row)
    {
    	int ind = appendRow();
    	System.arraycopy(row, 0, value, width*(height-1), width);
    	return ind;
    }
    public int push_back_row(float[] row,int off)
    {
    	int ind = appendRow();
    	System.arraycopy(row, off, value, width*(height-1), width);
    	return ind;
    }
    public int push_back_row(float[] row,int off,int w)
    {
    	int ind = appendRow();
    	int inoff = width*(height-1);
    	System.arraycopy(row, off, value, inoff, Math.min(width,w));
    	for(int i=w;i<width;i++)
    	{
    		value[inoff+i] = 0.0f;
    	}
    	return ind;
    }
    public void push_back_row_from_block(GeneralMatrixFloat row,int yFromFull)
    {
    	appendRow();
    	System.arraycopy(row.value, yFromFull*width, value, width*(height-1), width);
    }
    
    public void push_back_rows(GeneralMatrixFloat rows)
    {
    	if(rows.height==0)
    		return;
    	appendRows(rows.height);
    	System.arraycopy(rows.value, 0, value, width*(height-rows.height), width*rows.height);
    }

    public void push_back_matrices_as_row(GeneralMatrixFloat m0,GeneralMatrixFloat m1)
    {
    	int ind = appendRow();
    	int m0num = m0.width*m0.height;
    	System.arraycopy(m0.value, 0, value, width*ind, m0num);
    	int m1num = m1.width*m1.height;
    	System.arraycopy(m1.value, 0, value, width*ind+m0num, m1num);    	
    }
    
	public void swapRows(int r1,int r2,float[] buffer)
	{
		System.arraycopy(value, r1*width, buffer, 0, width);
		System.arraycopy(value, r2*width, value, r1*width, width);
		System.arraycopy(buffer, 0, value, r2*width, width);
	}

    public void setRow(int r,GeneralMatrixFloat row)
    {
    	System.arraycopy(row.value, 0, value, width*(r), width);
    }

    public void setRow(int r,GeneralMatrixDouble row)
    {
    	for(int i=0;i<width;i++)
    		value[width*r+i] = (float)row.value[i];
    }
    
    public void setRowFromSubset(int r,GeneralMatrixFloat full,int ys)
    {
    	System.arraycopy(full.value, ys*width, value, width*(r), width);
    }

	//For setting a vec or mat from an array of vecs/mats
	public void setFromSubset(GeneralMatrixFloat full,int ys)
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
	public void setFromSubset(GeneralMatrixDouble full,int ys)
	{
		int i=0;
		int fi = ys*width;
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				value[i] = (float)full.value[fi];
				i++;
				fi++;
			}
		}
	}
	
	public void setSubset(GeneralMatrixFloat subset,int xs,int ys)
	{
		int maxy = ys+subset.height;
		int maxx = xs+subset.width;
		for(int y=ys;y<maxy;y++)
		{
			for(int x=xs;x<maxx;x++)
			{
				value[x+y*width] = subset.value[(x-xs)+(y-ys)*subset.width];
			}
		}
	}


	//Setup matrix transforms
	
	public void set3DTransformPosition(float x,float y,float z)
	{
		value[4*3+0] = x;
		value[4*3+1] = y;
		value[4*3+2] = z;
	}
	public void calcBasisFromNormal(float x,float y,float z)
	{
		value[4*2+0] = x;
		value[4*2+1] = y;
		value[4*2+2] = z;

		if(
				(Math.abs(x)<=Math.abs(y))&&(Math.abs(x)<=Math.abs(z))
		  )
		{
			value[4*0+0] = 1.0f;
			value[4*0+1] = 0.0f;
			value[4*0+2] = 0.0f;
		}	
		else
		if(Math.abs(y)<Math.abs(z))
		{
			value[4*0+0] = 0.0f;
			value[4*0+1] = 1.0f;
			value[4*0+2] = 0.0f;			
		}
		else
		{
			value[4*0+0] = 0.0f;
			value[4*0+1] = 0.0f;
			value[4*0+2] = 1.0f;			
		}
		
		value[4*1+0] = y*value[4*0+2]-value[4*0+1]*z;
		value[4*1+1] = z*value[4*0+0]-value[4*0+2]*x;
		value[4*1+2] = x*value[4*0+1]-value[4*0+0]*y;

		//Normalise
		float total = value[4*1+0]*value[4*1+0];
		total += value[4*1+1]*value[4*1+1];
		total += value[4*1+2]*value[4*1+2];
		if(total<=EPSILON)
		{
			value[4*1+0] = 1.0f;
		}
		else
		{
			total = (float)Math.sqrt(total);
		}
		value[4*1+0] /= total;
		value[4*1+1] /= total;
		value[4*1+2] /= total;
		
		value[4*0+0] = y*value[4*1+2]-value[4*1+1]*z;
		value[4*0+1] = z*value[4*1+0]-value[4*1+2]*x;
		value[4*0+2] = x*value[4*1+1]-value[4*1+0]*y;

		total = value[4*0+0]*value[4*0+0];
		total += value[4*0+1]*value[4*0+1];
		total += value[4*0+2]*value[4*0+2];
		if(total<=EPSILON)
		{
			value[4*0+0] = 1.0f;
		}
		else
		{
			total = (float)Math.sqrt(total);
		}
		value[4*0+0] /= total;
		value[4*0+1] /= total;
		value[4*0+2] /= total;
	}
	public void calcBasisFromY(float x,float y,float z)
	{
		if((Math.abs(x)<=EPSILON)&&(Math.abs(y)<=EPSILON)&&(Math.abs(z)<=EPSILON))
		{
			setIdentity();
		}
		else
		if(
				(Math.abs(x)<=Math.abs(y))&&(Math.abs(x)<=Math.abs(z))
		  )
		{
			calcBasisFromY(x, y, z, 1.0f, 0.0f, 0.0f);
		}	
		else
		if(Math.abs(y)<=Math.abs(z))
		{
			calcBasisFromY(x, y, z, 0.0f, 1.0f, 0.0f);
		}
		else
		{
			calcBasisFromY(x, y, z, 0.0f, 0.0f, 1.0f);
		}

	}
	public void calcBasisFromY(float x,float y,float z,
			float Xx,float Xy,float Xz)
	{
		value[width*1+0] = x;
		value[width*1+1] = y;
		value[width*1+2] = z;
		float total = x*x;
		total += y*y;
		total += z*z;
		if(total<=EPSILON)
		{
			value[width*1+0] = 1.0f;
		}
		else
		{
			total = (float)Math.sqrt(total);
		}
		value[width*1+0] /= total;
		value[width*1+1] /= total;
		value[width*1+2] /= total;
		
		total = Xx*Xx;
		total += Xy*Xy;
		total += Xz*Xz;
		if(total<=EPSILON)
		{
			value[width*0+0] = 1.0f;
		}
		else
		{
			total = (float)Math.sqrt(total);
			value[width*0+0] = Xx/total;
			value[width*0+1] = Xy/total;
			value[width*0+2] = Xz/total;
		}

		
		value[width*2+0] = -y*value[width*0+2]+value[width*0+1]*z;
		value[width*2+1] = -z*value[width*0+0]+value[width*0+2]*x;
		value[width*2+2] = -x*value[width*0+1]+value[width*0+0]*y;

		//Normalise
		total = value[width*2+0]*value[width*2+0];
		total += value[width*2+1]*value[width*2+1];
		total += value[width*2+2]*value[width*2+2];
		if(total<=EPSILON)
		{
			value[width*2+0] = 1.0f;
		}
		else
		{
			total = (float)Math.sqrt(total);
			value[width*2+0] /= total;
			value[width*2+1] /= total;
			value[width*2+2] /= total;
		}
		
		value[width*0+0] = y*value[width*2+2]-value[width*2+1]*z;
		value[width*0+1] = z*value[width*2+0]-value[width*2+2]*x;
		value[width*0+2] = x*value[width*2+1]-value[width*2+0]*y;

		total = value[width*0+0]*value[width*0+0];
		total += value[width*0+1]*value[width*0+1];
		total += value[width*0+2]*value[width*0+2];
		if(total<=EPSILON)
		{
			value[width*0+0] = 1.0f;
		}
		else
		{
			total = (float)Math.sqrt(total);
			value[width*0+0] /= total;
			value[width*0+1] /= total;
			value[width*0+2] /= total;
		}
	}
	public void flipX()
	{
		value[4*0+0] = -value[4*0+0];
		value[4*0+1] = -value[4*0+1];
		value[4*0+2] = -value[4*0+2];
	}
	public void flipY()
	{
		value[4*1+0] = -value[4*1+0];
		value[4*1+1] = -value[4*1+1];
		value[4*1+2] = -value[4*1+2];
	}
	public void flipZ()
	{
		value[4*2+0] = -value[4*2+0];
		value[4*2+1] = -value[4*2+1];
		value[4*2+2] = -value[4*2+2];
	}
	public void set3DBasis(GeneralMatrixFloat basis)
	{
		value[4*0+0] = basis.value[3*0+0];
		value[4*0+1] = basis.value[3*0+1];
		value[4*0+2] = basis.value[3*0+2];

		value[4*1+0] = basis.value[3*1+0];
		value[4*1+1] = basis.value[3*1+1];
		value[4*1+2] = basis.value[3*1+2];

		value[4*2+0] = basis.value[3*2+0];
		value[4*2+1] = basis.value[3*2+1];
		value[4*2+2] = basis.value[3*2+2];
	}
	public void set3DTransformScale(float scale)
	{
		value[4*0+0] *= scale;
		value[4*0+1] *= scale;
		value[4*0+2] *= scale;
		
		value[4*1+0] *= scale;
		value[4*1+1] *= scale;
		value[4*1+2] *= scale;
		
		value[4*2+0] *= scale;
		value[4*2+1] *= scale;
		value[4*2+2] *= scale;
	}
	public void set3DBasis(float Xx,float Xy,float Xz,
							float Yx,float Yy,float Yz,
							float Zx,float Zy,float Zz)
	{
		value[4*0+0] = Xx;
		value[4*0+1] = Xy;
		value[4*0+2] = Xz;

		value[4*1+0] = Yx;
		value[4*1+1] = Yy;
		value[4*1+2] = Yz;

		value[4*2+0] = Zx;
		value[4*2+1] = Zy;
		value[4*2+2] = Zz;
	}
	public void set3DBasis(float[] basis, int offset)
	{
		value[4*0+0] = basis[3*0+0+offset];
		value[4*0+1] = basis[3*0+1+offset];
		value[4*0+2] = basis[3*0+2+offset];

		value[4*1+0] = basis[3*1+0+offset];
		value[4*1+1] = basis[3*1+1+offset];
		value[4*1+2] = basis[3*1+2+offset];

		value[4*2+0] = basis[3*2+0+offset];
		value[4*2+1] = basis[3*2+1+offset];
		value[4*2+2] = basis[3*2+2+offset];
	}
	public void get3DBasis(float[] basis, int offset)
	{
		basis[3*0+0+offset] = value[4*0+0];
		basis[3*0+1+offset] = value[4*0+1];
		basis[3*0+2+offset] = value[4*0+2];

		basis[3*1+0+offset] = value[4*1+0];
		basis[3*1+1+offset] = value[4*1+1];
		basis[3*1+2+offset] = value[4*1+2];

		basis[3*2+0+offset] = value[4*2+0];
		basis[3*2+1+offset] = value[4*2+1];
		basis[3*2+2+offset] = value[4*2+2];
	}
	public void set2DTransformZRotation(float z)
	{
		float sin=(float)Math.sin(z);
		float cos=(float)Math.cos(z);
		value[2*0+0] = cos;
		value[2*0+1] = -sin;
		value[2*1+0] = sin;
		value[2*1+1] = cos;
	}

	public void set3DTransformZRotation(float z)
	{
		float sin=(float)Math.sin(z);
		float cos=(float)Math.cos(z);
		value[4*0+0] = cos;
		value[4*0+1] = -sin;
		value[4*1+0] = sin;
		value[4*1+1] = cos;
		value[4*2+2] = 1.0f;
	}
	public void set3DTransformYRotation(float y)
	{
		float sin=(float)Math.sin(y);
		float cos=(float)Math.cos(y);
		if(width==4)
		{
			value[4*0+0] = cos;
			value[4*0+2] = sin;
			value[4*2+0] = -sin;
			value[4*2+2] = cos;
			value[4*1+1] = 1.0f;
		}
		else
		{
			value[3*0+0] = cos;
			value[3*0+2] = sin;
			value[3*2+0] = -sin;
			value[3*2+2] = cos;
			value[3*1+1] = 1.0f;
		}
	}
	public void set3DTransformXRotation(float x)
	{
		setIdentity();
		float sin=(float)Math.sin(x);
		float cos=(float)Math.cos(x);
		if(width==4)
		{
			value[4*1+1] = cos;
			value[4*1+2] = -sin;
			value[4*2+1] = sin;
			value[4*2+2] = cos;
			value[4*0+0] = 1.0f;
		}
		else
		{
			value[3*1+1] = cos;
			value[3*1+2] = -sin;
			value[3*2+1] = sin;
			value[3*2+2] = cos;
			value[3*0+0] = 1.0f;
		}
	}

	public void set3DTransformRotation(double x,double y,double z)
	{
		float sx=(float)Math.sin(x);
		float cx=(float)Math.cos(x);
		float sy=(float)Math.sin(y);
		float cy=(float)Math.cos(y);
		float sz=(float)Math.sin(z);
		float cz=(float)Math.cos(z);

		if(width==4)
		{
			value[4*0+0] = cz*cy;
			value[4*0+1] = (sy*sx*cz-cx*sz);
			value[4*0+2] = (sy*cx*cz+sz*sx);
	
			value[4*1+0] = sz*cy;
			value[4*1+1] = (sy*sx*sz+cx*cz);
			value[4*1+2] = (sy*cx*sz-cz*sx);
	
			value[4*2+0] = -sy;
			value[4*2+1] = cy*sx;
			value[4*2+2] = cy*cx;
		}
		else
		{
			value[3*0+0] = cz*cy;
			value[3*0+1] = (sy*sx*cz-cx*sz);
			value[3*0+2] = (sy*cx*cz+sz*sx);
	
			value[3*1+0] = sz*cy;
			value[3*1+1] = (sy*sx*sz+cx*cz);
			value[3*1+2] = (sy*cx*sz-cz*sx);
	
			value[3*2+0] = -sy;
			value[3*2+1] = cy*sx;
			value[3*2+2] = cy*cx;
		}
	}
	
//	[           cz*cy            ,-(sz),         cz*sy         ]
//	[                                                          ]
//	[(cx*sz*cy)+((-(sx))*(-(sy))),cx*cz,(cx*sz*sy)+((-(sx))*cy)]
//	[                                                          ]
//	[  (sx*sz*cy)+(cx*(-(sy)))   ,sx*cz,  (sx*sz*sy)+(cx*cy)   ]
	public void set3DTransformRotationXZY(double x,double y,double z)
	{
		float sx=(float)Math.sin(x);
		float cx=(float)Math.cos(x);
		float sy=(float)Math.sin(y);
		float cy=(float)Math.cos(y);
		float sz=(float)Math.sin(z);
		float cz=(float)Math.cos(z);

		if(width==4)
		{
			value[4*0+0] = cz*cy;
			value[4*0+1] = -(sz);
			value[4*0+2] = cz*sy;
	
			value[4*1+0] = (cx*sz*cy)+((-(sx))*(-(sy)));
			value[4*1+1] = cx*cz;
			value[4*1+2] = (cx*sz*sy)+((-(sx))*cy);
	
			value[4*2+0] = (sx*sz*cy)+(cx*(-(sy)));
			value[4*2+1] = sx*cz;
			value[4*2+2] = (sx*sz*sy)+(cx*cy);
		}
		else
		{
			value[3*0+0] = cz*cy;
			value[3*0+1] = -(sz);
			value[3*0+2] = cz*sy;
	
			value[3*1+0] = (cx*sz*cy)+((-(sx))*(-(sy)));
			value[3*1+1] = cx*cz;
			value[3*1+2] = (cx*sz*sy)+((-(sx))*cy);
	
			value[3*2+0] = (sx*sz*cy)+(cx*(-(sy)));
			value[3*2+1] = sx*cz;
			value[3*2+2] = (sx*sz*sy)+(cx*cy);
		}
	}
	
//	   [  (cy*cz)+(sy*sx*sz)   ,  (cy*(-(sz)))+(sy*sx*cz)   ,sy*cx]
//	   [                                                          ]
//	   [         cx*sz         ,           cx*cz            ,-(sx)]
//	   [                                                          ]
//	   [((-(sy))*cz)+(cy*sx*sz),((-(sy))*(-(sz)))+(cy*sx*cz),cy*cx]
	public void set3DTransformRotationYXZ(double x,double y,double z)
	{
		float sx=(float)Math.sin(x);
		float cx=(float)Math.cos(x);
		float sy=(float)Math.sin(y);
		float cy=(float)Math.cos(y);
		float sz=(float)Math.sin(z);
		float cz=(float)Math.cos(z);

		if(width==4)
		{
			value[4*0+0] = (cy*cz)+(sy*sx*sz);
			value[4*0+1] = (cy*(-(sz)))+(sy*sx*cz);
			value[4*0+2] = sy*cx;
	
			value[4*1+0] = cx*sz;
			value[4*1+1] = cx*cz;
			value[4*1+2] = -(sx);
	
			value[4*2+0] = ((-(sy))*cz)+(cy*sx*sz);
			value[4*2+1] = ((-(sy))*(-(sz)))+(cy*sx*cz);
			value[4*2+2] = cy*cx;
		}
		else
		{
			value[3*0+0] = (cy*cz)+(sy*sx*sz);
			value[3*0+1] = (cy*(-(sz)))+(sy*sx*cz);
			value[3*0+2] = sy*cx;
	
			value[3*1+0] = cx*sz;
			value[3*1+1] = cx*cz;
			value[3*1+2] = -(sx);
	
			value[3*2+0] = ((-(sy))*cz)+(cy*sx*sz);
			value[3*2+1] = ((-(sy))*(-(sz)))+(cy*sx*cz);
			value[3*2+2] = cy*cx;
		}
	}

//	[  cy*cz   ,  (cy*(-(sz))*cx)+(sy*sx)   ,  ((cy*(-(sz)))*(-(sx)))+(sy*cx)   ]
//	[                                                                           ]
//	[    sz    ,           cz*cx            ,            cz*(-(sx))             ]
//	[                                                                           ]
//	[(-(sy))*cz,((-(sy))*(-(sz))*cx)+(cy*sx),(((-(sy))*(-(sz)))*(-(sx)))+(cy*cx)]	
	public void set3DTransformRotationYZX(double x,double y,double z)
	{
		float sx=(float)Math.sin(x);
		float cx=(float)Math.cos(x);
		float sy=(float)Math.sin(y);
		float cy=(float)Math.cos(y);
		float sz=(float)Math.sin(z);
		float cz=(float)Math.cos(z);

		if(width==4)
		{
			value[4*0+0] = cy*cz;
			value[4*0+1] = (cy*(-(sz))*cx)+(sy*sx);
			value[4*0+2] = ((cy*(-(sz)))*(-(sx)))+(sy*cx);
	
			value[4*1+0] = sz;
			value[4*1+1] = cz*cx;
			value[4*1+2] = cz*(-(sx));
	
			value[4*2+0] = (-(sy))*cz;
			value[4*2+1] = ((-(sy))*(-(sz))*cx)+(cy*sx);
			value[4*2+2] = (((-(sy))*(-(sz)))*(-(sx)))+(cy*cx);
		}
		else
		{
			value[3*0+0] = cy*cz;
			value[3*0+1] = (cy*(-(sz))*cx)+(sy*sx);
			value[3*0+2] = ((cy*(-(sz)))*(-(sx)))+(sy*cx);
	
			value[3*1+0] = sz;
			value[3*1+1] = cz*cx;
			value[3*1+2] = cz*(-(sx));
	
			value[3*2+0] = (-(sy))*cz;
			value[3*2+1] = ((-(sy))*(-(sz))*cx)+(cy*sx);
			value[3*2+2] = (((-(sy))*(-(sz)))*(-(sx)))+(cy*cx);
		}
	}

	///*
	public void set3DTransformRotationZXY(double x,double y,double z)
	{
		float sx=(float)Math.sin(x);
		float cx=(float)Math.cos(x);
		float sy=(float)Math.sin(y);
		float cy=(float)Math.cos(y);
		float sz=(float)Math.sin(z);
		float cz=(float)Math.cos(z);

		if(width==4)
		{
			value[4*0+0] = (cz*cx-sz*sy*sx);
			value[4*0+1] = -sx*cy;
			value[4*0+2] = (sz*cx+cz*sy*sx);
	
			value[4*1+0] = cx*sy*sz+sx*cz;
			value[4*1+1] = cx*cy;
			value[4*1+2] = sz*sx-cz*sy*cx;
	
			value[4*2+0] = -cy*sz;
			value[4*2+1] = sy;
			value[4*2+2] = cy*cz;
		}
		else
		{
			value[3*0+0] = (cz*cx-sz*sy*sx);
			value[3*0+1] = -sx*cy;
			value[3*0+2] = (sz*cx+cz*sy*sx);
	
			value[3*1+0] = cx*sy*sz+sx*cz;
			value[3*1+1] = cx*cy;
			value[3*1+2] = sz*sx-cz*sy*cx;
	
			value[3*2+0] = -cy*sz;
			value[3*2+1] = sy;
			value[3*2+2] = cy*cz;
//			value[3*0+0] = (cz*cx-sz*sy*sx);
//			value[3*0+1] = -sz*cy;
//			value[3*0+2] = (sz*sx+cz*sy*cx);
//	
//			value[3*1+0] = cz*sy*sx+sz*cz;
//			value[3*1+1] = cz*cy;
//			value[3*1+2] = sz*sx-cz*sy*cx;
//	
//			value[3*2+0] = -cy*sx;
//			value[3*2+1] = sy;
//			value[3*2+2] = cy*cx;
		}
	}

//	[cz*cy,((-(sz))*cx)+(cz*sy*sx),((-(sz))*(-(sx)))+(cz*sy*cx)]
//	[                                                          ]
//	[sz*cy,  (cz*cx)+(sz*sy*sx)   ,  (cz*(-(sx)))+(sz*sy*cx)   ]
//	[                                                          ]
//	[-(sy),         cy*sx         ,           cy*cx            ]
	public void set3DTransformRotationZYX(double x,double y,double z)
	{
		float sx=(float)Math.sin(x);
		float cx=(float)Math.cos(x);
		float sy=(float)Math.sin(y);
		float cy=(float)Math.cos(y);
		float sz=(float)Math.sin(z);
		float cz=(float)Math.cos(z);

		if(width==4)
		{
			value[4*0+0] = cz*cy;
			value[4*0+1] = ((-(sz))*cx)+(cz*sy*sx);
			value[4*0+2] = ((-(sz))*(-(sx)))+(cz*sy*cx);
	
			value[4*1+0] = sz*cy;
			value[4*1+1] = (cz*cx)+(sz*sy*sx);
			value[4*1+2] = (cz*(-(sx)))+(sz*sy*cx);
	
			value[4*2+0] = -(sy);
			value[4*2+1] = cy*sx;
			value[4*2+2] = cy*cx;
		}
		else
		{
			value[3*0+0] = cz*cy;
			value[3*0+1] = ((-(sz))*cx)+(cz*sy*sx);
			value[3*0+2] = ((-(sz))*(-(sx)))+(cz*sy*cx);
	
			value[3*1+0] = sz*cy;
			value[3*1+1] = (cz*cx)+(sz*sy*sx);
			value[3*1+2] = (cz*(-(sx)))+(sz*sy*cx);
	
			value[3*2+0] = -(sy);
			value[3*2+1] = cy*sx;
			value[3*2+2] = cy*cx;
		}
	}

	public void set3DTransformRotationZXY_opengl(double x,double y,double z)
	{
		float sx=(float)Math.sin(x);
		float cx=(float)Math.cos(x);
		float sy=(float)Math.sin(y);
		float cy=(float)Math.cos(y);
		float sz=(float)Math.sin(z);
		float cz=(float)Math.cos(z);

		if(width==4)
		{
			value[4*0+0] = (cz*cx-sz*sy*sx);
			value[4*1+0] = -sx*cy;
			value[4*2+0] = (sz*cx+cz*sy*sx);
	
			value[4*0+1] = cx*sy*sz+sx*cz;
			value[4*1+1] = cx*cy;
			value[4*2+1] = sz*sx-cz*sy*cx;
	
			value[4*0+2] = -cy*sz;
			value[4*1+2] = sy;
			value[4*2+2] = cy*cz;
		}
	}
	//*/
	public void set3DTransformRotation_opengl(double x,double y,double z)
	{
		float sx=(float)Math.sin(x);
		float cx=(float)Math.cos(x);
		float sy=(float)Math.sin(y);
		float cy=(float)Math.cos(y);
		float sz=(float)Math.sin(z);
		float cz=(float)Math.cos(z);

		if(width==4)
		{
			value[4*0+0] = cz*cy;
			value[4*1+0] = (sy*sx*cz-cx*sz);
			value[4*2+0] = (sy*cx*cz+sz*sx);
	
			value[4*0+1] = sz*cy;
			value[4*1+1] = (sy*sx*sz+cx*cz);
			value[4*2+1] = (sy*cx*sz-cz*sx);
	
			value[4*0+2] = -sy;
			value[4*1+2] = cy*sx;
			value[4*2+2] = cy*cx;
		}
	}

	public boolean isIdentity()
	{
		float maxdif = 0.000001f;
		int ind = 0;
		for(int j=0;j<height;j++)
		{
			for(int i=0;i<width;i++)
			{
				if(i==j)
				{
					if(Math.abs(value[ind]-1.0f)>maxdif)
					{
						return false;
					}
				}
				else
				{
					if(Math.abs(value[ind]-0.0f)>maxdif)
					{
						return false;
					}
				}
				ind++;
			}			
		}
		return true;
	}
	
	public boolean orthonormalTransform()
	{
		int mw = width;
		float maxdif = 0.001f;
		float len;
		len = value[mw*0+0]*value[mw*0+0]+value[mw*0+1]*value[mw*0+1]+value[mw*0+2]*value[mw*0+2];
		if(Math.abs(len-1.0f)>maxdif)
		{
			return false;
		}
		len = value[mw*1+0]*value[mw*1+0]+value[mw*1+1]*value[mw*1+1]+value[mw*1+2]*value[mw*1+2];
		if(Math.abs(len-1.0f)>maxdif)
		{
			return false;
		}
		len = value[mw*2+0]*value[mw*2+0]+value[mw*2+1]*value[mw*2+1]+value[mw*2+2]*value[mw*2+2];
		if(Math.abs(len-1.0f)>maxdif)
		{
			return false;
		}
		
		float dot;
		dot = value[mw*0+0]*value[mw*1+0]+value[mw*0+1]*value[mw*1+1]+value[mw*0+2]*value[mw*1+2];
		if(Math.abs(dot)>maxdif)
		{
			return false;
		}
		dot = value[mw*0+0]*value[mw*2+0]+value[mw*0+1]*value[mw*2+1]+value[mw*0+2]*value[mw*2+2];
		if(Math.abs(dot)>maxdif)
		{
			return false;
		}
		dot = value[mw*1+0]*value[mw*2+0]+value[mw*1+1]*value[mw*2+1]+value[mw*1+2]*value[mw*2+2];
		if(Math.abs(dot)>maxdif)
		{
			return false;
		}
		
		return true;
	}
	
	public static final void getEulerZXY(GeneralMatrixFloat  m, GeneralMatrixFloat euler) 
	{
//		value[4*0+0] = (cz*cx-sz*sy*sx);
//		value[4*0+1] = -sx*cy;
//		value[4*0+2] = (sz*cx+cz*sy*sx);
//
//		value[4*1+0] = cx*sy*sz+sx*cz;
//		value[4*1+1] = cx*cy;
//		value[4*1+2] = sz*sx-cz*sy*cx;
//
//		value[4*2+0] = -cy*sz;
//		value[4*2+1] = sy;
//		value[4*2+2] = cy*cz;

		final int i=0;
		final int j=1;
		final int k=2;

		final int w=m.width;
		
		double cy = Math.sqrt(m.value[i*w+j]*m.value[i*w+j] + m.value[j*w+j]*m.value[j*w+j]);
		if (cy > 16*EPSILON) 
		{
		    euler.value[0] = (float)Math.atan2(-m.value[i*w+j], m.value[j*w+j]);
		    euler.value[1] = (float)Math.atan2(m.value[k*w+j], cy);
		    euler.value[2] = (float)Math.atan2(-m.value[k*w+i], m.value[k*w+k]);
		}
		else
		{
		    euler.value[0] = 0.0f;
		    euler.value[1] = (float)Math.atan2(m.value[k*w+j], cy);			
		    euler.value[2] = (float)Math.atan2(m.value[i*w+k], m.value[i*w+i]);
		}
	}
	
	public static final void getEulerYXZ(GeneralMatrixFloat  m, GeneralMatrixFloat euler) 
	{
//		value[4*0+0] = (cy*cz)+(sy*sx*sz);
//		value[4*0+1] = (cy*(-(sz)))+(sy*sx*cz);
//		value[4*0+2] = sy*cx;
//
//		value[4*1+0] = cx*sz;
//		value[4*1+1] = cx*cz;
//		value[4*1+2] = -(sx);
//
//		value[4*2+0] = ((-(sy))*cz)+(cy*sx*sz);
//		value[4*2+1] = ((-(sy))*(-(sz)))+(cy*sx*cz);
//		value[4*2+2] = cy*cx;

		final int i=0;
		final int j=1;
		final int k=2;

		final int w=m.width;
		
		double cx = Math.sqrt(m.value[j*w+i]*m.value[j*w+i] + m.value[j*w+j]*m.value[j*w+j]);
		if (cx > 16*EPSILON) {
		    euler.value[0] = (float)Math.atan2(-m.value[j*w+k], cx);
		    euler.value[1] = (float)Math.atan2(m.value[i*w+k], m.value[k*w+k]);
		    euler.value[2] = (float)Math.atan2(m.value[j*w+i], m.value[j*w+j]);
		} else {
			euler.value[0] = (float)Math.atan2(-m.value[j*w+k], cx);
		    euler.value[1] = 0;
		    euler.value[2] = (float)Math.atan2(m.value[i*w+i], m.value[i*w+j]);
		}
	}

	public static final void getEuler(GeneralMatrixFloat  m, GeneralMatrixFloat euler) 
	{
		final int i=0;
		final int j=1;
		final int k=2;

//		value[4*0+0] = cz*cy;
//		value[4*0+1] = (sy*sx*cz-cx*sz);
//		value[4*0+2] = (sy*cx*cz+sz*sx);
//
//		value[4*1+0] = sz*cy;
//		value[4*1+1] = (sy*sx*sz+cx*cz);
//		value[4*1+2] = (sy*cx*sz-cz*sx);
//
//		value[4*2+0] = -sy;
//		value[4*2+1] = cy*sx;
//		value[4*2+2] = cy*cx;
		
		final int w=m.width;

		double cy = Math.sqrt(m.value[i*w+i]*m.value[i*w+i] + m.value[j*w+i]*m.value[j*w+i]);
		if (cy > 16*EPSILON) {
		    euler.value[0] = (float)Math.atan2(m.value[k*w+j], m.value[k*w+k]);
		    euler.value[1] = (float)Math.atan2(-m.value[k*w+i], cy);
		    euler.value[2] = (float)Math.atan2(m.value[j*w+i], m.value[i*w+i]);
		} else {
			//pretty much pointing down x
			if(Math.abs(m.value[4*2+0]-(1.0f))<EPSILON)
			{
				euler.value[0] = 0.0f;
				euler.value[2] = 0.0f;
				euler.value[1] = (float)(Math.PI*0.5);
//				value[4*0+1] = (-sx*cz-cx*sz);
//				value[4*0+2] = (-cx*cz+sz*sx);
//				value[4*1+1] = (-sx*sz+cx*cz);
//				value[4*1+2] = (-cx*sz-cz*sx);
			}
			else
			if(Math.abs(m.value[4*2+0]-(-1.0f))<EPSILON)
			{
				euler.value[0] = 0.0f;
				euler.value[2] = 0.0f;
				euler.value[1] = -(float)(Math.PI*0.5);
//				value[4*0+1] = (sx*cz-cx*sz);
//				value[4*0+2] = (cx*cz+sz*sx);
//				value[4*1+1] = (sx*sz+cx*cz);
//				value[4*1+2] = (cx*sz-cz*sx);
			}
			else
			{
			    euler.value[0] = (float)Math.atan2(-m.value[i*w+k], m.value[j*w+j]);
			    euler.value[1] = (float)Math.atan2(-m.value[k*w+i], cy);
			    euler.value[2] = 0;
			}
		}
	}
	public static final void rowtransform(GeneralMatrixFloat a,GeneralMatrixFloat b,GeneralMatrixFloat c)
	{
		//     wxh
		//a is 3xn (h value of 1.0 is implied)
		//B is 3x4 (or at least the other values are unused)
		//float vdot;
		  for (int i = 0; i < a.height; i++)
		  {
			  //Apply the matrix to this vector

			  //for (int j = 0; j < a.width; j++)
			  //{
			  int j = 0;
				float x = 0.0f;
				for (int k = 0; k < a.width; k++)
				{
					x += a.value[i*a.width+k]*b.value[k*b.width+j];
				}
				  x += b.value[a.width*b.width+j];
				j++;
				float y = 0.0f;
				for (int k = 0; k < a.width; k++)
				{
					y += a.value[i*a.width+k]*b.value[k*b.width+j];
				}
				  y += b.value[a.width*b.width+j];
				j++;
				float z = 0.0f;
				for (int k = 0; k < a.width; k++)
				{
					z += a.value[i*a.width+k]*b.value[k*b.width+j];
				}
				  z += b.value[a.width*b.width+j];
				  //Add what would be the homogenious value to translate correctly
			  //}
			  
			  c.value[i*a.width+0] = x;
			  c.value[i*a.width+1] = y;
			  c.value[i*a.width+2] = z;
		  }
	}
	public float norm()
	{
		float total = 0.0f;
		for (int i = 0; i < width*height; i++)
		{
			total += value[i]*value[i];
		}
		total = (float)Math.sqrt(total);
		return total;
	}
	
	public static float sqrdist(GeneralMatrixFloat a,GeneralMatrixFloat b)
	{
		int s = a.width*b.width;
		float d = 0.0f;
		for(int i=0;i<s;i++)
		{
			float dv = b.value[i]-a.value[i];
			d += dv*dv;
		}
		return d;//(float)Math.sqrt(d);
	}

	public static float rowdist(GeneralMatrixFloat a,int rowa,GeneralMatrixFloat b,int rowb)
	{
		int offa = rowa*a.width;
		int offb = rowb*b.width;
		float d = 0.0f;
		for(int i=0;i<a.width;i++)
		{
			float dv = b.value[offb+i]-a.value[offa+i];
			d += dv*dv;
		}
		return (float)Math.sqrt(d);
	}
	
	public void normalise()
	{
		float total = 0.0f;
		for (int i = 0; i < width*height; i++)
		{
			total += value[i]*value[i];
		}
		if(total<=EPSILON)
		{
			value[0] = 1.0f;
			return;
		}
		total = (float)Math.sqrt(total);
		for (int i = 0; i < width*height; i++)
		{
			value[i] /= total;
		}
	}
	public void normaliseRow(int row,float len)
	{
		int ind = row*width;
		float total = 0.0f;
		for (int i = 0; i < width; i++)
		{
			total += value[i+ind]*value[i+ind];
		}
		if(total<=EPSILON)
		{
			value[0+ind] = 1.0f;
			return;
		}
		total = (float)Math.sqrt(total)/len;
		for (int i = 0; i < width; i++)
		{
			value[i+ind] /= total;
		}
	}

	public void scale(float s)
	{
		  for (int i = 0; i < width*height; i++)
		  {
			  value[i] = value[i]*s;
		  }
	}

	public void setIdentity()
	{
		int i=0;
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				if(x==y)
					value[i] = 1.0f;
				else
					value[i] = 0.0f;
				i++;
			}
		}
	}

	/*
	public static final boolean invert3x3(GeneralMatrixFloat a,GeneralMatrixFloat ia)
	{
//		| a11 a12 a13 |-1             |   a33a22-a32a23  -(a33a12-a32a13)   a23a12-a22a13  |
//		| a21 a22 a23 |    =  1/DET * | -(a33a21-a31a23)   a33a11-a31a13  -(a23a11-a21a13) |
//		| a31 a32 a33 |               |   a32a21-a31a22  -(a32a11-a31a12)   a22a11-a21a12  |
//
//		with DET  =  a11(a33a22-a32a23)-a21(a33a12-a32a13)+a31(a23a12-a22a13)

//		float det = a.value[0*3+0]*(a.value[0*3+0]*a.value[0*3+0]-a.value[0*3+0]*a.value[0*3+0]);
//		det -= a.value[0*3+0]*(a.value[0*3+0]*a.value[0*3+0]-a.value[0*3+0]*a.value[0*3+0]);
//		det += a.value[0*3+0]*(a.value[0*3+0]*a.value[0*3+0]-a.value[0*3+0]*a.value[0*3+0]);
		ia.value[0*3+0] = (a.value[2*3+2]*a.value[1*3+1]-a.value[2*3+1]*a.value[1*3+2]);
		ia.value[0*3+1] =-(a.value[2*3+2]*a.value[0*3+1]-a.value[2*3+1]*a.value[0*3+2]);
		ia.value[0*3+2] = (a.value[1*3+2]*a.value[0*3+1]-a.value[1*3+1]*a.value[0*3+2]);

		ia.value[1*3+0] = (a.value[2*3+2]*a.value[1*3+0]-a.value[2*3+0]*a.value[1*3+2]);
		ia.value[1*3+1] = (a.value[2*3+2]*a.value[0*3+0]-a.value[2*3+0]*a.value[0*3+2]);
		ia.value[1*3+2] =-(a.value[1*3+2]*a.value[0*3+0]-a.value[1*3+0]*a.value[0*3+2]);

		ia.value[2*3+0] = (a.value[2*3+1]*a.value[1*3+0]-a.value[2*3+0]*a.value[1*3+1]);
		ia.value[2*3+1] =-(a.value[2*3+1]*a.value[0*3+0]-a.value[2*3+0]*a.value[0*3+1]);
		ia.value[2*3+2] = (a.value[1*3+1]*a.value[0*3+0]-a.value[1*3+0]*a.value[0*3+1]);
		
		float odet = GeneralMatrixFloat.determinant(a);
		float det = a.value[0*0+0]*ia.value[0*3+0]+a.value[1*0+0]*ia.value[0*3+1]+a.value[2*0+0]*ia.value[0*3+2];
		if(Math.abs(det)<EPSILON)
		{
			System.out.println("matrix invert failed");
			return false;
		}
		float idet = 1.0f/det;
		ia.value[0] *= idet;
		ia.value[1] *= idet;
		ia.value[2] *= idet;
		ia.value[3] *= idet;
		ia.value[4] *= idet;
		ia.value[5] *= idet;
		ia.value[6] *= idet;
		ia.value[7] *= idet;
		ia.value[8] *= idet;
		return true;
	}
	*/
	
	public static final void invertTransform(GeneralMatrixFloat cameraTransformMatrix,GeneralMatrixFloat modelMatrix)
	{
		//GeneralMatrixFloat.invert(cameraTransformMatrix,modelMatrix);
		//*
		modelMatrix.value[0*4+0] = cameraTransformMatrix.value[0*4+0];
		modelMatrix.value[1*4+0] = cameraTransformMatrix.value[0*4+1];
		modelMatrix.value[2*4+0] = cameraTransformMatrix.value[0*4+2];

		modelMatrix.value[0*4+1] = cameraTransformMatrix.value[1*4+0];
		modelMatrix.value[1*4+1] = cameraTransformMatrix.value[1*4+1];
		modelMatrix.value[2*4+1] = cameraTransformMatrix.value[1*4+2];

		modelMatrix.value[0*4+2] = cameraTransformMatrix.value[2*4+0];
		modelMatrix.value[1*4+2] = cameraTransformMatrix.value[2*4+1];
		modelMatrix.value[2*4+2] = cameraTransformMatrix.value[2*4+2];

		float x = -cameraTransformMatrix.value[3*4+0];
		float y = -cameraTransformMatrix.value[3*4+1];
		float z = -cameraTransformMatrix.value[3*4+2];
		
		//needs to be rotated like the rest of the points in space
		float tx = modelMatrix.value[0*4+0]*x+modelMatrix.value[1*4+0]*y+modelMatrix.value[2*4+0]*z;
		float ty = modelMatrix.value[0*4+1]*x+modelMatrix.value[1*4+1]*y+modelMatrix.value[2*4+1]*z;
		float tz = modelMatrix.value[0*4+2]*x+modelMatrix.value[1*4+2]*y+modelMatrix.value[2*4+2]*z;
		modelMatrix.value[3*4+0] = tx;
		modelMatrix.value[3*4+1] = ty;
		modelMatrix.value[3*4+2] = tz;		
		//*/
	}

	public static final boolean invert(GeneralMatrixFloat a,GeneralMatrixFloat ia)
	{
		GeneralMatrixFloat temp = new GeneralMatrixFloat(a.width,a.height);
		temp.set(a);

		ia.setIdentity();
		int i,j,k,swap;
		float t;
		for(i=0;i<a.width;i++)
		{
			swap = i;
			for (j = i + 1; j < a.height; j++) {
			    if (Math.abs(a.get(i, j)) > Math.abs(a.get(i, j)))
			    {
			    	swap = j;
			    }
			}

			if (swap != i)
			{
			    /*
			    ** Swap rows.
			    */
			    for (k = 0; k < a.width; k++)
			    {
					t = temp.get(k,i);
					temp.set(k,i,temp.get(k, swap));
					temp.set(k, swap,t);

					t = ia.get(k,i);
					ia.set(k,i,ia.get(k, swap));
					ia.set(k, swap,t);
			    }
			}

			t = temp.get(i,i);
			if (t == 0.0f)
			{
			    /*
			    ** No non-zero pivot.  The matrix is singular, which shouldn't
			    ** happen.  This means the user gave us a bad matrix.
			    */
				System.out.println("inverse failed");
			    return false;
			}

			for (k = 0; k < a.width; k++)
			{
				temp.set(k,i,temp.get(k,i) / t);
				ia.set(k,i,ia.get(k,i) / t);
			}
			for (j = 0; j < a.width; j++)
			{
			    if (j != i)
			    {
			    	t = temp.get(i,j);
			    	for (k = 0; k < a.width; k++)
			    	{
			    		temp.set(k,j, temp.get(k,j) - temp.get(k,i)*t);
			    		ia.set(k,j, ia.get(k,j) - ia.get(k,i)*t);
			    	}
			    }
			}
	    }
	    return true;
	}

	public static final float dotProduct(GeneralMatrixFloat a,int arow,GeneralMatrixFloat b,int brow)
	{
		int aind = arow*a.width;
		int bind = brow*b.width;
		float result = 0.0f;
		for(int i=0;i<a.width;i++)
		{
			result += a.value[aind]*b.value[bind];
			aind++;
			bind++;
		}
		return result;
	}
	
	public static final float dotProduct3(GeneralMatrixFloat a,GeneralMatrixFloat b)
	{
		return a.value[0]*b.value[0]+a.value[1]*b.value[1]+a.value[2]*b.value[2];
	}
	
	public static final void crossProduct3(GeneralMatrixFloat a,GeneralMatrixFloat b,GeneralMatrixFloat c)
	{
		c.value[0] = a.value[1]*b.value[2]-b.value[1]*a.value[2];
		c.value[1] = a.value[2]*b.value[0]-b.value[2]*a.value[0];
		c.value[2] = a.value[0]*b.value[1]-b.value[0]*a.value[1];
	}

	public static final void multInPlace(GeneralMatrixFloat a,GeneralMatrixFloat b)
	{
		GeneralMatrixFloat c = new GeneralMatrixFloat(a.width,1);

		int n = a.height;
		int p = a.width;
		int r = b.width;

		if(a.width!=b.height)
		{
			System.out.println("Mult error, matricies sizes dont match");
			return;
		}
		
	  float vdot;
	  int i,j,k,m;


      int r5 = r*5;
	  int rowI = 0;
	  int crowI = 0;
	  for (i = 0; i < n; i++) {

	     for (j = 0; j < r; j++) {

	        vdot = 0.0f;

	        m = p%5;

	        int rowK = 0;
	        for (k = 0; k < m; k++) {

	           vdot += a.value[rowI+k]*b.value[rowK+j];
	           rowK += r;
	        }

	        for (k = m; k < p; k += 5) {

	           vdot += a.value[rowI+k]*b.value[rowK+j] +
	                   a.value[rowI+k+1]*b.value[rowK+r+j] +
	                   a.value[rowI+k+2]*b.value[rowK+r*2+j] +
	                   a.value[rowI+k+3]*b.value[rowK+r*3+j] +
	                   a.value[rowI+k+4]*b.value[rowK+r*4+j];

	           rowK += r5;
	        }

	        c.value[j] = vdot;

	     }
	     //now overright the a rowI
	     for (j = 0; j < r; j++) 
	     {
	    	 a.value[rowI+j] = c.value[j];
	     }
	     rowI += p;
	     crowI += r;
	  }
	}
	
	public static final void mult(GeneralMatrixFloat a,GeneralMatrixFloat b,GeneralMatrixFloat c)
	{
		int n = a.height;
		int p = a.width;
		int r = b.width;

		if(a.width!=b.height)
		{
			System.out.println("Mult error, matricies sizes dont match");
			return;
		}
		
	  float vdot;
	  int i,j,k,m;


      int r5 = r*5;
	  int rowI = 0;
	  int crowI = 0;
	  for (i = 0; i < n; i++) {

	     for (j = 0; j < r; j++) {

	        vdot = 0.0f;

	        m = p%5;

	        int rowK = 0;
	        for (k = 0; k < m; k++) {

	           vdot += a.value[rowI+k]*b.value[rowK+j];
	           rowK += r;
	        }

	        for (k = m; k < p; k += 5) {

	           vdot += a.value[rowI+k]*b.value[rowK+j] +
	                   a.value[rowI+k+1]*b.value[rowK+r+j] +
	                   a.value[rowI+k+2]*b.value[rowK+r*2+j] +
	                   a.value[rowI+k+3]*b.value[rowK+r*3+j] +
	                   a.value[rowI+k+4]*b.value[rowK+r*4+j];

	           rowK += r5;
	        }

	        c.value[crowI+j] = vdot;

	     }
	     rowI += p;
	     crowI += r;
	  }

	}

	public static final void add(GeneralMatrixFloat a,GeneralMatrixFloat b,GeneralMatrixFloat c)
	{
		  for (int i = 0; i < a.width*a.height; i++)
		  {
			  c.value[i] = a.value[i]+b.value[i];
		  }
	}
	public static final void add(GeneralMatrixFloat a,int arow,GeneralMatrixFloat b,int brow,GeneralMatrixFloat c,int crow)
	{
		int aoff = a.width*arow;
		int boff = b.width*brow;
		int coff = c.width*crow;
		  for (int i = 0; i < a.width; i++)
		  {
			  c.value[coff+i] = a.value[aoff+i]+b.value[boff+i];
		  }
	}
	public static final void addsqa(GeneralMatrixFloat a,GeneralMatrixFloat b,GeneralMatrixFloat c)
	{
		  for (int i = 0; i < a.width*a.height; i++)
		  {
			  c.value[i] = a.value[i]*a.value[i]+b.value[i];
		  }
	}

	public static final void transpose(GeneralMatrixFloat a,GeneralMatrixFloat at)
	{
		int i,j;

		int ai = 0;
		int atjR;
	      for (i = 0; i < a.height; i++) {

	  		atjR = 0;
	         for (j = 0; j < a.width; j++) {

	            at.value[atjR+i] = a.value[ai];
	            ai++;
	            atjR += a.height;
	         }

	      }
	}

	public final float trace()
	{
		float sum = 0.0f;
		int index = 0;
		for (int i = 0; i < height; i++)
		{
			sum += value[index];
			index += height+1;
		}
		return sum;
	}

	public static float determinant(GeneralMatrixFloat m)
	{
		int n = m.width; 
		if(n==1)
			return m.value[0];
		else
		if(n==2)
		{
			return m.value[0*2+0] * m.value[1*2+1] - m.value[1*2+0] * m.value[0*2+1];
		}
		else
		{
			float det = 0.0f;
			for (int j1=0;j1<n;j1++) 
			{
		         if(m.value[0*n+j1]==0.0f)
		        	 continue;

		         GeneralMatrixFloat subm = new GeneralMatrixFloat(n-1,n-1);

		         for (int i=1;i<n;i++) 
		         {
		            int j2 = 0;
		            for (int j=0;j<n;j++) 
		            {
		               if (j == j1)
		                  continue;
		               subm.value[(i-1)*(n-1)+j2] = m.value[i*n+j];
		               j2++;
		            }
		         }
		         int ind = 1+j1+1;
		         
		         if((ind%2)==0)
		        	 det += m.value[0*n+j1] * determinant(subm);
		         else
		        	 det -= m.value[0*n+j1] * determinant(subm);
			}
			return det;
		}
	}
	
	public static final void sub(GeneralMatrixFloat a,GeneralMatrixFloat b,GeneralMatrixFloat c)
	{
		  for (int i = 0; i < a.width*a.height; i++)
		  {
			  c.value[i] = a.value[i]-b.value[i];
		  }
	}
	public static final void sub(GeneralMatrixFloat a,int arow,GeneralMatrixFloat b,int brow,GeneralMatrixFloat c,int crow)
	{
		int aoff = a.width*arow;
		int boff = b.width*brow;
		int coff = c.width*crow;
		  for (int i = 0; i < a.width; i++)
		  {
			  c.value[coff+i] = a.value[aoff+i]-b.value[boff+i];
		  }
	}
	public static final void subsqb(GeneralMatrixFloat a,GeneralMatrixFloat b,GeneralMatrixFloat c)
	{
		  for (int i = 0; i < a.width*a.height; i++)
		  {
			  c.value[i] = a.value[i]-b.value[i]*b.value[i];
		  }
	}

	public static final void rowsub(GeneralMatrixFloat a,GeneralMatrixFloat b,GeneralMatrixFloat c)
	{
		int ai = 0;
		  for (int i = 0; i < a.height; i++)
		  {
			  for (int j = 0; j < a.width; j++)
			  {
				  c.value[ai] = a.value[ai]-b.value[j];
				  ai++;
			  }
		  }
	}

	public static final void setRow(int row,GeneralMatrixFloat a,GeneralMatrixFloat target)
	{
		int ind = row*a.width;
		for(int i=0;i<a.width;i++)
		{
			target.value[ind+i] = a.value[ind+i];
		}
	}
	
	public static final void blendRow(int row,float mag0,float mag1,GeneralMatrixFloat a,GeneralMatrixFloat b,GeneralMatrixFloat target)
	{
		int ind = row*a.width;
		for(int i=0;i<a.width;i++)
		{
			target.value[ind+i] = a.value[ind+i]*mag0+b.value[ind+i]*mag1;
		}
	}
	
//	public static final void blendRotations(GeneralMatrixFloat a,GeneralMatrixFloat b,float f,GeneralMatrixFloat c)
//	{
//		float[] qa = new float[4];
//		float[] qb = new float[4];
//		float[] qc = new float[4];
//		
//		Quaternion.MatrixtoQuaternion(a.value, qa);
//		Quaternion.MatrixtoQuaternion(b.value, qb);
//		Quaternion.slerp(qa, qb, f, qc);
//		Quaternion.QuaterniontoMatrix(qc, c.value);
//	}
}
