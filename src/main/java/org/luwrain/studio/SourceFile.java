
//LWR_API 1.0

package org.luwrain.studio;

import java.io.*;

import org.luwrain.controls.*;

public interface SourceFile
{
    public interface Editing
    {
	File getFile();	
	EditArea.CorrectorFactory getEditCorrectorFactory();
    }

    String getSourceFileName();
    Editing startEditing();
    void finishEditing();
}
