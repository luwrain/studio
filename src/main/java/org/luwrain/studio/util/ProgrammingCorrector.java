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

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.MultilineEdit.ModificationResult;

public class ProgrammingCorrector extends EditUtils.EmptyCorrector
{
    public ProgrammingCorrector(MultilineEditCorrector basicCorrector)
    {
	super(basicCorrector);
    }

    public int getIndent(int lineIndex)
    {
	final int tabLen = 4;
	final String line = getLine(lineIndex);
	int res = 0;
	for(int i = 0;i < line.length() && Character.isWhitespace(line.charAt(i));i++)
	    if (line.charAt(i) == '\t')
		res += tabLen; else
		++res;
	return res;
    }

    public boolean deleteIndent(int lineIndex)
    {
	final String line = getLine(lineIndex);
	int pos = 0;
	while (pos < line.length() && Character.isWhitespace(line.charAt(pos)))
	    pos++;
	if (pos == 0)
	    return true;
	return basicCorrector.deleteRegion(0, lineIndex, pos, lineIndex).isPerformed();
    }

    public boolean addIndent(int lineIndex, int len)
    {
	if (len == 0)
	    return true;
	final int tabLen = 4;
	final StringBuilder b = new StringBuilder();
	final int tabCount = len / tabLen;
	for(int i = 0;i < tabCount;i++)
	    b.append('\t');
	final int spaceCount = len % tabLen;
	for(int i = 0;i < spaceCount;i++)
	    b.append(' ');
	return basicCorrector.putChars(0, lineIndex, new String(b)).isPerformed();
    }
}
