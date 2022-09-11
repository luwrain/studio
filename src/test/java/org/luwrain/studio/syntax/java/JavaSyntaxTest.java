
package org.luwrain.studio.syntax.java;

import java.io.*;
import org.junit.*;

import org.luwrain.core.*;
import org.luwrain.studio.syntax.*;
import static org.luwrain.util.ResourceUtils.*;

public class JavaSyntaxTest extends Assert
{
    @Test public void helloWorld()
    {
	final JavaSyntax s = new JavaSyntax(getHelloWorld());
	s.parse();
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
