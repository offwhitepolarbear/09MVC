<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ page pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${ resultPage.currentPage <= resultPage.pageUnit }">
		�� ó��
</c:if>
<c:if test="${ resultPage.currentPage > resultPage.pageUnit }">
		<a class="pageDown">�� ����</a>
</c:if>

<c:forEach var="i"  begin="${resultPage.beginUnitPage}" end="${resultPage.endUnitPage}" step="1">
	<a class="pageNumber">${ i }</a>
</c:forEach>

<c:if test="${ resultPage.endUnitPage >= resultPage.maxPage }">
		�� ��
</c:if>
<c:if test="${ resultPage.endUnitPage < resultPage.maxPage }">
		<a class="pageUp">���� ��</a>
</c:if>