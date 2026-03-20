
package org.luwrain.studio.syntax.java;

import java.io.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.luwrain.core.*;
import org.luwrain.studio.syntax.*;
import static org.luwrain.util.ResourceUtils.*;

import org.luwrain.studio.syntax.SpanTree.*;

public class JavaSyntaxTest
{
    @Disabled @Test public void helloWorld()
    {
	final JavaSyntax s = new JavaSyntax(getHelloWorld());
	s.parse();
	final Span[] spans = s.spanTree.findAtPoint(s.source.getText().length() - 10);
	assertNotNull(spans);
	assertEquals(2, spans.length);
	assertEquals("{\n    static public void main(String[] args)\n    {\n\tSystem.out.println(\"Hello, world!\");\n    }\n", s.source.getText().substring(spans[0].getFromPos(), spans[0].getToPos()));
	assertEquals("{\n\tSystem.out.println(\"Hello, world!\");\n    ", s.source.getText().substring(spans[1].getFromPos(), spans[1].getToPos()));
	final JavaIndent indent = new JavaIndent(getHelloWorldSource(), s.spanTree, new SyntaxParams());
	assertEquals(0, indent.getIndentForLine(0));
	assertEquals(0, indent.getIndentForLine(1));
	assertEquals(0, indent.getIndentForLine(2));
	assertEquals(4, indent.getIndentForLine(3));
	assertEquals(4, indent.getIndentForLine(4));
		assertEquals(8, indent.getIndentForLine(5));
		//		assertEquals(4, indent.getIndentForLine(6));
    }
    
    private JavaSyntax.Source getHelloWorld()
    {
	try {
final Source s = new Source(readStringResource(getClass(), "HelloWorld.java", "UTF-8", System.lineSeparator()));
return (JavaSyntax.Source)()->s.getText();
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    private Source getHelloWorldSource()
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
