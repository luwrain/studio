
package org.luwrain.app.studio;

import java.io.*;
import java.nio.file.*;

public interface Strings
{
    static final String NAME = "luwrain.commander";

    String appName();
    String leftPanelName();
    String rightPanelName();
        String infoAreaName();
    String operationsAreaName();
    String panelActionTitle(String actionName, boolean multiple);
    String infoActionTitle(String actionName);
    String copyPopupName();
    String copyPopupPrefix(String copyWhat);
    String copyOperationName(String copyWhat, String copyTo);
    String movePopupName();
    String movePopupPrefix(String moveWhat);
    String moveOperationName(String moveWhat, String moveTo);
    String mkdirPopupName();
    String mkdirPopupPrefix();
    String mkdirErrorMessage(String comment);
    String mkdirOkMessage(String dirName);
    String delPopupName();
    String delPopupText(String whatToDelete);
    String delOperationName(File[] filesToDelete);
    String operationCompletedMessage(org.luwrain.base.FilesOperation op);
    String notAllOperationsFinished();
    String cancelOperationPopupName();
    String cancelOperationPopupText(org.luwrain.base.FilesOperation op);
    String bytesNum(long num);
    String opResultOk();
    String opResultInterrupted();
    String actionHiddenShow();
    String actionHiddenHide();
    String actionOpen();
    String actionEditAsText();
    String actionPreview();
    String actionPreviewAnotherFormat();
    String actionOpenChoosingApp();
    String actionCopy();
    String actionMove();
    String actionMkdir();
    String actionDelete();
    String actionSize();
    String dirMayNotBePreviewed();
    String enteredPathExists(String path);
    String actionCopyToClipboard();
    String actionPlay();
}
