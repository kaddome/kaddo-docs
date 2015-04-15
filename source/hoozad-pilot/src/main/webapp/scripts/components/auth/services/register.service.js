'use strict';

angular.module('hoozadApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


