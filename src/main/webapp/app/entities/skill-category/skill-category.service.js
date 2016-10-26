(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('SkillCategory', SkillCategory);

    SkillCategory.$inject = ['$resource'];

    function SkillCategory ($resource) {
        var resourceUrl =  'api/skill-categories/:id';

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
