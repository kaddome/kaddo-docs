'use strict';

angular.module('hoozadApp')
    .controller('RegisterExternalController', function ($scope, $translate, Register) {

        Register.get().$promise.then(
            function(data) {
                var registerAccount = data;
                angular.copy(registerAccount, $scope.registerAccount);

                // for registration, we're only ever linked to a single provider
                $scope.externalAccountProvider = data.externalAccounts[0].externalProvider;
            },
            function(httpResponse) {
                // a 404 means that there isn't an ongoing social registration.  this isn't really an error
                if (httpResponse.status != 404) {
                    $scope.status = 'failed';
                }
            }
        );
    });
