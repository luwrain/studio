/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.studio.edit.java;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.studio.*;
import org.luwrain.util.*;
import org.luwrain.app.base.*;

final class Editing implements TextEditing
{
    private final File file;
    private final MutableMarkedLinesImpl content;

    Editing(File file) throws IOException
    {
	NullCheck.notNull(file, "files");
	this.file = file;
	final String text = FileUtils.readTextFileSingleString(file, "UTF-8");
	final String[] lines = FileUtils.universalLineSplitting(text);
	this.content = new MutableMarkedLinesImpl(lines);
    }

    @Override public EditArea.Params getEditParams(ControlContext context)
    {
	final EditArea.Params params = new EditArea.Params();
	params.context = context;
	params.content = content;
	params.appearance = new EditUtils.DefaultEditAreaAppearance(context);
	params.editFactory = (editParams)->{
	    return new MultilineEdit(editParams);
	};
	params.name = file.getName();
	return params;
    }

    @Override public boolean save() throws IOException
    {
	return false;
    }

        @Override public Part.Action[] getActions()
    {
	return new Part.Action[0];
    }

    @Override public void onNewHotPoint(int hotPointX, int hotPointY)
    {
    }

    @Override public void closeEditing()
    {
    }

    
    public boolean hasUnsavedChanges() { return true; }
    public void onModification() {}

}
