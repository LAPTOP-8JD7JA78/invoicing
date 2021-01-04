package com.smartech.invoicing.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartech.invoicing.model.FixedAsset;

@Repository("fixedAssetDao")
@Transactional
public class FixedAssetDaoImpl implements FixedAssetDao{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public boolean saveFixAsset(FixedAsset fa) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.save(fa);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateFixAsset(FixedAsset fa) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.saveOrUpdate(fa);
			return true;			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public FixedAsset searchByAssetNumber(String fixedNumber) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FixedAsset.class);
		criteria.add(Restrictions.eq("assetNumber", fixedNumber));
		List<FixedAsset> list = criteria.list();
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
	public FixedAsset searchByPersonAssing(String personAssing) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FixedAsset.class);
		criteria.add(Restrictions.eq("personAssigned", personAssing));
		List<FixedAsset> list = criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		return null;
	}

}
