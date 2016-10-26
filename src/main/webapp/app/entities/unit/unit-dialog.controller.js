(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('UnitDialogController', UnitDialogController);

    UnitDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Unit', 'Employee', 'Location', 'PrimarySkill'];

    function UnitDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Unit, Employee, Location, PrimarySkill) {
        var vm = this;

        vm.unit = entity;
        vm.clear = clear;
        vm.save = save;
        vm.owners = Employee.query({filter: 'unit-is-null'});
        $q.all([vm.unit.$promise, vm.owners.$promise]).then(function() {
            if (!vm.unit.owner || !vm.unit.owner.id) {
                return $q.reject();
            }
            return Employee.get({id : vm.unit.owner.id}).$promise;
        }).then(function(owner) {
            vm.owners.push(owner);
        });
        vm.locations = Location.query({filter: 'unit-is-null'});
        $q.all([vm.unit.$promise, vm.locations.$promise]).then(function() {
            if (!vm.unit.location || !vm.unit.location.id) {
                return $q.reject();
            }
            return Location.get({id : vm.unit.location.id}).$promise;
        }).then(function(location) {
            vm.locations.push(location);
        });
        vm.skills = PrimarySkill.query({filter: 'unit-is-null'});
        $q.all([vm.unit.$promise, vm.skills.$promise]).then(function() {
            if (!vm.unit.skill || !vm.unit.skill.id) {
                return $q.reject();
            }
            return PrimarySkill.get({id : vm.unit.skill.id}).$promise;
        }).then(function(skill) {
            vm.skills.push(skill);
        });
        vm.units = Unit.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.unit.id !== null) {
                Unit.update(vm.unit, onSaveSuccess, onSaveError);
            } else {
                Unit.save(vm.unit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:unitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
