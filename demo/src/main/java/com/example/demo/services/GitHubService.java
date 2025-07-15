package com.example.demo.services;

import com.example.demo.Repository.RepositoryRepository;
import com.example.demo.entities.RepositoryEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    @Value("${github.api.url}")
    private String githubApiUrl;

    private final RestTemplate restTemplate;
    private final RepositoryRepository repositoryRepository;

    public GitHubService(RestTemplateBuilder builder, RepositoryRepository repositoryRepository) {
        this.restTemplate = builder.build(); // ✅ now this works
        this.repositoryRepository = repositoryRepository;
    }

    public List<RepositoryEntity> searchAndStore(String query, String language, String sort) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.github.com/search/repositories")
                .queryParam("q", query + (language != null ? "+language:" + language : ""))
                .queryParam("sort", sort != null ? sort : "stars")
                .queryParam("order", "desc")
                .queryParam("per_page", 100)
                .toUriString();

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");

        List<RepositoryEntity> repos = new ArrayList<>();

        for (Map<String, Object> item : items) {
            Map<String, Object> owner = (Map<String, Object>) item.get("owner");

            RepositoryEntity entity = new RepositoryEntity(
                    ((Number) item.get("id")).longValue(),
                    (String) item.get("name"),
                    (String) item.get("description"),
                    (String) owner.get("login"),
                    (String) item.get("language"),
                    ((Number) item.get("stargazers_count")).intValue(),
                    ((Number) item.get("forks_count")).intValue(),
                    Instant.parse((String) item.get("updated_at"))
            );

            repos.add(repositoryRepository.save(entity)); // upsert
        }

        return repos;
    }
    public List<RepositoryEntity> filterRepositories(String language, Integer minStars, String sortBy) {
        return repositoryRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (language != null) {
                predicates.add((Predicate) cb.equal(root.get("language"), language));
            }

            if (minStars != null) {
                predicates.add((Predicate) cb.greaterThanOrEqualTo(root.get("stars"), minStars));
            }

            query.orderBy(cb.desc(root.get(sortBy)));

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
