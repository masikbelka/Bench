(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('LanguageLevelDeleteController',LanguageLevelDeleteController);

    LanguageLevelDeleteController.$inject = ['$uibModalInstance', 'entity', 'LanguageLevel'];

    function LanguageLevelDeleteController($uibModalInstance, entity, LanguageLevel) {
        var vm = this;

        vm.languageLevel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LanguageLevel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
