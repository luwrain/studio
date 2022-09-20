/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.studio.edit.tex;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.nlp.*;

import static org.luwrain.script.Hooks.*;
import static org.luwrain.core.DefaultEventResponse.*;

final class TexAppearance extends EditUtils.DefaultEditAreaAppearance
{
    final MarkedLines content;
    boolean indent = false;
    TexAppearance(ControlContext context, MarkedLines content)
    {
	super(context);
	NullCheck.notNull(content, "content");
	this.content = content;
    }

    @Override public void announceLine(int index, String line)
    {
	if (line.trim().isEmpty())
	{
	    context.setEventResponse(hint(line.isEmpty()?Hint.EMPTY_LINE:Hint.SPACES));
	    return;
	}
	boolean hasSpellProblems = false;
	if (content.getLineMarks(index) != null)
	{
	    final LineMarks.Mark[] marks = content.getLineMarks(index).getMarks();
	    for(LineMarks.Mark m: marks)
		if (m.getMarkObject() != null && m.getMarkObject() instanceof SpellProblem)
		{
		    hasSpellProblems = true;
		    break;
		}
	}
	final StringBuilder b = new StringBuilder();
	if (indent)
	{
	    final int indentLen = getIndentLen(line);
	    if (indentLen > 0)
		b.append("Отступ ").append(String.valueOf(indentLen)).append(" ");
	}
	b.append(context.getSpeakableText(line, Luwrain.SpeakableTextType.PROGRAMMING));
	if (hasSpellProblems)
	    context.setEventResponse(text(Sounds.SPELLING, new String(b))); else
	    context.setEventResponse(text(new String(b)));
    }

    private int getIndentLen(String line)
    {
	//FIXME: Utilities
	int res = 0;
	for(int i = 0;i < line.length();i++)
	    if (Character.isWhitespace(line.charAt(i)))
		res++; else
		break;
	return res;
    }
}
