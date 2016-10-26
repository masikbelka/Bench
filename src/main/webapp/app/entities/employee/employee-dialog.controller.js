(function() {
    'use strict';

    angular
        .module('benchApp')
        .controller('EmployeeDialogController', EmployeeDialogController);

    EmployeeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Employee', 'ProbationStatus', 'Location', 'PrimarySkill', 'Title', 'LanguageLevel', 'ProductionStatus', 'JobFunction', 'Unit', 'BenchHistory', 'ProjectHistory', 'BenchPredictions', 'OpportunityPosition'];

    function EmployeeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Employee, ProbationStatus, Location, PrimarySkill, Title, LanguageLevel, ProductionStatus, JobFunction, Unit, BenchHistory, ProjectHistory, BenchPredictions, OpportunityPosition) {
        var vm = this;

        vm.employee = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.probations = ProbationStatus.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.probations.$promise]).then(function() {
            if (!vm.employee.probation || !vm.employee.probation.id) {
                return $q.reject();
            }
            return ProbationStatus.get({id : vm.employee.probation.id}).$promise;
        }).then(function(probation) {
            vm.probations.push(probation);
        });
        vm.locations = Location.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.locations.$promise]).then(function() {
            if (!vm.employee.location || !vm.employee.location.id) {
                return $q.reject();
            }
            return Location.get({id : vm.employee.location.id}).$promise;
        }).then(function(location) {
            vm.locations.push(location);
        });
        vm.primaryskills = PrimarySkill.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.primaryskills.$promise]).then(function() {
            if (!vm.employee.primarySkill || !vm.employee.primarySkill.id) {
                return $q.reject();
            }
            return PrimarySkill.get({id : vm.employee.primarySkill.id}).$promise;
        }).then(function(primarySkill) {
            vm.primaryskills.push(primarySkill);
        });
        vm.titles = Title.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.titles.$promise]).then(function() {
            if (!vm.employee.title || !vm.employee.title.id) {
                return $q.reject();
            }
            return Title.get({id : vm.employee.title.id}).$promise;
        }).then(function(title) {
            vm.titles.push(title);
        });
        vm.englishlevels = LanguageLevel.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.englishlevels.$promise]).then(function() {
            if (!vm.employee.englishLevel || !vm.employee.englishLevel.id) {
                return $q.reject();
            }
            return LanguageLevel.get({id : vm.employee.englishLevel.id}).$promise;
        }).then(function(englishLevel) {
            vm.englishlevels.push(englishLevel);
        });
        vm.productionstatuses = ProductionStatus.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.productionstatuses.$promise]).then(function() {
            if (!vm.employee.productionStatus || !vm.employee.productionStatus.id) {
                return $q.reject();
            }
            return ProductionStatus.get({id : vm.employee.productionStatus.id}).$promise;
        }).then(function(productionStatus) {
            vm.productionstatuses.push(productionStatus);
        });
        vm.jobfunctions = JobFunction.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.jobfunctions.$promise]).then(function() {
            if (!vm.employee.jobFunction || !vm.employee.jobFunction.id) {
                return $q.reject();
            }
            return JobFunction.get({id : vm.employee.jobFunction.id}).$promise;
        }).then(function(jobFunction) {
            vm.jobfunctions.push(jobFunction);
        });
        vm.units = Unit.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.units.$promise]).then(function() {
            if (!vm.employee.unit || !vm.employee.unit.id) {
                return $q.reject();
            }
            return Unit.get({id : vm.employee.unit.id}).$promise;
        }).then(function(unit) {
            vm.units.push(unit);
        });
        vm.benchhistories = BenchHistory.query();
        vm.projecthistories = ProjectHistory.query();
        vm.benchpredictions = BenchPredictions.query();
        vm.opportunitypositions = OpportunityPosition.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.employee.id !== null) {
                Employee.update(vm.employee, onSaveSuccess, onSaveError);
            } else {
                Employee.save(vm.employee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('benchApp:employeeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.hireDate = false;
        vm.datePickerOpenStatus.availableFrom = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
