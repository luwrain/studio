
package org.luwrain.studio.backends.js;

import java.io.*;

import com.google.gson.annotations.SerializedName;
import com.google.gson.*;

import org.luwrain.studio.Project;



public final class JsProjectLoader
{
    public Project load(File projFile) throws IOException
    {
	final Gson gson = new Gson();
	final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(projFile)));
final JsProject proj = gson.fromJson(reader, JsProject.class);
//proj.setProjectFile(projFile);
//proj.finalizeLoading();
return proj;
}
}
