package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URL;

public class TextImportExport 
{
	public static String convertStringToJavaEscapedString(String in)
	{
		in = in.replace("\n", "\\n");
		in = in.replace("\t", "\\t");
		in = in.replace("\"", "\\\"");
		return "public static final String str=\""+in+"\";";
	}
	
//	public static boolean parseIntRanges(String s,GeneralMatrixInt valuesAndRanges,int notrange)
//	{
//		String[] vars = s.split(",");
//		boolean val = false;
//		for(int i=0;i<vars.length;i++)
//		{
//			String[] range = vars[i].split("-");
//			if(range.length==1)
//				valuesAndRanges.push_back_row(Integer.parseInt(vars[i]),notrange);
//			else
//				valuesAndRanges.push_back_row(Integer.parseInt(range[0]),Integer.parseInt(range[1]));
//		
//			val = true;
//		}
//		return val;
//	}

	public static int readEscapedString(InputStream sin,int firstc,GeneralMatrixString result) throws Exception
	{
		boolean escaped = false;
		int c = sin.read();
        String v = "";
	    do
	    {
	      if(escaped)
	      {
	    	  if(c!=-1)
	    		  v+=(char)c;
	    	  escaped = false;
	      }
	      else
	      {
	          if((c==-1)||(c==firstc))
	        	  break;
	          if(c=='\\')
	          {
	        	  escaped = true;
	          }
	          else
	        	  v+=(char)c;
	      }
          c = sin.read();
        }
        while(true);

	    result.value[0] = v;
	    return c;
	}
	
	public static int readComponent(InputStream sin,int firstc,GeneralMatrixString result) throws Exception
	{
		//Read a string
		if(firstc=='\'')
		{
			readEscapedString(sin,firstc,result);
			return sin.read();
		}
		//Read another value entry
		else
		{
			int c = firstc;
	        String v = "";
		    do
		    {
	          if((c==-1)||(c==',')||(c==')'))
	        	  break;
	          v+=(char)c;
	          c = sin.read();
	        }
	        while(true);

		    result.value[0] = v;
		    return c;
		}
	}
	
	public static int readEntry(InputStream sin,int firstc,GeneralMatrixString result) throws Exception
	{
		GeneralMatrixString component = new GeneralMatrixString(1);
		while(true)
		{
			int c = sin.read();
			c = readComponent(sin, c, component);
			result.push_back(component.value[0]);
			if(c==')')
			{
				c = sin.read();
				return c;
			}
			else
			{
				if(c!=',')
				{
					System.out.println("Ooops");
				}
			}
		}
	}

	public static int readWord(StringReader sin,boolean prewhitespace,int firstc,GeneralMatrixString result) throws Exception
	{
        int c = firstc;      
        if(prewhitespace)
        {
	    do
	    {
          if((c!='\t')&&(c!=' '))
        	  break;
          c = sin.read();
        }
        while(true);
        }

        String v = "";
	    do
	    {
          if((c=='\t')||(c==' ')||(c==-1)||(c==','))
        	  break;
          v+=(char)c;
          c = sin.read();
        }
        while(true);

	    result.value[0] = v;
	    return c;
	}
	public static int readWord(InputStream sin,boolean prewhitespace,int firstc,GeneralMatrixString result) throws Exception
	{
        int c = firstc;      
        if(prewhitespace)
        {
	    do
	    {
          if((c!='\t')&&(c!=' '))
        	  break;
          c = sin.read();
        }
        while(true);
        }

        String v = "";
	    do
	    {
          if((c=='\t')||(c==' ')||(c==-1)||(c==','))
        	  break;
          v+=(char)c;
          c = sin.read();
        }
        while(true);

	    result.value[0] = v;
	    return c;
	}

	public static int within(int c,int[] set)
	{
		for(int i=0;i<set.length;i++)
		{
			if(set[i]==c)
				return i;
		}
		return -1;
	}

	public static int readUntilStart(InputStream sin,int firstc,int[] startchars) throws Exception
	{
        int c = firstc;   
	    do
	    {
	    	int starti = within(c,startchars);
	        if(starti!=-1)
	        {
	        	return c;
	        }
			c = sin.read();
		}
		while(c!=-1);

	    return c;
	}
	public static int readUntilStart(InputStream sin,int firstc,int[] startchars,long[] pos) throws Exception
	{
        int c = firstc;   
	    do
	    {
	    	int starti = within(c,startchars);
	        if(starti!=-1)
	        {
	        	return c;
	        }
			c = sin.read();
			pos[0]++;
		}
		while(c!=-1);

	    return c;
	}
	public static int readUntilStart(InputStream sin,int firstc,int[] startchars,GeneralMatrixObject result) throws Exception
	{
		String v = "";
        int c = firstc;   
	    do
	    {
	    	int starti = within(c,startchars);
	        if(starti!=-1)
	        {
	    	    result.push_back(v);
	        	return c;
	        }
	        v += (char)c;
			c = sin.read();
		}
		while(c!=-1);

	    result.push_back(v);
	    
	    return c;
	}
	public static int readUntilStart(InputStream sin,int firstc,int[] startchars,GeneralMatrixObject result,long[] pos) throws Exception
	{
		String v = "";
        int c = firstc;   
	    do
	    {
	    	int starti = within(c,startchars);
	        if(starti!=-1)
	        {
	    	    result.push_back(v);
	        	return c;
	        }
	        v += (char)c;
			c = sin.read();
			pos[0]++;
		}
		while(c!=-1);

	    result.push_back(v);
	    
	    return c;
	}
	
	public static int readHierarchy(InputStream sin,int firstc,int[] startchars,int[] endchars,GeneralMatrixObject result) throws Exception
	{
		if(firstc==-1)
			return -1;
		
        int c = firstc;      
        int endi = within(c,startchars);

        String vcut = "";
        
	    while(endi==-1)
	    {
	    	vcut += (char)firstc;
	    	firstc = sin.read();
	    	c = firstc;
	    	if(c==-1)
	    		return c;
	    	endi = within(c,startchars);
	    }
	    
	    vcut = vcut.trim();
	    if(vcut.length()>0)
	    	System.out.println(vcut);
	    
        int endc = endchars[endi];

        c = sin.read();
        
        boolean firsttime = true;
        String v = ""+(char)firstc;
	    do
	    {
          if(c==endc)
          {
              v+=(char)c;
              c = sin.read();
        	  break;
          }
          int starti = within(c,startchars);
          if(starti!=-1)
          {
        	  result.push_back(v);
        	  v = "";
              GeneralMatrixObject subelements = new GeneralMatrixObject(1);
	          c = readHierarchy(sin,c,startchars,endchars,subelements);
	          result.push_back(subelements);
          }
          else
          {
        	  v+=(char)c;
              c = sin.read();
          }
        }
        while(c!=-1);

	    if(v.length()>0)
	    	result.push_back(v);
	    return c;
	}
	public static int readHierarchy(InputStream sin,int firstc,int[] startchars,int[] endchars,GeneralMatrixObject result,long[] pos) throws Exception
	{
		if(firstc==-1)
			return -1;
		
        int c = firstc;      
        int endi = within(c,startchars);
        int endc = endchars[endi];

        c = sin.read();
        pos[0]++;
        
        boolean firsttime = true;
        String v = ""+(char)firstc;
	    do
	    {
          if(c==endc)
          {
              v+=(char)c;
        	  break;
          }
          int starti = within(c,startchars);
          if(starti!=-1)
          {
        	  result.push_back(v);
        	  v = "";
              GeneralMatrixObject subelements = new GeneralMatrixObject(1);
	          c = readHierarchy(sin,c,startchars,endchars,subelements,pos);
	          result.push_back(subelements);
          }
          else
        	  v+=(char)c;
          c = sin.read();
          pos[0]++;
        }
        while(c!=-1);

	    if(v.length()>0)
	    	result.push_back(v);
	    return c;
	}

	public static boolean isWhiteSpace(int c)
	{
		return (c==' ')||(c=='\t');
	}
//	public static void parseIntSequence(String s,int seperator,GeneralMatrixInt positions)
//	{
//		positions.width = 1;
//		positions.height = 0;
//		
//		try {
//			int c;
//		    StringReader sin = new StringReader(s);
//
//		    //Get rid of any space
//	        while(true)
//	        {
//	        	c = sin.read();
//	        	if(!isWhiteSpace(c))
//	        		break;
//	        	if(c==-1)
//	        		break;
//	        }
//
//	        if(c==-1)
//        		return;
//
//	        while(true)
//		    {
//	    		String v = "";
//		        do
//		        {
//		      	  	v = v+(char)c;
//		      	  	c = sin.read();
//
//		        	if(c==-1)
//		        		break;
//		      	  	if(c==seperator)
//		      	  		break;
//		        }
//		        while(true);
//		        
//		        positions.push_back(Integer.parseInt(v));
//		        
//	      	  	if(c==seperator)
//	      	  	{
//	      	  		c = sin.read();
//	      	  		break;
//	      	  	}
//		        if(c==-1)
//		        	break;
//		    }
//		}
//	    catch(Exception e)
//	    {
//	    	System.out.println(e.toString());
//	    }
//	}
//	public static void parseFloatSequence(String s,int seperator,GeneralMatrixFloat positions)
//	{
//		positions.width = 1;
//		positions.height = 0;
//		
//		try {
//			int c;
//		    StringReader sin = new StringReader(s);
//
//		    //Get rid of any space
//	        while(true)
//	        {
//	        	c = sin.read();
//	        	if(!isWhiteSpace(c))
//	        		break;
//	        	if(c==-1)
//	        		break;
//	        }
//
//	        if(c==-1)
//        		return;
//
//	        while(true)
//		    {
//	    		String v = "";
//		        do
//		        {
//		      	  	v = v+(char)c;
//		      	  	c = sin.read();
//
//		        	if(c==-1)
//		        		break;
//		      	  	if(c==seperator)
//		      	  		break;
//		        }
//		        while(true);
//		        
//		        positions.push_back(Float.parseFloat(v));
//		        
//	      	  	if(c==seperator)
//	      	  	{
//	      	  		c = sin.read();
//	      	  	}
//		        if(c==-1)
//		        	break;
//		    }
//		}
//	    catch(Exception e)
//	    {
//	    	System.out.println(e.toString());
//	    }
//	}
    public static void saveTextFile(String filepath,String text)
    {
		try
		{
			FileOutputStream f = new FileOutputStream(filepath);
			PrintStream p = new PrintStream(f);
			p.append(text);
			p.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
    }
    public static void saveTextFile(String filepath,GeneralMatrixString text)
    {
		try
		{
			FileOutputStream f = new FileOutputStream(filepath);
			PrintStream p = new PrintStream(f);
			for(int i=0;i<text.height;i++)
			{
				if(text.value[i]==null)
					p.append("\n");
				else
					p.append(text.value[i]+"\n");
			}
			p.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
    }
    public static int numLines(String s)
    {
    	int num = 1;
    	for(int i=0;i<s.length();i++)
    	{
    		if(s.charAt(i)=='\n')
    			num++;
    	}
    	return num;
    }
    public static String readline(InputStream bis)
    {
    	boolean empty = true;
    	String result = "";
    	try
    	{
	    	while(true)
	    	{
	    		int c = bis.read();
	    		if(c=='\r')
	    		{
	    			//read a line feed
	    			bis.read();
	    			return result;
	    		}
	    		else
	    		if(c=='\n')
	    		{	    			
		    		return result;
	    		}
	    		else
	    		if(c==-1)
	    		{
	    			
	    		}
	    		else
	    		{
	    			empty = false;
	    			result += (char)c;
	    		}
	    		if(c==-1)
	    		{
	    			if(empty)
	    				return null;
	    			else
	    				return result;
	    		}
	    	}
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.toString());
    	}
    	return null;
    }
    public static String readline(BufferedReader bis)
    {
    	boolean empty = false;
    	String result = "";
    	try
    	{
	    	while(true)
	    	{
	    		int c = bis.read();
	    		if(c=='\r')
	    		{
	    			//read a line feed
	    			bis.read();
	    			return result;
	    		}
	    		else
	    		if(c=='\n')
	    		{	    			
		    		return result;
	    		}
	    		else
	    		{
	    			empty = false;
	    			result += (char)c;
	    		}
	    		if(c==-1)
	    		{
	    			if(empty)
	    				return null;
	    			else
	    				return result;
	    		}
	    	}
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.toString());
    	}
    	return null;
    }
    public static String loadTextFileASString(File file)
    {
    	try
    	{
    		char[] cs = loadTextFile(file.toURL());
    		if(cs==null)
    			return null;
    		if(cs.length==0)
    			return "";
    	String result = new String(cs);
    	return result;
    	}catch(Exception e)
    	{
    		System.out.println(e.toString());
    	}
    	return null;
    }
    public static String loadURLTextFileASString(String urlpath)
    {
		try
		{
			String result = new String(loadTextFile(new URL(urlpath)));
	    	return result;
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			return null;
		}
    }
    public static void loadURLTextFileASString(String urlpath,GeneralMatrixString lines)
    {
		try
		{
			loadTextFile(new URL(urlpath),lines);
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
    }
    public static void loadTextFile(String filepath,GeneralMatrixString lines)
    {
		try
		{
			loadTextFile((new File(filepath)).toURL(),lines);
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}

    }
    public static void loadTextFile(URL file,GeneralMatrixString lines)
    {
		try
		{
			loadTextFile(file.openStream(),lines);
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
    }
    public static void loadTextFile(InputStream is,GeneralMatrixString lines)
    {
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(is));			
			String s;
			int line=0;
			
			while((s = in.readLine())!= null)
			{
				lines.push_back(s);
				line++;
			}
			
			if(line==0)
			{
				InputStreamReader isn = new InputStreamReader(is);
				int c;
				String result = "";
				while((c=isn.read())!=-1)
				{
					result += (char)c;
				}
				lines.push_back(result);
				
				isn.close();
			}
			
			in.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
    }
    public static String loadTextFile(String filepath)
    {
		try
		{
			return new String(loadTextFile((new File(filepath)).toURL()));
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		return null;
    }
    public static char[] loadTextFile(URL file)
    {
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(file.openStream()));			
			String result = "";
			String s;
			int line=0;
			
			while((s = in.readLine())!= null)
			{
				if(line!=0)
					result += "\n";
				result += s;
				line++;
			}
			
			if(line==0)
			{
				InputStreamReader isn = new InputStreamReader(file.openStream());
				int c;
				while((c=isn.read())!=-1)
				{
					result += (char)c;
				}
				line++;
				
				isn.close();
			}
			
			in.close();
			
//			CharBuffer result = CharBuffer.allocate(10240);
//			int size = in.read(result);
//			result.limit(size);
//			return result.array();
			return result.toCharArray();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		return null;
    }

}
