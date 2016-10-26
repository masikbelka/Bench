'use strict';

describe('Controller Tests', function() {

    describe('BenchPredictions Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBenchPredictions, MockPredictionDetails, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBenchPredictions = jasmine.createSpy('MockBenchPredictions');
            MockPredictionDetails = jasmine.createSpy('MockPredictionDetails');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BenchPredictions': MockBenchPredictions,
                'PredictionDetails': MockPredictionDetails,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("BenchPredictionsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'benchApp:benchPredictionsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
