package com.smartech.invoicing.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicing.model.NextNumber;

@Repository("nextNumberDao")
@Transactional
public class NextNumberDaoImpl implements NextNumberDao{
	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public synchronized int getLastNumber(String OrderType, String Organization) {
		 // TODO Auto-generated method stub
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria crit =  session.createCriteria(NextNumber.class);    
	     crit.setProjection(Projections.max("Folio"));
	     crit.add( Restrictions.eq("OrderType", OrderType));
	     crit.add( Restrictions.eq("Organization", Organization));
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

	@SuppressWarnings("unchecked")
	public synchronized NextNumber getNumber(String OrderType, String Org) {
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria crit =  session.createCriteria(NextNumber.class); 
	     crit.add( Restrictions.eq("OrderType", OrderType));
	     crit.add( Restrictions.eq("Organization", Org));
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
}
