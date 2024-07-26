package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDto;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository){
        this.skillRepository = skillRepository;
    }

    public boolean isExistSkill(String name){
        return this.skillRepository.existsByName(name);
    }

    public Skill saveSkill(Skill skill){
        return this.skillRepository.save(skill);
    }

    public Skill findSkillById(long id){
        return this.skillRepository.findById(id);
    }
    public Skill updateSkill(Skill skill){
        Skill new_skill = this.findSkillById(skill.getId());
        if (new_skill != null){
            new_skill.setName(skill.getName());
        }
        this.saveSkill(new_skill);
        return new_skill;
    }
    public ResultPaginationDto findAllSkill(Specification<Skill> spec, Pageable pageable){
        Page<Skill> pageSkill = this.skillRepository.findAll(spec,pageable); 
        ResultPaginationDto resultPaginationDto = new ResultPaginationDto();
        ResultPaginationDto.MetaDto  metaDto = new ResultPaginationDto.MetaDto();

        metaDto.setPage(pageable.getPageNumber() + 1);
        metaDto.setPageSize(pageable.getPageSize());

        metaDto.setTotal(pageSkill.getTotalElements());
        metaDto.setPages(pageSkill.getTotalPages());

        resultPaginationDto.setMetaDto(metaDto);
        resultPaginationDto.setResultPagination(pageSkill.getContent());

        return resultPaginationDto;
    }
    public List<Skill> skillExistInDb(List<Long> id){
        return this.skillRepository.findByIdIn(id);
    }
    public void deleteSkill(long id){
        Skill this_skill = this.findSkillById(id);
        List<Job> jobs = this_skill.getJobs();
        for (Job job : jobs){
            job.getSkills().remove(this_skill);
        }
        this.skillRepository.deleteById(id);
    }
    public boolean isIdExist(long id){
        return this.skillRepository.existsById(id);
    }
}
