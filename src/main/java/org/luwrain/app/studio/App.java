
package org.luwrain.app.studio;

import java.util.*;
import java.io.*;

import org.luwrain.base.*;
import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.studio.*;
import org.luwrain.template.*;

public final class App extends AppBase<Strings>
{
    private final String arg;

    private Project project = null;
    private Object treeRoot;

    

    public App()
    {
	this(null);
    }

    public App(String arg)
    {
	super(Strings.NAME, Strings.class);
	NullCheck.notNull(arg, "arg");
	this.arg = arg;
    }

	@Override protected boolean onAppInit()
	{
	    this.treeRoot = getStrings().treeRoot();
	    return true;
	}

    /*
    boolean runProject(Runnable outputRedrawing) throws IOException
    {
	NullCheck.notNull(outputRedrawing, "outputRedrawing");
	if (project == null || isProjectRunning())
	    return false;
	outputText.clear();
	compilationOutput = new Object[0];
	outputRedrawing.run();
	final OutputControl output = new OutputControl(()->luwrain.runUiSafely(outputRedrawing));
	final RunControl runControl = project.run(luwrain, output);
	if (runControl == null)
	{
	    luwrain.message("Проект к запуску не готов.", Luwrain.MessageType.ERROR);
	    return true;
	}
	if (runControl.isContinuous())
	    return runBackground(runControl, outputRedrawing);
	return runForeground(runControl);
    }
    */

    private boolean runBackground(RunControl runControl, Runnable outputRedrawing)
    {
	/*
	NullCheck.notNull(runControl, "runControl");
	NullCheck.notNull(outputRedrawing, "outputRedrawing");
	this.runTask = new FutureTask(()->{
		try {
		    runControl.getCallableObj().call();
		    luwrain.playSound(Sounds.DONE);
		}
		catch(javax.script.ScriptException e)
		{
		    compilationOutput = new Object[]{new ScriptExceptionWrapper(e), ""};
		    luwrain.runUiSafely(outputRedrawing);
		    luwrain.playSound(Sounds.ERROR);
		}
		catch(Exception e)
		{
		    luwrain.crash(e);
		}
	    }, null);
	luwrain.executeBkg(runTask);
	*/
	return true;
    }

    private boolean runForeground(RunControl runControl)
    {
	/*
	NullCheck.notNull(runControl, "runControl");
	try {
	    runControl.getCallableObj().call();
	    luwrain.playSound(Sounds.DONE);
	}
	catch(Exception e)
	{
	    luwrain.crash(e);
	}
	return true;
	*/
	return false;
    }



    Layouts createLayouts()
    {
	return new Layouts(){
	};
    }

        void activateProject(Project proj)
    {
	NullCheck.notNull(proj, "proj");
	this.project = proj;
		this.treeRoot = proj.getPartsRoot();
    }



    private void loadProjectByArg()
    {
	if (arg == null || arg.isEmpty())
	    return;
	final File file = new File(arg);
	if (!file.exists() || file.isDirectory())
	    return;
	final Project proj;
	try {
	    proj = ProjectFactory.load(file);
	}
	catch(IOException e)
	{
	    //FIXME: this notification isn't heard
	    getLuwrain().message(getLuwrain().i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
	    return;
	}
activateProject(proj);
//	treeArea.refresh();
	final Part mainFile = proj.getMainSourceFile();
	if (mainFile == null)
	    return;
	/*
	  final SourceFile.Editing editing = mainFile.startEditing();
	  if (editing == null)
	  return;
	  try {
	  base.startEditing(editing);
	  }
	  catch(IOException e)
	  {
	  }
	*/
    }

        Project getProject()
    {
	return project;
    }

    Object getTreeRoot()
    {
	return treeRoot;
    }


    @Override public AreaLayout getDefaultAreaLayout()
    {
	return null;//layout.getLayout();
    }
}
