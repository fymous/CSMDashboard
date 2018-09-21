package com.csm.dashboard.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csm.dashboard.dao.ContractTypeDAOImpl;
import com.csm.dashboard.model.PeriodicUsage;
import com.csm.dashboard.model.UsageBean;

@Service
public class ContractTypeServiceImpl implements ContractTypeService{
	
	@Autowired
	ContractTypeDAOImpl contractTypeDAO;
	
	private String getMonthName(int monthNumber) {
		String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}; 
		return months[monthNumber-1];
		
	}

	public HashMap<String, Object> usageMap () {
		HashMap<String,Object> usageMap = new HashMap<String,Object>();
		HashSet<String> contractTypeSet = new HashSet<String>();
		usageMap.put("quater", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("month", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("week", new HashMap<String, List<PeriodicUsage>>());
		usageMap.put("dropdown", contractTypeSet);
		
		
		contractTypeSet.add("all");
		List<Object[]> results=null;

		// Get the Quater map
		HashMap<String, List<PeriodicUsage>> quatermap=(HashMap<String, List<PeriodicUsage>>) usageMap.get("quater");
		HashMap<String, List<PeriodicUsage>> monthMap=(HashMap<String, List<PeriodicUsage>>)usageMap.get("month");
		HashMap<String, List<PeriodicUsage>> weekMap=(HashMap<String, List<PeriodicUsage>>)usageMap.get("week");

		List<String> quaterList = new ArrayList<String>();
	
		// GET THT QUATER USAGE DATA
		results = contractTypeDAO.getQuaterlyByContractType();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = (String) arr[0];
			String fiscalQuater = ((String) arr[1]).replace("FY19-", "");
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
		    double weeklyValue = valueBigDecimal.doubleValue();
		    
			String usageData =   weeklyValue+"";
			contractTypeSet.add(contractType);
			if(quatermap.containsKey(contractType)){
				quatermap.get(contractType).add(new PeriodicUsage(fiscalQuater, usageData));
				quatermap.get(contractType).add(new PeriodicUsage("Q3", "0"));
				quatermap.get(contractType).add(new PeriodicUsage("Q4", "0"));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalQuater,usageData));
				quatermap.put(contractType, periodicUsageList);			
			}
		}
		
		
		// GET THT MONTH USAGE DATA
		results = contractTypeDAO.getMonthlyByContractType();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = (String) arr[0];
			String fiscalMonth =  (arr[1]+"");
			
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(monthMap.containsKey(contractType)){
				monthMap.get(contractType).add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
				monthMap.put(contractType, periodicUsageList);			
			}

		}

		
		
		// GET THT WEEK USAGE DATA
		results = contractTypeDAO.getWeeklyByContractType();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = (String) arr[0];
			BigDecimal fiscalWeek = (BigDecimal) arr[1];
		    
			BigDecimal valueBigDecimal = (BigDecimal) arr[2];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(weekMap.containsKey(contractType)){
				weekMap.get(contractType).add(new PeriodicUsage(fiscalWeek+"", usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalWeek+"",usageData));
				weekMap.put(contractType, periodicUsageList);			
			}

		}
		
		// GET THT QUATER USAGE DATA All CONTRACT
		results = contractTypeDAO.getQuaterly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = "all";
			String fiscalQuater = ((String) arr[0]).replace("FY19-", "");
			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(quatermap.containsKey(contractType)){
				quatermap.get(contractType).add(new PeriodicUsage(fiscalQuater, usageData));
				quatermap.get(contractType).add(new PeriodicUsage("Q3", "0"));
				quatermap.get(contractType).add(new PeriodicUsage("Q4", "0"));
				
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalQuater,usageData));
				quatermap.put(contractType, periodicUsageList);			
			}

		}
		
		// GET THT MONTH USAGE DATA All CONTRACT
		results = contractTypeDAO.getMonthly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = "all";
			String fiscalMonth = arr[0]+"";

			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(monthMap.containsKey(contractType)){
				monthMap.get(contractType).add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(getMonthName(Integer.parseInt(fiscalMonth.split("-")[1])), usageData));
				monthMap.put(contractType, periodicUsageList);			
			}

		}

		
		// GET THT WEEK USAGE DATA All CONTRACT
		results = contractTypeDAO.getWeekly();
		for (int i = 0; i < results.size(); i++) {
			Object[] arr = results.get(i);
			String contractType = "all";
			String fiscalWeek = arr[0]+"";
			BigDecimal valueBigDecimal = (BigDecimal) arr[1];
		    double weeklyValue = valueBigDecimal.doubleValue();
			
			String usageData =   weeklyValue+"";
			
			if(weekMap.containsKey(contractType)){
				weekMap.get(contractType).add(new PeriodicUsage(fiscalWeek, usageData));
			}else{
				List <PeriodicUsage> periodicUsageList=new ArrayList<PeriodicUsage>();
				periodicUsageList.add(new PeriodicUsage(fiscalWeek,usageData));
				weekMap.put(contractType, periodicUsageList);			
			}

		}

		return usageMap;
	}

	@Override
	public HashMap<String, Object> getKPI(String contractType, String periodType, String periodValue) {
			HashMap<String, Object> lobAndProductsMap = new HashMap<String,Object>();
			List<UsageBean> lobList = getLOB(contractType, periodType, periodValue);
			List<UsageBean> productsList = getProducts(contractType, periodType, periodValue);
			lobAndProductsMap.put("lob", lobList);
			lobAndProductsMap.put("products", productsList);
			return lobAndProductsMap;
	}
	
	public List<UsageBean> getLOB(String contractType, String periodType, String periodValue) {
		List<Object[]> results = contractTypeDAO.getLOB(contractType, periodType, periodValue);
		HashMap<String, Double> lobMap = new HashMap<String, Double>();
		List<UsageBean> finalProductList=new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			String key = null;
			Double lobValue = 0.0;
			Double lobTarget = 0.0;
			Object[] arr = results.get(i);
			UsageBean bean=new UsageBean();
			for (int j = 0; j < arr.length; j++) {
				if(j==0){
					key = (String) arr[j];
					bean.setName(key);
				}
				else if (j==1){
					if(arr[j] != null){
					    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
					    lobValue = valueBigDecimal.doubleValue();
					}else{
						lobValue = 0.0;
					}
				    bean.setActual(lobValue+"");
				}
				else if (j==2){
					if(arr[j] != null){
					    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
					    lobTarget = valueBigDecimal.doubleValue();
					}else{
						lobTarget = 0.0;
					}
				    bean.setTarget((lobTarget)+"");
				}				
			}
			finalProductList.add(bean);
		}
		return finalProductList;
	}
	
	public List<UsageBean> getProducts(String contractType, String periodType, String periodValue) {
		List<Object[]> results = contractTypeDAO.getProducts(contractType, periodType, periodValue);
		HashMap<String, Double> productsMap = new HashMap<String, Double>();
		List<UsageBean> finalProductList=new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			String key = null;
			Double productValue = 0.0;			
			Object[] arr = results.get(i);
			UsageBean bean=new UsageBean();
			for (int j = 0; j < arr.length; j++) {
				if(j==0){
					key = (String) arr[j];
					bean.setName(key);
				}
				
				else if (j==1){
					if(arr[j] != null){
					    BigDecimal valueBigDecimal = (BigDecimal) arr[j];
					    productValue = valueBigDecimal.doubleValue();
					}else{
						productValue = 0.0;
					}
				    bean.setActual(productValue+"");
				}
			}
//			productsMap.put(key, Math.round(productValue*10.0)/10.0);
			if (Double.parseDouble(bean.getActual()) >= 0)
				finalProductList.add(bean);
		}
		return finalProductList;
	}
}
