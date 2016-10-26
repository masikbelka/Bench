(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectHistoryDeleteController',ProjectHistoryDeleteController);

    ProjectHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectHistory'];

    function ProjectHistoryDeleteController($uibModalInstance, entity, ProjectHistory) {
        var vm = this;

        vm.projectHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjectHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
