
wizard 'Новая статья', 'greeting', {
frame 'greeting', {
text 'Вас приветствует мастер создания статьи! Вам потребуется ответить на несколько вопросов.'
input 'title', 'Название: ', 'Научная статья'
button 'Продолжить', { values ->
if (values.getText(0).trim().isEmpty()) {
error 'Название статьи не может быть пустым'
return;
}
show 'second'
}
}

frame 'second', {
input 'authors', 'Авторы: ', ''
input 'org', 'Организация: ', ''
}

}
