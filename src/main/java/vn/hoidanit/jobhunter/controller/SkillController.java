package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDto;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.ValidIdException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;






@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService){
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a new skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws ValidIdException {
        if (this.skillService.isExistSkill(skill.getName())){
            throw new ValidIdException("This skill has already existed");
        }
        Skill new_skill =this.skillService.saveSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(new_skill);
    }

    @PutMapping("/skills")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws ValidIdException {
        if (this.skillService.isExistSkill(skill.getName())){
            throw new ValidIdException("This skill has already existed");
        }
        Skill new_skill = this.skillService.updateSkill(skill);
        return ResponseEntity.status(HttpStatus.OK).body(new_skill);
    }
    @GetMapping("/skills")
    @ApiMessage("Fetch all user")
    public ResponseEntity<ResultPaginationDto> getAllUser(
        @Filter Specification spec,
        Pageable pageable) 
        {
        ResultPaginationDto resultPaginationDto = this.skillService.findAllSkill(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDto);
    }
    
    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable ("id") long id) throws ValidIdException{
        if (!this.skillService.isIdExist(id)){
            throw new ValidIdException("I CAN'T FIND YOUR SKILL");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    
    
}
