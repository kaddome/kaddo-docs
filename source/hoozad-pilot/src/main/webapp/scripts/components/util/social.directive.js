'use strict';

angular.module('hoozadApp')
    .directive('jhSocial', function() {
        // these link to functionality provided by spring-social
        var authLinks = {
            'facebook': '/auth/facebook?scope=public_profile,email',
            'google-plus': '/auth/google?scope=https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email'
        };
        return {
            restrict: 'E',
            replace: true,
            transclude: true,
            link: function(scope, element, attrs, ctrl, transclude) {
                scope.$watch(attrs['externalAccountProvider'],
                    function(externalAccountProvider) {
                        if (externalAccountProvider !== undefined) {
                            // build either a block style or icon style button
                            var type = attrs['type'];
                            if (type === 'block') {
                                element.addClass('btn-block btn-social');
                            }
                            else if (type === 'icon') {
                                element.addClass('btn-social-icon btn-sm');
                            }
                            // turn the enumerated value into something more javascript/css friendly
                            var clientSideProviderName = externalAccountProvider.toLowerCase();
                            if (clientSideProviderName === "google") {
                                // workaround for google
                                clientSideProviderName = "google-plus";
                            }
                            // add classes common to both block and button styles. for example, "btn-facebook" and "fa-facebook".
                            element.addClass("btn-" + clientSideProviderName);
                            element.find('i').addClass("fa-" + clientSideProviderName);
                            // if the caller wants to create a link to authenticate with the service
                            if ('createAuthLink' in attrs) {
                                element.attr('href', authLinks[clientSideProviderName]);
                            }
                            // include whatever content the caller included in our element
                            element.append(transclude());
                        }
                    }
                );
            },
            template:
            '<a class="btn">' +
            '<i class="fa"></i>' +
            '</a>'
        }
    });
