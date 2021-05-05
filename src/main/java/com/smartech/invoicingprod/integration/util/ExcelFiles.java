package com.smartech.invoicing.integration.util;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.smartech.invoicing.dto.AssetLabel;

public class ExcelFiles {
	public static Map<String, byte[]> Main(List<AssetLabel> alabel) {
			try {
			Map<String, byte[]> attachedExcel= new HashMap<String, byte[]>();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int count = 2;
			//Crear libro de trabajo en blanco
	        Workbook workbook = new XSSFWorkbook();
	        //Crea hoja nueva
	        Sheet sheet = workbook.createSheet("Etiquetas");
	        //Por cada línea se crea un arreglo de objetos (Object[])
	        Map<String, Object[]> datos = new TreeMap<String, Object[]>();
	        datos.put("1", new Object[]{"CODIGO DE ACTIVO", "DESCRIPCION", "NÚMERO DE SERIE", "MARCA" ,"CODIGO BARRAS"});
	        if(alabel != null && !alabel.isEmpty()) {
	        	for(AssetLabel al: alabel) {
	        		datos.put(String.valueOf(count), new Object[] {al.getAssetNumber(),
	        															al.getAssetDescription(),
	        															al.getSerialNumber(),
	        															al.getBrand(),
	        															al.getLabelCode()});
	        		count = count + 1;
	        	}
	        }
//	        datos.put("2", new Object[]{1, "María", "Remen"});
//	        datos.put("3", new Object[]{2, "David", "Allos"});
//	        datos.put("4", new Object[]{3, "Carlos", "Caritas"});
//	        datos.put("5", new Object[]{4, "Luisa", "Vitz"});
	        //Iterar sobre datos para escribir en la hoja
	        Set<String> keyset = datos.keySet();
	        int numeroRenglon = 0;
	        for (String key : keyset) {
	            Row row = sheet.createRow(numeroRenglon++);
	            Object[] arregloObjetos = datos.get(key);
	            int numeroCelda = 0;
	            for (Object obj : arregloObjetos) {
	                Cell cell = row.createCell(numeroCelda++);
	                if (obj instanceof String) {
	                    cell.setCellValue((String) obj);
	                } else if (obj instanceof Integer) {
	                    cell.setCellValue((Integer) obj);
	                }
	            }
	        }
//	        FileOutputStream out = new FileOutputStream(new File("C:\\Users\\llope\\Desktop\\prueba.xlsx"));
//          workbook.write(out);
//          out.close();
	        workbook.write(bos);
		    byte[] bytes = bos.toByteArray();
            attachedExcel.put("Etiquetas.xlsx", bytes);
            return attachedExcel;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
