package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResCreateJobDto;
import vn.hoidanit.jobhunter.domain.response.ResUpdateJobDto;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDto;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.error.ValidIdException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final SkillService skillService;
    private final JobService jobService;

    public JobController (SkillService skillService,JobService jobService){
        this.skillService = skillService;
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    public ResponseEntity<ResCreateJobDto> postMethodName(@Valid @RequestBody Job job) {
        List<Long> ids = new ArrayList<Long>();
        for (Skill skill  : job.getSkills()){
            ids.add(skill.getId());
        }
        List<Skill> skills = this.skillService.skillExistInDb(ids);
        job.setSkills(skills);
        Job new_job = this.jobService.saveJob(job);
        ResCreateJobDto resCreateJobDto = this.jobService.convertToResCreateJobDto(new_job);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateJobDto);
    }
    @PutMapping("/jobs")
    public ResponseEntity<ResUpdateJobDto> updateJob(@Valid @RequestBody Job job) throws ValidIdException {
        if (!this.jobService.isExistId(job.getId())){
            throw new ValidIdException("I can't find the job");
        }
        List<Long> ids = new ArrayList<Long>();
        for (Skill skill  : job.getSkills()){
            ids.add(skill.getId());
        }
        List<Skill> skills = this.skillService.skillExistInDb(ids);
        job.setSkills(skills);
        Job new_job = this.jobService.updateJob(job);
        
        ResUpdateJobDto resUpdateJobDto = this.jobService.convertToResUpdateJobDto(new_job);

        return ResponseEntity.status(HttpStatus.OK).body(resUpdateJobDto) ;
    }
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable ("id") long id) throws ValidIdException{
        if (!this.jobService.isExistId(id)){
            throw new ValidIdException("I can't find your job");
        }
        this.jobService.DeleteJob(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/jobs")
    public ResponseEntity<ResultPaginationDto> getAllJob(@Filter Specification spec, Pageable pageable) {
        ResultPaginationDto resultPaginationDto = this.jobService.getALLJob(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDto);
    }
    
    
}
