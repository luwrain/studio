
package org.luwrain.studio.backends.java;

import java.io.*;

import org.luwrain.studio.*;

import org.luwrain.core.*;

public class ClassPart implements Part
{
    protected final String name;

    public ClassPart(String name)
    {
	NullCheck.notNull(name, "name");
	this.name = name;
    }

    public String getName()
    {
	return name;
    }

    @Override public String getTitle()
    {
	return getName();
    }

    @Override public Part[] getChildParts()
    {
	return new Part[0];
    }

    @Override public Editing startEditing() throws IOException
    {
	return null;
    }

    @Override public String toString()
    {
	return name;
    }
}
