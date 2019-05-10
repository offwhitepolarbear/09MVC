package com.model2.mvc.web.purchase;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;

@RestController
@RequestMapping("/purchase/*")
public class PurchaseRestController {

	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	public PurchaseRestController() {
		// TODO Auto-generated constructor stub
		System.out.println(this.getClass());
	}

	@RequestMapping(
			value = "json/getPurchase/{tranNo}",
			method = RequestMethod.GET)
	public Purchase getPurchase(@PathVariable String tranNo) throws Exception {

		System.out.println("/purchase/json/getPurchase : GET");

		// Business Logic
		return purchaseService.getPurchase(Integer.parseInt(tranNo));
	}

	@RequestMapping(
			value = "json/updateTranCode/{tranNo}/{tranCode}",
			method = RequestMethod.POST)
	public Purchase updateTranCode(@PathVariable String tranNo, @PathVariable String tranCode) throws Exception {
	
		//RequestBody�� ��ü�̽��� ���̴µ� ������ ����?
		
		System.out.println("/purchase/json/updateTranCode : POST ȣ�⼺��");
		Purchase purchase = new Purchase();
		purchase.setTranNo(Integer.parseInt(tranNo));
		purchase.setTranCode(tranCode);
		System.out.println(purchase);

		purchaseService.updateTranCode(purchase);
		
		purchase = purchaseService.getPurchase(purchase.getTranNo());

		return purchase;

	}
	
	@RequestMapping(value = "json/addPurchase", method = RequestMethod.POST)
	public Purchase addPurchase(@RequestBody Purchase purchase) throws Exception {
		System.out.println("/purchase/json/addPurchase : POST ȣ�⼺��");
		System.out.println(purchase);

		purchaseService.addPurchase(purchase);
		System.out.println("addPurchase�� ������ ����Ǹ� ��µ˴ϴ�.");
		purchase.setDivyRequest("��������Ȯ�ο� ��ۿ�û���� ����");
		return purchase;

	}
	
	@RequestMapping(value = "json/updatePurchase", method = RequestMethod.POST)
	public Purchase updatePurchase(@RequestBody Purchase purchase) throws Exception {
		
		System.out.println("purchase/json/updatePurchase : POST �����");
		purchaseService.updatePurchase(purchase);
		Purchase rePurchase = purchaseService.getPurchase(purchase.getTranNo());
		return rePurchase;
		
	}
	
	@RequestMapping(value = "json/listSalePurchase", method = RequestMethod.POST)
	public Map listSalePurchase(@RequestBody Search search) throws Exception {
		
		System.out.println("purchase/json/listSalePurchase : POST �����");
		Map map = purchaseService.getSaleList(search);
		map.put("word", "Rest��Ʈ�ѷ�json/listSalePurchase �ٳ�� ���Դϴ�.");
		return map;
	}
		

}