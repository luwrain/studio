/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.studio;

import lombok.*;

import org.luwrain.core.*;

import static org.luwrain.core.NullCheck.*;

@Data
public final class ProjectType implements Comparable
{
    static public final String
	JAVA_CONSOLE = "java-console",
	TEX_PRESENTATION = "tex-presentation",
	TEX_ARTICLE = "tex-article";

    final String id;
    final Integer orderIndex;
    final String title;

    public ProjectType(String id, int orderIndex, String title)
    {
	notEmpty(id, "id");
	notNull(title, "title");
	this.id = id;
	this.orderIndex = new Integer(orderIndex);
	this.title = title;
    }

    @Override public String toString()
    {
	return title;
    }

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof ProjectType t)
	    return id.equals(t.id);
	return false;
    }

    @Override public int compareTo(Object o)
    {
	if (o != null && o instanceof ProjectType t)
	    return orderIndex.compareTo(t.orderIndex);
	    return 0;
    }
}
