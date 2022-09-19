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

import org.junit.*;

public class SourceTest extends Assert
{
    @Test public void empty()
    {
	final Source s = new Source("");
	assertEquals(0, s.length());
	assertEquals("", s.getText());
	assertEquals(0, s.getLineCount());
    }

        @Test public void emptyLines()
    {
	final Source s = new Source(new String[0]);
	assertEquals(0, s.length());
	assertEquals("", s.getText());
	assertEquals(0, s.getLineCount());
    }

        @Test public void single()
    {
	final Source s = new Source("123");
	assertEquals(3, s.length());
		assertEquals("123", s.getText());
		assertEquals(1, s.getLineCount());
		assertEquals("123", s.getLine(0));
    }

            @Test public void singleLines()
    {
	final Source s = new Source(new String[]{"123"});
	assertEquals(3, s.length());
			assertEquals("123", s.getText());
			assertEquals(1, s.getLineCount());
			assertEquals("123", s.getLine(0));
    }

            @Test public void twoEmpty()
    {
	final Source s = new Source("\n");
	assertEquals(1, s.length());
		assertEquals("\n", s.getText());
		assertEquals(2, s.getLineCount());
		assertTrue(s.getLine(0).isEmpty());
				assertTrue(s.getLine(1).isEmpty());
    }

            @Test public void twoEmptyLines()
    {
	final Source s = new Source(new String[]{"", ""});
	assertEquals(1, s.length());
			assertEquals("\n", s.getText());
			assertEquals(2, s.getLineCount());
					assertTrue(s.getLine(0).isEmpty());
				assertTrue(s.getLine(1).isEmpty());
    }

    //FIXME:oneAndEmpty
    //FIXME:twoNonEmpty
    //FIXME:\r\n
}
