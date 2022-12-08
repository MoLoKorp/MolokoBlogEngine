import {
  createArticle,
  updateArticle,
  deleteArticle,
  importArticles,
  getArticles
} from './service/articleService.js'
import {
  registerUser
} from './service/userService.js'

const addArticle = (article, rootId) => {
  const div = document.createElement('div')
  div.id = article.id

  const p = document.createElement('p')
  const articleText = document.createTextNode(`${article.text} owner: ${article.owner}`)
  p.appendChild(articleText)
  div.appendChild(p)

  if (isLoggedIn()) {
    const editButton = document.createElement('button')
    editButton.innerHTML = 'edit'
    editButton.onclick = editArticleHandler.bind(this, article)

    const deleteButton = document.createElement('button')
    deleteButton.innerHTML = 'delete'
    deleteButton.onclick = () =>
      deleteArticle(article.id).then((_response) => window.location.reload())
    div.append(editButton, deleteButton)
  }

  document.getElementById(rootId).appendChild(div)
}

const getArticlesHandler = async () => {
  for (const article of await getArticles()) {
    addArticle(article, 'root')
  }
}

const redirectClickHandler = () => {
  document.getElementById('import-file').click()
}

const importArticlesHandler = async (e) => {
  await importArticles(new FormData(e.target.parentElement))
}

const createArticleHandler = async (e) => {
  const article = await createArticle({ text: document.getElementById('articleText').value })
  addArticle(article, 'root')
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

const getUserAuth = () => {
  const username = document.getElementById('username').value
  const password = document.getElementById('password').value
  return 'Basic ' + btoa(`${username}:${password}`)
}

const login = () => {
  localStorage.setItem('auth', getUserAuth())
  renderUserIfLoggedIn()
}

const logout = e => {
  localStorage.removeItem('auth')
  window.location.href = '/'
}

const isLoggedIn = () => !!localStorage.getItem('auth')

const renderUserIfLoggedIn = () => {
  if (isLoggedIn()) {
    document.getElementById('root').innerHTML =
                '<p>you are logged in</p>' +
                '<button type="button" on#click="./eventHandlers#logout">Log out</button>'
  }
}

const register = async (e) => {
  const username = document.getElementById('username').value
  const password = document.getElementById('password').value
  await registerUser(username, password)
  alert(`you have registered as ${username}`)
}

export {
  redirectClickHandler,
  importArticlesHandler,
  createArticleHandler,
  addArticle,
  getArticlesHandler,
  login,
  register,
  isLoggedIn,
  logout,
  renderUserIfLoggedIn
}
