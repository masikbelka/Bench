(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BenchCommentHistoryDialogController', BenchCommentHistoryDialogController);

    BenchCommentHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'BenchCommentHistory', 'User', 'Employee'];

    function BenchCommentHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, BenchCommentHistory, User, Employee) {
        var vm = this;

        vm.benchCommentHistory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.benchCommentHistory.id !== null) {
                BenchCommentHistory.update(vm.benchCommentHistory, onSaveSuccess, onSaveError);
            } else {
                BenchCommentHistory.save(vm.benchCommentHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:benchCommentHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.changeTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
