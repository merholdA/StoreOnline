<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>

	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>购物车</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
		<!-- 引入自定义css文件 style.css -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
		<style>
			body {
				margin-top: 20px;
				margin: 0 auto;
			}
			
			.carousel-inner .item img {
				width: 100%;
				height: 300px;
			}
			
			.container .row div {
				/* position:relative;
	 float:left; */
			}
			
			font {
				color: #3164af;
				font-size: 18px;
				font-weight: normal;
				padding: 0 10px;
			}
		</style>
	</head>

	<body>

		<%@ include file="header.jsp" %>

		<div class="container">
			<c:if test="${empty cart.cartItems}">
				<div class="row">
					<div class="col-md-12"><h2>购物车还是空的~</h2></div>
				</div>
			</c:if>
			<c:if test="${not empty cart.cartItems}">
				<div class="row">
					<div style="margin:0 auto; margin-top:10px;width:950px;">
						<strong style="font-size:16px;margin:5px 0;">订单详情</strong>
						<table class="table table-bordered">
							<tbody>
								<tr class="warning">
									<th>图片</th>
									<th>商品</th>
									<th>价格</th>
									<th>数量</th>
									<th>小计</th>
									<th>操作</th>
								</tr>
								<c:forEach items="${cart.cartItems}" var="item">
								<form id="del_cartItem_form" action="${pageContext.request.contextPath}/CartServlet?method=delCartItem" method="post">
									<tr class="active">
										<td width="60" width="40%">
											<input type="hidden" name="pid" value="${item.product.pid}">
											<img src="${pageContext.request.contextPath}/${item.product.pimage}" width="70" height="60">
										</td>
										<td width="30%">
											<a target="_blank">${item.product.pname}</a>
										</td>
										<td width="20%">
											￥${item.product.shop_price}
										</td>
										<td width="10%">
											<input type="text" name="quantity" value="${item.count}" maxlength="4" size="10">
										</td>
										<td width="15%">
											<span class="subtotal">￥${item.subTotal}</span>
										</td>
										<td>
											<a id="del_product_item" href="javascript:void(0)" class="delete">删除</a>
										</td>
									</tr>
								</form>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
	
				<div style="margin-right:130px;">
					<div style="text-align:right;">
						<em style="color:#ff6600;">
					登录后确认是否享有优惠&nbsp;&nbsp;
				</em> 赠送积分: <em style="color:#ff6600;">${cart.points}</em>&nbsp; 商品金额: <strong style="color:#ff6600;">￥${cart.totalAmount}元</strong>
					</div>
					<div style="text-align:right;margin-top:10px;margin-bottom:10px;">
						<form id="clear_cart_form" action="${pageContext.request.contextPath}/CartServlet?method=clearCart" method="post">
						<a href="javascript:void(0)" id="clear" class="clear">清空购物车</a>
						</form>
						<form id="submit_order_form" action="${pageContext.request.contextPath}/OrderServlet?method=submitOrder" method="post">
						<div>
							<hr/>
							<div class="form-group">
								<label for="address" class="col-sm-1 control-label">地址</label>
								<div class="col-sm-5">
									<input type="text" class="form-control" id="address" name="address" placeholder="请输入收货地址">
								</div>
							</div>
							<div class="form-group">
								<label for="name" class="col-sm-1 control-label">收货人</label>
								<div class="col-sm-5">
									<input type="text" class="form-control" id="name" name="name" placeholder="请输收货人">
								</div>
							</div>
							<div class="form-group">
								<label for="telephone" class="col-sm-1 control-label">电话</label>
								<div class="col-sm-5">
									<input type="number" class="form-control" id="telephone" name="telephone" placeholder="请输入联系方式">
								</div>
							</div>
							<hr/>
							<br/>
							<br/>
							<p>
							<a href="javascript:void(0)">
								<%--提交表单 --%>
								<input id="submit_order" type="submit" width="100" value="提交订单" name="submit" border="0" style="background: url('${pageContext.request.contextPath}/img/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0);
								height:35px;width:100px;color:white;">
							</a>
							</p>
						</div>
						</form>
					</div>
				</div>
			</c:if>

		</div>

		<!--	描述：页脚部分	-->
		<%@ include file="footer.jsp" %>

	</body>
	<script type="text/javascript">
		$(function() {
			$("#del_product_item").click(function() {
				$("#del_cartItem_form").submit();
			});
			
			$("#clear").click(function() {
				$("#clear_cart_form").submit();
			});
			
			$("#submit_order").click(function() {
				$("#submit_order_form").submit();
			});
		});
	</script>
</html>