import {getArticles} from "../js/service/articleService.js";
import {addArticle} from "../js/eventHandlers.js";

for (const article of await getArticles()) {
    addArticle(article, 'root')
}
