(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('PrimarySkillDetailController', PrimarySkillDetailController);

    PrimarySkillDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PrimarySkill', 'SkillCategory'];

    function PrimarySkillDetailController($scope, $rootScope, $stateParams, previousState, entity, PrimarySkill, SkillCategory) {
        var vm = this;

        vm.primarySkill = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:primarySkillUpdate', function(event, result) {
            vm.primarySkill = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
