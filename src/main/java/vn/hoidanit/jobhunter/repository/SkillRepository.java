package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import java.util.List;


@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill>  {
    boolean existsByName(String name);
    Skill findById(long id);
    Skill save(Skill skill);
    List<Skill> findAll();
    List<Skill> findByIdIn(List<Long> id);
    Void deleteById(long id);
    boolean existsById(long id);
}
