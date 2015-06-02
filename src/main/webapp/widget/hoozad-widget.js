'use strict';

angular
    .module('hoozadWidget', ['angucomplete-alt'])
    .controller('MainController', ['$scope', '$http',
        function MainController($scope, $http) {
            $scope.people = [
                {firstName: "Abdurahman", surname: "Jafar"},
                {firstName: "Alex", surname: "Fernandez"},
                {firstName: "Hass", surname: "Khafaji"},
                {firstName: "Lina", surname: "Naim"}
            ];
        }
    ]);


Polymer({
    is: 'hoozad-widget',
    properties: {
        text: {
            type: String,
            value: 'Sample text'
        }
    }
});
