/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

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

import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.script.hooks.*;
import org.luwrain.util.*;

import org.luwrain.studio.backends.java.JavaProjectLoader;
//import org.luwrain.studio.backends.js.JsProject;
import org.luwrain.studio.backends.js.JsProjectLoader;
import org.luwrain.studio.backends.tex.TexProjectLoader;
import org.luwrain.studio.backends.py.*;

public final class ProjectFactory
{
        static public final String KEY_JAVA = "luwrain-project-java";
    static public final String KEY_TEX_PRESENTATION = "luwrain-project-tex-presentation";
    static public final String KEY_PYTHON_CONSOLE = "luwrain-project-py-console";

    static public final String TYPES_LIST_HOOK = "luwrain.studio.project.types";
    static public final String CREATE_HOOK = "luwrain.studio.project.create";

    private final IDE ide;
    private final Luwrain luwrain;

    public ProjectFactory(IDE ide)
    {
	NullCheck.notNull(ide, "ide");
	this.ide = ide;
	this.luwrain = ide.getLuwrainObj();
    }

    public Project load(File projFile) throws IOException
    {
	NullCheck.notNull(projFile, "projFile");
	switch(getProjectType(projFile))
	{
	    	case "java": {
		final JavaProjectLoader loader = new JavaProjectLoader();
		return loader.load(ide, projFile);
	    }
	case "tex": {
		final TexProjectLoader texLoader = new TexProjectLoader();
		return texLoader.load(projFile);
	    }
	case "py": {
		final PyProjectLoader pyLoader = new PyProjectLoader();
		return pyLoader.load(projFile);
	    }
	case "js": {
		final JsProjectLoader jsProjectLoader = new JsProjectLoader();
		return jsProjectLoader.load(projFile);
	    }
	default:
	    return null;
	}
    }

    public Project create(String projType, File destDir) throws IOException
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notEmpty(projType, "projType");
	NullCheck.notNull(destDir, "destDir");
	final Object res;
	try {
	    res = new ProviderHook(luwrain).run(CREATE_HOOK, new Object[]{projType, destDir.getAbsolutePath()});
	}
	catch(RuntimeException e)
	{
	    throw new IOException(CREATE_HOOK + " failed", e);
	}
	if (res == null)
	    throw new IOException(CREATE_HOOK + " has not returned any value");
	final String projFileName = res.toString();
	if (projFileName == null || projFileName.isEmpty())
	    throw new IOException(CREATE_HOOK + " has not returned any value");
	final File projFile = new File(projFileName);
	if (!projFile.exists() || !projFile.isFile())
	    throw new IOException(CREATE_HOOK + " has returned \'" + projFileName + "\' but it is not a file");
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
	final ProjectType[] toSort = res.toArray(new ProjectType[res.size()]);
	Arrays.sort(toSort);
	return toSort;
    }

    private String getProjectType(File projFile) throws IOException
    {
	NullCheck.notNull(projFile, "projFile");
	final String text = FileUtils.readTextFileSingleString(projFile, "UTF-8");
		if (text.contains(KEY_JAVA))
	    return "java";
	if (text.contains(KEY_TEX_PRESENTATION))
	    return "tex";
	if (text.contains(KEY_PYTHON_CONSOLE))
	    return "py";
	return "";
    }
}
