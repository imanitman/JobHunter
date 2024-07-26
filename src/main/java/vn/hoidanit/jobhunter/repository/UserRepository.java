package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import java.util.List;


@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User, Long>, JpaSpecificationExecutor<User>{
    User save(User user);
    void deleteById(long id);
    User findById(long id);
    List<User> findAll();
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsById(long id);
    User findByRefreshTokenAndEmail(String refreshToken, String email);
    List<User> findByCompany(Company company);
    void deleteAll(Iterable<User> users);
}
