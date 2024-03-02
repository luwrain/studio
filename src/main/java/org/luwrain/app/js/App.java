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

package org.luwrain.app.js;

import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.app.base.*;
import org.luwrain.script.core.*;
import org.luwrain.script.*;

import org.luwrain.studio.proj.single.*;

public final class App extends AppBase<Strings>
{
    static public final String
	LOG_COMPONENT = "js";

    private Conv conv = null;
    private MainLayout mainLayout = null;
    public App()
    {
	super(Strings.NAME, Strings.class, "luwrain.js");
    }

    @Override protected AreaLayout onAppInit() throws IOException
    {
	this.conv = new Conv(this);
	this.mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    Layouts layouts()
    {
	return new Layouts(){
	    @Override public void main()
	    {
		//		setAreaLayout(projectBaseLayout);
		//		getLuwrain().announceActiveArea();
	    }
	};
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }


    Conv getConv() { return this.conv; }

        interface Layouts
    {
	void main();
    }

}
