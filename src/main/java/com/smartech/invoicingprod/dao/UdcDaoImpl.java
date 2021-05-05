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

import com.smartech.invoicingprod.model.Udc;

@Repository("UdcDao")
@Transactional
public class UdcDaoImpl implements UdcDao{
	@Autowired
	SessionFactory sessionFactory;
	
	public Udc getUDCById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Udc) session.get(Udc.class, id);
	}

	@SuppressWarnings({ "unchecked" })
	public List<Udc> getUDCList(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from Udc");
	    q.setFirstResult(start); // modify this to adjust paging
	    q.setMaxResults(limit);
		return (List<Udc>) q.list();
	}

	@SuppressWarnings({ "unchecked" })
	public List<Udc> searchCriteria(String query) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Udc.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("udcSystem", "%" + query + "%"))
				.add(Restrictions.like("udcKey", "%" + query + "%"))
				.add(Restrictions.like("systemRef", "%" + query + "%"))
				.add(Restrictions.like("KEYREF", "%" + query + "%"))
				.add(Restrictions.like("strValue1", "%" + query + "%"))
				.add(Restrictions.like("strValue2", "%" + query + "%"))
				);
		criteria.addOrder(Order.asc("strValue1"));
		return (List<Udc>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Udc searchBySystemAndKey(String UdcSystem, String UdcKey){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Udc.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", UdcSystem.trim()))
				.add(Restrictions.like("udcKey", UdcKey.trim()))
				);
		criteria.addOrder(Order.asc("strValue1"));
	   List<Udc> list =  criteria.list();
	   if(list != null){
		   if(!list.isEmpty())
			   return list.get(0);
		   else 
			   return null;
	   }else{
		   return null;
	   }
	}
	
	@SuppressWarnings("unchecked")
	public Udc searchBySystemAndKeyRef(String UdcSystem, String UdcKey, String systemRef){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Udc.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", UdcSystem.trim()))
				.add(Restrictions.eq("udcKey", UdcKey.trim()))
				.add(Restrictions.eq("KEYREF", systemRef.trim()).ignoreCase())
				);
		criteria.addOrder(Order.asc("strValue1"));
	   List<Udc> list =  criteria.list();
	   if(list != null){
		   if(!list.isEmpty())
			   return list.get(0);
		   else 
			   return null;
	   }else{
		   return null;
	   }
	}
	
	@SuppressWarnings("unchecked")
	public Udc searchBySystemAndStrValue(String UdcSystem, String UdcKey, String strValue){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Udc.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", UdcSystem.trim()))
				.add(Restrictions.like("udcKey", UdcKey.trim()))
				.add(Restrictions.like("strValue1", strValue.trim()))
				);
		criteria.addOrder(Order.asc("strValue1"));
	   List<Udc> list =  criteria.list();
	   if(list != null){
		   if(!list.isEmpty())
			   return list.get(0);
		   else 
			   return null;
	   }else{
		   return null;
	   }
	}
	
	@SuppressWarnings("unchecked")
	public List<Udc> searchBySystem(String UdcSystem){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Udc.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", UdcSystem.trim()))
				);
		criteria.addOrder(Order.asc("strValue1"));
	   return  criteria.list();	   	   
	}

	@SuppressWarnings("unchecked")
	public List<Udc> advaceSearch(String UdcSystem, String UdcKey,String systemRef,String keyRef){
		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Udc.class);
		criteria.add(Restrictions.like("udcSystem", "%" + UdcSystem + "%"));			
		criteria.add(Restrictions.like("udcKey", "%" + UdcKey + "%"));				
		criteria.add(Restrictions.like("systemRef", "%" + systemRef + "%"));			
		criteria.add(Restrictions.like("keyRef", "%" + keyRef + "%"));	
		criteria.addOrder(Order.asc("strValue1"));
		return (List<Udc>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Udc> advaceSearch(String UdcSystem, String UdcKey,String strValue1,String strValue2, int start, int limit){
		
		Session session = this.sessionFactory.getCurrentSession();
		
		Criteria criteria = session.createCriteria(Udc.class);
		criteria.add(Restrictions.like("udcSystem", "%" + UdcSystem + "%"));			
		criteria.add(Restrictions.like("udcKey", "%" + UdcKey + "%"));		
		
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("strValue1", "%" + strValue1 + "%"))
				.add(Restrictions.like("strValue2", "%" + strValue2 + "%"))
				);
		
		criteria.addOrder(Order.asc("id"));
		
		if(start != 0 && limit != 0) {
			criteria.setFirstResult(start);
			criteria.setMaxResults(limit);
		}
		
		return (List<Udc>) criteria.list();
	}
	
	public boolean saveUDC(Udc Udc) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.persist(Udc);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean updateUDC(Udc Udc) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.update(Udc);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteUDC(int id) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			Udc p = (Udc) session.load(Udc.class, new Integer(id));
			if(null != p){
				session.delete(p);
				return true;
			}
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public int getTotalRecords(){
		Session session = this.sessionFactory.getCurrentSession();
		Long count = (Long) session.createQuery("select count(*) from  Udc").uniqueResult();
		return count.intValue();
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Udc> getStoreCB(String udcSystem, String udcKey, int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Udc.class);
		criteria.add(Restrictions.eq("udcSystem", udcSystem));	
		if(udcKey != null && !"".contains(udcKey)) {
			criteria.add(Restrictions.like("udcKey", "%" + udcKey + "%"));
		}
		
		criteria.addOrder(Order.asc("strValue1"));
		
		criteria.setMaxResults(limit);
		criteria.setFirstResult(start);
		
		return criteria.list();
	}

	@Override
	public int getStoreCB(String udcSystem, String udcKey) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Udc.class);
		criteria.add(Restrictions.eq("udcSystem", udcSystem));	
		if(udcKey != null && !"".contains(udcKey)) {
			criteria.add(Restrictions.like("udcKey", "%" + udcKey + "%"));
		}
			
		return criteria.list().size();
	}
}
