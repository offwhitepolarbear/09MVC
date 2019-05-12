package com.model2.mvc.web.product;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	public ProductController() {
		// TODO Auto-generated constructor stub
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@Value("#{commonProperties['filePath']}")
	String filePath;
	
	@RequestMapping( value="addProduct", method=RequestMethod.GET )
	public String addProductView() throws Exception {

		System.out.println("/product/addProduct :GET");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	//@RequestMapping("/addProduct.do")
	@RequestMapping( value="addProduct", method=RequestMethod.POST )
	public String addProduct( @ModelAttribute("product") Product product, MultipartHttpServletRequest multipartHttpServletRequest, HttpSession session) throws Exception {
		//@RequestParam("uploadFile") MultipartFile uploadFile,
		
		System.out.println("/product/addProduct : POST");
		product.setManuDate(product.getManuDate().substring(2, 10));
		List<MultipartFile> files = multipartHttpServletRequest.getFiles("uploadFile");
		System.out.println("들어온 파일 갯수 : "+files.size());
		System.out.println(filePath+"저장경로입니다.");
			product.setFileName(files.get(0).getOriginalFilename());		
		
		for (int i = 0; i < files.size(); i++) {
			 File file = new File(filePath, files.get(i).getOriginalFilename());
			files.get(i).transferTo(file);
		}

		productService.addProduct(product);
		
		product.setProdNo(productService.getProdNo(product));
		
		System.out.println("프로드넘 들어갔나 찍어봐 : "+product.getProdNo());
		
		productService.addStock(product);
		
		return "redirect:/product/listProduct?menu=manage";
	}
	
	@RequestMapping( value="restockView/{prodNo}", method=RequestMethod.GET )
	public String restockView( @PathVariable String prodNo, Model model) throws Exception {
		//@RequestParam("uploadFile") MultipartFile uploadFile,
		
		System.out.println("/product/restockView : GET");
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/restock.jsp";
		
	}
	
	@RequestMapping( value="restock", method=RequestMethod.POST )
	public String restock( @ModelAttribute("product") Product product) throws Exception {
		
		System.out.println("/product/restock : POST");
		
		productService.restock(product);
		
		return "redirect:/product/listProduct?menu=manage";
	}
	
	//@RequestMapping("/getProduct.do")
	@RequestMapping( value="getProduct", method=RequestMethod.GET )
	public String getProduct(@RequestParam("prodNo") String prodNo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//System.out.println("/getProduct.do");
		System.out.println("/product/getProduct : GET");

		
		String history = null;
		Cookie[] cookies = request.getCookies();
		System.out.println(cookies);
		
		if (cookies != null) {
			System.out.println("쿠키가 있습니다.");
			Cookie CookieOne = new Cookie("history", prodNo + "");
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
						if (cookie.getName().equals("history")) {
							history = cookie.getValue();
							if(history.substring(history.length()-5).equals(prodNo+"")) {
								CookieOne = new Cookie("history", history);
								
								System.out.println("중복조회라 중복부분 삭제함.");

							}
							else {
								CookieOne = new Cookie("history", history + "," + prodNo);
								System.out.println("새로운 제품열람기록이 추가됨.");

							}
						}	
				}
				//쿠키를 세팅하는 주소가/product/... 로 변경되어서
				//기본페이지 경로인 port/ 로 시작하는 히스토리에서 조회 불가능해짐
				//쿠키를 사용할 수 있는 경로설정을 해주세요
				CookieOne.setPath("/");
			response.addCookie(CookieOne);
		}
		
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/getProduct.jsp";
	}
	
	//@RequestMapping("/updateProductView.do")
	@RequestMapping( value="updateProduct", method=RequestMethod.GET )

	public String updateProductView( @RequestParam("prodNo") String prodNo , Model model) throws Exception{

		System.out.println("/product/updateProduct : GET");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProduct.jsp";
	}
	
	//@RequestMapping("/updateProduct.do")
	@RequestMapping( value="updateProduct", method=RequestMethod.POST )

	public String updateProduct( @ModelAttribute("product") Product product, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{

		System.out.println("/product/updateProduct : POST");
		
		System.out.println(product);
		

		System.out.println("/product/addProduct : POST");
		product.setManuDate(product.getManuDate().substring(2, 10));
		List<MultipartFile> files = multipartHttpServletRequest.getFiles("uploadFile");
		System.out.println("들어온 파일 갯수 : "+files.size());
		System.out.println(filePath+"저장경로입니다.");
		product.setFileName(files.get(0).getOriginalFilename());		
		
		for (int i = 0; i < files.size(); i++) {
			 File file = new File(filePath, files.get(i).getOriginalFilename());
			files.get(i).transferTo(file);
		}
		productService.updateProduct(product);

		return "redirect:/product/getProduct?prodNo="+product.getProdNo();
	}
	
	//@RequestMapping("/listProduct.do")
	@RequestMapping( value="listProduct")

	public String listProduct(@ModelAttribute("search") Search search, Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		//System.out.println("메뉴로 들어온거 다시 세팅해줘"+menu);
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		//model.addAttribute("menu", menu);
		return "forward:/product/listProduct.jsp";
	}
}