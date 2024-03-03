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
import org.luwrain.app.base.*;

public final class App extends AppBase<Strings>
{
    private Conv conv = null;
    private File scriptsFile = null;
    private Scripts scripts = null;
    private MainLayout mainLayout = null;
    public App()
    {
	super(Strings.NAME, Strings.class, "luwrain.js");
    }

    @Override protected AreaLayout onAppInit() throws IOException
    {
	final var dataDir = getLuwrain().getAppDataDir("luwrain.studio.js").toFile();
	this.scriptsFile = new File(dataDir, "scripts.json");
	if (!scriptsFile.exists())
	{
	    this.scripts = new Scripts();
	    this.scripts.scripts = new ArrayList<>();
	} else
	    this.scripts = Scripts.load(scriptsFile);
	if (scripts.scripts.isEmpty())
	    this.scripts.scripts.add(new Script("default"));
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

    void save()
    {
	try {
	    Scripts.save(this.scriptsFile, this.scripts);
	}
	catch(IOException e)
	{
	    crash(e);
	}
    }


    Conv getConv() { return this.conv; }
    Scripts getScripts() { return this.scripts; }

        interface Layouts
    {
	void main();
    }
}
