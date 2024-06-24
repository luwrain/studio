/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

import java.io.*;


import org.luwrain.core.*;
import org.luwrain.script.core.*;
import org.luwrain.app.base.*;

public interface IDE
{
        AppBase<org.luwrain.app.studio.Strings> getAppBase();
        Luwrain getLuwrainObj();
    ScriptCore getScriptCore();
    org.luwrain.studio.Settings getSett();
    boolean loadProject(File file);
        void showWizard(LayoutBase wizardLayout);
        void onFoldersUpdate();
    void onEditingUpdate();
}
