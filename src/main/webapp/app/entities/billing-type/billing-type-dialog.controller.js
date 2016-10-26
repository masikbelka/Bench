(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BillingTypeDialogController', BillingTypeDialogController);

    BillingTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BillingType'];

    function BillingTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BillingType) {
        var vm = this;

        vm.billingType = entity;
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
            if (vm.billingType.id !== null) {
                BillingType.update(vm.billingType, onSaveSuccess, onSaveError);
            } else {
                BillingType.save(vm.billingType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:billingTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
