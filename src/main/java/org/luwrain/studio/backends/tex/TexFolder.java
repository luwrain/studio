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
import java.io.*;
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
    private transient IDE ide = null;

    void init(TexProject proj, IDE ide)
    {
	NullCheck.notNull(proj, "proj");
	NullCheck.notNull(ide, "ide");
	this.proj = proj;
	this.ide = ide;
	if (name == null)
	    name = "-";
	if (subfolders == null)
	    subfolders = new ArrayList<>();
			if (sourceFiles == null)
			    sourceFiles = new ArrayList<>();
	    for(TexFolder f: subfolders)
		f.init(proj, ide);
	    for(TexSourceFile f: sourceFiles)
		f.init(proj, ide);
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
		       action("Новый раздел", new InputEvent(InputEvent.Special.INSERT, EnumSet.of(InputEvent.Modifiers.CONTROL)), this::newSubfolder),
		       action("Добавить файл", new InputEvent(InputEvent.Special.INSERT), this::newSourceFile)
		       );
    }

    private boolean newSubfolder(IDE ide)
    {
	NullCheck.notNull(ide, "ide");
	final String name = Popups.textNotEmpty(ide.getLuwrainObj(), proj.getStrings().newFolderPopupName(), proj.getStrings().newFolderPopupPrefix(), "");
	if (name == null || name.trim().isEmpty())
	    return true;
	final TexFolder newFolder = new TexFolder();
	newFolder.setName(name.trim());
	newFolder.init(proj, ide);
	this.subfolders.add(newFolder);
	ide.onFoldersUpdate();
	return true;
    }

    private boolean newSourceFile(IDE ide)
    {
	NullCheck.notNull(ide, "ide");
	/*
	final String name = Popups.textNotEmpty(ide.getLuwrainObj(), proj.getStrings().newSourceFilePopupName(), proj.getStrings().newSourceFilePopupPrefix(), "");
	if (name == null || name.trim().isEmpty())
	    return true;
	final File file = Popups.existingFile(ide.getLuwrainObj(), "Новый файл");
	if (file == null)
	    return true;
	*/
	final String name = Popups.textNotEmpty(ide.getLuwrainObj(), "Новый файл", "Имя:", "");
	if (name == null)
	    return true;
	final String path = Popups.textNotEmpty(ide.getLuwrainObj(), "Новый файл", "Путь :", "");
	if (path == null)
	    return true;
	final TexSourceFile newFile = new TexSourceFile(name, path);
	newFile.init(proj, ide);
	this.sourceFiles.add(newFile);
	ide.onFoldersUpdate();
	return true;
    }
}

