(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('OpportunityDialogController', OpportunityDialogController);

    OpportunityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Opportunity', 'OpportunityType', 'OpportunityPosition', 'Location'];

    function OpportunityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Opportunity, OpportunityType, OpportunityPosition, Location) {
        var vm = this;

        vm.opportunity = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.types = OpportunityType.query({filter: 'opportunity-is-null'});
        $q.all([vm.opportunity.$promise, vm.types.$promise]).then(function() {
            if (!vm.opportunity.type || !vm.opportunity.type.id) {
                return $q.reject();
            }
            return OpportunityType.get({id : vm.opportunity.type.id}).$promise;
        }).then(function(type) {
            vm.types.push(type);
        });
        vm.opportunitypositions = OpportunityPosition.query();
        vm.locations = Location.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.opportunity.id !== null) {
                Opportunity.update(vm.opportunity, onSaveSuccess, onSaveError);
            } else {
                Opportunity.save(vm.opportunity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:opportunityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
