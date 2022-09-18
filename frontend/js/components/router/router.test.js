/**
 * @jest-environment jsdom
 */

import { Router } from './router.js'
import { jest, expect, test, beforeAll, describe, beforeEach } from '@jest/globals'

describe('Router', () => {
  beforeAll(() => {
    customElements.define('app-router', Router)

    global.fetch = jest.fn((url) => {
      if (url.includes('firstView')) {
        return Promise.resolve({
          text: () => '<p>firstView</p>'
        })
      }
      return Promise.resolve({
        text: () => '<p>secondView</p>'
      })
    })
  })

  beforeEach(() => {
    document.body.innerHTML = `
        <body>
            <a href="/secondView" class="routed">
            <app-router defaultView="firstView">
                <route path="/secondView" view="secondView" />
            </app-router>
        </body>`
  })

  test('mounts root div', async () => {
    const rootDiv = document.getElementById('root')
    expect(rootDiv).not.toBeNull()
    expect(rootDiv.firstChild).toBeNull()
  })

  test('renders the second view on the link click', async () => {
    document.getElementsByClassName('routed')[0].click()
    await new Promise(resolve => setTimeout(resolve, 10))

    const rootDiv = document.getElementById('root')
    expect(rootDiv.firstChild).not.toBeNull()
    expect(rootDiv.firstChild.textContent).toEqual('secondView')
    expect(document.head.innerHTML).toContain('script id="script" type="module" src="views/secondView.js')

    history.back()
    await new Promise(resolve => setTimeout(resolve, 10))

    expect(rootDiv.firstChild.textContent).toEqual('firstView')
    expect(document.head.innerHTML).toContain('script id="script" type="module" src="views/firstView.js')
  })

  test('goes back to the first view through the history API', async () => {
    document.getElementsByClassName('routed')[0].click()
    await new Promise(resolve => setTimeout(resolve, 10))
    history.back()
    await new Promise(resolve => setTimeout(resolve, 10))

    const rootDiv = document.getElementById('root')
    expect(rootDiv.firstChild.textContent).toEqual('firstView')
    expect(document.head.innerHTML).toContain('script id="script" type="module" src="views/firstView.js')
  })
})
