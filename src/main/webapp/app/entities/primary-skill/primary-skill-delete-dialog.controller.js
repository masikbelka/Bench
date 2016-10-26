(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('PrimarySkillDeleteController',PrimarySkillDeleteController);

    PrimarySkillDeleteController.$inject = ['$uibModalInstance', 'entity', 'PrimarySkill'];

    function PrimarySkillDeleteController($uibModalInstance, entity, PrimarySkill) {
        var vm = this;

        vm.primarySkill = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PrimarySkill.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
