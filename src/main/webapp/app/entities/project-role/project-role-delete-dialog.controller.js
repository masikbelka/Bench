(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectRoleDeleteController',ProjectRoleDeleteController);

    ProjectRoleDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectRole'];

    function ProjectRoleDeleteController($uibModalInstance, entity, ProjectRole) {
        var vm = this;

        vm.projectRole = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjectRole.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
