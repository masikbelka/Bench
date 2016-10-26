(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectCategoryDeleteController',ProjectCategoryDeleteController);

    ProjectCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectCategory'];

    function ProjectCategoryDeleteController($uibModalInstance, entity, ProjectCategory) {
        var vm = this;

        vm.projectCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjectCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
