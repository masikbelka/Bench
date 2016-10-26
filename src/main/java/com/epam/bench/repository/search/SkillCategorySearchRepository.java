package com.epam.bench.repository.search;

import com.epam.bench.domain.SkillCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SkillCategory entity.
 */
public interface SkillCategorySearchRepository extends ElasticsearchRepository<SkillCategory, Long> {
}
