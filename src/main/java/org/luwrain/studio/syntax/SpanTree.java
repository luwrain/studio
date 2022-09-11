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

import static org.luwrain.util.RangeUtils.*;

public final class SpanTree
{
    final Span root = new Span();
    final List<Span> stack = new ArrayList<>();

    public SpanTree()
    {
	stack.add(root);
    }

    public Span addSpan()
    {
	final Span newSpan = new Span();
	getLastInStack().children.add(newSpan);
	stack.add(newSpan);
	return newSpan;
    }

    public void pop()
    {
	if (stack.size() == 1)
	    throw new IllegalStateException("The root span can't be popped");
	stack.remove(stack.size() - 1);
    }

    public Span[] findAtPoint(int pos)
    {
	final List<Span> res = new ArrayList<>();
	findAtPoint(pos, root, res);
	return res.toArray(new Span[res.size()]);
    }

    private void findAtPoint(int pos, Span span, List<Span> res)
    {
	for(Span s: span.children)
	    if (between(pos, s.fromPos, s.toPos))
	    {
		res.add(s);
		findAtPoint(pos, s, res);
	    }
    }

    public Span getCurrentSpan()
    {
	return getLastInStack();
    }

    private Span getLastInStack()
    {
	return stack.get(stack.size() - 1);
    }

    static public final class Span
    {
	List<Span> children = new ArrayList<>();
	int fromPos = 0, toPos = 0;
	public int getFromPos() { return fromPos; }
	public int getToPos() { return toPos; }
	public void setFromPos(int fromPos)
	{
	    if (fromPos < 0)
		throw new IllegalArgumentException("fromPos can't be negative");
	    this.fromPos = fromPos;
	}
		public void setToPos(int toPos)
	{
	    if (toPos < 0)
		throw new IllegalArgumentException("toPos can't be negative");
	    this.toPos = toPos;
	}
    }
}
