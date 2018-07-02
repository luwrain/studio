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

import org.luwrain.core.*;

final class CodePronunciation
{
    private final Luwrain luwrain;
    private final Strings strings;

    CodePronunciation(Luwrain luwrain, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.strings = strings;
    }

    void announceLine(String line)
    {
	if (line == null || line.isEmpty())
	{
	    luwrain.setEventResponse(DefaultEventResponse.hint(Hint.EMPTY_LINE));
	    return;
	}
	final int indentLen = getIndent(line);
	final String indent = (indentLen > 0)?strings.codeIndent("" + indentLen):"";
	if (line.trim().startsWith("//"))
	{
	    luwrain.say(indent + " " + strings.codeComments() + " " + line.trim().substring(2));
	    return;
	}
	if (line.trim().equals("{"))
	{
	    luwrain.say(indent + " " + strings.codeBlockBegin());
	    return;
	}
	if (line.trim().equals("}"))
	{
	    luwrain.say(indent + " " + strings.codeBlockEnd());
	    return;
	}
	luwrain.say(indent + " " + luwrain.getSpokenText(line.trim(), Luwrain.SpokenTextType.PROGRAMMING));
    }

    private int getIndent(String line)
    {
	NullCheck.notNull(line, "line");
	int res = 0;
	for(int i = 0;i < line.length();++i)
	    switch(line.charAt(i))
	    {
	    case ' ':
		++res;
		break;
	    case '\t':
		res += 8;
		break;
	    default:
		return res;
	    }
	return res;
    }
}
