/*
The following custom html element is used to dynamically render external html/js views to the single page.
To prepare view, create html and non-mandatory js file in views folder with the same name (e.g. home.html/home.js).
Put class routed to the html element which onclick method should render the view.
Define router tag in the original html page: 'customElements.define("app-router", Router)'.
Setup routes in the route which connect path to some particular view.
Router is using history API, so it is possible to go back and forth through the history.
View is going to be rendered as the last element of the page.

Example:
<a href="/home" class="routed">Home</a>

<app-router>
  <route path="/home" view="home" />
</app-router>
*/

export class Router extends HTMLElement {
  static #routes = {}
  static #root
  static #default

  constructor () {
    super()

    Router.#default = this.getAttribute('default')

    Router.#root = document.createElement('div')
    Router.#root.id = 'root'
    this.after(Router.#root)

    for (const route of this.getElementsByTagName('route')) {
      Router.#routes[route.getAttribute('path')] =
          {
            view: route.getAttribute('view'),
            prescript: route.getAttribute('prescript')
          }
    }

    for (const el of document.getElementsByClassName('routed')) {
      el.onclick = this.#renderView
    }

    window.addEventListener('popstate', async function () {
      await Router.#addViewToRouterDom(window.location.pathname || Router.#default)
    })

    window.addEventListener('DOMContentLoaded', async function () {
      await Router.#addViewToRouterDom(Router.#default)
    })
  }

  async #renderView (event) {
    event.preventDefault()
    history.pushState({}, '', event.target.href)
    await Router.#addViewToRouterDom(window.location.pathname || Router.#default)
    return false
  }

  static async #addViewToRouterDom (route) {
    const { view, prescript } = Router.#routes[route]
    Router.#root.innerHTML = await (await fetch(`/views/${view}.html`)).text()
    if (prescript) {
      const [modulePath, func] = prescript.split('#')
      import(`../../${modulePath}.js`).then(mod => mod[func]())
    }
  }
}
