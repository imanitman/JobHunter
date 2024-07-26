package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Setter
@Getter
public class ResUserUpdateDto {
    private long id;
    private String name;
    private GenderEnum gender;
    private int age;
    private String address;
    private Instant updatedAt;
    private CompanyDto company;

    @Setter
    @Getter
    public static class CompanyDto {
        private long id;
        private String name;
    }
}
