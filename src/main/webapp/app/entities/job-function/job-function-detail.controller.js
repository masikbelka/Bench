(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('JobFunctionDetailController', JobFunctionDetailController);

    JobFunctionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JobFunction'];

    function JobFunctionDetailController($scope, $rootScope, $stateParams, previousState, entity, JobFunction) {
        var vm = this;

        vm.jobFunction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:jobFunctionUpdate', function(event, result) {
            vm.jobFunction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
