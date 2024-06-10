package com.smartech.invoicingprod.dao;

import java.util.List;

import com.smartech.invoicingprod.model.Company;

public interface CompanyDao {
	boolean createCompany(Company o);
	boolean updateCompany(Company o);
	boolean deleteCompany(Company o);
	List<Company> listCompanies(int start, int limit);
	Company getCompanyById(int id);
	List<Company> listCompaniesByName(String name);
	Company getCompanyByName(String companyName);
	Company getCompanyByBook(String assetBook);
	Company getCompanyByLegalEntity(String legalEntity);
	Company getCompanyByTaxId(String taxId);
}
