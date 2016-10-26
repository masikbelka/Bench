(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('TitleDetailController', TitleDetailController);

    TitleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Title'];

    function TitleDetailController($scope, $rootScope, $stateParams, previousState, entity, Title) {
        var vm = this;

        vm.title = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:titleUpdate', function(event, result) {
            vm.title = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
