package com.smartech.invoicingprod.service;

import java.util.List;

import com.smartech.invoicingprod.model.Company;

public interface CompanyService {
	List<Company> getAllCompanies(int start, int limit);
	Company getCompanyByName(String companyName);
	Company getCompanyById(int id);
	boolean update(Company c);
	boolean create(Company c);
	boolean delete(int id);
	Company getCompanyByBook(String book);
	Company getCompanyByLegalEntity(String legalEntity);
}
