(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BillingConceptDialogController', BillingConceptDialogController);

    BillingConceptDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BillingConcept'];

    function BillingConceptDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BillingConcept) {
        var vm = this;

        vm.billingConcept = entity;
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
            if (vm.billingConcept.id !== null) {
                BillingConcept.update(vm.billingConcept, onSaveSuccess, onSaveError);
            } else {
                BillingConcept.save(vm.billingConcept, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:billingConceptUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
