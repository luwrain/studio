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

package org.luwrain.studio.backends.ly;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.studio.util.*;
import org.luwrain.app.base.*;

final class LyEditing extends TextEditingBase
{
    LyEditing(File file) throws IOException
    {
	super(file);
    }

    @Override public EditArea.Params getEditParams(ControlContext context)
    {
	NullCheck.notNull(context, "context");
	final EditArea.Params params = new EditArea.Params();
	params.context = context;
	params.content = content;
	params.appearance = new EditUtils.DefaultEditAreaAppearance(context);
	params.editFactory = (editParams)->{
	    setEdit(new MultilineEdit(editParams), (MultilineEditCorrector)editParams.model);
	    return getEdit();
	};
	params.name = file.getName();
	return params;
    }
}
