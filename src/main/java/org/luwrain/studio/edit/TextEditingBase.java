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

package org.luwrain.studio.edit;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.controls.edit.MultilineEdit.ModificationResult;
import org.luwrain.studio.*;
import static org.luwrain.util.FileUtils.*;
import org.luwrain.script.core.*;
import org.luwrain.script.controls.*;
import org.luwrain.studio.syntax.*;

import static org.luwrain.script.Hooks.*;
import static org.luwrain.core.NullCheck.*;

public abstract class TextEditingBase implements TextEditing, HotPoint
{
    static private final Logger log = LogManager.getLogger();
    static public final String CHARSET = "UTF-8";

    protected final IDE ide;
    protected final File file;
    private int
	hotPointX = 0,
	hotPointY = 0;
    protected Source source;
    private MultilineEdit edit = null;
    private MultilineEditCorrector corrector = null;

    protected TextEditingBase(IDE ide, File file, int hotPointX, int hotPointY)
    {
	notNull(ide, "ide");
	notNull(file, "files");
	this.ide = ide;
	this.file = file;
	this.hotPointX = hotPointX;
	this.hotPointY = hotPointY;
    }

    protected TextEditingBase(IDE ide, File file)
    {
	this(ide, file, 0, 0);
    }

        public abstract MutableMarkedLines getContent();
    public abstract AtomicBoolean getModified();

    protected void load() throws IOException
    {
		if (!file.exists())
	{
	    log.info("The file doesn't exist, creating it on starting the edit: " + file.getAbsolutePath());
	    writeTextFileSingleString(file, "", CHARSET);
	}
	final String text = readTextFileSingleString(file, CHARSET);
	this.source = new Source(text);
	getContent().setLines(source.getLines());
    }



    @Override public boolean save() throws IOException
    {
	if (!getModified().get())
	    return false;
	writeTextFileMultipleStrings(file, getContent().getLines(), CHARSET, System.lineSeparator());
	getModified().set(false);
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

    @Override public AtomicBoolean getModifiedFlag()
    {
	return getModified();
    }

    @Override public boolean hasSameSource(Editing editing)
    {
	return false;
    }

    protected void setEdit(MultilineEdit edit, MultilineEditCorrector corrector)
    {
	notNull(edit, "edit");
	notNull(corrector, "corrector");
	this.edit = edit;
	this.corrector = corrector;
    }

    protected MultilineEdit getEdit()
    {
	if (this.edit == null)
	    throw new IllegalStateException("The edit object isn't created");
	return this.edit;
    }

    protected MultilineEditCorrector getCorrector()
    {
	if (this.corrector == null)
	    throw new IllegalStateException("The corrector object isn't created");
	return this.corrector;
    }

    protected String[] getRegion()
    {
	if (edit == null)
	    return null;
	return edit.getRegionText();
    }

    @Override public int getHotPointX() { return this.hotPointX; }
    @Override public int getHotPointY() { return this.hotPointY; }
    public Source getSourceCode() { return new Source(getContent()); }

    protected boolean insertText(String[] text)
    {
	notNullItems(text, "text");
	if (text.length == 0)
	    return true;
final ModificationResult res = getCorrector().insertRegion(getHotPointX(), getHotPointY(), text);
return res.isPerformed();
    }

    protected boolean insertText(String text)
    {
	notNull(text, "text");
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
		    res.set(chainOfResponsibilityNoExc(ide.getLuwrainObj(), EDIT_INPUT, new Object[]{
				new EditAreaObj(edit, lines),
				new InputEventObj(event)
			    }));
		});
	    return res.get();
	};
    }

    protected void replaceStr(String replaceExp, String replaceWith)
    {
	getContent().update((lines)->{
		for(int i = 0;i < lines.getLineCount();i++)
		    lines.setLine(i, lines.getLine(i).replaceAll(replaceExp, replaceWith));
	    });
	getModified().set(true);
    }

    protected MultilineEdit.Appearance getAppearance()
    {
	if (edit == null)
	    throw new IllegalStateException("No edit to get appearance");
	return edit.getMultilineEditAppearance();
    }
}
