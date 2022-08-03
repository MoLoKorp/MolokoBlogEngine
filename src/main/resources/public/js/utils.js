class Utils {
    static displayArticles(rootElem) {
        fetch('article')
            .then(response => response.json())
            .then(articles => {
                for (const article of articles) {
                    Utils.addArticle(article)
                }
            })
    }

    static addArticle = function(article) {
        const p = document.createElement('p')
        const articleText = document.createTextNode(`article ${article.id} ${article.text}`)
        p.appendChild(articleText);
        spaRootElem.appendChild(p);
    }
}