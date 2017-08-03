<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>交易管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
    function page(n, s) {
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }
    
    
    
    /*
     * 商品信息单选
     */
    function itemCheck(obj) {
        $("#checkeIds").val("");
        var checkBoxs = $('input[name="itemCheckbox"]');
        for (var i = 0; i < checkBoxs.length; i++) {
            // 如果被按下则拼接商品ID
            if (checkBoxs[i].checked) {
                var checkeIds = $("#checkeIds").val() + checkBoxs[i].value + ",";
                $("#checkeIds").val(checkeIds);
            }
        }
    }
    
    // 根据按钮不同改变searchForm的ACTION
    function changeAction(type) {
        // 检索按钮按下的时候
        if (type=='0') {
            $("#searchForm")[0].action = "${ctx}/order/propertyDeal/";
            return true;
        // 导出按钮按下的时候
        } else if (type=='1') {
            $("#searchForm")[0].action = "${ctx}/order/propertyDeal/export";
            $("#searchForm")[0].submit();
            return true;
        }
        return false;
    }
    function allCheck(obj) {
        var checked = obj.checked;
        // 如果是非选中的状态清除隐藏域的商品ID信息
        if (!checked) {
            $("#checkeIds").val("");
        }
        var checkBoxs = $('input[name="itemCheckbox"]');
        for (var i = 0; i < checkBoxs.length; i++) {
            checkBoxs[i].checked = checked;
            if (checkBoxs[i].checked) {
                var checkeIds = $("#checkeIds").val() + checkBoxs[i].value + ",";
                $("#checkeIds").val(checkeIds);
            }

        }
    }
</script>
</head>
<body>
    <ul class="nav nav-tabs">
        <li>
            <a href="${ctx}/order/businessDeal/">商户交易管理</a>
        </li>
         <li class="active">
             <a href="${ctx}/order/propertyDeal">物业交易管理</a>
         </li>
    </ul>
    <form:form id="searchForm" modelAttribute="propertyDeal" action="${ctx}/order/propertyDeal/" method="post" class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
        <ul class="ul-form">
            <li>
                <input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" value="<fmt:formatDate value="${propertyDeal.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
                -
                <input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" value="<fmt:formatDate value="${propertyDeal.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
            </li>
             <li class="btns">
                 <form:select path="propertyCompanyId" class="input-medium">
                    <form:option value="" label="全部物业" />
                    <form:options items="${allCompany}" itemLabel="companyName" itemValue="id" htmlEscape="false" />
                </form:select>
            </li>
            <li class="btns">
                <form:select path="villageInfoId" class="input-medium">
                    <form:option value="" label="全部楼盘" />
                    <form:options items="${allVillage}" itemLabel="villageName" itemValue="id" htmlEscape="false" />
                </form:select>
            </li>
             <li class="btns">
                 <form:select path="moduleManageId" class="input-medium">
                    <form:option value="" label="全部模块" />
                    <form:options items="${allModule}" itemLabel="moduleName" itemValue="id" htmlEscape="false" />
                </form:select>
            </li>
             <li class="btns">
                <form:select path="terminalSource" class="input-medium">
                    <form:option value="" label="全部来源"/>
                    <form:options items="${fns:getDictList('terminal_source')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </li>
            <li class="btns">
               <form:select path="payType" class="input-medium">
                    <form:option value="" label="付款方式"/>
                    <form:options items="${fns:getDictList('payType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </li>
             <li class="btns">
                <form:input path="orderNo" htmlEscape="false" maxlength="64" placeholder="订单号" class="input-xlarge "/>
            </li>
            
            <li class="btns">
                <input id="btnSubmit" class="btn btn-success" type="submit"  onclick="changeAction('0')" value="查询" />
            </li>
            <li class="clearfix"></li>
        </ul>
    </form:form>
   <div style="text-align: left; margin: 20px 50px 20px 20px;">
   		
		<ol class="breadcrumb" style="background-color: #FFF;border: 0px none #fff;">
		  	<li >
		  		<input type="checkbox" id="allCheck" onclick="allCheck(this)"> 全选&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  		<input id="btnBack" class="btn" type="button" value="导出交易明细" style="width: 200px;" onclick="changeAction('1')" />
			</li>
		  	<li style="margin-left: 10%;width: 20%;color: red;">订单总额:${total.orderMoney } 元</li>
			<li style="width: 20%;color: red;">物业缴费:${total.payMoney } 元</li>
			<li style="width: 20%;color: red;">平台优惠:${total.discountMoney } 元</li>
		</ol>
	</div>
    <sys:message content="${message}" />
    <table id="contentTable" class="table table-striped table-bordered table-condensed">
    	<input type="hidden" id="checkeIds" value=""/>
        <thead>
            <tr>
                <th>序号</th>
                <th>物业名称</th>
                <th>模块类别</th>
                <th>订单号  </th>
                <th>订单金额</th>
                <th>平台优惠</th>
                <th>付款方式</th>
                <th>支付状态</th>
                <th>终端来源</th>
                <th>下单时间</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${page.list}" var="propertyDeal" varStatus="status">
                <tr>
                  <td><input type="checkbox" name="itemCheckbox" onclick="itemCheck(this)" value="${propertyDeal.id}"> ${status.count}</td>
               	  <td>${propertyDeal.companyName}</td>
                	<td>${propertyDeal.moduleName}</td>
                  	 <td>
                  	 	<a href="${ctx}/order/propertyDeal/form?id=${propertyDeal.id}">${propertyDeal.orderNo}</a>
                  	 </td>
                  	<td>${propertyDeal.orderMoney}</td>
                  <td>${propertyDeal.discountMoney}</td>
                   <td> ${fns:getDictLabel(propertyDeal.payType, 'payType', "")}</td>
                    <td> ${fns:getDictLabel(propertyDeal.payState, 'Pay_State', "")}</td>
                     <td> ${fns:getDictLabel(propertyDeal.terminalSource, 'terminal_source', "")}</td>
                     <td><fmt:formatDate value="${propertyDeal.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <div class="pagination">${page}</div>
</body>
</html>