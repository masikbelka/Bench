(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProbationStatusDetailController', ProbationStatusDetailController);

    ProbationStatusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProbationStatus'];

    function ProbationStatusDetailController($scope, $rootScope, $stateParams, previousState, entity, ProbationStatus) {
        var vm = this;

        vm.probationStatus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:probationStatusUpdate', function(event, result) {
            vm.probationStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
