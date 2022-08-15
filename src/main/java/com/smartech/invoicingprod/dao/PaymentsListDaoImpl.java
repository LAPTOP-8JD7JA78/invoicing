package com.smartech.invoicingprod.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicingprod.model.PaymentsList;

@Repository("paymentsListDao")
@Transactional
public class PaymentsListDaoImpl implements PaymentsListDao{
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public boolean saveListpay(PaymentsList pl) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.save(pl);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateListPay(PaymentsList pl) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.update(pl);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentsList> getListPay(String status) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PaymentsList.class);
		try {
			criteria.add(Restrictions.eq("status", status));
						
			criteria.addOrder(Order.desc("id"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return  criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaymentsList getByReceiptNumber(String receiptNumber) {
		try {			
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(PaymentsList.class);	
			criteria.add( Restrictions.eq("receiptNumber", receiptNumber));	
			List<PaymentsList> list =  criteria.list();
			if(!list.isEmpty()){
				return list.get(0);
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaymentsList getByReceiptNumberCustomer(String receiptNumber, String customer) {
		try {			
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(PaymentsList.class);	
			criteria.add( Restrictions.eq("receiptNumber", receiptNumber));	
			criteria.add( Restrictions.eq("customerName", customer));
			List<PaymentsList> list =  criteria.list();
			if(!list.isEmpty()){
				return list.get(0);
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaymentsList getByReceiptIdCustomer(String receiptId, String customer) {
		try {			
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(PaymentsList.class);	
			criteria.add( Restrictions.eq("receiptId", receiptId));	
			criteria.add( Restrictions.eq("customerName", customer));
			List<PaymentsList> list =  criteria.list();
			if(!list.isEmpty()){
				return list.get(0);
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaymentsList getByReceiptIdCustomerLike(String receiptId, String customer) {
		try {			
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(PaymentsList.class);	
			criteria.add( Restrictions.eq("receiptId", receiptId));	
			criteria.add( Restrictions.like("customerName", "%" + customer + "%"));
			List<PaymentsList> list =  criteria.list();
			if(!list.isEmpty()){
				return list.get(0);
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
