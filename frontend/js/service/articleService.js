import config from '../config/config.js'

const getArticles = async () => {
  const response = await fetch(`${config.apiUrl}/article`)
  return await response.json()
}

const getArticle = async (id) => {
  const response = await fetch(`${config.apiUrl}/article/${id}`)
  return await response.json()
}

const createArticle = async (article) => {
  const response = await fetch(`${config.apiUrl}/article`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', Authorization: localStorage.getItem('auth') },
    body: JSON.stringify(article)
  })
  return await response.json()
}

const updateArticle = async (id, article) => {
  const response = await fetch(`${config.apiUrl}/article/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json', Authorization: localStorage.getItem('auth') },
    body: JSON.stringify(article)
  })
  return await response.json()
}

const importArticles = async (importFile) =>
  await fetch('article/import', {
    method: 'POST',
    body: importFile,
    headers: { Authorization: localStorage.getItem('auth') }
  })

const deleteArticle = async (id) =>
  await fetch(`${config.apiUrl}/article/${id}`, {
    method: 'DELETE',
    headers: { Authorization: localStorage.getItem('auth') }
  })

export {
  getArticles,
  getArticle,
  createArticle,
  updateArticle,
  deleteArticle,
  importArticles
}
