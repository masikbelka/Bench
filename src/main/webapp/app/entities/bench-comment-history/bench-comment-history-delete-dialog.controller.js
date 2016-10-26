(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('BenchCommentHistoryDeleteController',BenchCommentHistoryDeleteController);

    BenchCommentHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'BenchCommentHistory'];

    function BenchCommentHistoryDeleteController($uibModalInstance, entity, BenchCommentHistory) {
        var vm = this;

        vm.benchCommentHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BenchCommentHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
