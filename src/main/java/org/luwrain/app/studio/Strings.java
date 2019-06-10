
package org.luwrain.app.studio;

import java.io.*;
import java.nio.file.*;

public interface Strings
{
    static final String NAME = "luwrain.studio";

    String appName();

    String actionOpenProject();
    String actionBuild();
    String actionRun();
    String codeBlockBegin();
    String codeBlockEnd();
    String codeComments();
    String codeIndent(String len);
    String editAreaName();
    String fileSavedSuccessfully();
    String openProjectPopupName();
    String openProjectPopupPrefix();
    String outputAreaName();
    String treeAreaName();
    String treeRoot();
}
