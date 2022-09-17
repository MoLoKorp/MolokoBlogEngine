import  MenuBar  from 'frontend/js/components/menuBar/menuBar.js'

import { test, beforeEach } from '@jest/globals'

let menuI = [
    {'leftItem': 'Home'},
    {'leftItem': 'Export'},
    {'leftItem': 'Import'},
    {'rightItem': 'Sing Up'},
    {'rightItem': 'Login'}
]

beforeEach(() => {
    document.body.innerHTML = `
        <body>
          <template id="menu">
            <ul class="menu-base">
                 <li class="menu-logo-li" id="logo"><img class="menu-item-logo" src="" alt="Home" id="img-logo"/></li>
                 <li class="menu-item"><a class="active menu-item-link" href=""></a></li>
            </ul>
          </template>
        </body>`
})

test('add menu item', () => {
    MenuBar()
    // menuBar.addMenuItem(menuI, )
})
