/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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
import org.luwrain.controls.*;
import org.luwrain.studio.*;

import static org.luwrain.core.NullCheck.*;

final class ProjectTreeModel implements TreeListArea.Model<Part>
{
    final Project proj;

    ProjectTreeModel(Project proj)
    {
	notNull(proj, "proj");
	this.proj = proj;
    }

        @Override public Part getRoot()
    {
	return proj.getPartsRoot();
    }

    @Override public boolean getItems(Part part, TreeListArea.Collector<Part> collector)
    {
	notNull(part, "part");
	notNull(collector, "collector");
	final var c = part.getChildParts();
	if (c == null || c.length == 0)
	    return false;
	collector.collect(Arrays.asList(c));
	return true;
    }

    @Override public boolean isLeaf(Part part)
    {
	notNull(part, "part");
	final var c = part.getChildParts();
	return c == null || c.length == 0;
    }

}
