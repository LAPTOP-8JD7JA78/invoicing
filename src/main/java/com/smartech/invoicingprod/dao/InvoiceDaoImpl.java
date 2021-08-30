package com.smartech.invoicingprod.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicingprod.dto.InvoicePayments;
import com.smartech.invoicingprod.integration.dto.InvoiceInvoiceDetailsDTO;
import com.smartech.invoicingprod.integration.util.AppConstants;
import com.smartech.invoicingprod.model.Invoice;

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
	public Invoice getSingleInvoiceByFolio(String folio, String invType) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Invoice.class);
			criteria.add( Restrictions.eq("folio",  folio ));
//			criteria.add( Restrictions.eq("invoiceType", AppConstants.ORDER_TYPE_FACTURA));
			criteria.add( Restrictions.eq("invoiceType", invType));
			//criteria.add( Restrictions.ge("updatedDate", new SimpleDateFormat("yyyy-MM-dd").parse("2021-05-01")));
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
				criteria.add(Restrictions.eq("invoiceType",orderType));
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
				criteria.add(Restrictions.in("invoiceType",orderType));
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
				criteria.add(Restrictions.in("invoiceType",orderType));
			}			
			criteria.addOrder(Order.desc("folio"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return criteria.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Invoice getSingleInvoiceByFolioSerial(String folio) {
		SQLQuery query;
		String sql;
		Session session = sessionFactory.getCurrentSession();	
		try {
			sql = "SELECT * FROM invoice where concat_ws('', serial, folio) = '" + folio + "'";
			query = session.createSQLQuery(sql);
			query.setResultTransformer(Transformers.aliasToBean(Invoice.class));
			query.addScalar("id", new IntegerType());
			List<Invoice> invL = query.list();
			if(!invL.isEmpty()) {
				return invL.get(0);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}	
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Invoice getInvoiceByUuid(String uuid) {
		try {			
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Invoice.class);	
			criteria.add( Restrictions.eq("UUID", uuid));	
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
	
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public Invoice getInvoiceWithOutUuid(String id) {
		try {
			Invoice invoice = new Invoice();
			SQLQuery query;
			String sql;
			Session session = sessionFactory.getCurrentSession();				
			sql = "select * from invoice_payments where payments_id = " + id + ";";
			query = session.createSQLQuery(sql);
			query.setResultTransformer(Transformers.aliasToBean(InvoicePayments.class));
			query.addScalar("invoice_id", new IntegerType());
			query.addScalar("payments_id", new IntegerType());
			List<InvoicePayments> invL = query.list();
			if(!invL.isEmpty()) {
				return this.getSingleInvoiceById(invL.get(0).getInvoice_id());
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			Log.error("ERROR AL TRAER LA FACTURA PARA EL CPAGO: " + id + e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Invoice> getInvoiceToAdv(String orderType, boolean advApplied) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Invoice.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		try {
			criteria.add(Restrictions.eq("invoiceType",orderType));
			criteria.add(Restrictions.eq("advanceAplied",advApplied));
			
			criteria.addOrder(Order.desc("folio"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Invoice getInvoiceByOtFolio(String orderType, String salesOrder, String customerName) {
		try {			
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Invoice.class);	
			criteria.add( Restrictions.eq("orderType", orderType));	
			criteria.add( Restrictions.eq("fromSalesOrder", salesOrder));
			criteria.add( Restrictions.eq("customerName", customerName));
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Invoice> getInvoiceByOtFolioCustomer(String orderType, String salesOrder, String customerName) {
		try {			
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Invoice.class);	
			criteria.add( Restrictions.eq("orderType", orderType));	
			criteria.add( Restrictions.eq("fromSalesOrder", salesOrder));
			criteria.add( Restrictions.eq("customerName", customerName));
			
			return criteria.list();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Invoice> getAllError(boolean isError) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Invoice.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		try {
			criteria.add(Restrictions.eq("errorActive",isError));
			
			criteria.addOrder(Order.desc("folio"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Invoice getSingleInvoiceByFolioAndType(String folio, String orderType) {
		try {			
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(Invoice.class);	
			criteria.add( Restrictions.like("folio",  folio ));
			criteria.add( Restrictions.eq("invoiceType", orderType));
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

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public List<Invoice> getAllInvoiceToWarranty(String dataSearch) {
		try {			
			//Date dateT = new SimpleDateFormat("dd/MM/yyyy").parse("2021-05-01");			
			Session session = this.sessionFactory.getCurrentSession();
			String[] statusData = new String[] {AppConstants.STATUS_INVOICED, AppConstants.STATUS_FINISHED};
			Criteria criteria = session.createCriteria(Invoice.class);	
//			criteria.setProjection(Projections.distinct(Projections.property("id")));
			criteria.add( Restrictions.ge("updatedDate", new SimpleDateFormat("yyyy-MM-dd").parse(dataSearch)));
			criteria.add( Restrictions.eq("invoiceType", AppConstants.ORDER_TYPE_FACTURA));	
			criteria.add( Restrictions.isNotNull("UUID"));	
			criteria.add( Restrictions.in("status", statusData ));	
			/*criteria.add( Restrictions.eq("invoiceType", orderType));*/
			List<Invoice> list =  criteria.list();
			if(!list.isEmpty()){
				return list;
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Invoice getInvoiceIdFromInvoiceDetails(long id) {
		SQLQuery query;
		String sql;
		Session session = sessionFactory.getCurrentSession();	
		try {
			sql = "SELECT * FROM invoice_invoiceDetails where invoiceDetails_id = '" + id + "'";
			query = session.createSQLQuery(sql);
			query.setResultTransformer(Transformers.aliasToBean(InvoiceInvoiceDetailsDTO.class));
			query.addScalar("invoice_id", new LongType());
			query.addScalar("invoiceDetails_id", new LongType());
			List<InvoiceInvoiceDetailsDTO> invL = query.list();
			if(!invL.isEmpty()) {
				long numberId = invL.get(0).getInvoice_id();
				Invoice inv = getSingleInvoiceById(numberId);
				if(inv != null) {
					return inv;
				}else {
					return new Invoice();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
