(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('OpportunityPositionDialogController', OpportunityPositionDialogController);

    OpportunityPositionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'OpportunityPosition', 'Opportunity', 'ProjectRole', 'Employee'];

    function OpportunityPositionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, OpportunityPosition, Opportunity, ProjectRole, Employee) {
        var vm = this;

        vm.opportunityPosition = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.opportunities = Opportunity.query();
        vm.roles = ProjectRole.query({filter: 'opportunityposition-is-null'});
        $q.all([vm.opportunityPosition.$promise, vm.roles.$promise]).then(function() {
            if (!vm.opportunityPosition.role || !vm.opportunityPosition.role.id) {
                return $q.reject();
            }
            return ProjectRole.get({id : vm.opportunityPosition.role.id}).$promise;
        }).then(function(role) {
            vm.roles.push(role);
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
            if (vm.opportunityPosition.id !== null) {
                OpportunityPosition.update(vm.opportunityPosition, onSaveSuccess, onSaveError);
            } else {
                OpportunityPosition.save(vm.opportunityPosition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:opportunityPositionUpdate', result);
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
