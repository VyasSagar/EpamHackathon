package com.helios.datatypes;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import com.helios.excel.ExcelOutput;

public class SaleHeader {

	private SaleHeader() {

	}

	private static SaleHeader instance;

	public static SaleHeader getInstance() {
		if (instance == null)
			instance = new SaleHeader();
		return instance;
	}

	private String map;
	private String saleHeaderNumber;
	private String saleType;
	private String department;
	private String city;

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		if (map != null) {
			map = map.replaceAll(",", " ");
		}
		this.map = map;
	}

	public String getSaleHeaderNumber() {
		return saleHeaderNumber;
	}

	public void setSaleHeaderNumber(String saleHeaderNumber) {
		if (saleHeaderNumber != null) {
			saleHeaderNumber = saleHeaderNumber.replaceAll(",", " ");
		}
		this.saleHeaderNumber = saleHeaderNumber;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		if (saleType != null) {
			saleType = saleType.replaceAll(",", " ");
		}
		this.saleType = saleType;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		if (department != null) {
			department = department.replaceAll(",", " ");
		}
		this.department = department;
	}

	public String getCity() {
		return city;
	}

	public void setCountry(String city) {
		if (city != null) {
			city = city.replaceAll(",", " ");
		}
		this.city = city;
	}

	@Override
	public String toString() {
		return "|Map\t|Sale Header Number\t|SaleType\t|Department\t|City\t|\n" + "|" + map + "\t|" + saleHeaderNumber
				+ "\t\t\t|" + saleType + "\t\t|" + department + "\t|" + city + "\t\t|";
	}
	
	public void fetch(String map){
		String query = "select * from Sales_Header where Map='"+map+"'";
		Recordset recordset;
		try {
			recordset = ExcelOutput.executeSelectStatement(query);
			
			if(!recordset.next()) {
				System.out.println("No matching SaleHeader Found for the Map = " + map);
				return;
			}
			setSaleHeaderNumber(recordset.getField("SaleHeaderNumber"));
		} catch (FilloException e) {
			System.out.println("No matching SaleHeader Found for the Map = " + map);
			e.printStackTrace();
		}
	}
	
	public void dump(){
		String query = "INSERT INTO Sales_Header(Map,SaleHeaderNumber,SaleType,Department,City) VALUES('"+getMap()+"','"+getSaleHeaderNumber()+"','"+getSaleType()+"','"+getDepartment()+"','"+getCity()+"')";
		ExcelOutput.executeInsertStatement(query);
	}
	

}
