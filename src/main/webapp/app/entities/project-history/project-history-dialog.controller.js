(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectHistoryDialogController', ProjectHistoryDialogController);

    ProjectHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ProjectHistory', 'Employee', 'ProjectRole', 'Project'];

    function ProjectHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ProjectHistory, Employee, ProjectRole, Project) {
        var vm = this;

        vm.projectHistory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employees = Employee.query();
        vm.roles = ProjectRole.query({filter: 'projecthistory-is-null'});
        $q.all([vm.projectHistory.$promise, vm.roles.$promise]).then(function() {
            if (!vm.projectHistory.role || !vm.projectHistory.role.id) {
                return $q.reject();
            }
            return ProjectRole.get({id : vm.projectHistory.role.id}).$promise;
        }).then(function(role) {
            vm.roles.push(role);
        });
        vm.projects = Project.query({filter: 'projecthistory-is-null'});
        $q.all([vm.projectHistory.$promise, vm.projects.$promise]).then(function() {
            if (!vm.projectHistory.project || !vm.projectHistory.project.id) {
                return $q.reject();
            }
            return Project.get({id : vm.projectHistory.project.id}).$promise;
        }).then(function(project) {
            vm.projects.push(project);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projectHistory.id !== null) {
                ProjectHistory.update(vm.projectHistory, onSaveSuccess, onSaveError);
            } else {
                ProjectHistory.save(vm.projectHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:projectHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
