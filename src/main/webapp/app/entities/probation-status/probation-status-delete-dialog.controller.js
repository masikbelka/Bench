(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProbationStatusDeleteController',ProbationStatusDeleteController);

    ProbationStatusDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProbationStatus'];

    function ProbationStatusDeleteController($uibModalInstance, entity, ProbationStatus) {
        var vm = this;

        vm.probationStatus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProbationStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
