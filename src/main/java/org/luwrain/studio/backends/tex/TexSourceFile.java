/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.studio.backends.tex;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;

final class TexSourceFile implements org.luwrain.studio.SourceFile
{
    private final File file;

    TexSourceFile(File file)
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
	    @Override public EditArea.CorrectorWrapperFactory getEditCorrectorWrapperFactory()
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
	if (o == null || !(o instanceof TexSourceFile))
	    return false;
	final TexSourceFile f = (TexSourceFile)o;
	return file.equals(f.file);
    }
}
