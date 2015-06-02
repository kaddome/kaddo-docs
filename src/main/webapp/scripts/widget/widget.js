'use strict';

angular.module('hoozadWidget', ['angucomplete-alt']);

Polymer({
    is: 'hoozad-widget',
    properties: {
        text: {
            type: String,
            value: 'Sample text'
        }
    }
});
