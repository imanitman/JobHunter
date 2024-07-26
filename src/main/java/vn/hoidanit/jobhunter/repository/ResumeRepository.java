package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Resume;
import java.util.List;


@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume>{
    Resume save(Resume resume); 
    boolean existsById(long id);
    Resume findById(long id); 
    void deleteById(long id);
    List<Resume> findAll();
}
