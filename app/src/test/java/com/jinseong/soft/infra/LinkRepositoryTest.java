package com.jinseong.soft.infra;

import com.jinseong.soft.LinkTestFixture;
import com.jinseong.soft.UserTestFixture;
import com.jinseong.soft.domain.*;
import com.jinseong.soft.dto.LinkData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.transaction.Transactional;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class LinkRepositoryTest {
    @Autowired
    LinkRepository linkRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void testSaveLinkRepository() {
        LinkData linkData = LinkTestFixture.LINK_REQUEST;

        Category category =
                categoryRepository.save(LinkTestFixture.CATEGORY);
        Type type =
                typeRepository.save(LinkTestFixture.TYPE);
        Tag tag =
                tagRepository.save(LinkTestFixture.TAG);

        User user = userRepository.save(UserTestFixture.EXIST_USER);

        Link source = Link.builder()
                .title(linkData.getTitle())
                .linkURL(linkData.getLinkURL())
                .description(linkData.getDescription())
                .category(category)
                .type(type)
                .tags(Collections.singleton(tag))
                .user(user)
                .build();

        Long id = linkRepository.save(source).getId();

        Link link = linkRepository.findById(id).get();

        assertThat(link.getCategory()).isEqualTo(category);
        assertThat(link.getType()).isEqualTo(type);
        assertThat(link.getTags()).contains(tag);
        assertThat(link.getUser()).isEqualTo(user);
        System.out.println("====" + link.getCreateAt());
    }
}