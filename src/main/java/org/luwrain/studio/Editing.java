
//LWR_API 1.0

package org.luwrain.studio;

import java.io.*;
import java.util.concurrent.atomic.*;

import org.luwrain.app.base.*;

/** Editing session of a source files. */
public interface Editing
{
    void closeEditing();
    Part.Action[] getActions();
    AtomicBoolean getModifiedFlag();
    boolean save() throws IOException;
    boolean hasSameSource(Editing editing);
}
