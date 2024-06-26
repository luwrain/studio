
import org.luwrain.studio.proj.main.*
import org.luwrain.studio.edit.tex.TexSourceFile

wizard 'Новая статья', 'greeting', {
frame 'greeting', {
text 'Вас приветствует мастер создания статьи! Вам потребуется ответить на несколько вопросов.'
input 'title', 'Название: ', 'Научная статья'
button 'Продолжить', { values ->
if (values.getText(0).trim().isEmpty()) {
error 'Название статьи не может быть пустым'
return;
}
wizard.setValue 'title', values.getText(0).trim()
show 'second'
}
}

frame 'second', {
input 'authors', 'Авторы: ', wizard.getValue('authors')
input 'org', 'Организация: ', ''
button 'Создать', { values ->

def l = new ArrayList<String>()
l << '123'
wizard.writeFile 'sections.tex', l

def root = new Folder();
root.setName wizard.getValue('title')
root.getTexFiles().add(new TexSourceFile('Главное содержание', 'sections.tex'))
def proj = new ProjectImpl();
proj.setRootFolder root
wizard.finish 'article.lwrproj', proj
}
}

}
