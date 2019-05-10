<%@ page contentType="text/html; charset=euc-kr"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>�Ǹ� �����ȸ</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

	<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
	<script type="text/javascript">
$(function() {
	
	$("a.pageNumber").on("click" , function() {
		$("#currentPage").val($(this).text());
		$("form").attr("method" , "POST").attr("action" , "/purchase/listSalePurchase").submit();
	});
	
	$("a.pageDown").on("click" , function() {
		var count = Number($($("a.pageNumber")[0]).text());
		$("#currentPage").val(count-1);
		$("form").attr("method" , "POST").attr("action" , "/purchase/listSalePurchase").submit();
	});
	
	$("a.pageUp").on("click" , function() {
		var count = Number($($("a.pageNumber")[$("a.pageNumber").size()-1]).text());
		$("#currentPage").val(count+1);
		$("form").attr("method" , "POST").attr("action" , "/purchase/listSalePurchase").submit();
	});
	
	});

</script>
</head>

<body bgcolor="#ffffff" text="#000000">

	<div style="width: 98%; margin-left: 10px;">

		<form name="detailForm" action="/purchase/listSalePurchase" method="post">

			<table width="100%" height="37" border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"
						width="15" height="37"></td>
					<td background="/images/ct_ttl_img02.gif" width="100%"
						style="padding-left: 10px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="93%" class="ct_ttl01">�ŷ��� �����ȸ</td>
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
					<td colspan="13">��ü ${resultPage.totalCount} �Ǽ�, ����
						${resultPage.currentPage} ������</td>
				</tr>
				<tr>
					<td class="ct_list_b" width="7%">�ŷ���ȣ</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="15%">��ǰ��</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="10%">����</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="7%">ȸ��ID</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="25%">����ּ�</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="15%">��ۿ�û����</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b"width="20%">��ǰ��Ȳ</td>
					<td class="ct_line02"></td>
				</tr>
				<tr>
					<td colspan="13" bgcolor="808285" height="1"></td>
				</tr>

			<c:forEach var="i" items="${list}">
				<tr class="ct_list_pop">
					<td align="center"><a href="/purchase/getPurchase?tranNo=${i.tranNo}">${i.tranNo}</a>
					</td>
					<td></td>
					<td align="left">${i.purchaseProd.prodName}</td>
					<td></td>
					<td align="left">${i.purchaseProd.price}</td>
					<td></td>
					<td align="left"><a href="/user/getUser?userId=${i.buyer.userId}">${i.buyer.userId}</a>
					</td>
					<td></td>
					<td align="left">
					${i.divyAddr}</td>
					<td></td>
					<td align="left">
					${i.divyRequest}</td>
					<td></td>
					<td align="left">
					<c:if test="${i.tranCode=='1  ' }">
						���ſϷ� <a href="/purchase/updateTranCode?tranNo=${i.tranNo}&tranCode=2">����ϱ�</a>
						<a href="/purchase/updateTranCode?tranNo=${i.tranNo}&tranCode=4">�ŷ����</a>
					</c:if>
					 <c:if test="${i.tranCode=='2  ' }">
					�����
					</c:if> 
					<c:if test="${i.tranCode=='3  ' }">
					��ۿϷ� 
					</c:if>
					<c:if test="${i.tranCode=='4  ' }">
						�ֹ���ҵ�
					</c:if>
					</td>	
					
					</tr>
					<tr>
						<td colspan="13" bgcolor="D6D7D6" height="1"></td>
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