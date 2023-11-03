package com.devlop.siren.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageInfo {
    private int size;
    private int page;
    private long totalElements;
    private int totalPages;
}
