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
            <app-router default="/">
                <route path="/" view="firstView" />
                <route path="/secondView" view="secondView" />
            </app-router>
        </body>`
  })

  test('mounts root div', async () => {
    const rootDiv = document.getElementById('root')

    expect(rootDiv).not.toBeNull()
    expect(rootDiv.firstChild).toBeNull()
  })

  test('open the second view and go back to the first', async () => {
    document.getElementsByClassName('routed')[0].click()
    await new Promise(resolve => setTimeout(resolve, 10))

    let rootDiv = document.getElementById('root')
    expect(rootDiv.firstChild).not.toBeNull()
    expect(rootDiv.firstChild.textContent).toEqual('secondView')

    history.back()
    await new Promise(resolve => setTimeout(resolve, 10))

    rootDiv = document.getElementById('root')
    expect(rootDiv.firstChild.textContent).toEqual('firstView')
  })
})
