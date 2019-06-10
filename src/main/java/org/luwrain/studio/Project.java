
//LWR_API 1.0

package org.luwrain.studio;

import java.io.*;

import org.luwrain.core.*;

public interface Project
{
    Folder getFoldersRoot();
    Flavor[] getBuildFlavors();
    boolean build(Flavor flavor, Output output);
    RunControl run(Luwrain luwrain, Output output) throws IOException;
    void close(Luwrain luwrain);
    SourceFile getMainSourceFile();
}
