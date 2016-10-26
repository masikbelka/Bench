package com.epam.bench.repository.search;

import com.epam.bench.domain.BenchPredictions;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BenchPredictions entity.
 */
public interface BenchPredictionsSearchRepository extends ElasticsearchRepository<BenchPredictions, Long> {
}
