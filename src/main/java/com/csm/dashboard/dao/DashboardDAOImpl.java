package com.csm.dashboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csm.dashboard.model.UsageData;

@Repository
public class DashboardDAOImpl implements DashboardDAO{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private DashboardRepository dashboardRepositoryInstance;

	public List<UsageData> getAllUsageDataStarter() {
		return dashboardRepositoryInstance.findAll();
	}

	public List<Object[]> getContractTypeData() {
		Query query = entityManager.createNativeQuery("SELECT CONTRACT_TYPE, sum(TRUE_USAGE), MAX(CONTRACT_TYPE_TARGET) FROM USAGE_DATA GROUP BY CONTRACT_TYPE");
		//List<UsageData> list = query.getResultList();
		List<Object[]> results = query.getResultList();
		return results;
	}

	@Override
	public List<Object[]> getLOB() {
		Query query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
		List<Object[]> results = query.getResultList();
		return results;
	}

	@Override
	public List<Object[]> getProducts() {
		Query query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) AS ACTUAL_PRODUCT FROM USAGE_DATA GROUP BY CHILD_TIER ORDER BY ACTUAL_PRODUCT DESC");
		List<Object[]> results = query.getResultList();
		return results;
	}

	@Override
	public List<Object[]> getQuaterly() {
		Query query = entityManager.createNativeQuery("select FISCAL_QUATER, sum(true_usage) from usage_data group by fiscal_quater");
		List<Object[]> results = query.getResultList();
		return results;
	}

	@Override
	public List<Object[]> getMonthly() {
		Query query = entityManager.createNativeQuery("select TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON') AS MONTH_VAL, sum(true_usage) from usage_data group by FISCAL_MONTH_IN_QUATER ORDER BY MONTH_VAL ASC");
		List<Object[]> results = query.getResultList();
		return results;
	}

	@Override
	public List<Object[]> getWeekly() {
		Query query = entityManager.createNativeQuery("select FISCAL_WEEK_IN_QUATER, sum(true_usage) from usage_data group by FISCAL_WEEK_IN_QUATER order by FISCAL_WEEK_IN_QUATER ASC");
		List<Object[]> results = query.getResultList();
		return results;	
	}
}
