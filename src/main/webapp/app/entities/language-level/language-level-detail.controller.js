(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('LanguageLevelDetailController', LanguageLevelDetailController);

    LanguageLevelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LanguageLevel'];

    function LanguageLevelDetailController($scope, $rootScope, $stateParams, previousState, entity, LanguageLevel) {
        var vm = this;

        vm.languageLevel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('benchApp:languageLevelUpdate', function(event, result) {
            vm.languageLevel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
