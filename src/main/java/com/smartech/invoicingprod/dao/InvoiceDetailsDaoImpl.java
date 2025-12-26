package com.smartech.invoicingprod.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicingprod.dto.DataForInvoiceDetailsDTO;
import com.smartech.invoicingprod.dto.WarrantyDataProcessDTO;
import com.smartech.invoicingprod.model.Invoice;
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
		Criteria criteria = session.createCriteria(InvoiceDetails.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.like("itemSerial", "%" + itemSerial + "%"));
		criteria.add(Restrictions.isNotNull("lineType"));
		criteria.addOrder(Order.desc("id"));
		return  criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceDetails> searchForItemsCombo(String itemSerial, String sku) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(InvoiceDetails.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.like("itemSerial", "%" + itemSerial + "%"));
		criteria.add(Restrictions.like("itemNumber", "%" + sku + "%"));
		criteria.add(Restrictions.isNull("lineType"));
		criteria.addOrder(Order.desc("id"));
		return  criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceDetails> searchBySerialNumberForCombo(String itemSerial) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(InvoiceDetails.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.like("itemSerial", "%" + itemSerial + "%"));
		criteria.add(Restrictions.isNull("lineType"));
		criteria.addOrder(Order.desc("id"));
		return  criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceDetails> searchBySerialNumberAndSku(String itemSerial, String itemName) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(InvoiceDetails.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.like("itemSerial", "%" + itemSerial + "%"));
		criteria.add(Restrictions.eq("itemNumber", itemName));
		criteria.add(Restrictions.isNotNull("lineType"));
		criteria.addOrder(Order.desc("id"));
		return  criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DataForInvoiceDetailsDTO> searchDataForInvoiceDetails(long id) {
		try {
			SQLQuery query;
			String sql;
			Session session = sessionFactory.getCurrentSession();				
			sql = "select * from im_recolectapd.invoice_invoicedetails where invoiceDetails_id = " + id + ";";
			query = session.createSQLQuery(sql);
			query.setResultTransformer(Transformers.aliasToBean(DataForInvoiceDetailsDTO.class));
			query.addScalar("invoiceDetails_id", new IntegerType());
			List<DataForInvoiceDetailsDTO> invL = query.list();
			return invL;
			/*if(!invL.isEmpty()) {
				return this.getSingleInvoiceById(invL.get(0).getInvoice_id());
			}
			return null;*/
		}catch(Exception e) {
			return null;
		}
	}
}
