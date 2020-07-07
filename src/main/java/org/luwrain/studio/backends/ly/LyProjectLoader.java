
package org.luwrain.studio.backends.ly;

import java.io.*;

import com.google.gson.annotations.SerializedName;
import com.google.gson.*;

import org.luwrain.studio.Project;

public final class LyProjectLoader
{
    public LyProject load(File projFile) throws IOException
    {
	final Gson gson = new Gson();
	final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(projFile)));
	final LyProject proj = gson.fromJson(reader, LyProject.class);
	proj.setProjectFile(projFile);
	proj.finalizeLoading();
	return proj;
    }
}
