package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDto;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.ValidIdException;

import java.beans.Customizer;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RequestMapping("/api/v1")
@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company newCompany = this.companyService.SaveCompany(company);  
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany) ;
    }

    @GetMapping("/companies")
    @ApiMessage("fetch all companies")
    public ResponseEntity<ResultPaginationDto> getCompany(
        @Filter Specification spec,
        Pageable pageable) 
        {
        // String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        // String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        // Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSize));
        ResultPaginationDto resultPaginationDto = this.companyService.findAllCompanies(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDto);  
    }
    @PutMapping("/companies")
    public ResponseEntity<Company> putCompany(@Valid @RequestBody Company company) {
        Company company2 = this.companyService.updateCompany(company);
        this.companyService.SaveCompany(company2);
        return ResponseEntity.status(HttpStatus.OK).body(company2);
    }
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable ("id") long id){
        this.companyService.deleteCompanyById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
        // delete all users in company
    }
    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getDetailCompany(@PathVariable ("id") long id) throws ValidIdException {
        if (this.companyService.existsIdCompany(id)){
            throw new ValidIdException("I can't find your company");
        }
        Company company = this.companyService.findCompanyById(id);
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }
    
    
}
