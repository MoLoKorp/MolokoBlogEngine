fetch('article')
    .then(response => response.json())
    .then(articles => {
        for (const article of articles) {
            addArticle(article)
        }
    })

const form = document.getElementById('create-article-form')

form.onsubmit = function(event) {
    const formData = new FormData(form);
    const body = JSON.stringify(Object.fromEntries(formData))
    fetch('article', { method: 'POST', headers: {'Content-Type': 'application/json'}, body })
        .then(response => response.json())
        .then(article => addArticle(article))
    return false
}

const addArticle = function(article) {
    const p = document.createElement('p')
    const articleText = document.createTextNode(`article ${article.id} ${article.text}`)
    p.appendChild(articleText);
    document.body.appendChild(p);
}
