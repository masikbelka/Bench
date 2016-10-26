(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('PrimarySkillSearch', PrimarySkillSearch);

    PrimarySkillSearch.$inject = ['$resource'];

    function PrimarySkillSearch($resource) {
        var resourceUrl =  'api/_search/primary-skills/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
