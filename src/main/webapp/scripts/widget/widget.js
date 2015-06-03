'use strict';

angular.module('shared-components', []);
angular.module('hoozadWidget', ['angucomplete-alt','ngResource','shared-components'])
    .factory('RequestService', function RequestService(){
        return {
            request: function request(config) {
               //config.url = 'http://localhost:8080' + config.url ;
               return config;
            }
        }
    })

    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('RequestService');
    }]);

Polymer({
    is: 'hoozad-widget',
    properties: {
        text: {
            type: String,
            value: 'Sample text'
        }
    }
});
