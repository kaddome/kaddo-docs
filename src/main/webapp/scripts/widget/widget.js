'use strict';

angular.module('shared-components', []);
angular.module('hoozadWidget', ['ngResource','shared-components'])

    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.defaults.withCredentials = true;

        $httpProvider.interceptors.push(function($rootScope){
            return {
                request: function request(config) {
                   config.url = $rootScope.baseUrl + config.url ;
                   return config;
                }
            }
        });
    }])

    .run(['$rootScope', '$window', function($rootScope, $window) {
        $window.addEventListener('message', function(event) {
            if (~event.origin.indexOf($rootScope.baseUrl) && event.data == 'reload') {
                $rootScope.loadUser();
            }
        });
    }]);

Polymer({
    is: 'hoozad-widget'
});


