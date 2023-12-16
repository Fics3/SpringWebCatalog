package org.example.springwebcatalog.Services;

import org.example.springwebcatalog.Repositories.TagRepository;
import org.example.springwebcatalog.Model.Product.Tag;
import org.example.springwebcatalog.Services.ServiceInterfaces.TagService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagManager implements TagService {

    private final TagRepository tagRepository;

    public TagManager(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public Set<Tag> processTagsAndSave(String tagNames) {
        return Arrays.stream(tagNames.split(","))
                .map(String::trim)
                .map(tagName -> {
                    Tag tag = new Tag(tagName.toLowerCase());
                    if (tag.getName() == null || tag.getName().isEmpty()) {
                        tag.setName("other");
                    }
                    saveTag(tag);
                    return tag;
                })
                .collect(Collectors.toSet());

    }

    @Override
    public String tagsToString(Set<Tag> tags) {
        return tags.stream().map(Tag::getName)
                .collect(Collectors.joining(","));
    }


}
