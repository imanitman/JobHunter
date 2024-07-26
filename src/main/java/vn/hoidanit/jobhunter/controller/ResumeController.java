package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResCreateCv;
import vn.hoidanit.jobhunter.domain.response.ResDetailCv;
import vn.hoidanit.jobhunter.domain.response.ResUpdateCv;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDto;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.ValidIdException;

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
public class ResumeController {
    private ResumeService resumeService;
    private UserService userService;
    private JobService jobService;

    public ResumeController(ResumeService resumeService, UserService userService, JobService jobService){
        this.resumeService = resumeService;
        this.userService = userService;
        this.jobService = jobService;
    }

    @PostMapping("/resumes")
    public ResponseEntity<ResCreateCv> createCv(@RequestBody Resume resume) throws ValidIdException {
        if (resume == null){
            throw new ValidIdException("You have to fill the resume information");
        }
        else if (!this.userService.isExistIdInDB(resume.getUser().getId()) || !this.jobService.isExistId(resume.getJob().getId())) {
            throw new ValidIdException("You have to fill the correct user or job");
            
        }
        Resume new_Resume = this.resumeService.saveCv(resume);
        ResCreateCv resCreateCv = this.resumeService.convertToDto(new_Resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateCv);
    }

    @PutMapping("/resumes")
    public ResponseEntity<ResUpdateCv> updateCv(@RequestBody Resume resume) throws ValidIdException {
        if (!this.resumeService.existsByIdInDb(resume.getId()) || resume == null){
            throw new ValidIdException("I can't find your cv");
        }
        Resume new_resume = this.resumeService.updateCv(resume);
        ResUpdateCv resUpdateCv = this.resumeService.convertToUpdateDto(new_resume);        
        return ResponseEntity.status(HttpStatus.OK).body(resUpdateCv);
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<Void> deleteCv (@PathVariable ("id") long id) throws ValidIdException{
        if (!this.resumeService.existsByIdInDb(id)){
            throw new ValidIdException("I can't find your resume");
        }
        this.resumeService.deleteCv(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResDetailCv> getDetailCv(@PathVariable ("id") long id) throws ValidIdException {
        if (!this.resumeService.existsByIdInDb(id)){
            throw new ValidIdException("Your CV doesn't exist");
        }
        Resume old_resume = this.resumeService.findResumeById(id);
        ResDetailCv resume = this.resumeService.convertToDetailDto(old_resume);
        return ResponseEntity.status(HttpStatus.OK).body(resume);
    }

    @GetMapping("/resumes")
    public  ResponseEntity<ResultPaginationDto> getAllResumes(@Filter Specification spec, Pageable pageable) {
        ResultPaginationDto resultPaginationDto = this.resumeService.findAllCv(spec, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDto);
    }
    
    

    
}
