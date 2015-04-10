'use strict';

angular.module('hoozadApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
