
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
    input 'org', 'Организация: ', wizard.getValue('org')
    input 'city', 'Город: ', wizard.getValue('city ')
    button 'Создать', { values ->
      def l = new ArrayList<String>()
      l << '\\section*{Введение}'
      wizard.writeFile 'intro.tex', l

      def root = new Folder();
      root.setName wizard.getValue('title')
      root.getTexFiles().add(new TexSourceFile('Титульная часть', 'title.tex'))
      root.getTexFiles().add(new TexSourceFile('Введение', 'intro.tex'))
      root.getTexFiles().add(new TexSourceFile('Основной текст', 'main.tex'))
      root.getTexFiles().add(new TexSourceFile('Заключение', 'conclusion.tex'))
      root.getTexFiles().add(new TexSourceFile('Список литературы', 'biblio.tex'))

      def proj = new ProjectImpl()
      proj.setRootFolder root
      wizard.finish 'article.lwrproj', proj
    }
  }
}
