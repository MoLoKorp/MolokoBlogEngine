'use strict';

function Router(routes) {
    try {
        if(!routes) {
            throw 'error: routes param is mandatory';
        }

        this.constructor(routes);
        this.init();
    } catch(e) {
        console.log(e);
    }
}

Router.prototype = {
    routes: undefined,
    rootElem: undefined,

    constructor: function(routes) {
        this.routes = routes;
        this.rootElem = document.getElementById('molokoApp');
    },

    init: function() {
        let r = this.routes;
        (function(scope, r) {
            window.addEventListener('hashchange', function(e) {
                scope.hasChanged(scope, r);
            });
        }) (this, r);
    },

    hasChanged: function(scope, r) {
        if (window.location.hash.length > 0) {
            for(let i = 0; i < r.length; i++) {
                if (r[i].isActiveRoute(window.location.hash.substr(1))) {
                    scope.goToRoute(r[i].htmlName);
                }
            }
        } else {
            for(let i = 0; i < r.length; i++) {
                if(r[i].default) {
                    scope.goToRoute(r[i].htmlName);
                }
            }
        }
    },

    goToRoute: function(htmlName) {
        (function(scope) {
            fetch('views/' + htmlName)
            .then((response) => { return response.text(); })
            .then((html) => { scope.rootElem.innerHTML = html;})
        }) (this);

        let script = document.createElement('script');
        script.setAttribute("src", "js/" + htmlName.slice(0, -5) + ".js");
        script.setAttribute("defer", "defer");
        this.rootElem.appendChild(script);
    }
}