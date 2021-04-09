package com.tecSinapse.processoSeletivo.order.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.tecSinapse.processoSeletivo.model.entity.Order;
import com.tecSinapse.processoSeletivo.order.DTO.OrderDTO;

@Controller
@RequestMapping("/order")
public class OrderController {
	List<Order> orderList = new ArrayList<>();
	String greaterAmount;
	
	@GetMapping
	public ResponseEntity<List<Order>> getJson() throws ParseException {
		orderList = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<OrderDTO>> responseEntityOrder = restTemplate.exchange(
				"https://eventsync.portaltecsinapse.com.br/public/recrutamento/input?email=silvioubaldino@gmail.com",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<OrderDTO>>() {
				});
		for (OrderDTO orderDTO : responseEntityOrder.getBody()) {
			orderList.add(orderDTO.mappOrder(orderDTO));
		}
		return ResponseEntity.status(HttpStatus.OK).body(orderList);
	}

	@GetMapping("/{mes}")
	public ResponseEntity<List<Order>> getOrdersByMonth(@PathVariable int mes) throws ParseException {
		getJson();
		List<Order> ordersByMonth = new ArrayList<>();
		for (Order order : orderList) {
			int month = order.getDia().get(Calendar.MONTH) + 1;
			if (month == mes) {
				ordersByMonth.add(order);
			}
		}
		orderList = ordersByMonth;
		return ResponseEntity.status(HttpStatus.OK).body(ordersByMonth);
	}

	@GetMapping("/findGreaterAmount/{mes}")
	public ResponseEntity<String> findGreaterAmout(@PathVariable int mes) throws ParseException {
		List<String> items = new ArrayList<>();
		List<Integer> totalAmount = new ArrayList<>();
		for (Order order : orderList) {
			boolean hasItem = false;
			for (int i = 0; i < items.size(); i++) {
				if (order.getItem().equals(items.get(i))) {
					totalAmount.set(i, totalAmount.get(i) + order.getQuantidade());
					hasItem = true;
					break;
				}
			}
			if (!hasItem) {
				items.add(order.getItem());
				totalAmount.add(order.getQuantidade());
			}
		}
		int greaterI = 0;
		int greaterAmountInt = 0;
		String greaterItemName = new String();
		for (int i = 0; i < totalAmount.size(); i++) {
			if (totalAmount.get(i) > greaterAmountInt) {
				greaterAmountInt = totalAmount.get(i);
				greaterItemName = items.get(i);
				greaterI = i;
			}else if(totalAmount.get(i) == greaterAmountInt) {
				int comp = items.get(i).compareToIgnoreCase(greaterItemName);
				if(comp < 0) {
					greaterI = i;
				}
			}
		}
		greaterAmount = items.get(greaterI);
		return ResponseEntity.status(HttpStatus.OK).body(items.get(greaterI));
	}

	@GetMapping("/getGreaterAmountTotalByMonth/{mes}")
	public ResponseEntity<String> getTotalByItem(@PathVariable int mes) throws ParseException {
		Double total = 0.0;
		for (Order order : orderList) {
			if (order.getItem().equals(greaterAmount)) {
				total += order.getTotal();
			}
		}
		NumberFormat format = new DecimalFormat("#0.00");
		return ResponseEntity.status(HttpStatus.OK).body(format.format(total));
	}

	@GetMapping("/postGreaterItemTotal/{mes}")
	public ResponseEntity<String> postGreaterItemTotal (@PathVariable int mes) throws ParseException, IOException {
		getOrdersByMonth(mes);
		findGreaterAmout(mes);
		String total = getTotalByItem(mes).getBody();
		String result = greaterAmount + "#" + total;
		HttpEntity<String> requesta = new HttpEntity<>(result);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntityPOST = restTemplate.exchange(
				"https://eventsync.portaltecsinapse.com.br/public/recrutamento/finalizar?email=silvioubaldino@gmail.com",
				HttpMethod.POST, requesta, new ParameterizedTypeReference<String>() {
				});
		return responseEntityPOST;
	}
}
