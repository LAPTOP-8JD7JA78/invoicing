package com.smartech.invoicingprod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.CompanyDao;
import com.smartech.invoicingprod.model.Company;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService{
	@Autowired
	CompanyDao companyDao;
	
	@Override
	public List<Company> getAllCompanies(int start, int limit) {
		return companyDao.listCompanies(start, limit);
	}

	@Override
	public Company getCompanyByName(String companyName) {
		return companyDao.getCompanyByName(companyName);
	}
	
	@Override
	public Company getCompanyById(int id) {
		return companyDao.getCompanyById(id);
		
	}

	@Override
	public boolean update(Company c) {
		if(companyDao.updateCompany(c)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean create(Company c) {
		if(companyDao.createCompany(c)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(int id) {
		if(companyDao.deleteCompany(companyDao.getCompanyById(id))) {
			return true;
		}
		return false;
	}

	@Override
	public Company getCompanyByBook(String book) {
		return companyDao.getCompanyByBook(book);
	}

	@Override
	public Company getCompanyByLegalEntity(String legalEntity) {
		return companyDao.getCompanyByLegalEntity(legalEntity);
	}
	
	@Override
	public Company getCompanyByTaxId(String taxId) {
		return companyDao.getCompanyByTaxId(taxId);
	}
}
