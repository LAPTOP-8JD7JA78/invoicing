package com.smartech.invoicing.integration.util;

import java.util.List;

import com.smartech.invoicing.integration.xml.rowset.Row;

public interface ResponsiveLetter {
	public boolean createFile(List<Row> r);
}
