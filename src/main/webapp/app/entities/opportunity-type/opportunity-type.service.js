(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('OpportunityType', OpportunityType);

    OpportunityType.$inject = ['$resource'];

    function OpportunityType ($resource) {
        var resourceUrl =  'api/opportunity-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
