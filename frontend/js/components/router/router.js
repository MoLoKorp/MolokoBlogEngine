import config from '../../config/config.js'

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
  static #defaultView

  constructor () {
    super()

    Router.#defaultView = this.getAttribute('defaultView')

    Router.#root = document.createElement('div')
    Router.#root.id = 'root'
    this.after(Router.#root)

    const script = document.createElement('script')
    script.type = 'module'
    script.id = 'script'
    document.getElementsByTagName('head')[0].appendChild(script)

    for (const route of this.getElementsByTagName('route')) {
      Router.#routes[route.getAttribute('path')] = route.getAttribute('view')
    }

    for (const el of document.getElementsByClassName('routed')) {
      el.onclick = this.#renderView
    }

    window.addEventListener('popstate', async function () {
      await Router.#addViewToRouterDom(Router.#routes[window.location.pathname] || Router.#defaultView)
    })

    window.addEventListener('DOMContentLoaded', async function () {
      await Router.#addViewToRouterDom(Router.#defaultView)
    })
  }

  async #renderView (event) {
    event.preventDefault()
    history.pushState({}, '', event.target.href)
    await Router.#addViewToRouterDom(Router.#routes[window.location.pathname] || Router.#defaultView)
    return false
  }

  static async #addViewToRouterDom (view) {
    Router.#root.innerHTML = await (await fetch(`${config.apiUrl}/views/${view}.html`)).text()

    document.getElementById('script').remove()

    const script = document.createElement('script')
    script.id = 'script'
    script.type = 'module'
    script.src = `views/${view}.js?${new Date().getTime()}`
    document.getElementsByTagName('head')[0].appendChild(script)
  }
}
