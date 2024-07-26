package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.config.SecurityConfiguaration;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResDetailUserDto;
import vn.hoidanit.jobhunter.domain.response.ResUserDto;
import vn.hoidanit.jobhunter.domain.response.ResUserUpdateDto;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDto;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.ValidIdException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;





@RestController
@RequestMapping("/api/v1")

public class UserController {
    private final UserService userService ;
    private final SecurityConfiguaration securityConfiguaration;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, SecurityConfiguaration securityConfiguaration,PasswordEncoder passwordEncoder ){
        this.userService = userService;
        this.securityConfiguaration = securityConfiguaration;
        this.passwordEncoder = passwordEncoder;
    }
    // @GetMapping("/users/create")
    @PostMapping("/users")
    public ResponseEntity<ResUserDto> createNewUser(@RequestBody User userPostman) throws ValidIdException{
        if (this.userService.isExistEmailInDB(userPostman.getEmail())){
            throw new ValidIdException("Email has already existed !");
        }
        userPostman.setPassword(this.passwordEncoder.encode(userPostman.getPassword()));
        User newUser = this.userService.saveUser(userPostman);
        ResUserDto resUserDto = this.userService.convertToDto(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(resUserDto);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws ValidIdException{
        if (!this.userService.isExistIdInDB(id)){
            throw new ValidIdException("I can't find the id that you want to remove !");
        }
        this.userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("delete successfully");
    }
    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResDetailUserDto> getUser(@PathVariable("id") long id)
    throws ValidIdException{
        if (!this.userService.isExistIdInDB(id)){
            throw new ValidIdException("You have to get the correct id");
        }
        User thisUser = this.userService.fetchUserById(id);
        ResDetailUserDto resDetailUserDto = this.userService.convertResDetailUserDto(thisUser);
        return ResponseEntity.status(HttpStatus.OK).body(resDetailUserDto);
    }
    @GetMapping("/users")
    @ApiMessage("fetch users")
    public ResponseEntity<ResultPaginationDto> getAllUsers(
        @Filter Specification spec,
        Pageable pageable) {
        ResultPaginationDto userPaginationDto = this.userService.fetchAllUser(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(userPaginationDto);

    }
    @PutMapping("/users")
    public ResponseEntity<ResUserUpdateDto> putOneUser(@RequestBody User userUpdate) throws ValidIdException {
        User updateUser =  this.userService.updateOneUser(userUpdate);
        ResUserUpdateDto resUserUpdateDto = this.userService.convertToResUserUpdateDto(updateUser);
        // return ResponseEntity.ok(updateUser) shorter 
        return ResponseEntity.status(HttpStatus.OK).body(resUserUpdateDto);
    }


}
