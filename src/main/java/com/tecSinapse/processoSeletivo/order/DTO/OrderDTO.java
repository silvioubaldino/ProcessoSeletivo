package com.tecSinapse.processoSeletivo.order.DTO;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tecSinapse.processoSeletivo.model.entity.Order;

public class OrderDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String item;
	private Integer quantidade;
	private Double total;
	private String dia;
	
	public OrderDTO() {
	}
	
	public OrderDTO(String item, Integer quantidade, Double total, String dia) {
		super();
		this.item = item;
		this.quantidade = quantidade;
		this.total = total;
		this.dia = dia;
	}

	public Order mappOrder (OrderDTO orderDTO) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(orderDTO.getDia()));
		return new Order(orderDTO.getItem(),
				orderDTO.getQuantidade(),
				orderDTO.getTotal(),
				cal);
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}
	
	
		
}
