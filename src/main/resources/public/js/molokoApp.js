'use strict';

class MolokoApp {

static init() {
        let router =
           new Router([
               new Route('admin', 'admin.html')
           ]);
   }
}

MolokoApp.init();