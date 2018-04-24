/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.app.studio;

import java.util.*;
import java.io.*;

import org.luwrain.base.*;
import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.util.*;

final class Base
{
    static private final String CHARSET = "UTF-8";

    private final Luwrain luwrain;
    private final Strings strings;
    final Settings sett;
    private final String treeRoot;

    private Project project = null;

    final MutableLinesImpl fileText = new MutableLinesImpl();
    final EditCorrectorWrapper editCorrectorWrapper = new EditCorrectorWrapper();
    SourceFile.Editing openedEditing = null;

    Base (Luwrain luwrain, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.strings = strings;
	this.sett = Settings.create(luwrain.getRegistry());
	this.treeRoot = strings.treeRoot();
    }

    void activateProject(Project proj)
    {
	NullCheck.notNull(proj, "proj");
	this.project = proj;
    }

    void startEditing(SourceFile.Editing editing) throws IOException
    {
	NullCheck.notNull(editing, "editing");
	final File file = editing.getFile();
	NullCheck.notNull(file, "file");
	final String[] lines = FileUtils.readTextFileMultipleStrings(file, CHARSET, null);//null means using the default line separator
	fileText.setLines(lines);
	this.openedEditing = editing;
    }

    CachedTreeModelSource getTreeModel()
    {
	return new TreeModel();
    }

    private class TreeModel implements CachedTreeModelSource
    {
	@Override public Object getRoot()
	{
	    return treeRoot;
	}
	@Override public Object[] getChildObjs(Object obj)
	{
	    NullCheck.notNull(obj, "obj");
	    if (project == null)
		return new Object[0];
	    final Folder folder;
	    if (obj == treeRoot)
		folder = project.getFoldersRoot(); else
		if (obj instanceof Folder)
		    folder = (Folder)obj; else
		    return new Object[0];
	    if (folder == null)
		return new Object[0];
	    final List res = new LinkedList();
	    for(Folder f: folder.getSubfolders())
		res.add(f);
	    for(SourceFile f: folder.getSourceFiles())
		res.add(f);
	    return res.toArray(new Object[res.size()]);
	}
    }
}
