package com.jinseong.soft.application;

import com.jinseong.soft.domain.Link;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LinkServiceTest {

    private LinkService linkService = new LinkService();
    private static final Link LINK = Link.builder()
            .id(0L)
            .title("스프링 부트 문서")
            .linkURL("https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/")
            .category("Spring")
            .type("Document")
            .description("스프링 부트에 관한 공식 문서이다.")
            .build();

    @Nested
    @DisplayName("getLinks()")
    class Describe_getLinks {

        @Nested
        @DisplayName("링크가 존재하지 않을 때")
        class Context_with_no_link {
            @Test
            @DisplayName("비어있는 목록을 반환한다")
            void It_returns_empty_list() {
                List<Link> linkList = linkService.getLinks();

                assertThat(linkList).isEmpty();
            }
        }

        @Nested
        @DisplayName("링크가 존재하는 경우")
        class Context_with_links {
            @BeforeEach
            void setUp() {
                linkService.createLink(LINK);
            }

            @Test
            @DisplayName("존재하는 링크 목록을 반환한다")
            void It_returns_exist_link_list() {
                List<Link> linkList = linkService.getLinks();

                assertThat(linkList).isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("createLink()")
    class Describe_createLink {
        Link givenLink = LINK;

        @Test
        @DisplayName("주어진 링크를 저장한 뒤 반환한다.")
        void it_returns_saved_link() {
            Link link = linkService.createLink(givenLink);

            assertThat(link.getTitle()).isEqualTo(givenLink.getTitle());
            assertThat(link.getLinkURL()).isEqualTo(givenLink.getLinkURL());
            assertThat(link.getCategory()).isEqualTo(givenLink.getCategory());
            assertThat(link.getType()).isEqualTo(givenLink.getType());
            assertThat(link.getDescription()).isEqualTo(givenLink.getDescription());
        }
    }

    @Nested
    @DisplayName("updateLink()")
    class Describe_updateKLink {

        @Nested
        @DisplayName("존재하는 링크 id가 주어진다면")
        class Context_exist_link_id {
            Long givenLinkId = LINK.getId();
            Link updateSource = Link.builder()
                    .title("SpringBoot Document")
                    .linkURL("https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/")
                    .category("SpringBoot")
                    .type("Document")
                    .description("This is the official documentation for Spring Boot.")
                    .build();

            @BeforeEach
            void setUp() {
                linkService.createLink(LINK);
            }

            @Test
            @DisplayName("주어진 id와 일치하는 링크를 수정한 뒤 반환한다.")
            void it_returns_updated_link() {
                Link link = linkService.updateLink(givenLinkId, updateSource);

                assertThat(link.getTitle()).isEqualTo(updateSource.getTitle());
                assertThat(link.getLinkURL()).isEqualTo(updateSource.getLinkURL());
                assertThat(link.getCategory()).isEqualTo(updateSource.getCategory());
                assertThat(link.getType()).isEqualTo(updateSource.getType());
                assertThat(link.getDescription()).isEqualTo(updateSource.getDescription());
            }
        }
    }
}