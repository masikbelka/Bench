(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('JobFunctionDialogController', JobFunctionDialogController);

    JobFunctionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobFunction'];

    function JobFunctionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, JobFunction) {
        var vm = this;

        vm.jobFunction = entity;
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
            if (vm.jobFunction.id !== null) {
                JobFunction.update(vm.jobFunction, onSaveSuccess, onSaveError);
            } else {
                JobFunction.save(vm.jobFunction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:jobFunctionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
