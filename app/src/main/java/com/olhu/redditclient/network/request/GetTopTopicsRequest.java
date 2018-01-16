package com.olhu.redditclient.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@AllArgsConstructor
public class GetTopTopicsRequest {
    public static final int DEFAULT_LIMIT = 10;
    private String after;
    private String before;
    @Setter
    @Builder.Default
    private int limit = DEFAULT_LIMIT;
    @Getter
    private int newPageNumber;
}
