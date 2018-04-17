/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.app.studio;

import java.util.*;
import java.io.*;

import org.luwrain.base.*;
import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;

final class Base
{
    private final Luwrain luwrain;
    private final Strings strings;
    final Settings sett;
    private final String treeRoot;

    private Project project = null;

    Base (Luwrain luwrain, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.strings = strings;
	this.sett = Settings.create(luwrain.getRegistry());
	this.treeRoot = strings.treeRoot();
    }

    CachedTreeModelSource getTreeModel()
    {
	return new TreeModel();
    }

    private class TreeModel implements CachedTreeModelSource
    {
	@Override public Object getRoot()
	{
	    return treeRoot;
	}
	@Override public Object[] getChildObjs(Object obj)
	{
	    return new String[0];
	}
    }
}