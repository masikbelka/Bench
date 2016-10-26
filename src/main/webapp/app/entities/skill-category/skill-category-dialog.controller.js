(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('SkillCategoryDialogController', SkillCategoryDialogController);

    SkillCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SkillCategory', 'PrimarySkill'];

    function SkillCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SkillCategory, PrimarySkill) {
        var vm = this;

        vm.skillCategory = entity;
        vm.clear = clear;
        vm.save = save;
        vm.primaryskills = PrimarySkill.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.skillCategory.id !== null) {
                SkillCategory.update(vm.skillCategory, onSaveSuccess, onSaveError);
            } else {
                SkillCategory.save(vm.skillCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:skillCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
