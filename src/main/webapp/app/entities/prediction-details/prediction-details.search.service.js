(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('PredictionDetailsSearch', PredictionDetailsSearch);

    PredictionDetailsSearch.$inject = ['$resource'];

    function PredictionDetailsSearch($resource) {
        var resourceUrl =  'api/_search/prediction-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
