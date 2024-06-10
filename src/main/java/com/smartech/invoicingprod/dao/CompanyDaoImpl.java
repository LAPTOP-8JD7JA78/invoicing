package com.smartech.invoicingprod.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicingprod.model.Company;

@Repository("companyDao")
@Transactional
public class CompanyDaoImpl implements CompanyDao{
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public boolean createCompany(Company o) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.save(o);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateCompany(Company o) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.saveOrUpdate(o);
			return true;			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteCompany(Company o) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.delete(o);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Company> listCompanies(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from Company");
	    q.setFirstResult(start);
	    q.setMaxResults(limit);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Company getCompanyById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.add(Restrictions.eq("id", new Long(id)));
		List<Company> list = criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Company> listCompaniesByName(String name) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.add(Restrictions.eq("name", name));
		criteria.addOrder(Order.asc("name"));
		return  criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Company getCompanyByName(String companyName) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.add(Restrictions.eq("name", companyName));
		
		List<Company> list = criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Company getCompanyByBook(String assetBook) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.add(Restrictions.eq("assetBook", assetBook));		
		List<Company> list = criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Company getCompanyByLegalEntity(String legalEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.add(Restrictions.eq("businessUnitName", legalEntity));		
		List<Company> list = criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Company getCompanyByTaxId(String taxId) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.add(Restrictions.eq("taxIdentifier", taxId));		
		List<Company> list = criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		return null;
	}
}
