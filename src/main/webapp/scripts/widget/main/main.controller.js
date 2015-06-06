'use strict';

angular.module('hoozadWidget')
    .controller('MainController', function ($rootScope, $scope, Principal) {

        $rootScope.loadUser = function () {
            Principal.identity(true).then(function(account) {
                $scope.account = account;
                $scope.isAuthenticated = Principal.isAuthenticated;
            });
        };
        $rootScope.loadUser();
    });
