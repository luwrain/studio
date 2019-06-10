
package org.luwrain.app.studio;

import org.luwrain.core.*;

interface Settings
{
    static public final String PATH = "/org/luwrain/studio";
    
    static Settings create(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, PATH, Settings.class);
    }
}
