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
		System.out.println("� ���ϵ��Գ� : "+files.size());
		System.out.println(filePath+"�������Դϴ�.");
			product.setFileName(files.get(0).getOriginalFilename());		
		
		for (int i = 0; i < files.size(); i++) {
			 File file = new File(filePath, files.get(i).getOriginalFilename());
			files.get(i).transferTo(file);
		}
		
		//����θ� ã�ƺ��ô�.
		//String filePath2=session.getServletContext().getRealPath("/");
		//System.out.println("����� ����� ����� �غ�"+filePath2);
		//filePath2="C:\\workspace\\09.Model2MVCShop(jQuery)\\WebContent\\images\\uploadFiles";
		
		//
		//File file = new File(filePath, uploadFile.getOriginalFilename());		
		//uploadFile.transferTo(file);
		
		productService.addProduct(product);

		
		return "redirect:/product/listProduct?menu=manage";
	}
	
	//@RequestMapping("/getProduct.do")
	@RequestMapping( value="getProduct", method=RequestMethod.GET )
	public String getProduct( @RequestParam("prodNo") String prodNo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//System.out.println("/getProduct.do");
		System.out.println("/product/getProduct : GET");

		
		String history = null;
		Cookie[] cookies = request.getCookies();
		System.out.println(cookies);
		
		if (cookies != null) {
			System.out.println("��Ű�� �ֽ��ϴ�.");
			Cookie CookieOne = new Cookie("history", prodNo + "");
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
						if (cookie.getName().equals("history")) {
							history = cookie.getValue();
							if(history.substring(history.length()-5).equals(prodNo+"")) {
								CookieOne = new Cookie("history", history);
								
								System.out.println("�ߺ���ȸ�� �ߺ��κ� ������.");

							}
							else {
								CookieOne = new Cookie("history", history + "," + prodNo);
								System.out.println("���ο� ��ǰ��������� �߰���.");

							}
						}	
				}
				//��Ű�� �����ϴ� �ּҰ�/product/... �� ����Ǿ
				//�⺻������ ����� port/ �� �����ϴ� �����丮���� ��ȸ �Ұ�������
				//��Ű�� ����� �� �ִ� ��μ����� ���ּ���
				CookieOne.setPath("/");
			response.addCookie(CookieOne);
		}
		
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model �� View ����
		model.addAttribute("product", product);
		
		return "forward:/product/getProduct.jsp";
	}
	
	//@RequestMapping("/updateProductView.do")
	@RequestMapping( value="updateProduct", method=RequestMethod.GET )

	public String updateUserView( @RequestParam("prodNo") String prodNo , Model model ) throws Exception{

		System.out.println("/product/updateProduct : GET");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model �� View ����
		model.addAttribute("product", product);
		
		return "forward:/product/updateProduct.jsp";
	}
	
	//@RequestMapping("/updateProduct.do")
	@RequestMapping( value="updateProduct", method=RequestMethod.POST )

	public String updateProduct( @ModelAttribute("product") Product product,  String prodNo, Model model , HttpServletRequest request, HttpServletResponse response) throws Exception{

		System.out.println("/product/updateProduct : POST");
		
		System.out.println(product);
		

		if(FileUpload.isMultipartContent(request)) {
			String temDir = "C:\\workspace\\09.Model2MVCShop(jQuery)\\WebContent\\images\\uploadFiles";
			
			DiskFileUpload fileUpload = new DiskFileUpload();
			fileUpload.setRepositoryPath(temDir);
			fileUpload.setSizeMax(1024*1024*10);
			fileUpload.setSizeThreshold(1024*100);
			
			if(request.getContentLength()<fileUpload.getSizeMax()) {
				
				//product = new Product();
				
				StringTokenizer token = null;
				
				List fileItemList = fileUpload.parseRequest(request);
				
				int size = fileItemList.size();
				for (int i = 0 ; i<size; i++) {
					FileItem fileItem = (FileItem) fileItemList.get(i);
				
					if(fileItem.isFormField()) {
						if(fileItem.getFieldName().equals("manuDate")) {
							token = new StringTokenizer(fileItem.getString("euc-kr"), "-");
							String manuDate = token.nextToken()+token.nextToken()+token.nextToken();
							product.setManuDate(manuDate);
						}
						else if(fileItem.getFieldName().equals("prodName"))
							product.setProdName(fileItem.getString("euc-kr"));
						else if(fileItem.getFieldName().equals("prodDetail"))
							product.setProdDetail(fileItem.getString("euc-kr"));
						else if(fileItem.getFieldName().equals("price"))
							product.setPrice(Integer.parseInt(fileItem.getString("euc-kr")));
						else if(fileItem.getFieldName().equals("prodNo"))
							product.setProdNo(Integer.parseInt(fileItem.getString("euc-kr")));
						
					} else {
						
						if (fileItem.getSize()>0) {
							int idx = fileItem.getName().lastIndexOf("\\");
							
							if (idx == -1) {
								idx = fileItem.getName().lastIndexOf("/");
							}
							String fileName = fileItem.getName().substring(idx+1);
							product.setFileName(fileName);
							try {
								File uploadedFile = new File(temDir, fileName);
								fileItem.write(uploadedFile);
							} catch(IOException e) {
								System.out.println(e);
							}
						}else{
							product.setFileName("../../images/empty.GIF");
					}
				}
				
			}
				
				productService.updateProduct(product);
		} else {
			int overSize = (request.getContentLength() /1000000);
			System.out.println("�ø����� ũ��� "+overSize+"mb �Դϴ�. ����1mb�Ѿ ����" );
			System.out.println("history.back();</script>");
			
		}
		}else {
			System.out.println("���ڵ� Ÿ���� multipart/from-data �� �ƴմϴ�");
		}

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
		
		// Business logic ����
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		//System.out.println("�޴��� ���°� �ٽ� ��������"+menu);
		// Model �� View ����
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		//model.addAttribute("menu", menu);
		return "forward:/product/listProduct.jsp";
	}
}