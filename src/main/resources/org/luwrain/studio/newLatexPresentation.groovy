
wizard 'Новая статья', 'greeting', {


frame 'greeting', {
text 'Вас приветствует мастер создания статьи! Вам потребуется ответить на несколько вопросов.'
def f = new java.text.SimpleDateFormat('d MMMM YYYY г.')
def d = new Date()
text f.format(d)
input 'title', 'Название: ', 'Научная статья'
button 'Продолжить', {
}
}

}
