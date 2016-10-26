'use strict';

describe('Controller Tests', function() {

    describe('Employee Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEmployee, MockProbationStatus, MockLocation, MockPrimarySkill, MockTitle, MockLanguageLevel, MockProductionStatus, MockJobFunction, MockUnit, MockBenchHistory, MockProjectHistory, MockBenchPredictions, MockOpportunityPosition;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockProbationStatus = jasmine.createSpy('MockProbationStatus');
            MockLocation = jasmine.createSpy('MockLocation');
            MockPrimarySkill = jasmine.createSpy('MockPrimarySkill');
            MockTitle = jasmine.createSpy('MockTitle');
            MockLanguageLevel = jasmine.createSpy('MockLanguageLevel');
            MockProductionStatus = jasmine.createSpy('MockProductionStatus');
            MockJobFunction = jasmine.createSpy('MockJobFunction');
            MockUnit = jasmine.createSpy('MockUnit');
            MockBenchHistory = jasmine.createSpy('MockBenchHistory');
            MockProjectHistory = jasmine.createSpy('MockProjectHistory');
            MockBenchPredictions = jasmine.createSpy('MockBenchPredictions');
            MockOpportunityPosition = jasmine.createSpy('MockOpportunityPosition');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Employee': MockEmployee,
                'ProbationStatus': MockProbationStatus,
                'Location': MockLocation,
                'PrimarySkill': MockPrimarySkill,
                'Title': MockTitle,
                'LanguageLevel': MockLanguageLevel,
                'ProductionStatus': MockProductionStatus,
                'JobFunction': MockJobFunction,
                'Unit': MockUnit,
                'BenchHistory': MockBenchHistory,
                'ProjectHistory': MockProjectHistory,
                'BenchPredictions': MockBenchPredictions,
                'OpportunityPosition': MockOpportunityPosition
            };
            createController = function() {
                $injector.get('$controller')("EmployeeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'benchApp:employeeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
