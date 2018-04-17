
package org.luwrain.studio;

public interface Project
{
    Folder[] getFoldersRoot();
    Flavor[] getBuildFlavors();
    boolean build(Flavor flavor, Output output);
    void run(Output output);
}
