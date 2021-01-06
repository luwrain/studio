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

package org.luwrain.studio.backends;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.util.*;
import org.luwrain.app.base.*;

public abstract class TextEditingBase implements TextEditing
{
    static public final String CHARSET = "UTF-8";

    protected final File file;
    protected final MutableLinesImpl content;
    protected MultilineEdit edit = null;

    public TextEditingBase(File file) throws IOException
    {
	NullCheck.notNull(file, "files");
	this.file = file;
	final String text = FileUtils.readTextFileSingleString(file, CHARSET);
	final String[] lines = FileUtils.universalLineSplitting(text);
	this.content = new MutableLinesImpl(lines);
    }

        @Override public boolean save() throws IOException
    {
	FileUtils.writeTextFileMultipleStrings(file, content.getLines(), CHARSET, System.lineSeparator());
	return true;
    }

            @Override public LayoutBase.Actions getActions()
    {
	return new LayoutBase.Actions();
    }

    @Override public void closeEditing()
    {
    }

    @Override public void onNewHotPoint()
    {
    }

    protected String[] getRegion()
    {
	if (edit == null)
	    return null;
	return edit.getRegionText();
    }
}
