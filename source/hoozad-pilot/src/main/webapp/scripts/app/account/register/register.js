'use strict';

angular.module('hoozadApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('register', {
                abstract: 'true',
                parent: 'account',
                url: '/register',
                templateUrl: 'scripts/app/account/register/register.html',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/register/register.html',
                        controller: 'RegisterController'
                    }

                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        return $translate.refresh();
                    }]
                }
            })
            .state('register.default', {
                url: '',
                views: {
                    'registerContent@register': {
                        templateUrl: 'scripts/app/account/register/register.default.html'
                    }

                }
            })
            .state('register.external', {
                url: '/external',
                views: {
                    'registerContent@register': {
                        templateUrl: 'scripts/app/account/register/external/register.external.html',
                        controller: 'RegisterExternalController'
                    }

                }
            });
    });
