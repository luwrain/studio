
//LWR_API 1.0

package org.luwrain.studio;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.studio.backends.js.JsProject;
import org.luwrain.studio.backends.tex.TexProjectLoader;

public final class ProjectFactory
{
    static public Project load(File projFile) throws IOException
    {
	NullCheck.notNull(projFile, "projFile");
	/*
	final JsProject jsProj = new JsProject();
	jsProj.load(projFile);
	return jsProj;
	*/
	final TexProjectLoader texLoader = new TexProjectLoader();
	return texLoader.load(projFile);
    }
}
