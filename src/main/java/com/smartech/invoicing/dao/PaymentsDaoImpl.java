package com.smartech.invoicing.dao;

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

import com.smartech.invoicing.model.Payments;

@Repository("paymentsDao")
@Transactional
public class PaymentsDaoImpl implements PaymentsDao{
	
	@Autowired
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Payments> getPayments(String uuid) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Payments.class);
			criteria.add(Restrictions.eq("uuidReference", uuid));
			criteria.addOrder(Order.desc("paymentNumber"));
			return criteria.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Payments> getInvoiceDetails(int start, int limit) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			Query q = session.createQuery("from Payments");
		    q.setFirstResult(start); // modify this to adjust paging
		    q.setMaxResults(limit);
			return q.list();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Payments getPayment(long id) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Payments.class);	
			criteria.add( Restrictions.eq("id", id));	
			List<Payments> list =  criteria.list();
			if(!list.isEmpty()){
				return list.get(0);
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Payments> getPaymentsListByStatus(List<String> otList) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Payments.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		try {
			if(otList != null && !otList.isEmpty()) {
				criteria.add(Restrictions.in("paymentStatus",otList));
			}			
			criteria.addOrder(Order.desc("folio"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return criteria.list();
	}

	@Override
	public boolean updatePayment(Payments pay) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.update(pay);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Payments> getPaymentsByStatus(String status) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Payments.class);
			criteria.add(Restrictions.eq("paymentStatus", status));
			criteria.addOrder(Order.desc("id"));
			return criteria.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
