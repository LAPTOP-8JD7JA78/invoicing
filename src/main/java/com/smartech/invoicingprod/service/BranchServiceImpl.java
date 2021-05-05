package com.smartech.invoicingprod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.BranchDao;
import com.smartech.invoicingprod.model.Branch;

@Service("branchService")
public class BranchServiceImpl implements BranchService{
	
	@Autowired
	BranchDao branchDao;
	
	@Override
	public List<Branch> searchBranchList(int start, int limit) {
		return branchDao.searchBranchList(start, limit);
	}

	@Override
	public Branch getBranchById(long id) {
		return branchDao.getBranchById(id);
	}
	
	@Override
	public List<Branch> searchBranchList(int start, int limit, String query) {
		return branchDao.searchBranchList(start, limit, query);
	}
	
	@Override
	public int searchBranchListInt(int start, int limit, String query) {
		return branchDao.searchBranchListInt(start, limit, query);
	}

	@Override
	public boolean createBranch(Branch branch, String user) {	
		return branchDao.createBranch(branch, user);
	}

	@Override
	public boolean isAvaiableBranch(String orgCode) {
		return branchDao.isAvaiableBranch(orgCode);
	}

	@Override
	public boolean updateBranch(Branch branch, String user) {
		if(branchDao.updateBranch(branch)) {
			return true;
		}
		return false;
	}

	@Override
	public Branch getBranchByCode(String code) {
		return branchDao.getBranchByCode(code);
	}
	
	
	@Override
	public Branch getBranchByName(String name) {
		return branchDao.getBranchByName(name);
	}
	
}
