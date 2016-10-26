(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('TitleDeleteController',TitleDeleteController);

    TitleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Title'];

    function TitleDeleteController($uibModalInstance, entity, Title) {
        var vm = this;

        vm.title = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Title.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
