package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.hoidanit.jobhunter.domain.Job;
import java.util.List;


public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    Job save(Job job);
    boolean existsById(long id);
    Job findById(long id);
    void deleteById(long id);
    List<Job> findAll();
}
