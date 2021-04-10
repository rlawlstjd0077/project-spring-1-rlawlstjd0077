package com.jinseong.soft.application;

import com.jinseong.soft.domain.Category;
import com.jinseong.soft.domain.Like;
import com.jinseong.soft.domain.LikeRepository;
import com.jinseong.soft.domain.Link;
import com.jinseong.soft.domain.LinkRepository;
import com.jinseong.soft.domain.Tag;
import com.jinseong.soft.domain.Type;
import com.jinseong.soft.domain.User;
import com.jinseong.soft.dto.LinkData;
import com.jinseong.soft.errors.LinkNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LinkService {
    private final LinkRepository linkRepository;
    private final CategoryService categoryService;
    private final TypeService typeService;
    private final TagService tagService;
    private final LikeRepository likeRepository;

    public LinkService(LinkRepository linkRepository,
                       CategoryService categoryService,
                       TypeService typeService,
                       TagService tagService,
                       LikeRepository likeRepository) {
        this.linkRepository = linkRepository;
        this.categoryService = categoryService;
        this.typeService = typeService;
        this.tagService = tagService;
        this.likeRepository = likeRepository;
    }

    public List<Link> getLinks() {
        return linkRepository.findAll();
    }

    public Link createLink(LinkData linkData, User user) {
        Link link = convertRequestDataToLink(linkData);
        link.setCreatedBy(user);
        return linkRepository.save(link);
    }

    public Link updateLink(Long id, LinkData linkData) {
        Link link = findLink(id);
        Link updateSource = convertRequestDataToLink(linkData);

        link.changeWith(updateSource);
        return link;
    }

    public Link getLink(Long id) {
        return findLink(id);
    }

    public Link deleteLink(Long id) {
        Link link = findLink(id);
        linkRepository.delete(link);
        return link;
    }

    public void addLike(Long id, User user) {
        Link link = findLink(id);

        if (!isAlreadyLike(user, link)) {
            Like like = Like.builder()
                    .link(link)
                    .user(user)
                    .build();
            likeRepository.save(like);
            link.addLike(like);
        }
    }

    private boolean isAlreadyLike(User user, Link link) {
        return likeRepository.findByUserAndLink(user, link)
                .isPresent();
    }

    private Link convertRequestDataToLink(LinkData requestData) {
        Category category = categoryService.getOrCreateCategory(
                requestData.getCategory()
        );

        Type type = typeService.getOrCreateType(
                requestData.getType()
        );

        Set<Tag> tags = requestData.getTags()
                .stream()
                .map(tagService::getOrCreateTag)
                .collect(Collectors.toSet());

        return Link.builder()
                .title(requestData.getTitle())
                .linkURL(requestData.getLinkURL())
                .description(requestData.getDescription())
                .category(category)
                .type(type)
                .tags(tags)
                .build();
    }

    private Link findLink(Long id) {
        return linkRepository.findById(id)
                .orElseThrow(() -> new LinkNotFoundException(id));
    }
}
