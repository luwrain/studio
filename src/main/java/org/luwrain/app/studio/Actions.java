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

import java.io.*;
import java.util.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.util.*;

final class Actions
{
    private final Luwrain luwrain;
    private final Base base;
    private final Strings strings;

    final Conversations conv;

    Actions(Luwrain luwrain, Base base, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(base, "base");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.base = base;
	this.strings = strings;
	this.conv = new Conversations(luwrain, strings);
    }

    boolean onOpenProject(TreeArea treeArea)
    {
	NullCheck.notNull(treeArea, "treeArea");
	final File projFile = conv.openProject();
	if (projFile == null)
	    return true;
	final Project proj;
	try {
	    proj = ProjectFactory.load(projFile);
	}
	catch(IOException e)
	{
	    luwrain.message(luwrain.i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
	    return true;
	}
	base.activateProject(proj);
	treeArea.refresh();
	return true;
    }

    boolean onRun(NavigationArea outputArea)
    {
	NullCheck.notNull(outputArea, "outputArea");
	if (base.openedEditing != null)
	{
	    try {
		FileUtils.writeTextFileMultipleStrings(base.openedEditing.getFile(), base.fileText.getLines(), Base.CHARSET, null);
	    }
	    catch(IOException e)
	    {
		luwrain.message(luwrain.i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
		return true;
	    }
	}
	try {
	    return base.runProject(()->outputArea.redraw());
	}
	catch(IOException e)
	{
	    luwrain.message(luwrain.i18n().getExceptionDescr(e));
	    return true;
	}
    }

    boolean onSaveEdit()
    {
	if (base.openedEditing == null)
	    return false;
	try {
	FileUtils.writeTextFileMultipleStrings(base.openedEditing.getFile(), base.fileText.getLines(), Base.CHARSET, null);
	luwrain.message(strings.fileSavedSuccessfully(), Luwrain.MessageType.OK);	    
	}
	catch(IOException e)
	{
	    luwrain.message(luwrain.i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
	}
	return true;
    }

    boolean onOutputClick(int lineIndex, EditArea editArea)
    {
	final Base.PositionInfo posInfo = base.getCompilationOutputPositionInfo(lineIndex);
	if (posInfo == null)
	    return false;
	//FIXME:ensure the corresponding file is opened
	editArea.setHotPoint(posInfo.colNum > 0?posInfo.colNum:0, posInfo.lineNum - 1);
	luwrain.setActiveArea(editArea);
	return true;
    }
}
