package utils;
import utils.TextImportExport;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import web.WebSecurity;

public class FileStoreInterface 
{
	private static final int BUFFER = 8192;

	public String root = "/";
	
	//Locked files, with temp files that have been changed
	
	//This class is used to make accessing and changing large collections of files more reliable
	//The MVStore database used in the DatabaseInterface is fast and reliable 
	//however it is designed to load stored data entirely into memory
	//if large files, such as videos, are stored directly in the database 
	//then when they are accessed they will be entirely loaded into the computers memory
	//this will probably crash the program or at the very least make it run unreliably
	//for this reason larger files are typically stored directly on the hard disk or remote
	//server
	//
	public FileStoreInterface(String rootpath)
	{
		File rootf = new File(rootpath);
		try
		{
			root = rootf.getCanonicalPath();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	public void createAllParentDirectories(String dir)
	{
		String[] split = dir.split("/");
		String path = "";
		for(int i=1;i<(split.length-1);i++)
		{
			path += "/"+split[i];
			if(!exists(path))
				createDirectory(path);
		}
	}

	public String decodeFilePath( String uri )
	{
		String path = WebSecurity.decodePercent(uri);

		try
		{
			File f = new File(root+"/"+path);
			String canonical = f.getCanonicalPath();
			if(canonical.startsWith(root))
				return canonical;
			else
				return null;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public long lockForRead(String file)
	{
		//is the file already locked?
		return 0;
	}
	
	public long lockForWrite(String file)
	{
		//is the file already locked?
		return 0;
	}
	
	public void unlockForRead(String file,long id)
	{
		
	}
	public void unlockForWrite(String file,long id)
	{
		
	}
	
	//A queue of file operations that have been blocked because of a lock on the file
	public void processQueue()
	{
		//Go through the file operations in the queue and if the file is not locked
	}
	
	public boolean exists(String dir)
	{
		File dirf = new File(dir);
		return dirf.exists()&&dirf.isFile();
	}


	public String newuniquefile(String in)
	{
		if(!exists(in))
			return in;
		int num = 0;
		while(true)
		{
			String tryf = in+num;
			if(!exists(tryf))
				return tryf;
			num++;
		}
	}

	public boolean copyFile(String in, String out) 
	{
		File inf = new File(in);
		File outf = new File(out);
		return copyFile(inf, outf);
	}
	public boolean swapFiles(String in, String out) 
	{
		File inf = new File(in);
		File outf = new File(out);
		File outft = new File(newuniquefile(out));
		inf.renameTo(outft);
		outf.renameTo(inf);
		outft.renameTo(outf);
		return true;
	}
	public boolean moveFile(File file,File intodir)
	{
		boolean success = file.renameTo(new File(intodir, file.getName()));
		return success;
	}
	
	public static void deepCopy(File dir,String toFolder,GeneralMatrixString replace)
	{
		(new File(toFolder)).mkdir();
		File[] cs = dir.listFiles();
		for(int i=0;i<cs.length;i++)
		{
			if(cs[i].isFile())
			{
				GeneralMatrixString lines = new GeneralMatrixString(1);
				TextImportExport.loadTextFile(cs[i].getAbsolutePath(), lines);
				for(int li=0;li<lines.height;li++)
				{
					for(int ri=0;ri<replace.height;ri++)
					{
						String in = replace.value[ri*2+0];
						String out = replace.value[ri*2+1];
						
						if(lines.value[li].contains(in))
						{
							System.out.println(in);
						}
						lines.value[li] = lines.value[li].replace(in, out);
					}
				}
				TextImportExport.saveTextFile(toFolder+"/"+cs[i].getName(), lines);;
			}
			else
			{
				deepCopy(cs[i],toFolder+"/"+cs[i].getName(),replace);
			}
		}
	}

	public void deepCopy(File dir,String toFolder)
	{
		(new File(toFolder)).mkdir();
		File[] cs = dir.listFiles();
		for(int i=0;i<cs.length;i++)
		{
			if(cs[i].isFile())
			{
				copyFile(cs[i], new File(toFolder+"/"+cs[i].getName()));
			}
			else
			{
				deepCopy(cs[i],toFolder+"/"+cs[i].getName());
			}
		}
	}

	private static void close(Closeable closable) {
	    if (closable != null) {
	        try {
	            closable.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public void createDirectory(String dir)
	{
		boolean success = (new File(dir)).mkdir();
	    if (success) {
	      System.out.println("Directory: " + dir + " created");
	    }    		
	    else
	    {
	    	System.out.println("!Directory: " + dir + " not created");
	    }
	}

	public boolean copyFile(File source, File target) 
	{
		if(source.isDirectory())
		{
			createDirectory(target.getAbsolutePath());
			return true;
		}
		
        FileChannel in = null;
        FileChannel out = null;

        boolean success = true;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(target).getChannel();

            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER);
            while (in.read(buffer) != -1) {
                buffer.flip();

                while(buffer.hasRemaining()){
                    out.write(buffer);
                }

                buffer.clear();
            }
        } catch (IOException e) {
        	success = false;
            e.printStackTrace();
        } finally {
            close(in);
            close(out);
        }

	    return success;
	  }
	  
	public void deleteFile(File in) 
	{
		in.delete();
	}
	public void deleteFile(String in) 
	{
		new File(in).delete();
	}
	
	public static void deleteDirectory(String dir)
	{
		File dirf = new File(dir);
		deleteDirectory(dirf);
	}
	public static void deleteDirectory(File dirf)
	{
		if( dirf.exists() ) {
		      File[] files = dirf.listFiles();
		      for(int i=0; i<files.length; i++) {
		         if(files[i].isDirectory()) 
		         {
		           deleteDirectory(files[i]);
		         }
		         else {
		           files[i].delete();
		         }
		      }
		      dirf.delete();
		}
	}

	public void rename(String from,String to) 
	{
		File f = new File(from);
		File fto = new File(to);
		if(fto.exists())
		{
			System.out.println(to+":exists");
		}
		else
			f.renameTo(fto);
	}
}
