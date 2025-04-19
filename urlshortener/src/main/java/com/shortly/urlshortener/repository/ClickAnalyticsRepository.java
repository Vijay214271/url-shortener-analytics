package com.shortly.urlshortener.repository;

import com.shortly.urlshortener.entity.ClickAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClickAnalyticsRepository extends MongoRepository<ClickAnalytics, String> {
}
