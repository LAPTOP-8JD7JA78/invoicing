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

import com.smartech.invoicingprod.model.InvoiceDetails;

@Repository("invoiceDetailsDao")
@Transactional
public class InvoiceDetailsDaoImpl implements InvoiceDetailsDao{
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public boolean saveInvoiceDetails(InvoiceDetails r) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.persist(r);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateInvoiceDetails(InvoiceDetails r) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.update(r);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceDetails> getInvoiceById(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from InvoiceDetails where id =" + id);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceDetails> getInvoiceDetails(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from InvoiceDetails");
	    q.setFirstResult(start); // modify this to adjust paging
	    q.setMaxResults(limit);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceDetails> searchBySerialNumber(String itemSerial) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(InvoiceDetails.class);
		criteria.add(Restrictions.like("itemSerial", "%" + itemSerial + "%"));
		criteria.add(Restrictions.isNotNull("lineType"));
		criteria.addOrder(Order.desc("id"));
		return  criteria.list();
	}
}
