package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;

//==> 회원관리 Controller
@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	//setter Method 구현 않음
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	//@RequestMapping("/addPurchaseView.do")
	@RequestMapping( value="addPurchase/{prodNo}", method=RequestMethod.GET )
	public String addPurchaseView(@PathVariable String prodNo, Model model, HttpSession session) throws Exception {

		User user = (User)session.getAttribute("user");
		System.out.println("/purchase/addPurchase :GET ");
		model.addAttribute("product", productService.getProduct(Integer.parseInt(prodNo)));
		model.addAttribute("user", user);
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	//@RequestMapping("/addPurchase.do")
	@RequestMapping( value="addPurchase", method=RequestMethod.POST )
	public String addPurchase( @ModelAttribute("purchase") Purchase purchase, @ModelAttribute("user") User user, @ModelAttribute("product") Product product) throws Exception {

		System.out.println("/purchase/addPurchase : POST");
		//Business Logic
		purchase.setBuyer(user);
		purchase.setDivyDate(purchase.getDivyDate().substring(2).replace("-", "/"));
		purchase.setPurchaseProd(product);
		purchaseService.addPurchase(purchase);
		purchaseService.stockPurchase(purchase);
		
		return "redirect:/purchase/listPurchase";
	}
	
	//@RequestMapping("/getPurchase.do")
	@RequestMapping( value="getPurchase")
	public String getPurchase( @RequestParam("tranNo") String tranNo , Model model ) throws Exception {
		
		System.out.println("/getPurchaseo");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(Integer.parseInt(tranNo));
		purchase.setDivyDate(purchase.getDivyDate().substring(0,10));
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	//@RequestMapping("/updatePurchaseView.do")
	@RequestMapping( value="updatePurchase", method=RequestMethod.GET )
	public String updatePurchaseView( @RequestParam("tranNo") String tranNo , Model model ) throws Exception{

		System.out.println("/purchase/updatePurchase : POST");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(Integer.parseInt(tranNo));
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	//@RequestMapping("/updatePurchase.do")
	@RequestMapping( value="updatePurchase", method=RequestMethod.POST )
	public String updatePurchase( @ModelAttribute("purchase") Purchase purchase) throws Exception{

		System.out.println("/purchase/updatePurchase : POST");
		System.out.println("컨트롤로 넘어온 퍼체이스 봅시다."+purchase);
		//Business Logic
		purchaseService.updatePurchase(purchase);
		
		return "redirect:/purchase/getPurchase?tranNo="+purchase.getTranNo();
	}
		
	//@RequestMapping("/updateTranCode.do")
	@RequestMapping( value="updateTranCode/{prodNo}/{tranCode}/{menu}" )
	public String updateTranCode(@PathVariable String prodNo, @PathVariable String tranCode, @PathVariable String menu) throws Exception{

			System.out.println("/purchase/updateTranCode : service");
			System.out.println("menu부분으로 들어온 것 : "+menu);
			Purchase purchase = new Purchase();
			purchase.setTranCode(tranCode);
			if (menu.equals("manage")) {
			
			Product product = new Product();
			product.setProdNo(Integer.parseInt(prodNo));
			purchase.setPurchaseProd(product);
			purchaseService.updateTranCode2(purchase);
			
			return "redirect:/purchase/listSalePurchase";
			}
			else {
				purchase.setTranCode(tranCode);
				purchase.setTranNo(Integer.parseInt(prodNo));
				purchaseService.updateTranCode(purchase);
				return "redirect:/purchase/listPurchase";
			}
				
	}
		
	//@RequestMapping("/listPurchase.do")
	@RequestMapping( value="listPurchase" )
	public String listPurchase( @ModelAttribute("search") Search search, HttpSession session,  Model model) throws Exception{
		
		System.out.println("/purchase/listPurchase");
		User user = (User)session.getAttribute("user");
		System.out.println("userId추출되는가"+user.getUserId());
		search.setSearchKeyword(user.getUserId());
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=purchaseService.getPurchaseList(search, search.getSearchKeyword());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		return "forward:/purchase/listPurchase.jsp";
	}
	
	//@RequestMapping("/listSalePurchase.do")
	@RequestMapping( value="listSalePurchase" )
	public String listSalePurchase( @ModelAttribute("search") Search search, HttpSession session,  Model model) throws Exception{
		
		System.out.println("/purchase/listSalePurchase : Service");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=purchaseService.getSaleList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("saleCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		return "forward:/purchase/saleList.jsp";
	}
	
}