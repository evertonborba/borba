package br.com.ecborba.dataanalysis.util;

public enum DataType {

	SELLER("001"), CUSTOMER("002"), SALES("003");

	private final String idData;

	DataType(String idData) {
		this.idData = idData;
	}

	public String getIdData() {
		return this.idData;
	}

}
