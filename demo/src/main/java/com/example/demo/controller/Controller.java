package com.example.demo.controller;
import com.example.demo.dto.SearchRequest;
import com.example.demo.entities.RepositoryEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.GitHubService;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/github")
public class Controller {

    private final GitHubService gitHubService;

    public Controller(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        List<RepositoryEntity> repos = gitHubService.searchAndStore(request.getQuery(), request.getLanguage(), request.getSort());
        return ResponseEntity.ok(Map.of(
                "message", "Repositories fetched and saved successfully",
                "repositories", repos
        ));
    }

    @GetMapping("/repositories")
    public List<RepositoryEntity> getRepos(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort) {

        return gitHubService.filterRepositories(language, minStars, sort);
    }


}
