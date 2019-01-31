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
import java.util.*;
import com.google.gson. annotations.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;

final class TexSourceFile implements SourceFile
{
    @SerializedName("name")
    private String name = null;

    @SerializedName("path")
    private String path = null;

    private TexProject proj = null;

        void setProject(TexProject proj)
    {
	NullCheck.notNull(proj, "proj");
	this.proj = proj;
    }

    @Override public String getSourceFileName()
    {
	return name != null?name:"NONAME";
    }

    @Override public SourceFile.Editing startEditing()
    {
	return new SourceFile.Editing(){
	    @Override public File getFile()
	    {
		return new File(proj.getProjectDir(), path);
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
	return getSourceFileName();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof TexSourceFile))
	    return false;
	final TexSourceFile f = (TexSourceFile)o;
	return path.equals(f.path);
    }
}
