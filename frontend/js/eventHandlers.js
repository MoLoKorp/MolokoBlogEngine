import {
  createArticle,
  updateArticle,
  deleteArticle,
  importArticles
} from './service/articleService.js'

const addArticle = (article) => {
  const div = document.createElement('div')
  div.id = article.id

  const p = document.createElement('p')
  const articleText = document.createTextNode(article.text)
  p.appendChild(articleText)

  const editButton = document.createElement('button')
  editButton.innerHTML = 'edit'
  editButton.onclick = editArticleHandler.bind(this, article)

  const deleteButton = document.createElement('button')
  deleteButton.innerHTML = 'delete'
  deleteButton.onclick = () =>
    deleteArticle(article.id).then((_response) => window.location.reload())

  div.appendChild(p)
  div.appendChild(editButton)
  div.appendChild(deleteButton)
  document.body.appendChild(div)
}

const redirectClickHandler = (buttonId) => {
  document.getElementById(buttonId).click()
}

const importArticlesHandler = (formId) => {
  const importArticlesForm = document.getElementById(formId)
  // eslint-disable-next-line no-undef
  importArticles(new FormData(importArticlesForm)).then((response) =>
    window.location.reload()
  )
}

const createArticleHandler = (formId) => {
  const createArticleForm = document.getElementById(formId)
  // eslint-disable-next-line no-undef
  const formData = new FormData(createArticleForm)
  createArticle(Object.fromEntries(formData)).then((response) =>
    window.location.reload()
  )
  return false
}

const updateArticleHandler = (articleId, form) => {
  // eslint-disable-next-line no-undef
  const formData = new FormData(form)
  updateArticle(articleId, Object.fromEntries(formData)).then((_response) =>
    window.location.reload()
  )
  return false
}

const editArticleHandler = (article) => {
  const editArticleText = document.createElement('input')
  editArticleText.value = article.text
  editArticleText.name = 'text'

  const updateButton = document.createElement('button')
  updateButton.innerHTML = 'update'

  const form = document.createElement('form')
  form.onsubmit = updateArticleHandler.bind(this, article.id, form)
  form.append(editArticleText, updateButton)

  const div = document.getElementById(article.id)
  div.replaceChildren(form)
}

export {
  redirectClickHandler,
  importArticlesHandler,
  createArticleHandler,
  addArticle
}
