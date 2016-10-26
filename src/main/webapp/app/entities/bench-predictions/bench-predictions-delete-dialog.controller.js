(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BenchPredictionsDeleteController',BenchPredictionsDeleteController);

    BenchPredictionsDeleteController.$inject = ['$uibModalInstance', 'entity', 'BenchPredictions'];

    function BenchPredictionsDeleteController($uibModalInstance, entity, BenchPredictions) {
        var vm = this;

        vm.benchPredictions = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BenchPredictions.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
