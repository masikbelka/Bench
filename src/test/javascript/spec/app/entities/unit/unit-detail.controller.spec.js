'use strict';

describe('Controller Tests', function() {

    describe('Unit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUnit, MockEmployee, MockLocation, MockPrimarySkill;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUnit = jasmine.createSpy('MockUnit');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockLocation = jasmine.createSpy('MockLocation');
            MockPrimarySkill = jasmine.createSpy('MockPrimarySkill');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Unit': MockUnit,
                'Employee': MockEmployee,
                'Location': MockLocation,
                'PrimarySkill': MockPrimarySkill
            };
            createController = function() {
                $injector.get('$controller')("UnitDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'benchApp:unitUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
