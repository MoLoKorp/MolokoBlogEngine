import {importArticlesHandler, redirectClickHandler} from "../js/eventHandlers.js";

document.getElementById('import-button').onclick = redirectClickHandler.bind(
    this,
    'import-file'
)

document.getElementById('import-file').onchange = importArticlesHandler.bind(
    this,
    'import-articles-form'
)
