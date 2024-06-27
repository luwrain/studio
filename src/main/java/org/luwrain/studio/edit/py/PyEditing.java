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

package org.luwrain.studio.edit.py;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.studio.*;
import org.luwrain.studio.util.*;
import org.luwrain.util.*;
import org.luwrain.app.base.*;
import org.luwrain.studio.edit.*;

final class PyEditing extends TextEditingBase
{
    PyEditing(IDE ide, File file) throws IOException
    {
	super(ide, file);
    }

    @Override public EditArea.Params getEditParams(ControlContext context)
    {
	NullCheck.notNull(context, "context");
	final EditArea.Params params = new EditArea.Params();
	params.context = context;
	params.content = getContent();
	params.appearance = new EditUtils.DefaultEditAreaAppearance(context);
	params.editFactory = (editParams)->{
	    return new MultilineEdit(editParams);
	};
	params.name = file.getName();
	return params;
    }

    @Override public MutableMarkedLines getContent()
    {
	return null;
    }

    @Override public AtomicBoolean getModified()
    {
	return null;
    }

        @Override public boolean save() throws IOException
    {
	return false;
    }

    @Override public void onNewHotPoint(int hotPointX, int hotPointY)
    {
    }

    @Override public void closeEditing()
    {
    }
}
