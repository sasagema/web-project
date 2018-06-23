package beans;

import java.util.List;

import enums.OrderStatus;

public class Order {
	
	private String id;
	private List<OrderItem> stavkePorudzbine;
	private User kupac;
	private User dostavljac;
	private String datumPoruzdzbine;
	private String vremePorudzbine;
	private OrderStatus status;
	private String napomena;
}
