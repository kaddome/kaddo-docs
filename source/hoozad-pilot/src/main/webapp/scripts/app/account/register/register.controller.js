'use strict';

angular.module('hoozadApp')
    .controller('RegisterController', function ($scope, $translate, Auth) {
        $scope.status = {};
        $scope.registerAccount = {};

        $scope.register = function () {
            $scope.status.success = null;
            $scope.status.error = null;
            $scope.status.doNotMatch = null;
            $scope.status.errorUserExists = null;

            if (angular.isDefined($scope.registerAccount.confirmPassword) && $scope.registerAccount.password !== $scope.confirmPassword) {
                $scope.status.doNotMatch = 'ERROR';
            } else {
                $scope.registerAccount.langKey = $translate.use();

                Auth.createAccount($scope.registerAccount).then(function () {
                    $scope.status.success = 'OK';
                }).catch(function (response) {
                    $scope.status.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        $scope.status.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        $scope.status.errorEmailExists = 'ERROR';
                    } else {
                        $scope.status.error = 'ERROR';
                    }
                });
            }
        };
    });
