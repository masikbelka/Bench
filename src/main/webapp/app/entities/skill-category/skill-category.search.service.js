(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('SkillCategorySearch', SkillCategorySearch);

    SkillCategorySearch.$inject = ['$resource'];

    function SkillCategorySearch($resource) {
        var resourceUrl =  'api/_search/skill-categories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
