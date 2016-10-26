(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('SkillCategoryDetailController', SkillCategoryDetailController);

    SkillCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SkillCategory', 'PrimarySkill'];

    function SkillCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, SkillCategory, PrimarySkill) {
        var vm = this;

        vm.skillCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:skillCategoryUpdate', function(event, result) {
            vm.skillCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
