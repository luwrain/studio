
package org.luwrain.studio.backends.ly;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.util.*;

final class LyEditing implements TextEditing
{
    private final File file;
    private final MutableLinesImpl content;

    LyEditing(File file) throws IOException
    {
	NullCheck.notNull(file, "files");
	this.file = file;
	final String text = FileUtils.readTextFileSingleString(file, "UTF-8");
	final String[] lines = FileUtils.universalLineSplitting(text);
	this.content = new MutableLinesImpl(lines);
    }

    @Override public EditArea.Params getEditParams(ControlContext context)
    {
	NullCheck.notNull(context, "context");
	final EditArea.Params params = new EditArea.Params();
	params.context = context;
	params.content = content;
	params.appearance = new EditUtils.DefaultEditAreaAppearance(context);
	params.editFactory = (editParams)->{
	    return new MultilineEdit(editParams);
	};
	params.name = file.getName();
	return params;
    }

    @Override public void finishEditing()
    {
    }
}
