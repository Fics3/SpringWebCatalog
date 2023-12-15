package org.example.springwebcatalog.Services.ServiceInterfaces;

import org.example.springwebcatalog.Model.Product.Tag;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface TagService {


    void saveTag(Tag tag);

    Set<Tag> processTagsAndSave(String tagNames);

    String tagsToString(Set<Tag> tags);
}
