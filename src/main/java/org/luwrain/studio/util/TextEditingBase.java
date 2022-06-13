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

package org.luwrain.studio.util;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.MultilineEdit.ModificationResult;
import org.luwrain.studio.*;
import org.luwrain.util.*;
import org.luwrain.script.core.*;
import org.luwrain.script.controls.*;
import org.luwrain.app.base.*;

import static org.luwrain.script.Hooks.*;

public abstract class TextEditingBase implements TextEditing
{
    static public final String CHARSET = "UTF-8";

    protected final IDE ide;
    protected final File file;
    protected final MutableMarkedLinesImpl content;
    private MultilineEdit edit = null;
    private MultilineEditCorrector bottomCorrector = null;
    private int hotPointX = 0, hotPointY = 0;

    public TextEditingBase(IDE ide, File file) throws IOException
    {
	NullCheck.notNull(ide, "ide");
	NullCheck.notNull(file, "files");
	this.ide = ide;
	this.file = file;
	final String text = FileUtils.readTextFileSingleString(file, CHARSET);
	final String[] lines = FileUtils.universalLineSplitting(text);
	this.content = new MutableMarkedLinesImpl(lines);
    }

    @Override public boolean save() throws IOException
    {
	FileUtils.writeTextFileMultipleStrings(file, content.getLines(), CHARSET, System.lineSeparator());
	return true;
    }

    @Override public Part.Action[] getActions()
    {
	return new Part.Action[0];
    }

    @Override public void closeEditing()
    {
    }

    @Override public void onNewHotPoint(int hotPointX, int hotPointY)
    {
	this.hotPointX = hotPointX;
	this.hotPointY = hotPointY;
    }

    protected void setEdit(MultilineEdit edit, MultilineEditCorrector bottomCorrector)
    {
	NullCheck.notNull(edit, "edit");
	NullCheck.notNull(bottomCorrector, "bottomCorrector");
	this.edit = edit;
	this.bottomCorrector = bottomCorrector;
    }

    protected MultilineEdit getEdit()
    {
	if (this.edit == null)
	    throw new IllegalStateException("The edit object isn't created");
	return this.edit;
    }

    protected MultilineEditCorrector getBottomCorrector()
    {
	if (this.bottomCorrector == null)
	    throw new IllegalStateException("The bottom corrector object isn't created");
	return this.bottomCorrector;
    }

    protected String[] getRegion()
    {
	if (edit == null)
	    return null;
	return edit.getRegionText();
    }

    protected int getHotPointX()
    {
	return this.hotPointX;
    }

    protected int getHotPointY()
    {
	return this.hotPointY;
    }

    protected boolean insertText(String[] text)
    {
	NullCheck.notNullItems(text, "text");
	if (text.length == 0)
	    return true;
final ModificationResult res = getBottomCorrector().insertRegion(getHotPointX(), getHotPointY(), text);
return res.isPerformed();
    }

    protected boolean insertText(String text)
    {
	NullCheck.notNull(text, "text");
	if (text.isEmpty())
	    return true;
	return insertText(new String[]{text});
    }

        protected EditArea.InputEventListener createEditAreaInputEventHook()
    {
	return (edit, event)->{
	    final MultilineEditCorrector corrector = (MultilineEditCorrector)edit.getEdit().getMultilineEditModel();
	    final AtomicBoolean res = new AtomicBoolean(false);
	    corrector.doEditAction((lines, hotPoint)->{
		    res.set(chainOfResponsibility(ide.getLuwrainObj(), EDIT_INPUT, new Object[]{
				new EditAreaObj(edit, lines),
				new InputEventObj(event)
			    }));
		});
	    return res.get();
	};
    }

}
