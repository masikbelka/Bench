(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('PredictionDetailsDetailController', PredictionDetailsDetailController);

    PredictionDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PredictionDetails', 'Project'];

    function PredictionDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, PredictionDetails, Project) {
        var vm = this;

        vm.predictionDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:predictionDetailsUpdate', function(event, result) {
            vm.predictionDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
