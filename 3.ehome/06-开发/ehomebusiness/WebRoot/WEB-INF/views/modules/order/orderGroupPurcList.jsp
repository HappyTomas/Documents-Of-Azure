<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>订单-团购类管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
        // 根据导航下拉菜单的值跳转至不同窗口
        function hrefByProdType(value) {
            if ('0' == value) {
                // 迁移至商品订单列表页面
                window.location.href="${ctx}/order/orderGoods";
            } else if ('1' == value) {
                // 迁移至服务订单列表页面
                window.location.href="${ctx}/order/orderService";
            } else if ('2' == value) {
                // 迁移至课程培训订单列表页面
                window.location.href="${ctx}/order/orderLesson";
            } else if ('3' == value) {
                // 迁移至场地预约订单列表页面
                window.location.href="${ctx}/order/orderField";
            } else {
                // 迁移至团购订单列表页面
                window.location.href="${ctx}/order/orderGroupPurc";
            }
        }
        // 根据按钮不同改变searchForm的ACTION
        function changeAction(type) {
            // 检索按钮按下的时候
            if (type=='0') {
                $("#searchForm")[0].action = "${ctx}/order/orderGroupPurc/";
                return true;
            // 导出按钮按下的时候
            } else if (type=='1') {
                $("#searchForm")[0].action = "${ctx}/order/orderGroupPurc/export";
                return true;
            }
            return false;
        }
        $(function(){
        	$('#phone').keyup(function(e){
        		var key = e.keyCode;
        		if(!(key>=48 && key<=57)){
        			$(this).val($(this).val().replace(/\D/g,''));
        		}
        	});
        });
    </script>
</head>
<body>
    <ul class="nav nav-tabs">
        <li><p style="font-weight:bold;font-size:18px;">订单管理</p></li>
        <li>
            <select id="nowProdType" name="nowProdType" style="width:200px;margin-left:15px;" onchange="hrefByProdType(this.value)">
                <c:forEach items="${businessCategorydictList}" var="businessCategorydict" varStatus="status">
                    <c:choose>
                        <c:when test="${businessCategorydict.prodType == '0'}">
                            <option value="0" <c:if test="${nowProdType == '0'}">selected</c:if>>商品订单</option>
                        </c:when>
                        <c:when test="${businessCategorydict.prodType == '1'}">
                            <option value="1" <c:if test="${nowProdType == '1'}">selected</c:if>>服务订单</option>
                        </c:when>
                        <c:when test="${businessCategorydict.prodType == '2'}">
                            <option value="2" <c:if test="${nowProdType == '2'}">selected</c:if>>课程培训</option>
                        </c:when>
                        <c:when test="${businessCategorydict.prodType == '3'}">
                            <option value="3" <c:if test="${nowProdType == '3'}">selected</c:if>>场地预约</option>
                        </c:when>
                    </c:choose>
                </c:forEach>
                <option value="4" <c:if test="${nowProdType == '4'}">selected</c:if>>团购订单</option>
            </select>
        </li>
    </ul>
    <form:form id="searchForm" modelAttribute="orderGroupPurc" action="${ctx}/order/orderGroupPurc/" method="post" class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <ul class="ul-form">
            <li><label>订单号：</label>
                <form:input path="orderNo" htmlEscape="false" maxlength="64" class="input-medium"/>
            </li>
            <li><label>团购名称：</label>
                <form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
            </li>
             <li><label>手机号：</label>
                <form:input path="accountPhoneNumber" id="phone" htmlEscape="false" maxlength="11" class="input-medium"/>
            </li>
            <li>
           		 <label style="width: 25px;"></label>
                <form:select path="orderState" class="input-medium">
                    <form:option value="" label="订单状态"/>
                    <form:options items="${fns:getDictList('order_group_purc_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </li>
            <form:hidden path="searchFlg" value="1"/>
            <li class="btns"> <input id="btnSubmit" class="commonsmallbtn" type="submit" style="width: 60px;height: 30px;font-size: 14px;"  onclick="changeAction('0')" value="查询"/></li>
           	<li class="btns"> <input class="commonsmallbtn" type="button" style="width: 60px;height: 30px;font-size: 14px;" onclick="formReset('#searchForm')" value="重置"/></li>
            <li class="btns"> <input id="btnExport" class="commonbtn" style="width: 60px;" type="submit"  onclick="changeAction('1')" value="导出"/></li>
        </ul>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable" class="table table-striped table-bordered table-condensed">
        <thead>
            <tr>
                <th>订单号</th>
                <th>团购名称</th>
                <th>团购价</th>
                <th>数量</th>
                <th>实付金额</th>
                <th>订单状态</th>
                <th>手机号</th>
                <th>下单时间</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="orderGroupPurc" varStatus="status">
           	<tr>
                <td><a style="color: #60C1F7;" href="${ctx}/order/orderGroupPurc/form?id=${orderGroupPurc.id}&tag=1">
                    ${orderGroupPurc.orderNo}
                </a></td>
                <td>
               	 <a style="color: #60C1F7;" href="${ctx}/coupon/groupPurchase/detail?id=${orderGroupPurc.groupPurchaseId}">
                    ${orderGroupPurc.name}
                  </a>
                </td>
                <td>
                    ${orderGroupPurc.groupPurcPrice}
                </td>
                <td>
                    ${orderGroupPurc.payNumber}
                </td>
                <td>
                   ${orderGroupPurc.payMoney}
                </td>
                <td <c:if test="${orderGroupPurc.orderState == 1}">style="color:red"</c:if>>
                    ${fns:getDictLabel(orderGroupPurc.orderState, 'order_group_purc_state', "")}
                </td>
                <td>
                	 ${orderGroupPurc.accountPhoneNumber}
                </td>
                <td>
                    <c:if test="${orderGroupPurc.createDate !=null && orderGroupPurc.createDate !=''}">
                    <span>下单：<fmt:formatDate value="${orderGroupPurc.createDate}" pattern="yyyy-MM-dd HH:mm"/></span><br/>
                    </c:if>
                    <c:if test="${orderGroupPurc.payTime !=null && orderGroupPurc.payTime !=''}">
                    <span>支付：<fmt:formatDate value="${orderGroupPurc.payTime}" pattern="yyyy-MM-dd HH:mm"/></span><br/>
                    </c:if>
                     <c:if test="${orderGroupPurc.orderState=='1' && orderGroupPurc.consumeTime !=null && orderGroupPurc.consumeTime !=''}">
                    <span>消费：<fmt:formatDate value="${orderGroupPurc.consumeTime}" pattern="yyyy-MM-dd HH:mm"/></span>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="pagination">${page}</div>
</body>
</html>