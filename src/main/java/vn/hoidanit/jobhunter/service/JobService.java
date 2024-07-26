package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResCreateJobDto;
import vn.hoidanit.jobhunter.domain.response.ResUpdateJobDto;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDto;
import vn.hoidanit.jobhunter.repository.JobRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }

    public Job saveJob(Job job){
        return this.jobRepository.save(job);
    }

    public ResCreateJobDto convertToResCreateJobDto(Job job){
        ResCreateJobDto resCreateJobDto = new ResCreateJobDto();
        if (job != null){
            resCreateJobDto.setId(job.getId());
            resCreateJobDto.setName(job.getName());
            resCreateJobDto.setLocation(job.getLocation());
            resCreateJobDto.setSalary(job.getSalary());
            resCreateJobDto.setQuantity(job.getQuantity());
            resCreateJobDto.setLevel(job.getLevel());
            resCreateJobDto.setStartDate(job.getStartDate());
            resCreateJobDto.setEndDate(job.getEndDate());
            if (job.getSkills() != null){
                ArrayList<String> skills = new ArrayList<String>();
                for (Skill skill : job.getSkills()){
                    skills.add(skill.getName());
                }
                resCreateJobDto.setSkills(skills);
            }
            resCreateJobDto.setCreatedAt(job.getCreatedAt());
            resCreateJobDto.setCreatedBy(job.getCreatedBy());
            resCreateJobDto.setActive(job.isActive());
        }
        return resCreateJobDto;
    }

    public ResUpdateJobDto convertToResUpdateJobDto(Job job){
        ResUpdateJobDto resUpdateJobDto = new ResUpdateJobDto();
        if (job != null){
            resUpdateJobDto.setId(job.getId());
            resUpdateJobDto.setName(job.getName());
            resUpdateJobDto.setLocation(job.getLocation());
            resUpdateJobDto.setSalary(job.getSalary());
            resUpdateJobDto.setQuantity(job.getQuantity());
            resUpdateJobDto.setLevel(job.getLevel());
            resUpdateJobDto.setStartDate(job.getStartDate());
            resUpdateJobDto.setEndDate(job.getEndDate());
            if (job.getSkills() != null){
                ArrayList<String> skills = new ArrayList<String>();
                for (Skill skill : job.getSkills()){
                    skills.add(skill.getName());
                }
                resUpdateJobDto.setSkills(skills);
            }
            resUpdateJobDto.setUpdatedAt(job.getUpdatedAt());
            resUpdateJobDto.setUpdatedBy(job.getUpdatedBy());
            resUpdateJobDto.setActive(job.isActive());
        }

        return resUpdateJobDto;
    }

    public Job updateJob (Job job){
        Job new_job = this.jobRepository.findById(job.getId());
        if (new_job != null){
            new_job.setId(job.getId());
            new_job.setName((job.getName() != null) ? job.getName() : new_job.getName());
            new_job.setLocation((job.getLocation() != null) ? job.getLocation() : new_job.getLocation());
            new_job.setSalary(job.getSalary());
            new_job.setQuantity(job.getQuantity());
            new_job.setLevel(job.getLevel());
            new_job.setStartDate(job.getStartDate());
            new_job.setEndDate(job.getEndDate());
            new_job.setSkills(job.getSkills());
            new_job.setCreatedAt(new_job.getCreatedAt());
            new_job.setCreatedBy(new_job.getCreatedBy());
            new_job.setUpdatedAt(job.getUpdatedAt());
            new_job.setUpdatedBy(job.getUpdatedBy());
            new_job.setActive(job.isActive());
        }
       return this.saveJob(new_job);
        
    }
    public boolean isExistId(long id){
        return this.jobRepository.existsById(id);
    }
    public Job findById(long id){
        return this.jobRepository.findById(id);
    }

    public void DeleteJob (long id){
        this.jobRepository.deleteById(id);
    }
    public ResultPaginationDto getALLJob(Specification<Job> spec, Pageable pageable){
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);

        ResultPaginationDto resultPaginationDto = new ResultPaginationDto();
        ResultPaginationDto.MetaDto metaDto = new ResultPaginationDto.MetaDto();

        metaDto.setPage(pageable.getPageNumber() + 1);
        metaDto.setPageSize(pageable.getPageSize());

        metaDto.setTotal(pageJob.getTotalElements());
        metaDto.setPages(pageJob.getTotalPages());

        resultPaginationDto.setMetaDto(metaDto);

        resultPaginationDto.setResultPagination(pageJob.getContent());

        return resultPaginationDto;

    }
}

