<%@ page contentType="text/html; charset=euc-kr"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>

��ǰ ��� ��ȸ

</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">
	<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	// �˻� / page �ΰ��� ��� ��� Form ������ ���� JavaScrpt �̿�  
	
$(function() {  
		
	$("a.pageNumber").on("click" , function() {
		$("#currentPage").val($(this).text());
		$("form").attr("method" , "POST").attr("action" , "/product/listProduct").submit();
	});
	
	$("a.pageDown").on("click" , function() {
		var count = Number($($("a.pageNumber")[0]).text());
		$("#currentPage").val(count-1);
		$("form").attr("method" , "POST").attr("action" , "/product/listProduct").submit();
	});
	
	$("a.pageUp").on("click" , function() {
		var count = Number($($("a.pageNumber")[$("a.pageNumber").size()-1]).text());
		$("#currentPage").val(count+1);
		$("form").attr("method" , "POST").attr("action" , "/product/listProduct").submit();
	});
	
	
	
function fncOrderBy(input) {
	document.getElementById("orderBy").value = input;
	document.getElementById("currentPage").value = 1;
	$("form").attr("method" , "POST").attr("action" , "/product/listProduct").submit();

}

function fncListSale(input) {
	document.getElementById("listSale").value = input;
	document.getElementById("currentPage").value = 1;
	$("form").attr("method" , "POST").attr("action" , "/product/listProduct").submit();
	
}
	
	$( "td.multiplay:contains('����ϱ�')" ).on("click" , function() {
	//Debug..
	//alert($("td.multiplay").index(this)+"???");
	//alert($(this).text());
	//alert($($(".prodNo")[$("td.multiplay").index(this)]).text());
	//alert("/purchase/updateTranCode.do?prodNo="+$($(".prodNo")[$("td.multiplay").index(this)]).text()+"&tranCode=2");
	var uri = "/purchase/updateTranCode/"+$($(".prodNo")[$("td.multiplay").index(this)]).text()+"/2/manage";
	
	$("form").attr("method" , "GET").attr("action" , uri).submit();
	});
});
	
	
</script>

</head>

<body bgcolor="#ffffff" text="#000000">

	<div style="width: 98%; margin-left: 10px;">

		<form name="detailForm">

			<table width="100%" height="37" border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"
						width="15" height="37" /></td>
					<td background="/images/ct_ttl_img02.gif" width="100%"
						style="padding-left: 10px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="93%" class="ct_ttl01">��ǰ �����ȸ</td>
							</tr>
						</table>
					</td>
					<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"
						width="12" height="37"></td>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
					<td align="right"><select name="searchCondition"
						class="ct_input_g" style="width: 80px">
							<option value="0" ${ ! empty search.searchCondition && search.searchCondition==0 ? "selected" : "" }>��ǰ��ȣ</option>
							<option value="1" ${ ! empty search.searchCondition && search.searchCondition==1 ? "selected" : "" }>��ǰ��</option>
							<option value="2" ${ ! empty search.searchCondition && search.searchCondition==2 ? "selected" : "" }>����</option>
					</select> <input type="text" name="searchKeyword"
						value="${! empty search.searchKeyword ? search.searchKeyword : ""}"  
						class="ct_input_g" style="width: 200px; height: 20px"
						 onKeypress="javascript:if(event.keyCode==13) fncGetUserList(1);"></td>
					<td align="right" width="70">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="17" height="23"><img
									src="/images/ct_btnbg01.gif" width="17" height="23" /></td>
								<td background="/images/ct_btnbg02.gif" class="ct_btn01"
									style="padding-top: 3px;"><a
									href="javascript:fncGetUserList('1');">�˻�</a></td>
								<td width="14" height="23"><img
									src="/images/ct_btnbg03.gif" width="14" height="23" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
					<td colspan="15">��ü ${resultPage.totalCount} �Ǽ�, ���� ${resultPage.currentPage} ������</td>

					<input type="hidden" id="menu" 	name="menu" value="${menu}" />
					<input type="hidden" id="orderBy" 	name="orderBy" value="${ !empty search.orderBy ? search.orderBy : ""}"/>
					<input type="hidden" id="listSale" 	name="listSale" value="${ !empty search.listSale ? search.listSale : 0}"/>
					<input type="button" value="�Ѱͺ���" onclick="javascript:fncOrderBy(1); ">
					<input type="button" value="��Ѱͺ���" onclick="javascript:fncOrderBy(2); ">
					&nbsp;&nbsp;
					<input type="button" value="�Ǹ���" onclick="javascript:fncListSale(1); ">
					<input type="button" value="���Ǹ���" onclick="javascript:fncListSale(2); ">
			</tr>										

				<tr>
					<td class="ct_list_b" width="100">No</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="100">��ǰNo</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">��ǰ��</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">����</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">������</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">�����</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">�������</td>
				</tr>
				<tr>
					<td colspan="15" bgcolor="808285" height="1"></td>
				</tr>
	<c:set var ="a" value="0"/>
	<c:forEach var="i" items="${list}">
	<c:set var="a" value="${a+1}"/>
					<tr class="ct_list_pop">
						<td align="center" class='countNo'>${a}</td>
						
						<td></td>
						<td align="center" class='prodNo'>${i.prodNo}</td>
						
						<td></td>
						<c:if test="${user.role!='admin'}">
						<td align="left">
						<a href="/product/getProduct?prodNo=${i.prodNo}&menu=${menu}">
						${i.prodName}</a>
						</c:if>
						<c:if test="${user.role=='admin' && i.stock == 0 }">
						<td align="left">
						<a href="/product/getProduct?prodNo=${i.prodNo}&menu=${menu}">
						${i.prodName}</a>
						</c:if>
						<c:if test="${user.role=='admin' && i.stock != 0}">
						<td align="left">
						<a href="/product/updateProduct?prodNo=${i.prodNo}&menu=${menu}">
						${i.prodName}</a>
						</c:if>
						
						</td>
						<td></td>
						<td align="left">${i.price}</td>
						<td></td>
						<td align="left">${i.prodDetail}</td>
						<td></td>
						<td align="left">${i.regDate}</td>
						<td></td>
						<td align="left" class="multiplay">
						<c:if test="${empty i.proTranCode || i.proTranCode=='4  '}">
					�Ǹ��� <!-- <a href="/addToCart.do?prodNo=${i.prodNo}">��ٱ��Ͽ� ���</a> -->
					</c:if>
					 <c:if test="${i.proTranCode=='1  ' }">
					���ſϷ�<c:if test="${menu=='manage'}"> ����ϱ�</c:if>
					</c:if>
					 <c:if test="${i.proTranCode=='2  ' }">�����</c:if>
					 <c:if test="${i.proTranCode=='3  ' }">��ۿϷ� </c:if>

					</td>
					</tr>
					<tr>
						<td colspan="15" bgcolor="D6D7D6" height="1"></td>
					</tr>
				</c:forEach>
			</table>

			<!-- PageNavigation Start... -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
				
					<td align="center"><input type="hidden" id="currentPage"
						name="currentPage" value="" /><jsp:include page="../common/pageNavigator.jsp"/>	
			
    	</td>
	</tr>
</table>
<!-- PageNavigation End... -->

</form>
</div>

</body>
</html>