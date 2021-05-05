package com.smartech.invoicingprod.service;

import java.util.List;

import com.smartech.invoicingprod.model.Branch;

public interface BranchService {
	public List<Branch> searchBranchList(int start, int limit);
	public Branch getBranchByCode(String code);
	public Branch getBranchById(long l);
	public List<Branch> searchBranchList(int start, int limit, String query);
	public boolean createBranch(Branch branch, String user);
	public boolean updateBranch(Branch branch, String user);
	int searchBranchListInt(int start, int limit, String query);
	public boolean isAvaiableBranch(String orgCode);
	Branch getBranchByName(String name);
}
