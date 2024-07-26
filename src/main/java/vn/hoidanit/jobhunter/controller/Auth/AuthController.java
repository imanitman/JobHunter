package vn.hoidanit.jobhunter.controller.Auth;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.ReqLoginDto;
import vn.hoidanit.jobhunter.domain.response.ResLoginDto;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.ValidIdException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RequestMapping("/api/v1")
@RestController
public class AuthController {
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;    
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,  UserService userService){
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDto> postLogin(@Valid @RequestBody ReqLoginDto loginDto) {
        // Authenticate user
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassWord());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // create token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // User currentUserDB = this.userService.fetchUserById()
        ResLoginDto resLoginDto = new ResLoginDto();
        User currentUser = this.userService.findUserByEmail(loginDto.getUserName());
        ResLoginDto.UserLogin userLogin = new ResLoginDto.UserLogin(currentUser.getId(),currentUser.getEmail(),currentUser.getName());
        resLoginDto.setUser(userLogin);
        String access_token = this.securityUtil.createAccessToken(authentication.getName(),resLoginDto.getUser());
        resLoginDto.setAccessToken(access_token);

        // tạo và lưu refresh_token với model user
        String refresh_token = this.securityUtil.createRefreshToken(loginDto.getUserName(), resLoginDto);
        this.userService.updateUserToken(refresh_token, loginDto.getUserName());
        // set cookies
        ResponseCookie responseCookies = ResponseCookie.from("refresh_token", refresh_token)
                                                        .httpOnly(true)
                                                        .secure(true)
                                                        .path("/")
                                                        .maxAge(refreshTokenExpiration)
                                                        .build();
        return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE,responseCookies.toString())
        .body(resLoginDto);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDto.UserGetAccount> getInfoCurrentUser() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = this.userService.findUserByEmail(email);
        ResLoginDto.UserLogin userLogin = new ResLoginDto.UserLogin();
        ResLoginDto.UserGetAccount userGetAccount = new ResLoginDto.UserGetAccount();
        if (currentUser != null){
            userLogin.setId(currentUser.getId());
            userLogin.setEmail(email);
            userLogin.setName(currentUser.getName());
        }
        userGetAccount.setUser(userLogin);
        return ResponseEntity.status(HttpStatus.OK).body(userGetAccount);
    }
    @GetMapping("/auth/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<ResLoginDto> getRefreshToken(@CookieValue(name = "refresh_token", defaultValue = "error") String refresh_token) throws ValidIdException {
        if (refresh_token.equals("error")){
            throw new ValidIdException("You have to transfer cookie for me");
        }
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentUser == null){
            throw new ValidIdException("User doesn't exist");
        }
        ResLoginDto resLoginDto = new ResLoginDto();
        ResLoginDto.UserLogin userLogin = new ResLoginDto.UserLogin(currentUser.getId(),currentUser.getEmail(),currentUser.getName());
        resLoginDto.setUser(userLogin);
        
        String access_token = this.securityUtil.createAccessToken(email,resLoginDto.getUser());
        resLoginDto.setAccessToken(access_token);

        // tạo và lưu refresh_token với model user
        String new_refresh_token = this.securityUtil.createRefreshToken(email, resLoginDto);
        this.userService.updateUserToken(new_refresh_token, email);
        // set cookies
        ResponseCookie responseCookies = ResponseCookie.from("refresh_token", new_refresh_token)
                                                        .httpOnly(true)
                                                        .secure(true)
                                                        .path("/")
                                                        .maxAge(refreshTokenExpiration)
                                                        .build();
        return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE,responseCookies.toString())
        .body(resLoginDto);
    }
    @PostMapping("/auth/logout")
    @ApiMessage("Logout user")
    public ResponseEntity<Void> postLogOutPage() throws ValidIdException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email.equals("")){
            throw new ValidIdException("Access Token is invalid");
        }
        this.userService.updateUserToken(null, email);
        ResponseCookie deleteSpringCookie = ResponseCookie
            .from("refresh_token", null)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);
    }
    
    
    
    
}
