package com.elasticsearch.test;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchRequest implements Serializable {

	private Integer from;
	private Integer size;
	private Boolean asc;
	private String orderByFieldName;
	private Map<String,Object> queryTerm=new HashMap();

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}


	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Boolean isAsc() {
		return asc;
	}

	public void setAsc(Boolean asc) {
		this.asc = asc;
	}


	public String getOrderByFieldName() {
		return orderByFieldName;
	}

	public void setOrderByFieldName(String orderByFieldName) {
		this.orderByFieldName = orderByFieldName;
	}
	public Map<String, Object> getQueryTerm() {
		return queryTerm;
	}

	public void setQueryTerm(Map<String, Object> queryTerm) {
		this.queryTerm = queryTerm;
	}

}
