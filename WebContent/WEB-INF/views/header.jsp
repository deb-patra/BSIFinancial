<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
	<c:if test="${not empty previousPage}">
	  	<a class="container-section" href="${previousPage}">Back</a>
	</c:if>
	<br>
	<img style="max-width: 71%; height: auto; padding-left: 30px;" src="${imageName}"/>
</html>