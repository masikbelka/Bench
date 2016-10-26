(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProbationStatusDialogController', ProbationStatusDialogController);

    ProbationStatusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProbationStatus'];

    function ProbationStatusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProbationStatus) {
        var vm = this;

        vm.probationStatus = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.probationStatus.id !== null) {
                ProbationStatus.update(vm.probationStatus, onSaveSuccess, onSaveError);
            } else {
                ProbationStatus.save(vm.probationStatus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:probationStatusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
