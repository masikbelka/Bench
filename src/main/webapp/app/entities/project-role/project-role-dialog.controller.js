(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectRoleDialogController', ProjectRoleDialogController);

    ProjectRoleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectRole'];

    function ProjectRoleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectRole) {
        var vm = this;

        vm.projectRole = entity;
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
            if (vm.projectRole.id !== null) {
                ProjectRole.update(vm.projectRole, onSaveSuccess, onSaveError);
            } else {
                ProjectRole.save(vm.projectRole, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:projectRoleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
