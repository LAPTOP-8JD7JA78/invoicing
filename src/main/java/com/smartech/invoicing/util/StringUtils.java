package com.smartech.invoicing.util;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class StringUtils {
	
	static Logger log = Logger.getLogger(StringUtils.class.getName());

	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	
	public static String getAllSerialsNumbersByRange(String serialFrom, String serialTo) {
			
		String from1 = org.apache.commons.lang3.StringUtils.difference(serialTo, serialFrom);
		String to1 = org.apache.commons.lang3.StringUtils.difference(serialFrom, serialTo);
		String prefix1 = org.apache.commons.lang3.StringUtils.getCommonPrefix(serialFrom, serialTo);
		String serialsNumbs = "";
		int length1 = from1.length();
		
		if(isInteger(from1) && isInteger(to1)) {
			int from2 = Integer.parseInt(from1);
			int to2 = Integer.parseInt(to1);
			int var4 = from2;
			for(int i = 0; i <= (to2 - from2); i++) {
				String var3 = intToString(var4, length1);
				var3 = prefix1 + var3;
				serialsNumbs = serialsNumbs + var3 + ",";
				var4++;
			}
			
			if(serialsNumbs != null && !"".contains(serialsNumbs)) {
				serialsNumbs = serialsNumbs.substring(0, serialsNumbs.length() - 1);
				return serialsNumbs;
			}
		}
		
		return null;
	}
	
	public static String intToString(int num, int digits) {
	    String output = Integer.toString(num);
	    while (output.length() < digits) output = "0" + output;
	    return output;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	public List<String> getSerialListByString(String serials){
		if(serials != null && !"".contains(serials)) {
			try {
				List<String> items = Arrays.asList(serials.split(","));
				return items;
			}catch(Exception e) {
				e.printStackTrace();
				log.error("ERROR AL CONVERTIR STRING EN LISTA \'getSerialListByString\' ", e);
			}
		}
			
		return null;
	}
}
