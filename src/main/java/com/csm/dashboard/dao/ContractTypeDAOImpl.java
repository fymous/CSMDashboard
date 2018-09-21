package com.csm.dashboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class ContractTypeDAOImpl implements ContractTypeDAO{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object[]> getQuaterlyByContractType() {
		Query query = entityManager.createNativeQuery("select contract_type, FISCAL_QUATER, sum(true_usage) from usage_data group by fiscal_quater,contract_type order by fiscal_quater");
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
	public List<Object[]> getMonthlyByContractType() {
		Query query = entityManager.createNativeQuery("select contract_type, TO_DATE(SUBSTR(FISCAL_MONTH_IN_QUATER,6,3),'MON') AS MONTH_VAL, sum(true_usage) from usage_data group by FISCAL_MONTH_IN_QUATER,contract_type ORDER BY MONTH_VAL ASC");
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
	public List<Object[]> getWeeklyByContractType() {
		Query query = entityManager.createNativeQuery("select contract_type,FISCAL_WEEK_IN_QUATER, sum(true_usage) from usage_data group by FISCAL_WEEK_IN_QUATER, contract_type order by FISCAL_WEEK_IN_QUATER ASC");
		List<Object[]> results = query.getResultList();
		return results;	
	}
	@Override
	public List<Object[]> getWeekly() {
		Query query = entityManager.createNativeQuery("select FISCAL_WEEK_IN_QUATER, sum(true_usage) from usage_data group by FISCAL_WEEK_IN_QUATER order by FISCAL_WEEK_IN_QUATER ASC");
		List<Object[]> results = query.getResultList();
		return results;	
	}
	public List<Object[]> getLOB(String contractType, String periodType, String periodValue) {
		System.out.println("contractType "+contractType+" periodType"+periodType+" periodValue"+periodValue);
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY19-"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA where fiscal_quater = "+updatedQuater+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE contract_type = "+contractTypeVal+" AND fiscal_quater = "+updatedQuater+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY19-"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA where FISCAL_MONTH_IN_QUATER = "+updatedMonth+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE contract_type = "+contractTypeVal+" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS "
						+ "SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA where FISCAL_WEEK_IN_QUATER = "+updatedWeek+" GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";
				query = entityManager.createNativeQuery("SELECT METEREDLOB, sum(TRUE_USAGE) AS SUM_TRUE_USAGE,MAX(LOBTARGET) FROM USAGE_DATA" + 
						" WHERE contract_type = "+contractTypeVal+" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+ " GROUP BY METEREDLOB ORDER BY SUM_TRUE_USAGE DESC");
			}
			results = query.getResultList();
		}
		return results;	
	}
	public List<Object[]> getProducts(String contractType, String periodType, String periodValue) {
		System.out.println("contractType "+contractType+" periodType"+periodType+" periodValue"+periodValue);
		List<Object[]> results = null;
		Query query = null;
		if(periodType.equalsIgnoreCase("quater")) {
			String updatedQuater = "'FY19-"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND fiscal_quater ="+ updatedQuater+" "
								+ "GROUP BY CHILD_TIER UNION SELECT 'Autonomous', sum(TRUE_USAGE)"+
						" AS Autonomous FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous%'"+ 
						" AND fiscal_quater = "+updatedQuater+" ORDER BY 2 DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";			
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND fiscal_quater ="+ updatedQuater+" AND contract_type="+contractTypeVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous', sum(TRUE_USAGE)"+
						" AS Autonomous FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous%'"+ 
						" AND fiscal_quater = "+updatedQuater+" AND contract_type="+contractTypeVal+
						" ORDER BY 2 DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("month")) {
			String updatedMonth = "'FY19-"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_MONTH_IN_QUATER ="+ updatedMonth+" "
								+ "GROUP BY CHILD_TIER UNION SELECT 'Autonomous', sum(TRUE_USAGE)"+
						" AS Autonomous FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous%'"+ 
						" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+" ORDER BY 2 DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";			
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_MONTH_IN_QUATER ="+ updatedMonth+" AND contract_type="+contractTypeVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous', sum(TRUE_USAGE)"+
						" AS Autonomous FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous%'"+ 
						" AND FISCAL_MONTH_IN_QUATER = "+updatedMonth+" AND contract_type="+contractTypeVal+
						" ORDER BY 2 DESC");
			}
			results = query.getResultList();
		}
		else if(periodType.equalsIgnoreCase("week")) {
			String updatedWeek = "'"+periodValue+"'";
			if(contractType.equalsIgnoreCase("all")) {
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_WEEK_IN_QUATER ="+ updatedWeek+" "
								+ "GROUP BY CHILD_TIER UNION SELECT 'Autonomous', sum(TRUE_USAGE)"+
						" AS Autonomous FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous%'"+ 
						" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+" ORDER BY 2 DESC");
			}
			else {
				String contractTypeVal = "'"+contractType+"'";
				query = entityManager.createNativeQuery("SELECT CHILD_TIER, sum(TRUE_USAGE) FROM USAGE_DATA  where CHILD_TIER NOT LIKE 'Autonomous%'"+
						" AND FISCAL_WEEK_IN_QUATER ="+ updatedWeek+" AND contract_type="+contractTypeVal+
						" GROUP BY CHILD_TIER UNION SELECT 'Autonomous', sum(TRUE_USAGE)"+
						" AS Autonomous FROM USAGE_DATA WHERE CHILD_TIER LIKE 'Autonomous%'"+ 
						" AND FISCAL_WEEK_IN_QUATER = "+updatedWeek+" AND contract_type="+contractTypeVal+
						" ORDER BY 2 DESC");
			}
			results = query.getResultList();
		}
		return results;	
	}

}
