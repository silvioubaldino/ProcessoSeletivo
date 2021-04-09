package com.tecSinapse.processoSeletivo.model.entity;

import java.time.LocalDateTime;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

	private String item;
	private Integer quantidade;
	private Double total;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Calendar dia;
	
	public Order() {
	}	
	
	public Order(String item, Integer quantidade, Double total, Calendar dia) {
		super();
		this.item = item;
		this.quantidade = quantidade;
		this.total = total;
		this.dia = dia;
	}
	
	public int getMonth (LocalDateTime dia) {	
		return dia.getMonthValue();
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Calendar getDia() {
		return dia;
	}

	public void setDia(Calendar dia) {
		this.dia = dia;
	}
	
	@Override
	  public String toString() {
	    return "item:" + item
	    		+ "quantidade:" + quantidade
	    		+ "total:" + total
	    		+ "dia:" + dia;
	  }

}
