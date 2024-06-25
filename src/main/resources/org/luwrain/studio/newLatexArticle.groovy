
import org.luwrain.studio.proj.main.*

wizard 'Новая статья', 'greeting', {
frame 'greeting', {
text 'Вас приветствует мастер создания статьи! Вам потребуется ответить на несколько вопросов.'
input 'title', 'Название: ', 'Научная статья'
button 'Продолжить', { values ->
if (values.getText(0).trim().isEmpty()) {
error 'Название статьи не может быть пустым'
return;
}
wizard.setValue 'authors', values.getText(0).trim()
def l = new ArrayList<String>()
l << '123'
wizard.writeFile 'sections.tex', l
show 'second'
}
}

frame 'second', {
input 'authors', 'Авторы: ', wizard.getValue('authors')
input 'org', 'Организация: ', ''
button 'Создать' {
def root = new Folder();
root.setName values.getText(0)
def proj = new ProjectImpl();
proj.setRootFolder root
wizard.saveProject 'article.lwrproj', proj

}
}

}
