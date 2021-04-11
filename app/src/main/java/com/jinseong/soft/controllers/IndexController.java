package com.jinseong.soft.controllers;

import com.jinseong.soft.application.LinkService;
import com.jinseong.soft.domain.Link;
import com.jinseong.soft.domain.Tag;
import com.jinseong.soft.dto.LinkResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    private final LinkService linkService;

    public IndexController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping
    public String index(Model model) {
        List<LinkResponseData> links = linkService.getLinks()
                .stream()
                .map(LinkResponseData::convertLinkToLinkData)
                .collect(Collectors.toList());

        model.addAttribute("links", links);
        return "index";
    }

    @GetMapping("update-link/{id}")
    public String updateLink(@PathVariable Long id, Model model) {
        Link link = linkService.getLink(id);
        String tags = link.getTags()
                .stream()
                .map(Tag::getTitle)
                .collect(Collectors.joining(","));

        model.addAttribute("link", LinkResponseData.convertLinkToLinkData(link));
        model.addAttribute("tags", tags);

        return "update-link";
    }
}
