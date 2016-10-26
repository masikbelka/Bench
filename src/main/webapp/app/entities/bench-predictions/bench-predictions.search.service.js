(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('BenchPredictionsSearch', BenchPredictionsSearch);

    BenchPredictionsSearch.$inject = ['$resource'];

    function BenchPredictionsSearch($resource) {
        var resourceUrl =  'api/_search/bench-predictions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
