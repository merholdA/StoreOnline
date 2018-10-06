<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<!--描述：菜单栏-->
	<div class="container-fluid">
		<div class="col-md-4">
			<img src="${pageContext.request.contextPath}/img/logo2.png" />
		</div>
		<div class="col-md-5">
			<img src="${pageContext.request.contextPath}/img/header.png" />
		</div>
		<div class="col-md-3" style="padding-top:20px">
			<ol class="list-inline">
				<c:if test="${ empty userBean }">
				<li><a href="${pageContext.request.contextPath}/UserServlet?method=loginUI">登录</a></li>
				<li><a href="${pageContext.request.contextPath}/UserServlet?method=registerUI">注册</a></li>
				</c:if>
				<c:if test="${ not empty userBean }">
				<li>欢迎光临 VIP: ${ userBean.username }</li>
				<li><a href="${pageContext.request.contextPath}/UserServlet?method=userLogout">登出</a></li>
				</c:if>
				<li><a href="${pageContext.request.contextPath}/jsp/cart.jsp">购物车</a></li>
				<li><a href="${pageContext.request.contextPath}/OrderServlet?method=getOrdersByPaging&pageIndex=1">我的订单</a></li>
			</ol>
		</div>
	</div>
	
	<!--描述：导航条-->
	<div class="container-fluid">
		<nav class="navbar navbar-inverse">
			<div class="container-fluid">
				<!-- Brand and toggle get grouped for better mobile display -->
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">首页</a>
				</div>

				<!-- Collect the nav links, forms, and other content for toggling -->
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav" id="ul_all_category">
					</ul>
					<form class="navbar-form navbar-right" role="search">
						<div class="form-group">
							<input type="text" class="form-control" placeholder="Search">
						</div>
						<button type="submit" class="btn btn-default">Submit</button>
					</form>

				</div>
				<!-- /.navbar-collapse -->
			</div>
			<!-- /.container-fluid -->
		</nav>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		//ajax网络请求
		$.post("${pageContext.request.contextPath}/CategoryServlet",
				{"method" : "getAllCategory"}, function(data) {
					console.log(data);
					//将数据绑定到li标签 并逐个添加到ul标签
					$(data).each(function(i, category) {
						var liEle = "<li><a href='${pageContext.request.contextPath}/ProductServlet?method=getProductByCategoryWithPaging&cid="
								+category.cid+"&pageIndex=1'>"+category.cname+"</a></li>";
						$("#ul_all_category").append(liEle);
					})
				}, "json");
	});
</script>
</html>