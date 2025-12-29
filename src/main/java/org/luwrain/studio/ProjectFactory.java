
package org.luwrain.studio;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

import org.luwrain.studio.backends.tex.TexProject;
//import org.luwrain.studio.proj.wizards.*;
import org.luwrain.app.studio.Strings;

import static java.util.Objects.*;
import static org.luwrain.studio.ProjectType.*;
import static org.luwrain.core.NullCheck.*;
import static org.luwrain.util.FileUtils.*;

public final class ProjectFactory
{

    private final IDE ide;
    //    private final Luwrain luwrain;
    private final Strings strings;

    public ProjectFactory(IDE ide, Strings strings)
    {
	this.ide = requireNonNull(ide, "ide can't be null");
	//	this.luwrain = ide.getLuwrainObj();
	this.strings = strings;
    }

        public ProjectType[] getNewProjectTypes()
    {
	return  new ProjectType[]{
	    new ProjectType(JAVA_CONSOLE, 0, strings.projTypeJavaConsole()),
	    new ProjectType(TEX_PRESENTATION, 0, strings.projTypeTexPresentation()),
	    new ProjectType(TEX_ARTICLE, 0, strings.projTypeTexArticle()),
	    new ProjectType("lilypond-piano", 0, "Фортепианная пьеса Lilypond"),
};
    }

        public void create(String projType, File destDir)
    {
	final var w = new ProjectWizard(ide, strings, destDir, projType + ".groovy");
	    ide.showWizard(w);
	    ide.getLuwrainObj().announceActiveArea();
    }


    /*
    public Project load(File projFile) throws IOException
    {
	final Project loader = readProjectKey(projFile);
	if (loader == null)
	    throw new IOException("No known keys in the file");
	final Project proj = loader.load(projFile, ide);
	if (proj == null)
	    throw new IOException(projFile.getPath() + " doesn't contain proper  project structure");
	return proj;
    }
    */

    private Project readProjectKey(File projFile) throws IOException
    {
	final String text = readTextFile(projFile);
	if (text.contains(TexProject.KEY))
	    return new TexProject();
	return null;
    }

}
