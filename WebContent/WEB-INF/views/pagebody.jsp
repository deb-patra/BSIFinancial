<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
	<form action="${nextPage}?userId=${userId}" method="post">
			<section class="container-section">
				<div class="panel-body">
			    	<table class="table table-hover" border="1" >
			         <thead class="thead-inverse">
			         <tr>
			             <th>User Id</th>
			             <th>Email Id</th>
			             <th>Layer Name</th>
			             <th>Channel Name</th>
			             <th>Experiment Token</th>
			             <th>Experiment Id</th>
			             <th>Experiment Name</th>
			         </tr>
			         </thead>
			         <tr>
			             <td>${eventSubmit.userId}</td>
			             <td>${eventSubmit.emailId}</td>
			             <td>${eventSubmit.layerName}</td>
			             <td>${eventSubmit.channelName}</td>
			             <td>${eventSubmit.variantToken}</td>
			             <td>${eventSubmit.expId}</td>
			             <td>${eventSubmit.exptName}</td>
			         </tr>
			        </table>
			    </div>
			    <c:if test="${not empty nextPage}">
				  	<input type="submit" value = "Checkout" class="btn-primary">
				</c:if>
				<input type="hidden" value="${userId}">
			</section>
	</form>
</html>