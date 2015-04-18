'use strict';

angular.module('hoozadApp')
    .controller('SettingsController', function ($scope, Principal, Auth) {
        $scope.success = null;
        $scope.error = null;
        $scope.placeOptions = { types: ['(cities)']};

        Principal.identity().then(function(account) {
            $scope.settingsAccount = account;
        });

        $scope.save = function () {
            $scope.settingsAccount.deliveryDetails.city = $scope.settingsAccount.deliveryDetails.city.formatted_address;
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                Principal.identity().then(function(account) {
                    $scope.settingsAccount = account;
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };
    });
