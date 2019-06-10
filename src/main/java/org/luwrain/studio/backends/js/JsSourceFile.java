
package org.luwrain.studio.backends.js;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;

final class JsSourceFile implements org.luwrain.studio.SourceFile
{    private final File file;

    JsSourceFile(File file)
    {
	NullCheck.notNull(file, "file");
	this.file = file;
    }

    @Override public String getSourceFileName()
    {
	return file.getName();
    }

    @Override public org.luwrain.studio.SourceFile.Editing startEditing()
    {
	return new org.luwrain.studio.SourceFile.Editing(){
	    @Override public File getFile()
	    {
		return file;
	    }
	    @Override public EditArea.CorrectorFactory getEditCorrectorFactory()
	    {
		//FIXME:
		return null;
	    }
	};
    }

    @Override public void finishEditing()
    {
    }

    @Override public String toString()
    {
	return file.getName();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof JsSourceFile))
	    return false;
	final JsSourceFile f = (JsSourceFile)o;
	return file.equals(f.file);
    }
}
