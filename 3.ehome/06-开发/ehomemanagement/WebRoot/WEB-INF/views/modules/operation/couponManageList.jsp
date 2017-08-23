<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>优惠券管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		fillPro(); // 加载全部省市区数据
		$("#btuElemEdit").click(function(){
			if(elemEdit("优惠券")){
				var activeState = $("#"+$("#selectElemId").val()).html();
				if(activeState=="待开始"){
					return true;
				} else {
					alertx("活动已开始不能编辑",closed);
				    return false;
				}
			} else {
				return false;
			}
		});
		$("#btuElemDelete").click(function(){
			if($("#selectElemId").val()){
				var activeState = $("#"+$("#selectElemId").val()).html();
                var elemId = $("#selectElemId").val();
                var tempHref = $("#btuElemDelete").attr("href") + elemId;
	            if(activeState=="待开始"){
	                if (confirmx("确定删除此优惠券活动？",tempHref)) {
	                    return true;
	                } else {
	                    return false;
	                }
	            } else {
	                alertx("活动已开始不能删除",closed);
	                return false;
	            }
	        } else {
	            alertx("请选择要删除的优惠券活动");
	            return false;
	        }
		});
        $("#btuElemClose").click(function(){
            if($("#selectElemId").val()){
                var activeState = $("#"+$("#selectElemId").val()).html();
                var elemId = $("#selectElemId").val();
                var tempHref = $("#btuElemClose").attr("href") + elemId;
                if(activeState=="活动中"){
                    if (confirmx("确认关闭此优惠券活动？",tempHref)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    alertx("只有活动中的优惠券活动允许关闭",closed);
                    return false;
                }
            } else {
                alertx("请选择要关闭的优惠券活动");
                return false;
            }
        });
	});
    function page(n, s) {
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/operation/couponManage/">优惠券管理列表</a></li>
		<shiro:hasPermission name="operation:couponManage:edit">
			<li><a href="${ctx}/operation/couponManage/form">优惠券管理添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="couponManage"
		action="${ctx}/operation/couponManage/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<ul class="ul-form">
			<li><input name="activeStartTime" type="text" placeholder="活动起始时间"
				readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${couponManage.activeStartTime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			</li>
			<li>至 <input name="activeEndTime" type="text" placeholder="活动结束时间"
				readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${couponManage.activeEndTime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			</li>
			<li class="btns"><select id="addrpro" name="addrPro"
				style="width: 120px" onchange="changeCity()">
					<option value="">全部省份</option>
			</select> <select id="addrcity" name="addrCity" style="width: 120px"
				onchange="changeVillage()">
					<option value="">全部城市</option>
			</select> <select id="addrarea" name="addrArea" style="display: none;">
					<option value="">全部区域</option>
			</select> <select id="addrVillage" name="villageInfoId" style="width: 120px">
					<option value="">全部楼盘</option>
			</select> <input type="text" class="hide" id="hidProId" value="${couponManage.addrPro}"> <input
				type="text" class="hide" id="hidCityId" value="${couponManage.addrCity}"> <input
				type="text" class="hide" id="hidAreaId" value=""> <input
				type="text" class="hide" id="hidVillageId"
				value="${couponManage.villageInfoId}"></li>
			<li><form:select path="activeState" placeholder="活动状态"
					class="input-medium">
					<form:option value="" label="活动状态" />
					<form:options items="${fns:getDictList('active_state')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select></li>
			<li><form:input path="couponName" placeholder="优惠券名称"
					htmlEscape="false" maxlength="64" class="input-medium" /></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<!-- 操作按钮 start -->
	<div style="margin: 10px">
		<shiro:hasPermission name="operation:couponManage:edit">
			<a class="btn btn-primary" href="${ctx}/operation/couponManage/form"><i
				class="icon-plus icon-custom"></i> 添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="operation:couponManage:edit">
			<a id="btuElemEdit" href="${ctx}/operation/couponManage/form?id=" 
			    class="btn btn-primary"><i
				class="icon-edit icon-custom"></i> 编辑</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="operation:couponManage:edit">
			<a id="btuElemDelete" href="${ctx}/operation/couponManage/delete?id="
				class="btn btn-primary"><i
				class="icon-trash icon-custom"></i> 删除</a>
		</shiro:hasPermission>
        <shiro:hasPermission name="operation:couponManage:edit">
            <a id="btuElemClose" href="${ctx}/operation/couponManage/close?id="
                class="btn btn-primary"><i
                class="icon-trash icon-custom"></i> 关闭</a>
        </shiro:hasPermission>
	</div>
	<!-- 操作按钮 end -->
	<input id="selectElemId" type="hidden" value="" />
	<sys:message content="${message}" />
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>楼盘名称</th>
				<th>优惠券名称</th>
				<th>优惠券内容</th>
				<th>发放总量</th>
				<th>领取总量</th>
				<th>有效期</th>
				<th>活动状态</th>
				<shiro:hasPermission name="operation:couponManage:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="couponManage" varStatus="status">
				<tr onClick="selectElem(this)">
					<td>${status.count}<input id="elemId" type="hidden"
						value="${couponManage.id}" />
					</td>
					<td>${couponManage.villageName}</td>
					<td>${couponManage.couponName}</td>
					<td><c:choose>
							<c:when test="${couponManage.couponType==0}">
					           <fmt:formatNumber type="number">${couponManage.couponMoney}</fmt:formatNumber>元券
					       </c:when>
							<c:otherwise>
					           ${couponManage.couponMoney/10}折券<br />
					           <c:choose>
					               <c:when test="${couponManage.upperLimitMoney==null}">
						                                      （无上限）    
					               </c:when>
					               <c:otherwise>
					                                                   （上限 ${couponManage.upperLimitMoney}元）
					               </c:otherwise>
					           </c:choose>
					       </c:otherwise>
						</c:choose></td>
					<td><c:choose>
							<c:when test="${couponManage.grantType==0}">
								${fns:getDictLabel(couponManage.grantType, 'grant_type', '')}
							</c:when>
							<c:otherwise>
								<fmt:formatNumber type="number">${couponManage.limitedNum}</fmt:formatNumber>&nbsp;张
							</c:otherwise>
						</c:choose></td>
					<td>${couponManage.receiveCount}</td>
					<td><c:choose>
							<c:when test="${couponManage.validityType==0}">
								<fmt:formatDate value="${couponManage.validityStartTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /> 至<fmt:formatDate
									value="${couponManage.validityEndTime}"
									pattern="yyyy-MM-dd HH:mm:ss" />
							</c:when>
							<c:otherwise>
							     自领取之日起${couponManage.validityDays}天内有效
							</c:otherwise>
						</c:choose></td>
					<td><div id="${couponManage.id}">${fns:getDictLabel(couponManage.activeState, 'active_state', '无状态')}</div></td>
					<shiro:hasPermission name="operation:memberDiscount:view">
						<td><a
							href="${ctx}/operation/memberDiscount/list?discountId=${couponManage.id}">查看优惠券</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>