package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDto;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository){
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company SaveCompany(Company company){
        return this.companyRepository.save(company);   
    }
    public ResultPaginationDto findAllCompanies(Specification<Company> spec, Pageable pageable){
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDto resultPaginationDto = new ResultPaginationDto();
        ResultPaginationDto.MetaDto metaDto = new ResultPaginationDto.MetaDto();
        metaDto.setPage(pageable.getPageNumber() + 1);
        metaDto.setPageSize(pageable.getPageSize());

        metaDto.setTotal(pageCompany.getTotalElements());
        metaDto.setPages(pageCompany.getTotalPages());

        resultPaginationDto.setMetaDto(metaDto);
        resultPaginationDto.setResultPagination(pageCompany.getContent());

        return resultPaginationDto;
    }
    public Company findCompanyById(long id){
        return this.companyRepository.findById(id);
    }
    public Company updateCompany(Company company){
        Company company2 = this.findCompanyById(company.getId());
        if (company2 != null){
            company2.setName(company.getName());
            company2.setDescription(company.getDescription());
            company2.setAddress(company.getAddress());
            company2.setLogo(company.getLogo());
        }
        return company2;
    }
    public void  deleteCompanyById(long id){
        Company company = this.companyRepository.findById(id);
        if (company != null){
            List<User> users = this.userRepository.findByCompany(company);
            this.userRepository.deleteAll(users);
        }       
        this.companyRepository.deleteById(id);
    }
    public boolean existsIdCompany(long id){
        return this.companyRepository.existsById(id);
    }
}
