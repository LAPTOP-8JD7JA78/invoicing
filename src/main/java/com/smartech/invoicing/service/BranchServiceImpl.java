package com.smartech.invoicing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.BranchDao;
import com.smartech.invoicing.model.Branch;

@Service("branchService")
public class BranchServiceImpl {
	@Autowired
	BranchDao branchDao;
	
	@Autowired
	CompanyService companyService;
	
	public List<Branch> searchBranchList(int start, int limit) {
		return branchDao.searchBranchList(start, limit);
	}

	public Branch getBranchById(long id) {
		return branchDao.getBranchById(id);
	}
	
	public List<Branch> searchBranchList(int start, int limit, String query) {
		return branchDao.searchBranchList(start, limit, query);
	}
	
	public int searchBranchListInt(int start, int limit, String query) {
		return branchDao.searchBranchListInt(start, limit, query);
	}

	public boolean createBranch(Branch branch, String user) {	
		return branchDao.createBranch(branch, user);
	}

	public boolean isAvaiableBranch(String orgCode) {
		return branchDao.isAvaiableBranch(orgCode);
	}

	public boolean updateBranch(Branch branch, String user) {
		if(branchDao.updateBranch(branch)) {
			return true;
		}
		return false;
	}
}
