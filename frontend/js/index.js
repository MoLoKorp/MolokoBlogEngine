import { Router } from './components/router/router.js'

customElements.define('app-router', Router)

for (const event of ['click', 'submit', 'change']) {
  document.addEventListener(event, e => {
    if (e.target.getAttribute(`on#${event}`)) {
      const [modulePath, func, ...args] = e.target.getAttribute(`on#${event}`).split('#')
      import(`${modulePath}.js`).then(mod => mod[func](e, ...args))
    }
  })
}
