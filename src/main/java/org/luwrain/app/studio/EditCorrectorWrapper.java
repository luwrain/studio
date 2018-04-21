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

final class EditCorrectorWrapper implements MultilineEditCorrector
{
    private MultilineEditCorrector activatedCorrector = null;
    private MultilineEditCorrector wrappedCorrector = null;

    void setActivatedCorrector(MultilineEditCorrector corrector)
    {
	NullCheck.notNull(corrector, "corrector");
	this.activatedCorrector = corrector;
    }

    void deactivateCorrector()
    {
	this.activatedCorrector = null;
    }

    void setWrappedCorrector(MultilineEditCorrector corrector)
    {
	NullCheck.notNull(corrector, "corrector");
	this.wrappedCorrector = corrector;
    }

    MultilineEditCorrector getWrappedCorrector()
    {
	return wrappedCorrector;
    }

    @Override public int getLineCount()
    {
	if (activatedCorrector != null)
	    return activatedCorrector.getLineCount();
	return wrappedCorrector.getLineCount();
    }

    @Override public String getLine(int index)
    {
	if (activatedCorrector != null)
	    return activatedCorrector.getLine(index);
	return wrappedCorrector.getLine(index);
    }

    @Override public int getHotPointX()
    {
	if (activatedCorrector != null)
	    return activatedCorrector.getHotPointX();
	return wrappedCorrector.getHotPointX();
    }

    @Override public int getHotPointY()
    {
	if (activatedCorrector != null)
	    return activatedCorrector.getHotPointY();
	return wrappedCorrector.getHotPointY();
    }

    @Override public String getTabSeq()
    {
	if (activatedCorrector != null)
	    return activatedCorrector.getTabSeq();
	return wrappedCorrector.getTabSeq();
    }

    @Override public char deleteChar(int pos, int lineIndex)
    {
	if (activatedCorrector != null)
	    return activatedCorrector.deleteChar(pos, lineIndex);
	return wrappedCorrector.deleteChar(pos, lineIndex);
    }

    @Override public boolean deleteRegion(int fromX, int fromY, int toX, int toY)
    {
	if (activatedCorrector != null)
	    return activatedCorrector.deleteRegion(fromX, fromY, toX, toY);
	return wrappedCorrector.deleteRegion(fromX, fromY, toX, toY);
    }

    @Override public boolean insertRegion(int x, int y, String[] lines)
    {
	NullCheck.notNullItems(lines, "lines");
	if (activatedCorrector != null)
	    return activatedCorrector.insertRegion(x, y, lines);
	return wrappedCorrector.insertRegion(x, y, lines);
    }

    @Override public void insertChars(int pos, int lineIndex, String str)
    {
	NullCheck.notNull(str, "str");
	if (activatedCorrector != null)
	    activatedCorrector.insertChars(pos, lineIndex, str); else
	    wrappedCorrector.insertChars(pos, lineIndex, str);
    }

    @Override public void mergeLines(int firstLineIndex)
    {
	if (activatedCorrector != null)
	    activatedCorrector.mergeLines(firstLineIndex); else
	    wrappedCorrector.mergeLines(firstLineIndex);
    }

    @Override public String splitLines(int pos, int lineIndex)
    {
	if (activatedCorrector != null)
	    return activatedCorrector.splitLines(pos, lineIndex);
	return wrappedCorrector.splitLines(pos, lineIndex);
    }

    @Override public void doDirectAccessAction(DirectAccessAction action)
    {
	if (activatedCorrector != null)
	    activatedCorrector.doDirectAccessAction(action); else
	    wrappedCorrector.doDirectAccessAction(action);
    }
}
