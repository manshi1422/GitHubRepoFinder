package com.example.demo;

import com.example.demo.Repository.RepositoryRepository;
import com.example.demo.entities.RepositoryEntity;
import com.example.demo.services.GitHubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GitHubServiceTest {

	@Mock
	private RepositoryRepository repositoryRepository;

	@Mock
	private RestTemplateBuilder restTemplateBuilder;

	@Mock
	private RestTemplate restTemplate;

	private GitHubService gitHubService;

	@Test
	void testSearchAndSaveNewRepositories() {
		String query = "spring boot";
		String language = "Java";
		String sort = "stars";

		// Mock GitHub API response
		Map<String, Object> mockItem = new HashMap<>();
		mockItem.put("id", 123456);
		mockItem.put("name", "spring-boot-example");
		mockItem.put("description", "An example repo");
		mockItem.put("language", "Java");
		mockItem.put("stargazers_count", 450);
		mockItem.put("forks_count", 120);
		mockItem.put("updated_at", "2024-01-01T12:00:00Z");
		mockItem.put("owner", Map.of("login", "user123"));

		Map<String, Object> mockResponse = Map.of("items", List.of(mockItem));

		// Set up mocks
		when(restTemplateBuilder.build()).thenReturn(restTemplate);
		when(restTemplate.getForEntity(anyString(), eq(Map.class))).thenReturn(ResponseEntity.ok(mockResponse));
		when(repositoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));

		// Inject GitHubService with the mocked builder
		gitHubService = new GitHubService(restTemplateBuilder, repositoryRepository);

		List<RepositoryEntity> result = gitHubService.searchAndStore(query, language, sort);

		assertEquals(1, result.size());
		assertEquals("spring-boot-example", result.get(0).getName());
		assertEquals("user123", result.get(0).getOwner());
	}

	@Test
	void testFilterRepositoriesByLanguageAndStars() {
		RepositoryEntity repo1 = new RepositoryEntity(1L, "Repo1", "desc", "Alice", "Java", 200, 50, Instant.now());
		RepositoryEntity repo2 = new RepositoryEntity(2L, "Repo2", "desc", "Bob", "Java", 300, 80, Instant.now());

		when(repositoryRepository.findAll(any(Specification.class))).thenReturn(List.of(repo1, repo2));

		// Inject GitHubService with dummy RestTemplate (not used in this method)
		gitHubService = new GitHubService(restTemplateBuilder, repositoryRepository);

		List<RepositoryEntity> results = gitHubService.filterRepositories("Java", 100, "stars");

		assertEquals(2, results.size());
		assertEquals("Repo1", results.get(0).getName());
	}
}
