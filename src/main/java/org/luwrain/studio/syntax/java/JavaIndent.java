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

package org.luwrain.studio.syntax.java;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.studio.syntax.*;
import org.luwrain.studio.syntax.SpanTree.*;

public final class JavaIndent
{
    final Source source;
    final SpanTree spanTree;
    final SyntaxParams params;
    final IndentUtils utils;
    public JavaIndent(Source source, SpanTree spanTree, SyntaxParams params)
    {
	NullCheck.notNull(source, "source");
	NullCheck.notNull(spanTree, "spanTree");
	NullCheck.notNull(params, "params");
	this.source = source;
	this.spanTree = spanTree;
	this.params = params;
	this.utils = new IndentUtils(params);
    }

    public int getIndentForLine(int lineIndex)
    {
	if (lineIndex > source.getLineCount())
	    throw new IllegalArgumentException("lineIndex can't be greater than number of lines");
	final int lineStart = source.getLineStart(lineIndex);
	final Span[] spans = spanTree.findAtPoint(lineStart);
	final Span span = (spans.length > 0)?spans[spans.length - 1]:null;
	final int spanStart = (span != null)?span.getFromPos():0;
	final int spanStartLine = source.getLineWithPos(spanStart);
	if (span != null && isEmptyFragment(spanStart, lineStart))
	{
	    //Analyzing the line with the span start
	    final String line = source.getLine(spanStartLine);
	    return utils.getIndent(line) + params.getIndentStep();
	} else
	{
	    //Looking for the first previous non-empty line
	    int baseIndex = lineIndex - 1;
	    while(baseIndex >= 0 && source.getLine(baseIndex).trim().isEmpty())
		baseIndex--;
	    if (baseIndex < 0)
		return 0;
	    return utils.getIndent(source.getLine(baseIndex));
	}
    }

    private boolean isEmptyFragment(int fromPos, int toPos)
    {
	final String text = source.getText();
	for(int i = fromPos;i < toPos;i++)
	    if (!Character.isWhitespace(text.charAt(i)))
		return false;
	return true;
    }
}
