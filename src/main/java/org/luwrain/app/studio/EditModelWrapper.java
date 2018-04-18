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

package org.luwrain.app.studio ;

import org.luwrain.core.*;
import org.luwrain.controls.*;

final class EditModelWrapper implements MultilineEdit.Model
{
    private MultilineEdit.Model activatedModel = null;
    private MultilineEdit.Model wrappedModel = null;

    void setActivatedModel(MultilineEdit.Model model)
    {
	NullCheck.notNull(model, "model");
	this.activatedModel = model;
    }

    void deactivateModel()
    {
	this.activatedModel = null;
    }

    void setWrappedModel(MultilineEdit.Model model)
    {
	NullCheck.notNull(model, "model");
	this.wrappedModel = model;
    }

    MultilineEdit.Model getWrappedModel()
    {
	return wrappedModel;
    }

    @Override public int getLineCount()
    {
	if (activatedModel != null)
	    return activatedModel.getLineCount();
	return wrappedModel.getLineCount();
    }

    @Override public String getLine(int index)
    {
	if (activatedModel != null)
	    return activatedModel.getLine(index);
	return wrappedModel.getLine(index);
    }

    @Override public int getHotPointX()
    {
	if (activatedModel != null)
	    return activatedModel.getHotPointX();
	return wrappedModel.getHotPointX();
    }

    @Override public int getHotPointY()
    {
	if (activatedModel != null)
	    return activatedModel.getHotPointY();
	return wrappedModel.getHotPointY();
    }

    @Override public String getTabSeq()
    {
	if (activatedModel != null)
	    return activatedModel.getTabSeq();
	return wrappedModel.getTabSeq();
    }

    @Override public char deleteChar(int pos, int lineIndex)
    {
	if (activatedModel != null)
	    return activatedModel.deleteChar(pos, lineIndex);
	return wrappedModel.deleteChar(pos, lineIndex);
    }

    @Override public boolean deleteRegion(int fromX, int fromY, int toX, int toY)
    {
	if (activatedModel != null)
	    return activatedModel.deleteRegion(fromX, fromY, toX, toY);
	return wrappedModel.deleteRegion(fromX, fromY, toX, toY);
    }

    @Override public boolean insertRegion(int x, int y, String[] lines)
    {
	NullCheck.notNullItems(lines, "lines");
	if (activatedModel != null)
	    return activatedModel.insertRegion(x, y, lines);
	return wrappedModel.insertRegion(x, y, lines);
    }

    @Override public void insertChars(int pos, int lineIndex, String str)
    {
	NullCheck.notNull(str, "str");
	if (activatedModel != null)
	    activatedModel.insertChars(pos, lineIndex, str); else
	    wrappedModel.insertChars(pos, lineIndex, str);
    }

    @Override public void mergeLines(int firstLineIndex)
    {
	if (activatedModel != null)
	    activatedModel.mergeLines(firstLineIndex); else
	    wrappedModel.mergeLines(firstLineIndex);
    }

    @Override public String splitLines(int pos, int lineIndex)
    {
	if (activatedModel != null)
	    return activatedModel.splitLines(pos, lineIndex);
	return wrappedModel.splitLines(pos, lineIndex);
    }
}
