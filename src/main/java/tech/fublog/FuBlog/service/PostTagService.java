package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.dto.TagDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.PostTagEntity;
import tech.fublog.FuBlog.entity.TagEntity;
import tech.fublog.FuBlog.repository.PostTagRepository;
import tech.fublog.FuBlog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


