(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('TitleDialogController', TitleDialogController);

    TitleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Title'];

    function TitleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Title) {
        var vm = this;

        vm.title = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.title.id !== null) {
                Title.update(vm.title, onSaveSuccess, onSaveError);
            } else {
                Title.save(vm.title, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:titleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
