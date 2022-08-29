const test = 1
import {
  getArticles
} from './service/articleService.js'
import {
  redirectClickHandler,
  importArticlesHandler,
  createArticleHandler,
  addArticle
} from './eventHandlers.js'

for (const article of await getArticles()) {
  addArticle(article)
}

const createArticleFormId = 'create-article-form'
document.getElementById(
  createArticleFormId
).onsubmit = createArticleHandler.bind(this, createArticleFormId)

document.getElementById('import-button').onclick = redirectClickHandler.bind(
  this,
  'import-file'
)

document.getElementById('import-file').onchange = importArticlesHandler.bind(
  this,
  'import-articles-form'
)
