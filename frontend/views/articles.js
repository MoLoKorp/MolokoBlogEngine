import {getArticles} from "../js/service/articleService.js";
import {addArticle, createArticleHandler} from "../js/eventHandlers.js";

for (const article of await getArticles()) {
    addArticle(article, 'root')
}

const createArticleFormId = 'create-article-form'
document.getElementById(
    createArticleFormId
).onsubmit = createArticleHandler.bind(this, createArticleFormId)
