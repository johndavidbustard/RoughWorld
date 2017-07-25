package website.views;

import utils.FileStoreInterface;
import web.OutputStreamCallback;
import web.WebRequest;

//Common base class for all dynamic pages
public abstract class DynamicWebPage implements OutputStreamCallback
{
	public FileStoreInterface fs = null;
	
	public DynamicWebPage(FileStoreInterface fs)
	{
		this.fs = fs;
	}
	
	public abstract boolean process(WebRequest toProcess);

}
