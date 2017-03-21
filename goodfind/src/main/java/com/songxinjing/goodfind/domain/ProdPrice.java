package com.songxinjing.goodfind.domain;

public class ProdPrice {

	private String prodCode;

	private String buyPrice1;

	private String salePrice1;

	private int buyAmount1;

	private int saleAmount1;

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getBuyPrice1() {
		return buyPrice1;
	}

	public void setBuyPrice1(String buyPrice1) {
		this.buyPrice1 = buyPrice1;
	}

	public String getSalePrice1() {
		return salePrice1;
	}

	public void setSalePrice1(String salePrice1) {
		this.salePrice1 = salePrice1;
	}

	public int getBuyAmount1() {
		return buyAmount1;
	}

	public void setBuyAmount1(int buyAmount1) {
		this.buyAmount1 = buyAmount1;
	}

	public int getSaleAmount1() {
		return saleAmount1;
	}

	public void setSaleAmount1(int saleAmount1) {
		this.saleAmount1 = saleAmount1;
	}

	@Override
	public String toString() {
		return "prodCode=" + prodCode + "\n" + salePrice1
				+ "\t" + saleAmount1 + "\n ---------------- \n" + buyPrice1 + "\t" + buyAmount1 + "\n";
	}

}
