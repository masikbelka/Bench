(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('OpportunityTypeDialogController', OpportunityTypeDialogController);

    OpportunityTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OpportunityType'];

    function OpportunityTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OpportunityType) {
        var vm = this;

        vm.opportunityType = entity;
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
            if (vm.opportunityType.id !== null) {
                OpportunityType.update(vm.opportunityType, onSaveSuccess, onSaveError);
            } else {
                OpportunityType.save(vm.opportunityType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:opportunityTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
