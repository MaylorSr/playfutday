package com.salesianos.triana.playfutday.data.post.service;

import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repo;


    public List<PostResponse> findAllPost() {
        return repo.findAll().stream().map(PostResponse::of).toList();
    }


}
