/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>

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

//LWR_API 1.0

package org.luwrain.studio;

import org.luwrain.core.*;

public final class ProjectType implements Comparable
{
    private final String id;
    private final Integer orderIndex;
    private final String title;

    public ProjectType(String id, int orderIndex, String title)
    {
	NullCheck.notEmpty(id, "id");
	NullCheck.notNull(title, "title");
	this.id = id;
	this.orderIndex = new Integer(orderIndex);
	this.title = title;
    }

    public String getId()
    {
	return id;
    }

    public int getOrderIndex()
    {
	return orderIndex.intValue();
    }

    public String getTitle()
    {
	return title;
    }

    @Override public String toString()
    {
	return title;
    }

    @Override public boolean equals(Object o)
    {
		if (o == null || !(o instanceof ProjectType))
		    return false;
		return id.equals(((ProjectType)o).id);
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof ProjectType))
	    return 0;
	return orderIndex.compareTo(((ProjectType)o).orderIndex);
    }
}
