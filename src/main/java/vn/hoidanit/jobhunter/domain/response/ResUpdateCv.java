package vn.hoidanit.jobhunter.domain.response;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateCv {
    private Instant updatedAt;
    private String updatedBy;
}
