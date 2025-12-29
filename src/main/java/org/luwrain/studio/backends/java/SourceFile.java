
package org.luwrain.studio.backends.java;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;
import org.luwrain.util.*;
import org.luwrain.studio.*;

final class SourceFile implements Part
{
    static private final String LOG_COMPONENT = Project.LOG_COMPONENT;

    private final Project proj;
    private final File file;
    private String[] lines = new String[0];

    SourceFile(Project proj, File file)
    {
	NullCheck.notNull(proj, "proj");
	NullCheck.notNull(file, "file");
	this.proj = proj;
	this.file = file;
	proj.getExecutor().execute(new FutureTask<>(()->update(), null));
    }

    void update()
    {
	if (proj.isClosed())
	    return;
	try {
	    if (file.length() > proj.getPreloadFileSizeLimit())
	    {
		Log.info(LOG_COMPONENT, file.getAbsolutePath() + " is too large, skipping preloading");
		return;
	    }
    	this.lines = FileUtils.readTextFileMultipleStrings(file, "UTF-8", null);
	final long timeStart = System.currentTimeMillis();
	/*
	try {
	final Parser p = new Parser();
	p.parse(lines);
	}
		    catch (OutOfMemoryError e)
		    {
			Log.error(LOG_COMPONENT, "unable to parse " + file.getAbsolutePath() + ": no enough memory");
		    }
	*/
	final long timeEnd = System.currentTimeMillis();
	final long mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
	Log.debug(LOG_COMPONENT, "time " + (timeEnd - timeStart) + "ms, mem " + mem + "M, file " + file.getAbsolutePath());
	this.lines = null;
	}
	catch(IOException e)
	{
	    e.printStackTrace();
	}
    }

    @Override public String getTitle()
    {
	return file.getName();
    }

    @Override public Part[] getChildParts()
    {
	return new Part[0];
    }

    @Override public Editing startEditing() throws IOException
    {
	return new Editing(file);
    }

    @Override public String toString()
    {
	return getTitle();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof SourceFile))
	    return false;
	final SourceFile f = (SourceFile)o;
	return file.equals(f.file);
    }

        @Override public Action[] getActions()
    {
	return new Action[0];
    }

}
