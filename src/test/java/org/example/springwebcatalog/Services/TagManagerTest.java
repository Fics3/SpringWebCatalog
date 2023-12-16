package org.example.springwebcatalog.Services;

import org.example.springwebcatalog.Repositories.TagRepository;
import org.example.springwebcatalog.Model.Product.Tag;
import org.example.springwebcatalog.Services.ServiceInterfaces.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TagManagerTest {

    @Mock
    private TagRepository tagRepository;

    private TagService tagManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tagManager = new TagManager(tagRepository);
    }

    @Test
    @DisplayName("saveTag - should call save method in repository")
    void saveTag_shouldCallSaveInRepository() {
        // Arrange
        Tag tag = new Tag("testTag");

        // Act
        tagManager.saveTag(tag);

        // Assert
        verify(tagRepository).save(tag);
    }

    @Test
    @DisplayName("processTagsAndSave - should process tags and save")
    void processTagsAndSave_shouldProcessTagsAndSave() {
        // Arrange
        String tagNames = "tag1, tag2, tag3";
        Set<Tag> expectedTags = new HashSet<>(Set.of(new Tag("tag1"), new Tag("tag2"), new Tag("tag3")));

        // Mock repository save method
        doAnswer(invocation -> {
            Tag savedTag = invocation.getArgument(0);
            expectedTags.remove(savedTag); // Remove saved tag from expected set
            return null;
        }).when(tagRepository).save(any(Tag.class));

        // Act
        Set<Tag> result = tagManager.processTagsAndSave(tagNames);

        // Assert
        verify(tagRepository, times(expectedTags.size())).save(any(Tag.class));
    }


    @Test
    @DisplayName("tagsToString - should convert tags to string")
    void tagsToString_shouldConvertTagsToString() {
        // Arrange
        Set<Tag> tags = new HashSet<>(Set.of(new Tag("tag1"), new Tag("tag2"), new Tag("tag3")));
        String expectedString = "tag1,tag2,tag3";

        // Act
        String result = tagManager.tagsToString(tags);

        // Convert strings to sorted lists
        List<String> expectedList = Arrays.asList(expectedString.split(","));
        List<String> resultList = Arrays.asList(result.split(","));

        // Sort lists
        expectedList.sort(String::compareTo);
        resultList.sort(String::compareTo);

        // Assert
        assertThat(resultList).containsExactlyElementsOf(expectedList);
    }
}
