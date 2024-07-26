package vn.hoidanit.jobhunter.domain.response;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.StatusEnum;

@Getter
@Setter
public class ResDetailCv {
    private long id;

    private String email;
    private String url;
    private StatusEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private UserDto user;
    private JobDto job;
    private String companyName;

    @Getter
    @Setter
    public static class UserDto {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    public static class JobDto{
        private long id;
        private String name;
    } 
}
