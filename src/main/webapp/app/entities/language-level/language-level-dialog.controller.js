(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('LanguageLevelDialogController', LanguageLevelDialogController);

    LanguageLevelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LanguageLevel'];

    function LanguageLevelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LanguageLevel) {
        var vm = this;

        vm.languageLevel = entity;
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
            if (vm.languageLevel.id !== null) {
                LanguageLevel.update(vm.languageLevel, onSaveSuccess, onSaveError);
            } else {
                LanguageLevel.save(vm.languageLevel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:languageLevelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
