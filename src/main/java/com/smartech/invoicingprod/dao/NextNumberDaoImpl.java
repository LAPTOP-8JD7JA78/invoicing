package com.smartech.invoicingprod.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicingprod.model.Branch;
import com.smartech.invoicingprod.model.Company;
import com.smartech.invoicingprod.model.NextNumber;

@Repository("nextNumberDao")
@Transactional
public class NextNumberDaoImpl implements NextNumberDao{
	@Autowired
	SessionFactory sessionFactory;

	@Override
	@SuppressWarnings("unchecked")
	public synchronized int getLastNumber(String OrderType, Branch Organization) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria crit =  session.createCriteria(NextNumber.class);    
	     crit.setProjection(Projections.max("Folio"));
	     crit.add( Restrictions.eq("OrderType", OrderType));
	     crit.add( Restrictions.eq("branch", Organization));
	     List<Long> orders = crit.list(); 
	     if(orders.get(0) != null) {
	    	 try {
	    		 long ipInt = ((Number) orders.get(0)).longValue();
	    		 return (int) (ipInt + 1);
	    	 }catch(Exception e) {
	    		 e.printStackTrace();
	    		 return 0;
	    	 }
	     }else {
	    	 return 1;
	     }
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized NextNumber getNumberCon(String OrderType, Branch Org) {
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria crit =  session.createCriteria(NextNumber.class); 
	     crit.add( Restrictions.eq("OrderType", OrderType));
	     crit.add( Restrictions.eq("branch", Org));
	     List<NextNumber> orders = crit.list(); 
	     if(orders.get(0) != null) {
	    	 try {
	    		 orders.get(0).setFolio(orders.get(0).getFolio() + 1);
	    	 }catch(Exception e) {
	    		 e.printStackTrace();
	    		 return null;
	    	 }
	     }else {
	    	 return null;
	     }
		return orders.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public NextNumber getNumber(String orderType, Branch org) {
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria crit =  session.createCriteria(NextNumber.class); 
	     crit.add( Restrictions.eq("OrderType", orderType));
	     crit.add( Restrictions.eq("branch", org));
	     List<NextNumber> orders = crit.list(); 
	     if(orders != null && !orders.isEmpty()) {
	    	return orders.get(0); 
	     }
	     return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public synchronized NextNumber getLastNumberByItem(String OrderType, Company Organization) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria crit =  session.createCriteria(NextNumber.class); 
	    crit.add( Restrictions.eq("OrderType", OrderType));
	    crit.add( Restrictions.eq("company", Organization));
	    List<NextNumber> orders = crit.list(); 
	    if(orders != null && !orders.isEmpty()) {
	     if(orders.get(0) != null) {
		   	 try {
		   		 orders.get(0).setFolio(orders.get(0).getFolio() + 1);
		   	 }catch(Exception e) {
		   		 e.printStackTrace();
		   		 return null;
		   	 }
		    }else {
		   	 return null;
		    } 
	     }else {
	    	 return null;
	     }
		return orders.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public NextNumber existCombo(String OrderType, Company Organzation) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria crit =  session.createCriteria(NextNumber.class); 
	    crit.add( Restrictions.eq("OrderType", OrderType));
	    crit.add( Restrictions.eq("company", Organzation));
	    List<NextNumber> orders = crit.list(); 
	    if(orders != null && !orders.isEmpty()) {
	    	if(orders.get(0) != null) {
	    		return orders.get(0);
		 	}else {
		   	 return null;
		    } 
	     }else {
	    	 return null;
	     }
	}

	@Override
	public NextNumber getNumberById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		long lId = Long.valueOf(id);
		return (NextNumber) session.get(NextNumber.class, lId);
	}
}
