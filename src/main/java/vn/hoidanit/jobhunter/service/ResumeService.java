package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResCreateCv;
import vn.hoidanit.jobhunter.domain.response.ResDetailCv;
import vn.hoidanit.jobhunter.domain.response.ResUpdateCv;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDto;
import vn.hoidanit.jobhunter.repository.ResumeRepository;

@Service
public class ResumeService {
    
    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository){
        this.resumeRepository = resumeRepository;
    }

    public Resume saveCv(Resume resume){
        return this.resumeRepository.save(resume);
    }

    public ResCreateCv convertToDto(Resume resume){
        ResCreateCv resCreateCv = new ResCreateCv();
        resCreateCv.setId(resume.getId());
        resCreateCv.setCreatedAt(resume.getCreatedAt());
        resCreateCv.setCreatedBy(resume.getCreatedBy());
        return resCreateCv;
    }

    public boolean existsByIdInDb(long id){
        return this.resumeRepository.existsById(id);
    }

    public Resume findResumeById(long id){
        return this.resumeRepository.findById(id);
    }

    public Resume updateCv(Resume resume){
        Resume new_resume =  this.findResumeById(resume.getId());
        new_resume.setStatus(resume.getStatus());
        return this.resumeRepository.save(new_resume);
    }

    public ResUpdateCv convertToUpdateDto (Resume resume){
        ResUpdateCv resUpdateCv = new ResUpdateCv();
        resUpdateCv.setUpdatedAt(resume.getUpdatedAt());
        resUpdateCv.setUpdatedBy(resume.getUpdatedBy());
        return resUpdateCv;
    }
    public void deleteCv(long id){
        this.resumeRepository.deleteById(id);
    }
    public ResDetailCv convertToDetailDto (Resume resume){
        ResDetailCv resDetailCv = new ResDetailCv();
        resDetailCv.setId(resume.getId());
        resDetailCv.setCreatedAt(resume.getCreatedAt());
        resDetailCv.setCreatedBy(resume.getCreatedBy());
        resDetailCv.setEmail(resume.getEmail());
        resDetailCv.setStatus(resume.getStatus());
        resDetailCv.setUpdatedAt(resume.getUpdatedAt());
        resDetailCv.setUpdatedBy(resume.getUpdatedBy());
        resDetailCv.setUrl(resume.getUrl());
        if (resume.getUser() != null){
            ResDetailCv.UserDto userDto = new ResDetailCv.UserDto();
            userDto.setId(resume.getUser().getId());
            userDto.setName(resume.getUser().getName());
            resDetailCv.setUser(userDto);}
        if(resume.getJob() != null){
            ResDetailCv.JobDto jobDto = new ResDetailCv.JobDto();
            jobDto.setId(resume.getJob().getId());
            jobDto.setName(resume.getJob().getName());
            resDetailCv.setCompanyName(resume.getJob().getName());
            resDetailCv.setJob(jobDto);
        }
        return resDetailCv;
    }
   public ResultPaginationDto findAllCv(Specification<Resume> spec, Pageable pageable){
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDto resultPaginationDto = new ResultPaginationDto();
        ResultPaginationDto.MetaDto metaDto = new ResultPaginationDto.MetaDto();
        metaDto.setPage(pageable.getPageNumber() + 1);
        metaDto.setPageSize(pageable.getPageSize());

        metaDto.setTotal(pageResume.getTotalElements());
        metaDto.setPages(pageResume.getTotalPages());

        resultPaginationDto.setMetaDto(metaDto);
        List<Resume> resumes = pageResume.getContent();
        List<ResDetailCv> resDetailCvs = new ArrayList<ResDetailCv>();
        for (Resume resume : resumes){
            resDetailCvs.add(this.convertToDetailDto(resume));
        }
        resultPaginationDto.setResultPagination(resDetailCvs);

        return resultPaginationDto;
    }
    
}
