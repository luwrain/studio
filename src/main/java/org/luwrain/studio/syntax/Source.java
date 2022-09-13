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

package org.luwrain.studio.syntax;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;

public class Source
{
    static private final char NL = '\n';

    private String content = "";
    private String[] lines = new String[0];

    public Source(String content)
    {
	NullCheck.notNull(content, "content");
	if (!content.isEmpty())
	{
	    this.content = content.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
	    this.lines = this.content.split("\n", -1);
	}
    }

    public int getLineStart(int lineIndex)
    {
	if (lineIndex < 0)
	    throw new IllegalArgumentException("lineIndex can't be negative");
	if (lineIndex > lines.length)
	    throw new IllegalArgumentException("lineIndex can't be greater than number of lines");
	int res = 0;
	for(int i = 0;i < lineIndex;i++)
	    res += (lines[i].length() + 1);
	return res;
    }

    public int getLineWithPos(int pos)
    {
	if (pos < 0)
	    throw new IllegalArgumentException("pos can't be negative");
	int res = 0;
	for(int i = 0;i < lines.length;i++)
	{
	    if (pos <= res + lines[i].length())//equals to cover trailing \n
		return i;
	    res += (lines[i].length() + 1);
	}
	return -1;
	    }

    public int getLineCount()
    {
	return lines.length;
    }

    public String getLine(int index)
    {
	return lines[index];
    }

    public String getText()
    {
	return content;
    }

    public int length()
    {
	return content.length();
    }
}
