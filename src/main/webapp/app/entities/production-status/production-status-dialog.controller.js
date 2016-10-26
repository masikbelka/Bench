(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProductionStatusDialogController', ProductionStatusDialogController);

    ProductionStatusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProductionStatus'];

    function ProductionStatusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProductionStatus) {
        var vm = this;

        vm.productionStatus = entity;
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
            if (vm.productionStatus.id !== null) {
                ProductionStatus.update(vm.productionStatus, onSaveSuccess, onSaveError);
            } else {
                ProductionStatus.save(vm.productionStatus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:productionStatusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
