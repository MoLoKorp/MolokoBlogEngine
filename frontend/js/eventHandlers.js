import {
  createArticle,
  updateArticle,
  deleteArticle,
  importArticles
} from './service/articleService.js'

const addArticle = (article, rootId) => {
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

  div.append(p, editButton, deleteButton)
  document.getElementById(rootId).appendChild(div)
}

const redirectClickHandler = () => {
  document.getElementById('import-file').click()
}

const importArticlesHandler = async (e) => {
  await importArticles(new FormData(e.target.parentElement))
}

const createArticleHandler = (e) => {
  createArticle(Object.fromEntries(new FormData(e.target))).then((_response) =>
    window.location.reload()
  )
  return false
}

const updateArticleHandler = (articleId, form) => {
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
