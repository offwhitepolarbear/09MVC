<%@ page contentType="text/html; charset=euc-kr"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<html>
<head>
<title>구매 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">
	<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
	<script type="text/javascript">


	$(function() {
		
		$("a.pageNumber").on("click" , function() {
			$("#currentPage").val($(this).text());
			$("form").attr("method" , "POST").attr("action" , "/purchase/listPurchase").submit();
		});
		
		$("a.pageDown").on("click" , function() {
			var count = Number($($("a.pageNumber")[0]).text());
			$("#currentPage").val(count-1);
			$("form").attr("method" , "POST").attr("action" , "/purchase/listPurchase").submit();
		});
		
		$("a.pageUp").on("click" , function() {
			var count = Number($($("a.pageNumber")[$("a.pageNumber").size()-1]).text());
			$("#currentPage").val(count+1);
			$("form").attr("method" , "POST").attr("action" , "/purchase/listPurchase").submit();
		});
		
		
		$( "td.multi:contains('배송중')" ).on("click" , function() {
			var uri = "/purchase/updateTranCode/"+$($(".tranNo")[$("td.multi").index(this)]).text()+"/3/user";
		$("form").attr("method" , "GET").attr("action" , uri).submit();
		});
		
		$( "td.multi:contains('배송완료')" ).on("click" , function() {
			var uri = "/purchase/updateTranCode/"+$($(".tranNo")[$("td.multi").index(this)]).text()+"/4/user";
			$("form").attr("method" , "GET").attr("action" , uri).submit();
			});		
	});
	
	</script>
</head>

<body bgcolor="#ffffff" text="#000000">

	<div style="width: 98%; margin-left: 10px;">

		<form name="detailForm" >

			<table width="100%" height="37" border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"
						width="15" height="37"></td>
					<td background="/images/ct_ttl_img02.gif" width="100%"
						style="padding-left: 10px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="93%" class="ct_ttl01">구매 목록조회</td>
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
					<td colspan="11">전체 ${resultPage.totalCount} 건수, 현재
						${resultPage.currentPage} 페이지</td>
				</tr>
				<tr>
					<td class="ct_list_b" width="100">거래번호</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="100">회원ID</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="100">회원명</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">전화번호</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">배송주소</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b"width="125">물품현황</td>
				</tr>
				<tr>
					<td colspan="11" bgcolor="808285" height="1"></td>
				</tr>

	<c:forEach var="i" items="${list}">

				<tr class="ct_list_pop">
					<td align="center" class = "tranNo"><a href="/purchase/getPurchase?tranNo=${i.tranNo}">${i.tranNo}</a>
					</td>
					<td></td>
					<td align="left"><a href="/user/getUser?userId=${user.userId}">${user.userId}</a>
					</td>
					<td></td>
					<td align="left">${i.receiverName}</td>
					<td></td>
					<td align="left">${i.receiverPhone}</td>
					<td></td>
					<td align="left">
					${i.divyAddr}</td>
					<td></td>
					<td align="left" class="multi">
					<c:if test="${i.tranCode=='1  ' }">
						구매완료
					</c:if>
					 <c:if test="${i.tranCode=='2  ' }">
					배송중	수취확인
					</c:if> 
					<c:if test="${i.tranCode=='3  ' }">
					배송완료 주문취소
					</c:if>
					<c:if test="${i.tranCode=='4  ' }">
						주문취소됨
					</c:if>
					
					</td>
					
					</tr>
					<tr>
						<td colspan="11" bgcolor="D6D7D6" height="1"></td>
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