package com.smartech.invoicing.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicing.model.TaxCodes;

@Repository("taxCodesDao")
@Transactional
public class TaxCodesDaoImpl implements TaxCodesDao{
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public boolean saveTaxCodes(TaxCodes tc) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.persist(tc);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateTaxCodes(TaxCodes tc) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.update(tc);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteTaxCodes(int id) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			TaxCodes p = (TaxCodes) session.load(TaxCodes.class, new Integer(id));
			if(null != p){
				session.delete(p);
				return true;
			}
			return false;			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public TaxCodes getTaxCodesById(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (TaxCodes) session.get(TaxCodes.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaxCodes> getTaxCodesList(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from TaxCodes");
	    q.setFirstResult(start); // modify this to adjust paging
	    q.setMaxResults(limit);
		return (List<TaxCodes>) q.list();
	}
}
