
package org.luwrain.studio.backends.java;

import org.junit.*;

import org.luwrain.core.*;

public class ParserTest extends Assert
{
    /*
    @Test public void emptyClass()
    {
	final String text = "class Proba {}";
	final Parser p = new Parser();
	p.parse(text);
	final ClassPart[] classes = p.getClasses();
	assertNotNull(classes);
	assertTrue(classes.length == 1);
	assertNotNull(classes[0]);
	assertTrue(classes[0].getName().equals("Proba"));
    }
    */

    /*
    @Test public void emptyClassWithPackage()
    {
	final String text = "package org.luwrain; class Proba {}";
	final Parser p = new Parser();
	p.parse(text);
	final ClassPart[] classes = p.getClasses();
	assertNotNull(classes);
	assertTrue(classes.length == 1);
	assertNotNull(classes[0]);
	assertTrue(classes[0].getName().equals("org.luwrain.Proba"));
    }
    */

    /*
    @Test public void file()
    {
	try {
	    final String text = org.luwrain.util.FileUtils.readTextFileSingleString(new java.io.File("/tmp/proba.java"), "UTF-8");
	    final Parser p = new Parser();
	    p.parse(text);
	    final ClassPart[] classes = p.getClasses();
	    System.out.println("" + classes.length + " classes");
	    for(ClassPart c: classes)
		System.out.println(c.getName());
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
    }
    */
}
