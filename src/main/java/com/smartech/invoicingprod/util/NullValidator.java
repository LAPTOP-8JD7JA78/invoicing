package com.smartech.invoicingprod.util;

import java.util.Date;

public class NullValidator {
	public static boolean isNull(Boolean obj) {
		 if (obj == null)
			 return false;
		 else
			 return Boolean.valueOf(obj);
	}
	
	public static double isNull(Double obj) {
		 if (obj == null)
			 return 0;
		 else
			 return Double.valueOf(obj);
	}

	public static double isNullPrice(Double obj) {
		 if (obj == null)
			 return 1.00;
		 else
			 return Double.valueOf(obj);
	}
	
	public static String isNull(String obj) {
		 if (obj != null) {
			 String nil = "{\"xsi:nil\":\"true\"}";
			 if(obj.contains(nil)){
				 return "";
			 }else {
				 String str = obj.replace("\"", "");
				 return str;
			 }
		 }	
		 else 
			 return "";
	}

	public static long isNull(Long obj) {
		 if (obj == null)
			 return 0;
		 else
			 return Long.valueOf(obj);
	}

	public static int isNull(Integer obj) {
		 if (obj == null)
			 return 0;
		 else
			 return Integer.valueOf(obj);
	}
	
	public static double isNullD(String obj) {
		 if (obj == null)
			 return 0;
		 else
			 return Double.valueOf(obj);
	}
	
	public static Date isNull(Date obj) {
		 if (obj == null)
			 return null;
		 else
			 return obj;
	}
	
	public static String isNullUnitCost(String obj) {
		 if (obj != null) {
			 String nil = "{\"xsi:nil\":\"true\"}";
			 if(obj.contains(nil)){
				 return "0";
			 }else {
				 String str = obj.replace("\"", "");
				 return str;
			 }
		 }	
		 else 
			 return "0";
	}
}
