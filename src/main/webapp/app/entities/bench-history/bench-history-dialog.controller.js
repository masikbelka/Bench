(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BenchHistoryDialogController', BenchHistoryDialogController);

    BenchHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BenchHistory', 'Employee'];

    function BenchHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BenchHistory, Employee) {
        var vm = this;

        vm.benchHistory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.benchHistory.id !== null) {
                BenchHistory.update(vm.benchHistory, onSaveSuccess, onSaveError);
            } else {
                BenchHistory.save(vm.benchHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:benchHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdTime = false;
        vm.datePickerOpenStatus.validTo = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
