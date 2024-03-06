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

package org.luwrain.studio.indents.js;

import java.util.*;
import static java.util.Arrays.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest
{
    @Test public void singleLineFuncBrief()
    {
	final var h = new Handler();
	new Parser(h, asList(
			     "function foo(){",
			     "  bar();",
			     "}")).parse();
	assertEquals(0, h.getCalculatedIndent(1));
	assertEquals(4, h.getCalculatedIndent(2));
	assertEquals(0, h.getCalculatedIndent(3));
    }

        @Test public void singleLineFunc()
    {
	final var h = new Handler();
	new Parser(h, asList(
			     "function foo()",
			     "{",
			     "  bar();",
			     "}")).parse();
	assertEquals(0, h.getCalculatedIndent(1));
		assertEquals(0, h.getCalculatedIndent(2));
	assertEquals(4, h.getCalculatedIndent(3));
	assertEquals(0, h.getCalculatedIndent(4));
    }

        @Test public void singleLineFuncIndentBrief()
    {
	final var h = new Handler();
	new Parser(h, asList(
			     "  function foo(){",
			     "    bar();",
			     "  }")).parse();
		assertEquals(0, h.getCalculatedIndent(1));
	assertEquals(6, h.getCalculatedIndent(2));
	assertEquals(2, h.getCalculatedIndent(3));
    }

            @Test public void singleLineFuncIndent()
    {
	final var h = new Handler();
	new Parser(h, asList(
			     "  function foo()",
			     "{",
			     "    bar();",
			     "  }")).parse();
		assertEquals(0, h.getCalculatedIndent(1));
				assertEquals(2, h.getCalculatedIndent(2));
	assertEquals(6, h.getCalculatedIndent(3));
	assertEquals(2, h.getCalculatedIndent(4));
    }

    @Test public void singleLineIf()
    {
	final var h = new Handler();
	new Parser(h, asList(
			     "function foo(){",
			     "  if (check())",
			     "  bar();",
			     "}")).parse();
	assertEquals(0, h.getCalculatedIndent(1));
	assertEquals(4, h.getCalculatedIndent(2));
	assertEquals(6, h.getCalculatedIndent(3));
	assertEquals(0, h.getCalculatedIndent(4));
    }

@Test public void forBlock()
    {
	final var h = new Handler();
	new Parser(h, asList(
			     "function foo(){",
			     "  for (let i of v)",
			     "  {",
			     "    bar();",
			     "  }",
			     "}")).parse();
	assertEquals(0, h.getCalculatedIndent(1));
		assertEquals(4, h.getCalculatedIndent(2));
		assertEquals(2, h.getCalculatedIndent(3));
		assertEquals(6, h.getCalculatedIndent(4));
				assertEquals(2, h.getCalculatedIndent(5));
								assertEquals(0, h.getCalculatedIndent(6));
    }

@Test public void forBlockBrief()
    {
	final var h = new Handler();
	new Parser(h, asList(
			     "function foo(){",
			     "  for (let i of v) {",
			     "    bar();",
			     "  }",
			     "}")).parse();
	assertEquals(0, h.getCalculatedIndent(1));
		assertEquals(4, h.getCalculatedIndent(2));
		assertEquals(6, h.getCalculatedIndent(3));
				assertEquals(2, h.getCalculatedIndent(4));
								assertEquals(0, h.getCalculatedIndent(5));
    }

@Test public void ifBlock()
    {
	final var h = new Handler();
	new Parser(h, asList(
			     "function foo(){",
			     "  if(a == b)",
			     "  {",
			     "    bar();",
			     "  }",
			     "}")).parse();
	assertEquals(0, h.getCalculatedIndent(1));
		assertEquals(4, h.getCalculatedIndent(2));
		assertEquals(2, h.getCalculatedIndent(3));
		assertEquals(6, h.getCalculatedIndent(4));
				assertEquals(2, h.getCalculatedIndent(5));
								assertEquals(0, h.getCalculatedIndent(6));
    }

@Test public void ifBlockBrief()
    {
	final var h = new Handler();
	new Parser(h, asList(
			     "function foo(){",
			     "  if(a == b) {",
			     "    bar();",
			     "  }",
			     "}")).parse();
	assertEquals(0, h.getCalculatedIndent(1));
		assertEquals(4, h.getCalculatedIndent(2));
		assertEquals(6, h.getCalculatedIndent(3));
				assertEquals(2, h.getCalculatedIndent(4));
								assertEquals(0, h.getCalculatedIndent(5));
    }
}
