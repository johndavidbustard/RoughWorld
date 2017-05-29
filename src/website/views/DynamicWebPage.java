package website.views;

import web.OutputStreamCallback;
import web.WebRequest;

//Common base class for all dynamic pages
public abstract class DynamicWebPage implements OutputStreamCallback
{
	public abstract boolean process(WebRequest toProcess);

}
