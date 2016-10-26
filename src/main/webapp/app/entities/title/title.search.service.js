(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('TitleSearch', TitleSearch);

    TitleSearch.$inject = ['$resource'];

    function TitleSearch($resource) {
        var resourceUrl =  'api/_search/titles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
