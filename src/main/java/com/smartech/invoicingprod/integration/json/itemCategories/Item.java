package com.smartech.invoicingprod.integration.json.itemCategories;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "CategoryName",
    "DFF"
})
public class Item {
	@JsonProperty("CategoryName")
    private String CategoryName;
	
	@JsonProperty("DFF")
	private List<DFF> dff = null;

	@JsonProperty("CategoryName")
	public String getCategoryName() {
		return CategoryName;
	}

	@JsonProperty("CategoryName")
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	@JsonProperty("DFF")
	public List<DFF> getDff() {
		return dff;
	}

	@JsonProperty("DFF")
	public void setDff(List<DFF> dff) {
		this.dff = dff;
	}
	
}
