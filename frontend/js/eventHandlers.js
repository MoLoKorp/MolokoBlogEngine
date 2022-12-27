import {
  createArticle,
  updateArticle,
  deleteArticle,
  importArticles
} from './service/articleService.js'
import {
  reRender
} from './components/router/router.js'

const createArticleHandler = async () => {
  await createArticle({ text: document.getElementById('articleText').value })
  window.dispatchEvent(reRender)
}

const isNotInUpdate = (id) => !localStorage.getItem(id)
const isInUpdate = (id) => !isNotInUpdate(id)

const updateArticleHandler = async (_e, id) => {
  await updateArticle(id, { text: document.getElementById(`update_${id}`).value })
  localStorage.removeItem(id)
  window.dispatchEvent(reRender)
}

const editArticleHandler = (_e, id, text) => {
  localStorage.setItem(id, true)
  window.dispatchEvent(reRender)
}

const deleteArticleHandler = async (_e, id) => {
  await deleteArticle(id)
  window.dispatchEvent(reRender)
}

const redirectClickHandler = () => {
  document.getElementById('import-file').click()
}

const importArticlesHandler = async (e) => {
  await importArticles(new FormData(e.target.parentElement))
}

export {
  redirectClickHandler,
  importArticlesHandler,
  createArticleHandler,
  editArticleHandler,
  deleteArticleHandler,
  updateArticleHandler,
  isInUpdate,
  isNotInUpdate
}
