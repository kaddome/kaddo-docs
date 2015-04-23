'use strict';

angular.module('ecommerceApp', ['LocalStorageModule', 'tmh.dynamicLocale', 'ngResource', 'ui.router', 'ngCookies', 'pascalprecht.translate', 'ngCacheBuster'])

    .run(function ($rootScope, $location, $http, $state) {
        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;
        });

        $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
            $rootScope.previousStateName = fromState.name;
            $rootScope.previousStateParams = fromParams;
        });

        $rootScope.back = function() {
            if ($state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };
    })

    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, httpRequestInterceptorCacheBusterProvider) {
        //enable CSRF
        $httpProvider.defaults.xsrfCookieName= 'CSRF-TOKEN';
        $httpProvider.defaults.xsrfHeaderName= 'X-CSRF-TOKEN';

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*rest.*/, /.*protected.*/], true);

        $stateProvider.state('site', {
            'abstract': true
        });

        $urlRouterProvider.otherwise('/');

    });
