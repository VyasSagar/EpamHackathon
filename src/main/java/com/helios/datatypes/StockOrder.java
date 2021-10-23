package com.helios.datatypes;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import com.helios.excel.ExcelOutput;

public class StockOrder {

	private StockOrder() {
	}

	private static StockOrder instance;

	public static StockOrder getInstance() {
		if (instance == null)
			instance = new StockOrder();
		return instance;
	}

	private String map = "NA";
	private String stockOrderNumber = "NA";
	private String itemNumber1 = "NA";
	private String itemNumber2 = "NA";
	private String itemNumber3 = "NA";
	private String netAmount = "NA";
	private String insuranceAmount = "NA";
	private String currency = "NA";
	private String shipmentNumber = "NA";

	public String getShipmentNumber() {
		return shipmentNumber;
	}

	public void setShipmentNumber(String shipmentNumber) {
		this.shipmentNumber = shipmentNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMap() {
		return map;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public String getInsuranceAmount() {
		return insuranceAmount;
	}

	public void setInsuranceAmount(String insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getStockOrderNumber() {
		return stockOrderNumber;
	}

	public void setStockOrderNumber(String stockOrderNumber) {
		this.stockOrderNumber = stockOrderNumber;
	}

	public String getItemNumber1() {
		return itemNumber1;
	}

	public void setItemNumber1(String itemNumber1) {
		this.itemNumber1 = itemNumber1;
	}

	public String getItemNumber2() {
		return itemNumber2;
	}

	public void setItemNumber2(String itemNumber2) {
		this.itemNumber2 = itemNumber2;
	}

	public String getItemNumber3() {
		return itemNumber3;
	}

	public void setItemNumber3(String itemNumber3) {
		this.itemNumber3 = itemNumber3;
	}

	@Override
	public String toString() {
		return "StockOrder [map=" + map + ", stockOrderNumber=" + stockOrderNumber + ", itemNumber1=" + itemNumber1
				+ ", itemNumber2=" + itemNumber2 + ", itemNumber3=" + itemNumber3 + ", netAmount=" + netAmount
				+ ", insuranceAmount=" + insuranceAmount + ", currency=" + currency + ", shipmentNumber="
				+ shipmentNumber + "]";
	}

	public void fetch(String map) {
		String query = "select * from Stock_Order where Map='" + map + "'";
		Recordset recordset;
		try {
			System.out.println(SellerAgreement.getInstance().getMap());
			recordset = ExcelOutput.executeSelectStatement(query);

			if (!recordset.next()) {
				System.out.println("No matching SaleHeader Found for the Map = " + map);
				return;
			}
			setMap(recordset.getField("Map"));
			setStockOrderNumber(recordset.getField("StockOrderNumber"));
			setItemNumber1(recordset.getField("ItemNumber1"));
			setItemNumber2(recordset.getField("ItemNumber2"));
			setInsuranceAmount(recordset.getField("Insurance_Value"));
			setNetAmount(recordset.getField("Net_To_Seller"));
			setCurrency(recordset.getField("currency"));
			setShipmentNumber(recordset.getField("Shipment_Number"));
		} catch (FilloException e) {
			System.out.println("No matching StockOrder Found for the Map = " + map);
			e.printStackTrace();
		}
	}

	public void dump() {
		String strQuery = "INSERT INTO Stock_Order(Map,StockOrderNumber,ItemNumber1,ItemNumber2,ItemNumber3,Shipment_Number,Insurance_Value,currency,Net_To_Seller) VALUES('"
				+ getMap() + "','" + getStockOrderNumber() + "','" + getItemNumber1() + "','" + getItemNumber2() + "','"
				+ getItemNumber3() + "','" + getShipmentNumber() + "','" + getInsuranceAmount() + "','" + getCurrency()
				+ "','" + getNetAmount() + "')";
		System.out.println(strQuery);
		ExcelOutput.executeInsertStatement(strQuery);
	}

}
