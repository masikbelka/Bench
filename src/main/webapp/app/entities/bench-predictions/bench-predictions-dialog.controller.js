(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BenchPredictionsDialogController', BenchPredictionsDialogController);

    BenchPredictionsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'BenchPredictions', 'PredictionDetails', 'Employee'];

    function BenchPredictionsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, BenchPredictions, PredictionDetails, Employee) {
        var vm = this;

        vm.benchPredictions = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.details = PredictionDetails.query({filter: 'benchpredictions-is-null'});
        $q.all([vm.benchPredictions.$promise, vm.details.$promise]).then(function() {
            if (!vm.benchPredictions.details || !vm.benchPredictions.details.id) {
                return $q.reject();
            }
            return PredictionDetails.get({id : vm.benchPredictions.details.id}).$promise;
        }).then(function(details) {
            vm.details.push(details);
        });
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.benchPredictions.id !== null) {
                BenchPredictions.update(vm.benchPredictions, onSaveSuccess, onSaveError);
            } else {
                BenchPredictions.save(vm.benchPredictions, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:benchPredictionsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
