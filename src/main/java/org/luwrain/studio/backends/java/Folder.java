
package org.luwrain.studio.backends.java;

import java.io.*;
import java.util.*;

import com.google.gson.annotations.SerializedName;

import org.luwrain.core.*;
import org.luwrain.studio.*;

final class Folder implements org.luwrain.studio.Part
{
    @SerializedName("name")
    private String name = "";

    @SerializedName("subfolders")
    private List<Folder> subfolders = null;

    @SerializedName("sourceFiles")
    private List<SourceFile> sourceFiles = null;

    private Project proj = null;

    void setProject(Project proj)
    {
	NullCheck.notNull(proj, "proj");
	this.proj = proj;
	if (name == null)
	    name = "-";
	if (subfolders == null)
	    subfolders = new LinkedList();
	if (sourceFiles == null)
	    sourceFiles = new LinkedList();
	for(Folder f: subfolders)
	    f.setProject(proj);
	for(SourceFile f: sourceFiles)
	    f.setProject(proj);
    }

    @Override public org.luwrain.studio.Editing startEditing()
    {
	return null;
    }

    @Override public org.luwrain.studio.Part[] getChildParts()
    {
	final List<Part> res = new LinkedList();
	for(Folder f: subfolders)
	    res.add(f);
	for(SourceFile f: sourceFiles)
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
	return o != null && (o instanceof Folder);
    }
}
