package com.smartech.invoicing.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicing.model.Invoice;

@Repository("invoiceDao")
@Transactional
public class InvoiceDaoImpl implements InvoiceDao{
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Invoice getSingleInvoiceById(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Invoice) session.get(Invoice.class, Long.valueOf(id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Invoice getSingleInvoiceByFolio(String folio) {
		try {
//			SQLQuery query;
//			String sql;
//			Session session = sessionFactory.getCurrentSession();	
//			
//			sql = "SELECT * FROM invoice where folio = '" + folio + "'";
//			query = session.createSQLQuery(sql);
//			query.setResultTransformer(Transformers.aliasToBean(Invoice.class));
//			query.addScalar("id", new IntegerType());
//			List<Invoice> invL = query.list();
//			if(!invL.isEmpty()) {
//				return invL.get(0);
//			}
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Invoice.class);	
			criteria.add( Restrictions.eq("folio", folio));	
			List<Invoice> list =  criteria.list();
			if(!list.isEmpty()){
				return list.get(0);
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}

		
	}

	@Override
	public boolean updateInvoice(Invoice o) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.update(o);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveInvoice(Invoice o) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.save(o);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Invoice> getInvoiceByParams(String folio, String Company, String status,
			String startDate, String endDate, int start, int limit, String customer, String branch, String orderType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Invoice.class);
		try {
			
			if(StringUtils.isNotBlank(folio)) {
				criteria.add(Restrictions.eq("folio", Integer.valueOf(folio)));
			}
			
			if(StringUtils.isNotBlank(Company)) {
				criteria.add(Restrictions.eq("Company", Long.valueOf(Company)));
			}
			
			if(StringUtils.isNotBlank(status)) {
				criteria.add(Restrictions.eq("status", status));
			}
			
			if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {			
				criteria.add(Restrictions.between("creationDate", sdf.parse(startDate), sdf.parse(endDate)));
			}

			if(StringUtils.isNotBlank(customer)) {
				criteria.add(Restrictions.like("customerName", "%" + customer + "%"));	
			}
			
			if(StringUtils.isNotBlank(branch)) {
				criteria.add(Restrictions.eq("Branch", Long.valueOf(branch)));
			}

			if(StringUtils.isNotBlank(orderType)) {
				criteria.add(Restrictions.eq("t.id", orderType));
			}
			criteria.addOrder(Order.desc("folio"));
			
			System.out.println(criteria.list());
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return criteria.list();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Invoice> getInvoiceListByStatusCode(String status, String orderType) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Invoice.class);
		try {
			if(StringUtils.isNotBlank(status)) {
				criteria.add(Restrictions.eq("status", status));
			}
			if(StringUtils.isNotBlank(orderType)) {
				criteria.add(Restrictions.eq("orderType",orderType));
			}			
			criteria.addOrder(Order.desc("folio"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return  criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Invoice> getInvoiceListByStatusCode(String status, List<String> orderType) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Invoice.class);
		try {
			if(StringUtils.isNotBlank(status)) {
				criteria.add(Restrictions.eq("status", status));
			}
			if(orderType != null && !orderType.isEmpty()) {
				criteria.add(Restrictions.in("orderType",orderType));
			}			
			criteria.addOrder(Order.desc("folio"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Invoice> getInvoiceListByStatusCode(List<String> status, List<String> orderType) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Invoice.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		try {
			if(status != null && !status.isEmpty()) {
				criteria.add(Restrictions.in("status", status));
			}
			if(orderType != null && !orderType.isEmpty()) {
				criteria.add(Restrictions.in("orderType",orderType));
			}			
			criteria.addOrder(Order.desc("folio"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return criteria.list();
	}
}
