package com.salesianos.triana.playfutday.search.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class PageResponse<T> {
    @JsonView({viewUser.UserDetailsByAdmin.class, viewPost.PostResponse.class, viewPost.PostLikeMe.class})
    private List<T> content;

    private boolean last;

    private boolean first;
    @JsonView({viewUser.UserDetailsByAdmin.class, viewPost.PostResponse.class, viewPost.PostLikeMe.class})

    private int totalPages;

    private Long totalElements;


    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.last = page.isLast();
        this.first = page.isFirst();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }
}
