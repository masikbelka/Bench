(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('JobFunctionDeleteController',JobFunctionDeleteController);

    JobFunctionDeleteController.$inject = ['$uibModalInstance', 'entity', 'JobFunction'];

    function JobFunctionDeleteController($uibModalInstance, entity, JobFunction) {
        var vm = this;

        vm.jobFunction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            JobFunction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
