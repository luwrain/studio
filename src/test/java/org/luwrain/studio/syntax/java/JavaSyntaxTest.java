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

import java.io.*;
import org.junit.*;

import org.luwrain.core.*;
import org.luwrain.studio.syntax.*;
import static org.luwrain.util.ResourceUtils.*;

import org.luwrain.studio.syntax.SpanTree.*;

public class JavaSyntaxTest extends Assert
{
    @Test public void helloWorld()
    {
	final JavaSyntax s = new JavaSyntax(getHelloWorld());
	s.parse();
	final Span[] spans = s.spanTree.findAtPoint(s.source.length() - 10);
	assertNotNull(spans);
	assertEquals(2, spans.length);
	assertEquals("{\n    static public void main(String[] args)\n    {\n\tSystem.out.println(\"Hello, world!\");\n    }\n", s.source.getText().substring(spans[0].getFromPos(), spans[0].getToPos()));
	assertEquals("{\n\tSystem.out.println(\"Hello, world!\");\n    ", s.source.getText().substring(spans[1].getFromPos(), spans[1].getToPos()));
	final JavaIndent indent = new JavaIndent(s.source, s.spanTree, new SyntaxParams());
	assertEquals(0, indent.getIndentForLine(0));
	assertEquals(0, indent.getIndentForLine(1));
	assertEquals(0, indent.getIndentForLine(2));
	assertEquals(4, indent.getIndentForLine(3));
	assertEquals(4, indent.getIndentForLine(4));
		assertEquals(8, indent.getIndentForLine(5));
		//		assertEquals(4, indent.getIndentForLine(6));
    }
    
    private Source getHelloWorld()
    {
	try {
	    return new Source(readStringResource(getClass(), "HelloWorld.java", "UTF-8", System.lineSeparator()));
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }
}
