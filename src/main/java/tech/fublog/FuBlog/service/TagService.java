package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.dto.TagDTO;
import tech.fublog.FuBlog.entity.TagEntity;
import tech.fublog.FuBlog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagDTO> viewAll() {
        List<TagEntity> entityList = tagRepository.findAll();
        if (!entityList.isEmpty()) {
            List<TagDTO> dtoList = new ArrayList<>();
            for (TagEntity entity : entityList) {
//                for (PostTagEntity entitytag : entity.getPostTags()) {
//                    System.out.println(entitytag.getPost().getId());
//                }
                TagDTO dto = new TagDTO(entity.getId(), entity.getTagName());
                dtoList.add(dto);
            }
            return dtoList;
        } else return new ArrayList<>();
    }
}
