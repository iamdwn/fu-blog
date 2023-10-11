package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.dto.TagDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.PostTagEntity;
import tech.fublog.FuBlog.entity.TagEntity;
import tech.fublog.FuBlog.repository.PostTagRepository;
import tech.fublog.FuBlog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void insertPostTag(List<TagDTO> tagDTOList, BlogPostEntity blogPostEntity) {
        for (TagDTO tagDTO : tagDTOList) {
            TagEntity tagEntity = tagRepository.findByTagName(tagDTO.getTagName());
            if (tagEntity != null) {
                Optional<PostTagEntity> postTagEntity = postTagRepository.findByPostAndTag(blogPostEntity, tagEntity);
                if (postTagEntity.isEmpty()) {
                    PostTagEntity entity = new PostTagEntity(blogPostEntity, tagEntity);
                    postTagRepository.save(entity);
                }
            }
        }
    }

}


