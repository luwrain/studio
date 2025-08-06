
//import org.luwrain.studio.proj.main.*
//import org.luwrain.studio.edit.tex.TexSourceFile

wizard 'Новая статья', 'greeting', {
frame 'greeting', {
text getStrings().javaConsoleGreeting()
input 'title', getStrings().javaConsoleProjNameEdit(), 'my-console-app'
button getStrings().buttonContinue(), { values ->
if (values.getText(0).trim().isEmpty()) {
error getStrings().javaConsoleEmptyProjName()
return;
}
/*
wizard.setValue 'title', values.getText(0).trim()
show 'second'
*/
}
}
}
