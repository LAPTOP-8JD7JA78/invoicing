package com.smartech.invoicing.dao;

import java.util.List;

import com.smartech.invoicing.model.Branch;

public interface BranchDao {
	public List<Branch> searchBranchList(int start, int limit);
	public Branch getBranchById(long id);
	public List<Branch> searchBranchList(int start, int limit, String query);
	public int searchBranchListInt(int start, int limit, String query);
	public boolean createBranch(Branch branch, String user);
	public boolean isAvaiableBranch(String orgCode);
	public boolean updateBranch(Branch branch);
	public Branch getBranchByCode(String code);
	Branch getBranchByName(String name);
}
