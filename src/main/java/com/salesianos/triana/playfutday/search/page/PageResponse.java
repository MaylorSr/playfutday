package com.salesianos.triana.playfutday.search.page;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T> {

    private List<T> content;
    private boolean last;
    private boolean first;
    private int totalPages;
    private Long totalElements;

    public PageResponse(Page<T> page){
        this.content = page.getContent();
        this.last = page.isLast();
        this.first = page.isFirst();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }
}
