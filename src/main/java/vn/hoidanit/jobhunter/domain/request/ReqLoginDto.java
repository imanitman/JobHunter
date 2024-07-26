package vn.hoidanit.jobhunter.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoginDto {
    @JsonProperty("username")
    @NotBlank(message = "You have to fill the username")
    private String userName;
    
    @JsonProperty("password")
    @NotBlank(message = "You have to fill the password")
    private String passWord;

    private String address;    
}
