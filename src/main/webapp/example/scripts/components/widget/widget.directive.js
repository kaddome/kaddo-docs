/* globals $ */
'use strict';

angular.module('ecommerceApp')
    .directive('hoozadWidget', function() {
        return {
            restrict: 'A',
            require: 'form',
            link: function (scope, element) {
            }
        };
    });
