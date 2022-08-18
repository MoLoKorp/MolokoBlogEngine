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
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(article)
  })
  return await response.json()
}

const updateArticle = async (id, article) => {
  const response = await fetch(`${config.apiUrl}/article/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(article)
  })
  return await response.json()
}

const importArticles = async (importFile) =>
  fetch('article/import', { method: 'POST', body: importFile })

const deleteArticle = async (id) =>
  await fetch(`${config.apiUrl}/article/${id}`, { method: 'DELETE' })

export {
  getArticles,
  getArticle,
  createArticle,
  updateArticle,
  deleteArticle,
  importArticles
}
