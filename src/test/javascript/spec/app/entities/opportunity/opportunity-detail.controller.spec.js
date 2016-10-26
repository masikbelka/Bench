'use strict';

describe('Controller Tests', function() {

    describe('Opportunity Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOpportunity, MockOpportunityType, MockOpportunityPosition, MockLocation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOpportunity = jasmine.createSpy('MockOpportunity');
            MockOpportunityType = jasmine.createSpy('MockOpportunityType');
            MockOpportunityPosition = jasmine.createSpy('MockOpportunityPosition');
            MockLocation = jasmine.createSpy('MockLocation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Opportunity': MockOpportunity,
                'OpportunityType': MockOpportunityType,
                'OpportunityPosition': MockOpportunityPosition,
                'Location': MockLocation
            };
            createController = function() {
                $injector.get('$controller')("OpportunityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'benchApp:opportunityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
