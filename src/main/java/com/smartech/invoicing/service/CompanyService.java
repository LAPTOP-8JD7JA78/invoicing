package com.smartech.invoicing.service;

import java.util.List;

import com.smartech.invoicing.model.Company;

public interface CompanyService {
	List<Company> getAllCompanies(int start, int limit);
	Company getCompanyByName(String companyName);
	Company getCompanyById(int id);
	boolean update(Company c);
	boolean create(Company c);
	boolean delete(int id);
}
