(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProductionStatusDetailController', ProductionStatusDetailController);

    ProductionStatusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProductionStatus'];

    function ProductionStatusDetailController($scope, $rootScope, $stateParams, previousState, entity, ProductionStatus) {
        var vm = this;

        vm.productionStatus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:productionStatusUpdate', function(event, result) {
            vm.productionStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
