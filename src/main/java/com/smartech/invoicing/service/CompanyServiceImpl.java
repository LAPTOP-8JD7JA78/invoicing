package com.smartech.invoicing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.CompanyDao;
import com.smartech.invoicing.model.Company;

@Service("companyService")
public class CompanyServiceImpl {
	@Autowired
	CompanyDao companyDao;
	
	public List<Company> getAllCompanies(int start, int limit) {
		return companyDao.listCompanies(start, limit);
	}

	public Company getCompanyByName(String companyName) {
		return companyDao.getCompanyByName(companyName);
	}
	
	public Company getCompanyById(int id) {
		return companyDao.getCompanyById(id);
		
	}

	public boolean update(Company c) {
		if(companyDao.updateCompany(c)) {
			return true;
		}
		return false;
	}

	public boolean create(Company c) {
		if(companyDao.createCompany(c)) {
			return true;
		}
		return false;
	}

	public boolean delete(int id) {
		if(companyDao.deleteCompany(companyDao.getCompanyById(id))) {
			return true;
		}
		return false;
	}
}
