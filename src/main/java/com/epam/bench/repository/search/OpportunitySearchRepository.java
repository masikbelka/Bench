package com.epam.bench.repository.search;

import com.epam.bench.domain.Opportunity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Opportunity entity.
 */
public interface OpportunitySearchRepository extends ElasticsearchRepository<Opportunity, Long> {
}
