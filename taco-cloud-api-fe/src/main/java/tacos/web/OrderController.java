package tacos.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import tacos.Order;
import tacos.Taco;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {
	private RestTemplate rest = new RestTemplate();
	@GetMapping("/current")
	public String orderForm(Model model) {
		model.addAttribute("order", new Order());
		return "orderForm";
	}
	
	@GetMapping
	public String ShowOrderForm(Model model) {
		model.addAttribute("order", new Order());
		return "orders";
	}
	
	@PostMapping
	public String processOrder(@RequestParam("tacos") String tacoIDs) {
		List<Taco> tacos = new ArrayList<>();
		for(String tacoID: tacoIDs.split(",")) {
			Taco taco = rest.getForObject("http://localhost:8080/design/{id}", Taco.class, tacoID);
			
			tacos.add(taco);
		}
		Order order = new Order();
		order.setTacos(tacos);
		rest.postForObject("http://localhost:8080/orders", order, Order.class);
		return "redirect:/";
	}
}