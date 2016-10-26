(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BenchPredictionsDetailController', BenchPredictionsDetailController);

    BenchPredictionsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BenchPredictions', 'PredictionDetails', 'Employee'];

    function BenchPredictionsDetailController($scope, $rootScope, $stateParams, previousState, entity, BenchPredictions, PredictionDetails, Employee) {
        var vm = this;

        vm.benchPredictions = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:benchPredictionsUpdate', function(event, result) {
            vm.benchPredictions = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
