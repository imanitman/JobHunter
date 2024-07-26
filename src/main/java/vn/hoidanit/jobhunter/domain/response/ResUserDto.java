package vn.hoidanit.jobhunter.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUserDto {
    private String name;
    private String email;
    private String address;
    private GenderEnum gender;
    private int age;
    private CompanyDto company;

    @Setter
    @Getter
    public static class CompanyDto {
        private long id;
        private String name;
    }
    
}
