(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('PredictionDetailsDialogController', PredictionDetailsDialogController);

    PredictionDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PredictionDetails', 'Project'];

    function PredictionDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PredictionDetails, Project) {
        var vm = this;

        vm.predictionDetails = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.projects = Project.query({filter: 'predictiondetails-is-null'});
        $q.all([vm.predictionDetails.$promise, vm.projects.$promise]).then(function() {
            if (!vm.predictionDetails.project || !vm.predictionDetails.project.id) {
                return $q.reject();
            }
            return Project.get({id : vm.predictionDetails.project.id}).$promise;
        }).then(function(project) {
            vm.projects.push(project);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.predictionDetails.id !== null) {
                PredictionDetails.update(vm.predictionDetails, onSaveSuccess, onSaveError);
            } else {
                PredictionDetails.save(vm.predictionDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:predictionDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
