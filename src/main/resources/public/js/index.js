const form = document.getElementById('create-article-form');
spaRootElem = document.getElementById('molokoApp');
Utils.displayArticles(spaRootElem);

form.onsubmit = function(event) {
    console.log("hel");
    const formData = new FormData(form);
    const body = JSON.stringify(Object.fromEntries(formData))
    fetch('article', { method: 'POST', headers: {'Content-Type': 'application/json'}, body })
        .then(response => response.json())
        .then(article => Utils.addArticle(article))
    return false
}


