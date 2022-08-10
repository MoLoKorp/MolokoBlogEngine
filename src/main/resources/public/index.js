fetch('article')
    .then(response => response.json())
    .then(articles => {
        for (const article of articles) {
            addArticle(article)
        }
    })

const redirectToFileInput = () => {
    document.getElementById("import-file").click()
}

const importArticle = () => {
    const importArticlesForm = document.getElementById('import-articles-form')
    const body = new FormData(importArticlesForm)
    fetch('article/import', { method: 'POST', body })
        .then(response => window.location.reload())
}

const form = document.getElementById('create-article-form')

form.onsubmit = () => {
    const formData = new FormData(form);
    const body = JSON.stringify(Object.fromEntries(formData))
    fetch('article', { method: 'POST', headers: {'Content-Type': 'application/json'}, body })
        .then(response => response.json())
        .then(article => addArticle(article))
    return false
}

const addArticle = article => {
    const div = document.createElement('div')
    div.id = article.id

    const p = document.createElement('p')
    const articleText = document.createTextNode(article.text)
    p.appendChild(articleText)
    
    const editButton = document.createElement('button')
    editButton.innerHTML = 'edit'
    editButton.onclick = () => {
        const editArticleText = document.createElement('input')
        editArticleText.value = article.text
        editArticleText.name = 'text'

        const updateButton = document.createElement('button')
        updateButton.innerHTML = 'update'
        
        const form = document.createElement('form')
        form.onsubmit = () => {
            const formData = new FormData(form);
            const body = JSON.stringify(Object.fromEntries(formData))
            fetch(`article/${article.id}`, { method: 'PUT', headers: {'Content-Type': 'application/json'}, body })
                .then(_response => window.location.reload())
            return false
        }
        form.append(editArticleText, updateButton)

        const div = document.getElementById(article.id)
        div.replaceChildren(form)
    }

    const deleteButton = document.createElement('button')
    deleteButton.innerHTML = 'delete'
    deleteButton.onclick = () => {
        fetch(`article/${article.id}`, { method: 'DELETE' })
            .then(_response => window.location.reload())
    }

    div.appendChild(p)
    div.appendChild(editButton)
    div.appendChild(deleteButton)
    document.body.appendChild(div)
}
