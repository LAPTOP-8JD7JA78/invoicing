package com.smartech.invoicing.dao;

import java.util.List;

import com.smartech.invoicing.model.Company;

public interface CompanyDao {
	boolean createCompany(Company o);
	boolean updateCompany(Company o);
	boolean deleteCompany(Company o);
	List<Company> listCompanies(int start, int limit);
	Company getCompanyById(int id);
	List<Company> listCompaniesByName(String name);
	Company getCompanyByName(String companyName);
	Company getCompanyByBook(String assetBook);
}
