/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>

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

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;

public interface Part
{
public interface ActionProc
{
    boolean onAction(IDE ide);
}

    public interface Action extends ActionProc
    {
	String getTitle();
	InputEvent getHotKey();
    }

    String getTitle();
    Action[] getActions();
    Part[] getChildParts();
    Editing startEditing() throws IOException;

    static public Action action(String title, ActionProc proc)
    {
	NullCheck.notEmpty(title, "title");
	NullCheck.notNull(proc, "proc");
	return new Action(){
	    @Override public String getTitle() { return title; }
	    @Override public InputEvent getHotKey() { return null; }
	    @Override public boolean onAction(IDE ide) { return proc.onAction(ide); }
	};
    }

    static public Action action(String title, InputEvent hotKey, ActionProc proc)
    {
	NullCheck.notEmpty(title, "title");
	NullCheck.notNull(hotKey, "hotKey");
	NullCheck.notNull(proc, "proc");
	return new Action(){
	    @Override public String getTitle() { return title; }
	    @Override public InputEvent getHotKey() { return hotKey; }
	    @Override public boolean onAction(IDE ide) { return proc.onAction(ide); }
	};
    }


    static public Action[] actions(Action ... actions)
    {
	return actions;
    }
}
