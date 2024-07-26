package vn.hoidanit.jobhunter.domain.response;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDto {
    @JsonProperty("meta")
    private MetaDto metaDto;
    @JsonProperty("result")
    private Object resultPagination;

    @Getter
    @Setter
    public static class MetaDto{
        private int page;
        private int pageSize;
        private int pages;
        private long total;  
        
    }
}
