(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BenchHistoryDetailController', BenchHistoryDetailController);

    BenchHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BenchHistory', 'Employee'];

    function BenchHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, BenchHistory, Employee) {
        var vm = this;

        vm.benchHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:benchHistoryUpdate', function(event, result) {
            vm.benchHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
