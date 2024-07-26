package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResDetailUserDto;
import vn.hoidanit.jobhunter.domain.response.ResUserDto;
import vn.hoidanit.jobhunter.domain.response.ResUserUpdateDto;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDto;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.ValidIdException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService){
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User saveUser(User user){
        return this.userRepository.save(user);
    }
    public void deleteUser(long id){
        this.userRepository.deleteById(id);
    }
    public User fetchUserById(long id){
        return this.userRepository.findById(id);
    }
    /**
     * @param specification
     * @param pageable
     * @return
     */
    public ResultPaginationDto fetchAllUser(Specification specification,Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(specification, pageable);
        ResultPaginationDto resultPaginationDto = new ResultPaginationDto();
        ResultPaginationDto.MetaDto metaDto = new ResultPaginationDto.MetaDto();

        metaDto.setPage(pageable.getPageNumber());
        metaDto.setPageSize(pageable.getPageSize());
        metaDto.setPages(pageUser.getTotalPages());
        metaDto.setTotal(pageUser.getTotalElements());

        resultPaginationDto.setMetaDto(metaDto);
        List<ResDetailUserDto> allUsers = new ArrayList<>();
        for (User defaultUser : pageUser.getContent()) {
            ResDetailUserDto resDetailUserDto = this.convertResDetailUserDto(defaultUser);
            allUsers.add(resDetailUserDto);
        }

        resultPaginationDto.setResultPagination(allUsers);

        return resultPaginationDto;


    }
    public User updateOneUser(User user) throws ValidIdException{
        if (!this.isExistIdInDB(user.getId())){
            throw new ValidIdException("We can't find the user that you want to update");
        }
        else{        
        User oldUser = this.userRepository.findById(user.getId());
        if (oldUser != null){
            oldUser.setName(user.getName());
            oldUser.setAddress(user.getAddress());
            oldUser.setAge(user.getAge());
            oldUser.setGender(user.getGender());
            if (user.getCompany() != null){
                oldUser.setCompany(user.getCompany());
            }
            this.saveUser(oldUser);
        }
        return oldUser; 
        }
    }
    public List<User> findAllUserByCompany(Company company){
        return this.userRepository.findByCompany(company);
    }
    public User findUserByEmail(String email){
        User user = this.userRepository.findByEmail(email);
        return user;
    }
    public boolean isExistEmailInDB(String email){
        return this.userRepository.existsByEmail(email);
    }

    public ResUserDto convertToDto(User user){
        ResUserDto resUserDto = new ResUserDto();
        Company company1 = new Company();
        if (user != null){
            resUserDto.setEmail(user.getEmail());
            resUserDto.setName(user.getName());
            resUserDto.setAddress(user.getAddress());
            resUserDto.setAge(user.getAge());
            resUserDto.setGender(user.getGender());
            if (user.getCompany() != null){
               company1 = this.companyService.findCompanyById(user.getCompany().getId()); 
               ResUserDto.CompanyDto com = new ResUserDto.CompanyDto();
               com.setId(company1.getId());
               com.setName(company1.getName());
               resUserDto.setCompany(com);
            }
        }
        return resUserDto;
    }
    public boolean isExistIdInDB(long id){
        return this.userRepository.existsById(id);
    }
    public ResUserUpdateDto convertToResUserUpdateDto(User user){
        ResUserUpdateDto resUserUpdateDto = new ResUserUpdateDto();
        Company company1 = new Company();
        if (user != null){
            resUserUpdateDto.setId(user.getId());
            resUserUpdateDto.setName(user.getName());
            resUserUpdateDto.setAge(user.getAge());
            resUserUpdateDto.setAddress(user.getAddress());
            resUserUpdateDto.setGender(user.getGender());
            resUserUpdateDto.setUpdatedAt(user.getUpdatedAt());
            if (user.getCompany() != null){
                company1 = this.companyService.findCompanyById(user.getCompany().getId()); 
                ResUserUpdateDto.CompanyDto com = new ResUserUpdateDto.CompanyDto();
                com.setId(company1.getId());
                com.setName(company1.getName());
                resUserUpdateDto.setCompany(com);
             }
        }
        return resUserUpdateDto;
    }
    public ResDetailUserDto convertResDetailUserDto (User user){
        ResDetailUserDto resDetailUserDto = new ResDetailUserDto();
        if (user != null){
            resDetailUserDto.setId(user.getId());
            resDetailUserDto.setName(user.getName());
            resDetailUserDto.setEmail(user.getEmail());
            resDetailUserDto.setAddress(user.getAddress());
            resDetailUserDto.setAge(user.getAge());
            resDetailUserDto.setGender(user.getGender());
            if (user.getCompany() != null){
                ResDetailUserDto.CompanyDto company = new ResDetailUserDto.CompanyDto();
                company.setId(user.getCompany().getId());
                company.setName(user.getCompany().getName());
                resDetailUserDto.setCompany(company);
            }
            resDetailUserDto.setCreatedAt(user.getCreatedAt());
            resDetailUserDto.setUpdatedAt(user.getUpdatedAt());
        }
        return resDetailUserDto;
    }

    public void updateUserToken(String token, String email){
        User currentUser = this.findUserByEmail(email);
        if (currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
    public User getUserByRefreshTokenAndEmail(String token, String email){
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }  
}
