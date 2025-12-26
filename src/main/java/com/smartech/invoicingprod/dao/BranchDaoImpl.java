package com.smartech.invoicingprod.dao;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicingprod.model.Branch;

@Repository("branchDao")
@Transactional
public class BranchDaoImpl implements BranchDao{
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DataSource dataSource;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Branch> searchBranchList(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from Branch");
	    q.setFirstResult(start); 
	    q.setMaxResults(limit);
		return q.list();
	}
	
	@Override
	public Branch getBranchById(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		long lId = Long.valueOf(id);
		return (Branch) session.get(Branch.class, lId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Branch> searchBranchList(int start, int limit, String query) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Branch.class);
		
		criteria.add(Restrictions.disjunction()
				.add(Restrictions.like("code","%" +  query + "%"))
				.add(Restrictions.like("name","%" +  query + "%"))
				);
		
	    criteria.setFirstResult(start); 
	    criteria.setMaxResults(limit);
		return criteria.list();
	}

	@Override
	public boolean createBranch(Branch branch, String user) {
		Session session = this.sessionFactory.getCurrentSession();
		long id = (long) session.save(branch);
		
		if(id > 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public int searchBranchListInt(int start, int limit, String query) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Branch.class);
		
		criteria.add(Restrictions.disjunction()
				.add(Restrictions.like("code","%" +  query + "%"))
				.add(Restrictions.like("name","%" +  query + "%"))
				);
		
		return criteria.list().size();
	}

	@Override
	public boolean isAvaiableBranch(String orgCode) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Branch.class);
		
		criteria.add(Restrictions.like("code","%" +  orgCode + "%"));
		
		if(criteria.list().size() > 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean updateBranch(Branch branch) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.update(branch);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public Branch getBranchByCode(String code) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Branch.class);
		
		criteria.add(Restrictions.eq("code", code));
		
		if(!criteria.list().isEmpty()) {
			return (Branch) criteria.list().get(0);
		}
		return null;
	}
	
	@Override
	public Branch getBranchByName(String name) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Branch.class);
		
		criteria.add(Restrictions.eq("name", name));
		
		if(!criteria.list().isEmpty()) {
			return (Branch) criteria.list().get(0);
		}
		return null;
	}
	
	@Override
	public Branch getBranchByNameTransfer(String name) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Branch.class);
		
		criteria.add(Restrictions.eq("nameTransfer", name));
		
		if(!criteria.list().isEmpty()) {
			return (Branch) criteria.list().get(0);
		}
		return null;
	}
}
