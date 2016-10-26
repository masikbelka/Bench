(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('PredictionDetailsDeleteController',PredictionDetailsDeleteController);

    PredictionDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'PredictionDetails'];

    function PredictionDetailsDeleteController($uibModalInstance, entity, PredictionDetails) {
        var vm = this;

        vm.predictionDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PredictionDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
