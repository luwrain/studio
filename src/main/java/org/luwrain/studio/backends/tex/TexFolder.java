/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.studio.backends.tex;

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.studio.*;
import org.luwrain.popups.*;

import static org.luwrain.studio.Part.*;

public final class TexFolder implements Part
{
        @SerializedName("name")
    private String name = "";

    @SerializedName("subfolders")
    private List<TexFolder> subfolders = null;

        @SerializedName("sourceFiles")
    private List<TexSourceFile> sourceFiles = null;

    private transient TexProject proj = null;

    void init(TexProject proj)
    {
	NullCheck.notNull(proj, "proj");
	this.proj = proj;
	if (name == null)
	    name = "-";
	if (subfolders == null)
	    subfolders = new ArrayList<>();
			if (sourceFiles == null)
			    sourceFiles = new ArrayList<>();
	    for(TexFolder f: subfolders)
		f.init(proj);
	    for(TexSourceFile f: sourceFiles)
		f.setProject(proj);
    }

    @Override public Part [] getChildParts()
    {
	final List<Part> res = new ArrayList<>();
	if (subfolders != null)
	    for(Part p: subfolders)
		res.add(p);
	if (sourceFiles != null)
	    for(Part p: sourceFiles)
		res.add(p);
	return res.toArray(new Part[res.size()]);
    }

    @Override public Editing startEditing()
    {
	return null;
    }

    @Override public String getTitle()
    {
	return name;
    }

    @Override public String toString()
    {
	return name;
    }

    public String getName()
    {
	return this.name;
    }

    public void setName(String name)
    {
	NullCheck.notEmpty(name, "name");
	this.name = name;
    }

    public List<TexFolder> getSubfolders()
    {
	return this.subfolders != null?this.subfolders:new ArrayList<>();
    }

public void setSubfolders(List<TexFolder> subfolders)
{
    NullCheck.notNull(subfolders, "subfolders");
    this.subfolders = subfolders;
}

    public List<TexSourceFile> getSourceFiles()
    {
	return this.sourceFiles != null?this.sourceFiles:new ArrayList<>();
    }

    public void setSourceFiles(List<TexSourceFile> sourceFiles)
    {
	NullCheck.notNull(sourceFiles, "sourceFiles");
	this.sourceFiles = sourceFiles;
    }

                @Override public org.luwrain.studio.Part.Action[] getActions()
    {
	return actions(
		       action("Создать новый раздел", new InputEvent(InputEvent.Special.INSERT), (ide)->newSubfolder(ide))
		       );
    }

    private boolean newSubfolder(IDE ide)
    {
	NullCheck.notNull(ide, "ide");
	Log.debug("proba", "creating");
	final String name = Popups.textNotEmpty(ide.getLuwrainObj(), proj.getStrings().newFolderPopupName(), proj.getStrings().newFolderPopupPrefix(), "");
	if (name == null || name.trim().isEmpty())
	    return true;
	final TexFolder newFolder = new TexFolder();
	newFolder.setName(name.trim());
	newFolder.init(proj);
	this.subfolders.add(newFolder);
	ide.onFoldersUpdate();
	return true;
    }

}
