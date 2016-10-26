(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('SkillCategoryDeleteController',SkillCategoryDeleteController);

    SkillCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'SkillCategory'];

    function SkillCategoryDeleteController($uibModalInstance, entity, SkillCategory) {
        var vm = this;

        vm.skillCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SkillCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
