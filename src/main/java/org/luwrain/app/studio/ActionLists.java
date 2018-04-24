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

import org.luwrain.core.*;
import org.luwrain.core.events.*;

class ActionLists
{
    private final Strings strings;
    private final Base base;

    ActionLists(Strings strings, Base base)
    {
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(base, "base");
	this.strings = strings;
	this.base = base;
    }

    private List<Action> commonActions()
    {
	final List<Action> res = new LinkedList();
	res.add(new Action("open-project", strings.actionOpenProject()));
	if (base.getProject() != null && base.getProject().getBuildFlavors().length > 0)
	    res.add(new Action("build", strings.actionBuild()));
	res.add(new Action("run", strings.actionRun()));
		return res;
    }

    Action[] getTreeActions()
    {
	final List<Action> res = commonActions();
	return res.toArray(new Action[res.size()]);
    }

    Action[] getEditActions()
    {
	final List<Action> res = commonActions();
	return res.toArray(new Action[res.size()]);
    }

    Action[] getOutputActions()
    {
	final List<Action> res = commonActions();
	return res.toArray(new Action[res.size()]);
    }
}
