package com.blogschool.blogs.service;

import com.blogschool.blogs.dto.TagDTO;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.PostTagEntity;
import com.blogschool.blogs.entity.TagEntity;
import com.blogschool.blogs.exception.PostTagException;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.PostTagRepository;
import com.blogschool.blogs.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostTagService {
    private final PostTagRepository postTagRepository;

    private final TagRepository tagRepository;

    @Autowired
    public PostTagService(PostTagRepository postTagRepository, TagRepository tagRepository) {
        this.postTagRepository = postTagRepository;
        this.tagRepository = tagRepository;
    }

    public void insertPostTag(Set<TagDTO> tagDTOList, BlogPostEntity blogPostEntity) {
        for (TagDTO tagDTO : tagDTOList) {
            Optional<TagEntity> tagEntity = tagRepository.findById(tagDTO.getTagId());
            if (tagEntity.isPresent()) {
                Optional<PostTagEntity> postTagEntity = postTagRepository.findByPostAndTag(blogPostEntity, tagEntity.get());
                if (postTagEntity.isEmpty()) {
                    PostTagEntity entity = new PostTagEntity(blogPostEntity, tagEntity.get());
                    postTagRepository.save(entity);
                }
            }
        }
    }

}


