package com.smartech.invoicing.integration.xml.rowset;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "rowset")
public class Rowset {

    private List<Row> Row;
    
    public List<Row> getRow() {
        if (Row == null) {
        	Row = new ArrayList<Row>();
        }
        return this.Row;
    }

	public void setRow(List<Row> row) {
		Row = row;
	}

}
