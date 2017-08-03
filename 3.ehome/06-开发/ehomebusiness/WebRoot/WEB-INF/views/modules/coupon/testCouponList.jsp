<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>验券管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
        <li style="height:30px"><span><a href="${ctx}/coupon/groupPurchase/">优惠/验券管理 </a>
        > 验券
        </span>
        </li>
    </ul>
    <form:form id="searchForm" modelAttribute="orderGroupPurcList" action="${ctx}/coupon/testCoupon/" method="post" class="breadcrumb form-search" style="width:700px;">		
		<ul class="ul-form">
			<li class="btns">
				<label>券号：</label>
				<form:input path="groupPurcNumber" id="groupPurcNumber" placeholder="请输入12位券号进行查找" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns" style="float:right;margin-right:60px"><input id="btnSubmit" class="btn btn-success" type="submit" value="查找券号" onclick="querySub()"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
    <sys:message content="${message}"/>
    <input type="hidden" id="checkedNumberId"/>
    <input type="hidden" id="data" value="${data}"/>
    <!-- 查询结果：确认消费、确认结果、券号列表 -->
    <div id="searchResult" style="display:none;">   
	    <!-- 确认消费 -->
	    <div id="confirm"  class="breadcrumb" style="width: 700px;">
	    	<div style="margin-bottom:5px">
	    		<label style="margin-left:40px">已搜索出券号：</label>
	    		<label>${orderGroupPurcList.groupPurcNumber}</label>
	    		<div style="float:right;margin-right:65px">
	    		   <input id="btnConfirm" class="btn btn-success" type="submit" value="确认消费" disabled="disabled"/>
	    		</div>
	    	</div>
	    	<div style="margin-left:35px;">
	    		<span class="help-inline">同一订单中还查找到下列券号，如需验券请从列表中勾选</span>
	    	</div>
	    </div>
	    <!-- 确认结果-->
	    <div id="confirmResult" class="breadcrumb" style="width: 700px;display:none;">
	    	<div style="margin-bottom:5px">
		    	<label id="groupNumbers" style="margin-left:40px;width:440px;"></label>
		    	<div style="float:right;margin-right:65px">
			    	<input id="btnReturn" class="btn" type="submit" value="返回"/>
			    </div>
			 </div>
	    </div>
	    <!-- 券号列表 -->
	    <div id="numberList"  class="breadcrumb" style="width: 700px;">
	    	<table id="contentTable" class="table table-bordered table-condensed">
				<thead>
					<tr>
					    <th><input type="checkbox" id="allCheck" onclick="allCheck(this)">全选</th>
					    <th>券号</th>
					    <th>类型</th>
						<th>状态</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${orderGroupList}" var="orderGroup" varStatus="status">
					<tr>
					    <td>
					    	<c:choose>
				    			<c:when test="${orderGroup.groupPurcState == 0}">
					    			<input id="checkbox${status.count}" type="checkbox" name="itemCheckbox" onclick="itemCheck(this,${orderGroup.groupPurcState})" value="${orderGroup.id}">
					    		</c:when>
					    		<c:otherwise>
					    			<input id="checkbox${status.count}" type="checkbox" name="itemCheckbox" onclick="itemCheck(this,${orderGroup.groupPurcState})" value="${orderGroup.id}" disabled="disabled">
					    		</c:otherwise>
					    	</c:choose>					    	
					    </td>
		                <td>
		                    <label id="number${status.count}">${orderGroup.groupPurcNumber}</label>
		                </td>
						<td>
							<label id="type${status.count}">${orderGroup.type}</label>
		                </td>
						<td>
							<label id="state${status.count}">${fns:getDictLabel(orderGroup.groupPurcState, 'ordergrouppurcstate', '')}</label>
		                </td>						
					</tr>
				</c:forEach>
				</tbody>
			</table>
	    </div>	    
	</div>
	
	<script type="text/javascript">	
	//页面初始化
	$(document).ready(function() {
		var data = '${data}';
		if(data == "exist"){
			//查询结果区域
			$("#searchResult").css("display","block");
		}
	});
	//查找券号按钮按下
    function querySub(){
		//券号
        var number = $("#groupPurcNumber").val();
		if(number == '' || number == null){
			//alertx("请输入券号");
			return
		}
    }
	
	//团购券消费全选
	function allCheck(obj) {
		var checked = obj.checked;
		// 如果是非选中的状态清除隐藏域的团购券ID信息
		if (!checked) {
			$("#checkedNumberId").val("");
			$("#btnConfirm").attr("disabled",true);
		}
		var checkBoxs = $('input[name="itemCheckbox"]');
		for (var i = 0;i < checkBoxs.length; i++) {
			if(!checkBoxs[i].disabled){
				checkBoxs[i].checked=checked;
				if (checkBoxs[i].checked) {
		            var checkedNumberId = $("#checkedNumberId").val() + checkBoxs[i].value +",";
		            $("#checkedNumberId").val(checkedNumberId);
		            $("#btnConfirm").attr("disabled",false);
				}
			}
		}
	}

    //团购券消费单选
    function itemCheck(obj,state) {
    	$("#checkedNumberId").val("");
        var checkBoxs = $('input[name="itemCheckbox"]');
        var checkFlag = 0;
        for (var i = 0;i < checkBoxs.length; i++) {
        	// 如果被按下则拼接服务ID
        	if (checkBoxs[i].checked) {
                var checkedNumberId = $("#checkedNumberId").val() + checkBoxs[i].value +",";
                $("#checkedNumberId").val(checkedNumberId);
                checkFlag = 1;
        	}
        }
        
        if(checkFlag == 0){
        	$("#btnConfirm").attr("disabled",true);
        }else{        	
        	$("#btnConfirm").attr("disabled",false);
        }
	}
    
    //点确认消费按钮，确认团购券消费
	$("#btnConfirm").click(function(){
		//选中的团购券信息Id集合
		var checkedNumberId=$("#checkedNumberId").val();
		var numberIds = checkedNumberId.split(",");
	
		//确认消费
		$.get("${ctx}/coupon/testCoupon/confirm"
				, {checkedNumberId:checkedNumberId},
			function (data,status) {
                if(status=="success"){
                	//确认消费区域
        			$("#confirm").css("display","none");
                	
                	//结果确认团购券号
        			var groupNumbers="";
        			for (var i=0; i<data.length; i++){
        				var count=i+1;
        				//团购券状态
        				if("0" == data[i].groupPurcState){        					
        				    $("#state" + count).text("未消费")
        				}else{
        					$("#state" + count).text("已消费")
        				}
        				//已勾选的团购券号
        				for(var j=0; j < numberIds.length; j++){
        					if(data[i].id == numberIds[j]){
        						//确认的团购券号
                				groupNumbers =groupNumbers+data[i].groupPurcNumber+"、";
        					}
        				}       				
        			}
        			
        			//去掉末尾的、符号
        			if(groupNumbers !=""){
        				groupNumbers=groupNumbers.substr(0,groupNumbers.length-1);
        			}
        			
        			$("#groupNumbers").text(groupNumbers+" 券号已确认消费。");
                	//确认结果
                	$("#confirmResult").css("display","block");
                	
                }else{
                	alertx("确认消费失败！");
                }
	    }); 
		
	});
    
	//确认团购券消费结果中，返回按钮的点击时间
	$("#btnReturn").click(function(){		
		//清除前次查询的券号以及查询结果
		//清除前次查询的券号
		$("#groupPurcNumber").val("");
		//查询结果：确认消费、确认结果、券号列表 
		$("#searchResult").css("display","none");
	});
	</script>
</body>
</html>