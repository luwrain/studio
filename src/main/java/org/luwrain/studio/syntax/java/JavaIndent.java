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

package org.luwrain.studio.syntax.java;

import java.util.*;


import org.luwrain.studio.syntax.*;
import org.luwrain.studio.syntax.SpanTree.*;

import static org.luwrain.core.NullCheck.*;

public final class JavaIndent
{
    final Source source;
    final SpanTree spanTree;
    final SyntaxParams params;
    final IndentUtils utils;
    public JavaIndent(Source source, SpanTree spanTree, SyntaxParams params)
    {
	notNull(source, "source");
	notNull(spanTree, "spanTree");
	notNull(params, "params");
	this.source = source;
	this.spanTree = spanTree;
	this.params = params;
	this.utils = new IndentUtils(params);
    }

    public int getIndentForLine(int lineIndex)
    {
	if (lineIndex > source.getLineCount())
	    throw new IllegalArgumentException("lineIndex can't be greater than number of lines");
	System.out.println("proba " + source.getLine(lineIndex));
	final int lineStart = source.getLineStart(lineIndex);
	final Span[] spans = spanTree.findAtPoint(lineStart);
	final Span span;
	if (spans.length > 0)
	{
	    //There is at least one block span
	    final Span lastSpan = spans[spans.length - 1];
	    	    //Checking if we have a block span at the beginning of the line
	    if (lineStart == lastSpan.getFromPos())
		span = (spans.length >= 2)?spans[spans.length - 2]:null; else
		span = lastSpan;
	} else
	    span = null;
	final int spanStart = (span != null)?span.getFromPos():0;
	System.out.println(lineStart + " " + spanStart);
	System.out.println(source.getText().charAt(spanStart));
	final int spanStartLine = source.getLineWithPos(spanStart);
	System.out.println("spanStartLine=" + spanStartLine);
	if (span != null && isEmptyFragment(spanStart + 1, lineStart))//+ 1 to skip the left block brace itself
	{
	    System.out.println("nothing between");
	    //Analyzing the line with the span start
	    final String line = source.getLine(spanStartLine);
	    System.out.println(line);
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
