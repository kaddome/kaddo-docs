'use strict';

angular.module('hoozadWidget')
    .controller('MainController', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.people = [
            {firstName: "Abdurahman", surname: "Jafar"},
            {firstName: "Alex", surname: "Fernandez"},
            {firstName: "Hass", surname: "Khafaji"},
            {firstName: "Lina", surname: "Naim"}
        ];
    });
