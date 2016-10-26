(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectCategoryDialogController', ProjectCategoryDialogController);

    ProjectCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectCategory'];

    function ProjectCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectCategory) {
        var vm = this;

        vm.projectCategory = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projectCategory.id !== null) {
                ProjectCategory.update(vm.projectCategory, onSaveSuccess, onSaveError);
            } else {
                ProjectCategory.save(vm.projectCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:projectCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
