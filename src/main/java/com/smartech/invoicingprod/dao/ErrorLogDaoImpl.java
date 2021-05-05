package com.smartech.invoicingprod.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicingprod.model.ErrorLog;

@Repository("errorLogDao")
@Transactional
public class ErrorLogDaoImpl implements ErrorLogDao{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public boolean saveError(ErrorLog er) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.save(er);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateError(ErrorLog er) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.saveOrUpdate(er);
			return true;			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ErrorLog searchError(String error, String orderNumber) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ErrorLog.class);
		criteria.add(Restrictions.eq("errorMsg", error));
		criteria.add(Restrictions.eq("orderNumber", orderNumber));
		List<ErrorLog> list = criteria.list();
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
	public List<ErrorLog> getAllError(boolean isNew) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ErrorLog.class);		
		criteria.add(Restrictions.eq("isNew", isNew));
		return criteria.list();
	}

}
