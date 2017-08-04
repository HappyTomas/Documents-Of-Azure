<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>结算单明细</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function() {
		        top.$.jBox.confirm("确认要导出结算明细吗？", "系统提示",
		        function(v, h, f) {
		            if (v == 'ok') {
		                $("#searchForm").prop("action", "${ctx}/balance/businessBalanceDetail/export");
		                $("#searchForm").submit();
		            }
		        },
		        {
		            buttonsFocus: 1
		        });
		        top.$('.jbox-body .jbox-icon').css('top', '55px');
		    });
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
<style type="text/css">
.div-inline {
    display: inline;
    padding-right: 50px;
}
</style>	
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">结算单明细</li>
	</ul>
	<form:form id="searchForm" modelAttribute="businessBalanceDetail"
		action="${ctx}/balance/businessBalanceDetail/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
        <input type="hidden" name="businessBalanceId"
            value="${businessBalanceDetail.businessBalanceId}" />
		<ul class="ul-form">
			<li><form:select path="payType" class="input-medium">
					<form:option value="" label="所有产品模式订单" />
					<form:options items="${fns:getDictList('prod_type')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" /></li>
			<li style="float: right"><a class="btn btn-primary"
				href="${ctx}/balance/businessBalance/"><i
					class="icon-eye-open icon-custom"></i> 返回</a></li>
			<li style="float: right"><a id="btnExport" href="#" class="btn btn-primary"><i
                        class="icon-edit icon-custom"></i> 导出结算明细单</a></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
                <th>序号</th>
                <th>交易完成时间</th>
                <th>订单号</th>
				<th>产品模式</th>
				<th>支付方式</th>
				<th>在线交易金额（元）</th>
				<th>扣点金额（元）</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="businessBalanceDetail" varStatus="status">
			<tr>
			    <td>
                    ${status.count }
			    </td>
                <td>
                    <fmt:formatDate value="${businessBalanceDetail.tradeCompleteTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>
                    <c:choose>
                    <c:when test="${businessBalanceDetail.prodType==0}">
                        <a href="${ctx}/order/orderGoods/form?id=${businessBalanceDetail.orderId}"> 
	                       ${businessBalanceDetail.orderGroupNo}
	                    </a>        
                    </c:when>
                    <c:when test="${businessBalanceDetail.prodType==1}">
                        <a href="${ctx}/order/orderService/form?id=${businessBalanceDetail.orderId}"> 
                           ${businessBalanceDetail.orderNo}
                        </a>                
                    </c:when>
                    <c:when test="${businessBalanceDetail.prodType==2}">
                        <a href="${ctx}/order/orderLesson/form?id=${businessBalanceDetail.orderId}"> 
                           ${businessBalanceDetail.orderNo}
                        </a>        
                    </c:when>
                    <c:when test="${businessBalanceDetail.prodType==3}">
                        <a href="${ctx}/order/orderField/form?id=${businessBalanceDetail.orderId}"> 
                           ${businessBalanceDetail.orderNo}
                        </a>        
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/order/orderGroupPurc/form?id=${businessBalanceDetail.orderId}"> 
                           ${businessBalanceDetail.orderNo}
                        </a>        
                    </c:otherwise>
                    </c:choose>
                </td>
				<td>
					${fns:getDictLabel(businessBalanceDetail.prodType, 'prod_type', '未知')}
				</td>
				<td>
					${fns:getDictLabel(businessBalanceDetail.payType, 'payType', '未知')}
					-
					${fns:getDictLabel(businessBalanceDetail.payOrg, 'pay_type', '未知')}
				</td>
				<td>
					<fmt:formatNumber  type="currency">${businessBalanceDetail.orderMoney}</fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber  type="currency">${businessBalanceDetail.deductionMoney}</fmt:formatNumber>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>