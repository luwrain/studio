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

import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.script.hooks.*;
import org.luwrain.studio.backends.js.JsProject;
import org.luwrain.studio.backends.js.JsProjectLoader;
import org.luwrain.studio.backends.tex.TexProjectLoader;

public final class ProjectFactory
{
    static public final String TYPES_LIST_HOOK = "luwrain.studio.project.types";
    
    private final Luwrain luwrain;

    public ProjectFactory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }
    
    static public Project load(File projFile) throws IOException
    {
	NullCheck.notNull(projFile, "projFile");
	final JsProjectLoader jsProjectLoader = new JsProjectLoader();
	final JsProject jsProj = jsProjectLoader.load(projFile);
	return jsProj;
	/*
	final TexProjectLoader texLoader = new TexProjectLoader();
	return texLoader.load(projFile);
	*/
    }

    static public Project create(Luwrain luwrain, String projType, File destDir) throws IOException
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notEmpty(projType, "projType");
	NullCheck.notNull(destDir, "destDir");
	final String HOOK_NAME = "luwrain.studio.project.create";
	final Object res;
	try {
	    res = new ProviderHook(luwrain).run(HOOK_NAME, new Object[]{projType, destDir.getAbsolutePath()});
	}
	catch(RuntimeException e)
	{
	    throw new IOException(HOOK_NAME + " failed", e);
	}
	if (res == null)
	    throw new IOException(HOOK_NAME + " has not returned any value");
	final String projFileName = res.toString();
	if (projFileName == null || projFileName.isEmpty())
	    throw new IOException(HOOK_NAME + " has not returned any value");
	final File projFile = new File(projFileName);
	if (!projFile.exists() || !projFile.isFile())
	    throw new IOException(HOOK_NAME + " has returned \'" + projFileName + "\' but it is not a file");
	return load(projFile);
    }

    public ProjectType[] getNewProjectTypes()
    {

	final Object[] objs;
	try {
	    objs = new CollectorHook(luwrain).runForArrays(TYPES_LIST_HOOK, new Object[0]);
	}
	catch(RuntimeException e)
	{
	    luwrain.crash(e);
	    return new ProjectType[0];
	}
	final List<ProjectType> res = new LinkedList();
	for(Object o: objs)
	    if (o != null)
	{
	    final Object idObj = ScriptUtils.getMember(o, "id");
	    	    final Object orderIndexObj = ScriptUtils.getMember(o, "orderIndex");
		    	    final Object titleObj = ScriptUtils.getMember(o, "title");
			    if (idObj == null || titleObj == null || orderIndexObj == null)
				continue;
			    final String id = ScriptUtils.getStringValue(idObj);
			    final Integer orderIndex = ScriptUtils.getIntegerValue(orderIndexObj);
			    final String title = ScriptUtils.getStringValue(titleObj);
			    if (id == null || id.isEmpty() ||
				orderIndex == null || orderIndex.intValue() < 0 ||
				title == null || title.isEmpty())
				continue;
			    res.add(new ProjectType(id, orderIndex.intValue(), title));
	}
	return res.toArray(new ProjectType[res.size()]);
	    }
 }
