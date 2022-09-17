class MenuBar extends HTMLElement {
    constructor() {
        super()
        this.component = document.querySelector('#menu')
    }
    addMenuItem(menuI, item) {
        let menu_item = this.component.content.querySelector('.menu-item')
        menu_item.removeChild(item)
        menuI.forEach((it) => {
            let itemIter = item.cloneNode(true)
            let menuItemIter = menu_item.cloneNode(true)
            let key = Object.keys(it)[0]
            if (key === 'leftItem'){
                itemIter.classList.add('menu-item-link')
                itemIter.classList.add('active')
                itemIter.innerText = it.leftItem
            }
            if (key === 'rightItem') {
                itemIter.classList.add('menu-item-link')
                menuItemIter.classList.add('right')
                itemIter.innerText = it.rightItem
            }
            menuItemIter.appendChild(itemIter)
            this.component.content.querySelector('.menu-base').appendChild(menuItemIter)
        })
    }
    connectedCallback() {
        let item = this.component.content.querySelector('.menu-item-link')
        if (menuI) { this.addMenuItem(menuI, item) }
        this.appendChild(this.component.content.cloneNode(true))
    }
    attributeChangedCallback(attrName, oldValue, newValue) {
        if (attrName === 'name') {
            if (newValue) {
                let img = this.component.content.querySelector('#img-logo')
                img.src = newValue
            }
        }
    }
}
MenuBar.observedAttributes = ['name'];
window.customElements.define('menu-bar', MenuBar);