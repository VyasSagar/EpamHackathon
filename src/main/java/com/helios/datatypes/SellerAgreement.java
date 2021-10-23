package com.helios.datatypes;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import com.helios.excel.ExcelOutput;

public class SellerAgreement {
	
	private static SellerAgreement instance;
	private String map = "-1";
	private String location;
	private String sourceSite;
	private String sellerAgreementNumber;
	private String accountNumber;
	private String accountName;
	private String address;
	private String kycStatus;
	private String performanceCommission;
	private String dealType;
	private String introCommission;
	private String signatoriesDetails;
	private String status;
	
	private SellerAgreement() {
		
	}
	
	public static SellerAgreement getInstance() {
		if(instance == null ) instance = new SellerAgreement();
		return instance;
	}
	
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		if (map != null) {
			map = map.replaceAll(",", " ");
		}
		this.map = map;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		if (location != null) {
			location = location.replaceAll(",", "");
		}
		this.location = location;
	}
	
	public String getSourceSite() {
		return sourceSite;
	}
	public void setSourceSite(String sourceSite) {
		if (sourceSite != null) {
			sourceSite = sourceSite.replaceAll(",", " ");
		}
		this.sourceSite = sourceSite;
	}
	
	public String getSellerAgreementNumber() {
		return sellerAgreementNumber;
	}
	public void setSellerAgreementNumber(String sellerAgreementNumber) {
		if(sellerAgreementNumber != null) {
			sellerAgreementNumber = sellerAgreementNumber.replace(",", " ");
		}
		
		this.sellerAgreementNumber = sellerAgreementNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		if(accountNumber != null) {
			accountNumber = accountNumber.replace(",", " ");
		}
		
		this.accountNumber = accountNumber;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		if(accountName != null) {
			accountName = accountName.replace(",", " ");
		}
		
		this.accountName = accountName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		if(address != null) {
			address = address.replaceAll("'", "").replaceAll(",","");
		}
		
		this.address = address;
	}
	public String getKycStatus() {
		return kycStatus;
	}
	public void setKycStatus(String kycStatus) {
		if(kycStatus != null) {
			kycStatus = kycStatus.replace(",", " ");
		}
		
		this.kycStatus = kycStatus;
	}
	public String getPerformanceCommission() {
		return performanceCommission;
	}
	public void setPerformanceCommission(String performanceCommission) {
		if(performanceCommission != null) {
			performanceCommission = performanceCommission.replace(",", " ");
		}
		
		this.performanceCommission = performanceCommission;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		if(dealType != null) {
			dealType = dealType.replace(",", " ");
		}
		
		this.dealType = dealType;
	}
	public String getIntroCommission() {
		return introCommission;
	}
	public void setIntroCommission(String introCommission) {
		if(introCommission != null) {
			introCommission = introCommission.replace(",", " ");
		}
		
		this.introCommission = introCommission;
	}
	public String getSignatoriesDetails() {
		return signatoriesDetails;
	}
	public void setSignatoriesDetails(String signatoriesDetails) {
		if(signatoriesDetails != null) {
			signatoriesDetails = signatoriesDetails.replaceAll("'", "").replaceAll(","," ");
		}
		
		this.signatoriesDetails = signatoriesDetails;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		if(status != null) {
			status = status.replace(",", " ");
		}
		
		this.status = status;
	}


	
	public void fetch(String map){
		String query = "select * from Seller_Agreement where Map='"+map+"'";
		
		try {
			Recordset recordset = ExcelOutput.executeSelectStatement(query);
			if(!recordset.next()) {
				System.out.println("No matching SellerAgreement Found for the Map = " + map);
				return;
			}
			setMap(recordset.getField("Map"));
			setSellerAgreementNumber(recordset.getField("SellerAgreementNumber"));
			setLocation(recordset.getField("Location"));
			setAccountNumber(recordset.getField("AccountNumber"));
			setAccountName(recordset.getField("AccountName"));
			setSourceSite(recordset.getField("SourceSite"));
			setAddress(recordset.getField("Address"));
			setKycStatus(recordset.getField("KycStatus"));
		
		} catch (FilloException e) {
			System.out.println("No matching SellerAgreement Found for the Map = " + StockOrder.getInstance().getMap());
			e.printStackTrace();
		}
		
	}
	
	public void dump(){
		String query = "INSERT INTO Seller_Agreement(Map,Location,SellerAgreementNumber,AccountNumber,AccountName,SourceSite,Address,KYCStatus,PerformanceCommission,DealType,IntroCommission,SignatoriesDetails,Status) "
		        + "VALUES('"+getMap()+"','"+getLocation()+"','"+getSellerAgreementNumber()+"','"+getAccountNumber()+"','"+getAccountName()+"','"+getSourceSite()+"','"+getAddress()+"','"+getKycStatus()+"','"+getPerformanceCommission()+"','"+getDealType()+"','"+getIntroCommission()+"','"+getSignatoriesDetails()+"','"+getStatus()+"')";;
		ExcelOutput.executeInsertStatement(query);
	}
	
	
	
	
	
	
	
	
	
}
