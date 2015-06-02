'use strict';

angular.module('shared-components', []);
angular.module('hoozadWidget', ['angucomplete-alt','ngResource','shared-components']);

Polymer({
    is: 'hoozad-widget',
    properties: {
        text: {
            type: String,
            value: 'Sample text'
        }
    }
});
