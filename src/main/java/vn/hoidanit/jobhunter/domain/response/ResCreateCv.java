package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateCv {
    private long id;
    private  Instant createdAt;
    private String createdBy;
}
