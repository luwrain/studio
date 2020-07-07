
package org.luwrain.studio.backends.ly;

import java.io.*;
import java.util.*;
import com.google.gson. annotations.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;

final class PySourceFile implements Part
{
    @SerializedName("name")
    private String name = null;

    @SerializedName("path")
    private String path = null;

    private LyProject proj = null;

        void setProject(LyProject proj)
    {
	NullCheck.notNull(proj, "proj");
	this.proj = proj;
    }

    @Override public String getTitle()
    {
	return name != null?name:"NONAME";
    }

    @Override public Part[] getChildParts()
    {
	return new Part[0];
    }

    @Override public Editing startEditing() throws IOException
    {
	return new LyEditing(new File(proj.getProjectDir(), path));
    }

    @Override public String toString()
    {
	return getTitle();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof PySourceFile))
	    return false;
	final PySourceFile f = (PySourceFile)o;
	return path.equals(f.path);
    }
}
