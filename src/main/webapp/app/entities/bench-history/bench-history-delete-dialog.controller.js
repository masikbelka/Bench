(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BenchHistoryDeleteController',BenchHistoryDeleteController);

    BenchHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'BenchHistory'];

    function BenchHistoryDeleteController($uibModalInstance, entity, BenchHistory) {
        var vm = this;

        vm.benchHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BenchHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
