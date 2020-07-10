
package org.luwrain.studio.backends.js;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;

final class JsSourceFile implements org.luwrain.studio.Part
{    private final File file;

    JsSourceFile(File file)
    {
	NullCheck.notNull(file, "file");
	this.file = file;
    }

    @Override public String getTitle()
    {
	return file.getName();
    }

    @Override public org.luwrain.studio.Editing startEditing()
    {
	return null;
    }

    @Override public org.luwrain.studio.Part[] getChildParts()
    {
	return new org.luwrain.studio.Part[0];
    }
}
