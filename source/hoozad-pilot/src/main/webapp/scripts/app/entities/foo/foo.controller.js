'use strict';

angular.module('hoozadApp')
    .controller('FooController', function ($scope, Foo) {
        $scope.foos = [];
        $scope.loadAll = function() {
            Foo.query(function(result) {
               $scope.foos = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Foo.update($scope.foo,
                function () {
                    $scope.loadAll();
                    $('#saveFooModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Foo.get({id: id}, function(result) {
                $scope.foo = result;
                $('#saveFooModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Foo.get({id: id}, function(result) {
                $scope.foo = result;
                $('#deleteFooConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Foo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFooConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.foo = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
