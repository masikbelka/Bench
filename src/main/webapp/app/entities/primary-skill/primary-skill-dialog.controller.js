(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('PrimarySkillDialogController', PrimarySkillDialogController);

    PrimarySkillDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PrimarySkill', 'SkillCategory'];

    function PrimarySkillDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PrimarySkill, SkillCategory) {
        var vm = this;

        vm.primarySkill = entity;
        vm.clear = clear;
        vm.save = save;
        vm.skillcategories = SkillCategory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.primarySkill.id !== null) {
                PrimarySkill.update(vm.primarySkill, onSaveSuccess, onSaveError);
            } else {
                PrimarySkill.save(vm.primarySkill, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:primarySkillUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
