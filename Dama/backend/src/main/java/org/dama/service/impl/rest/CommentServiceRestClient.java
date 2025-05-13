package org.dama.service.impl.rest;

import org.dama.service.CommentService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.dama.entity.Comment;
import java.util.Arrays;
import java.util.List;

@Service
@Profile("client")
public class CommentServiceRestClient implements CommentService {
    private static final String URL = "http://localhost:8080/api/comment";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void addComment(Comment comment) {
        restTemplate.postForObject(URL, comment, Void.class);
    }

    @Override
    public List<Comment> getComments(String game) {
        Comment[] comments = restTemplate.getForObject(URL + "/" + game, Comment[].class);
        return comments != null ? Arrays.asList(comments) : List.of();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Reset not supported via REST client.");
    }
}
