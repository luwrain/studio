
package org.luwrain.studio.backends.tex;

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.*;

import org.luwrain.core.*;
import org.luwrain.studio.*;

final class TexFolder implements Folder
{
        @SerializedName("name")
    private String name = "";

    @SerializedName("subfolders")
    private List<TexFolder> subfolders = null;

    private TexProject proj = null;

    void setProject(TexProject proj)
    {
	NullCheck.notNull(proj, "proj");
	this.proj = proj;
	if (subfolders != null)
	    for(TexFolder f: subfolders)
		f.setProject(proj);
		if (sourceFiles != null)
	    for(TexSourceFile f: sourceFiles)
		f.setProject(proj);
    }

        @SerializedName("files")
    private List<TexSourceFile> sourceFiles = null;

    @Override public Folder[] getSubfolders()
    {
	if (subfolders == null)
	    return new Folder[0];
	return subfolders.toArray(new Folder[subfolders.size()]);
    }

    @Override public SourceFile[] getSourceFiles()
    {
	if (sourceFiles == null)
	return new SourceFile[0];
	return sourceFiles.toArray(new SourceFile[sourceFiles.size()]);
    }

    @Override public String toString()
    {
	return name;
    }
}


