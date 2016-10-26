(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BenchCommentHistoryDetailController', BenchCommentHistoryDetailController);

    BenchCommentHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BenchCommentHistory', 'User', 'Employee'];

    function BenchCommentHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, BenchCommentHistory, User, Employee) {
        var vm = this;

        vm.benchCommentHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:benchCommentHistoryUpdate', function(event, result) {
            vm.benchCommentHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
