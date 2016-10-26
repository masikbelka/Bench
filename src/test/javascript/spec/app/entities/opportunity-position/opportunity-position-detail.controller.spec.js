'use strict';

describe('Controller Tests', function() {

    describe('OpportunityPosition Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOpportunityPosition, MockOpportunity, MockProjectRole, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOpportunityPosition = jasmine.createSpy('MockOpportunityPosition');
            MockOpportunity = jasmine.createSpy('MockOpportunity');
            MockProjectRole = jasmine.createSpy('MockProjectRole');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OpportunityPosition': MockOpportunityPosition,
                'Opportunity': MockOpportunity,
                'ProjectRole': MockProjectRole,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("OpportunityPositionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'benchApp:opportunityPositionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
