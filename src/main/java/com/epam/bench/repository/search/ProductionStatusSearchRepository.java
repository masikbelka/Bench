package com.epam.bench.repository.search;

import com.epam.bench.domain.ProductionStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProductionStatus entity.
 */
public interface ProductionStatusSearchRepository extends ElasticsearchRepository<ProductionStatus, Long> {
}
