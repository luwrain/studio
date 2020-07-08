
package org.luwrain.studio.backends.ly;

import java.io.*;
import java.util.*;

import com.google.gson.annotations.SerializedName;

import org.luwrain.core.*;
import org.luwrain.studio.*;

final class LyFolder implements org.luwrain.studio.Part
{
    @SerializedName("name")
    private String name = "";

    @SerializedName("subfolders")
    private List<LyFolder> subfolders = null;

    @SerializedName("sourceFiles")
    private List<PySourceFile> sourceFiles = null;

    private LyProject proj = null;

    void setProject(LyProject proj)
    {
	NullCheck.notNull(proj, "proj");
	this.proj = proj;
	if (name == null)
	    name = "-";
	if (subfolders == null)
	    subfolders = new LinkedList();
	if (sourceFiles == null)
	    sourceFiles = new LinkedList();
	for(LyFolder f: subfolders)
	    f.setProject(proj);
	for(PySourceFile f: sourceFiles)
	    f.setProject(proj);
    }

    @Override public org.luwrain.studio.Editing startEditing()
    {
	return null;
    }

    @Override public org.luwrain.studio.Part[] getChildParts()
    {
	final List<Part> res = new LinkedList();
	for(LyFolder f: subfolders)
	    res.add(f);
	for(PySourceFile f: sourceFiles)
	    res.add(f);
	return res.toArray(new Part[res.size()]);
    }

    @Override public String getTitle()
    {
	return name;
    }

    @Override public String toString()
    {
	return getTitle();
    }

    @Override public boolean equals(Object o)
    {
	return o != null && (o instanceof LyFolder);
    }
}