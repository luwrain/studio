
package org.luwrain.studio.edit.java;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.studio.*;
import org.luwrain.util.*;
import org.luwrain.app.base.*;

import static org.luwrain.util.TextUtils.*;
import static org.luwrain.util.FileUtils.*;

final class Editing implements TextEditing
{
    private final File file;
    private final MutableMarkedLinesImpl content;

    Editing(File file) throws IOException
    {
	NullCheck.notNull(file, "files");
	this.file = file;
	final String text = readTextFile(file);
	final String[] lines = splitLines(text);
	this.content = new MutableMarkedLinesImpl(lines);
    }

    @Override public EditArea.Params getEditParams(ControlContext context)
    {
	final EditArea.Params params = new EditArea.Params();
	params.context = context;
	params.content = content;
	params.appearance = new DefaultEditAreaAppearance(context);
	params.editFactory = (editParams)->{
	    return new MultilineEdit(editParams);
	};
	params.name = file.getName();
	return params;
    }

    @Override public boolean save() throws IOException
    {
	return false;
    }

        @Override public Part.Action[] getActions()
    {
	return new Part.Action[0];
    }

    @Override public void onNewHotPoint(int hotPointX, int hotPointY)
    {
    }

    @Override public void closeEditing()
    {
    }

    
    public boolean hasUnsavedChanges() { return true; }
    public void onModification() {}

    @Override public AtomicBoolean getModifiedFlag()
    {
	return null;
    }

    @Override public boolean hasSameSource(org.luwrain.studio.Editing editing)
    {
	return false;
    }

}
