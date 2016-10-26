(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('LanguageLevelSearch', LanguageLevelSearch);

    LanguageLevelSearch.$inject = ['$resource'];

    function LanguageLevelSearch($resource) {
        var resourceUrl =  'api/_search/language-levels/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
