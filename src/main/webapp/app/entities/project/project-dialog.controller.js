(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('ProjectDialogController', ProjectDialogController);

    ProjectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Project', 'BillingConcept', 'BillingType', 'ProjectCategory'];

    function ProjectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Project, BillingConcept, BillingType, ProjectCategory) {
        var vm = this;

        vm.project = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.billingconcepts = BillingConcept.query({filter: 'project-is-null'});
        $q.all([vm.project.$promise, vm.billingconcepts.$promise]).then(function() {
            if (!vm.project.billingConcept || !vm.project.billingConcept.id) {
                return $q.reject();
            }
            return BillingConcept.get({id : vm.project.billingConcept.id}).$promise;
        }).then(function(billingConcept) {
            vm.billingconcepts.push(billingConcept);
        });
        vm.billingtypes = BillingType.query({filter: 'project-is-null'});
        $q.all([vm.project.$promise, vm.billingtypes.$promise]).then(function() {
            if (!vm.project.billingType || !vm.project.billingType.id) {
                return $q.reject();
            }
            return BillingType.get({id : vm.project.billingType.id}).$promise;
        }).then(function(billingType) {
            vm.billingtypes.push(billingType);
        });
        vm.categories = ProjectCategory.query({filter: 'project-is-null'});
        $q.all([vm.project.$promise, vm.categories.$promise]).then(function() {
            if (!vm.project.category || !vm.project.category.id) {
                return $q.reject();
            }
            return ProjectCategory.get({id : vm.project.category.id}).$promise;
        }).then(function(category) {
            vm.categories.push(category);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.project.id !== null) {
                Project.update(vm.project, onSaveSuccess, onSaveError);
            } else {
                Project.save(vm.project, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:projectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
