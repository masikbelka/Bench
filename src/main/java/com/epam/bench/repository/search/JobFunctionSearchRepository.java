package com.epam.bench.repository.search;

import com.epam.bench.domain.JobFunction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the JobFunction entity.
 */
public interface JobFunctionSearchRepository extends ElasticsearchRepository<JobFunction, Long> {
}
