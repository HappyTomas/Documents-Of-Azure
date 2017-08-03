<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>充值计划管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/modules/recharge/rechargeManage.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			//加载全部省市区数据
			fillPro();
			$.validator.addMethod("checkRechargeMoney",function(value,element,params){
				var flag = false;
				$.each($("#rechargeMoneyDiv").find("input[name='tempRechargeMoney']"),function(index,value,array){
					if ($(value).val() != null && $(value).val() != "") {
						flag = true;
					}
				});
				return flag;
			},"请输入充值面额");
			$("#inputForm").validate({
				submitHandler: function(form){
					// 提交前数据预处理
					dataPretreatment();
					if (checkMoneys() && checkDuplicateMoneys()) {
						loading('正在提交，请稍等...');
						form.submit();
					}
				},
				rules : {
					tempRechargeMoney : {
						checkRechargeMoney:"param"
					},
					"villageInfo.id" : {
						remote : {
							type:"POST",
				               url:"${ctx}/recharge/rechargeManage/checkVillageInfoId",
				               data:{
				            	   villageId:function(){return $("#addrVillage").val();}
				               } 
				        }
				    },
				},
				messages : {
					"villageInfo.id" : {
					      remote : "当前楼盘已存在充值计划，请勿重复添加",
						  required : "请选择楼盘"
					   }
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else if (element.is("#rechargeMoney")) {
						$("#rechargeMoneyDiv").find("br").eq(0).before(error);
					} else {
						error.appendTo(element.parent());
					}
				}
			});
			
			// 初始化充值面额
			formatRechargeDiv();
			
			//手动清除楼盘必填提示
			$("#addrVillage").change(function(){
	    		var msgAddrVillage = "请选择楼盘";
	    		var msgDuplicateVillage = "当前楼盘已存在充值计划，请勿重复添加";
	    	    var $labelError = $("#addrVillage").parent().children("label.error");
	    	    if ($labelError.size() != 0) {
	    	        $.each($labelError,function(index,value,array){
	    	       		if ($(value).text() == msgAddrVillage && $("#addrVillage").val() != "") {
	    	            	$(value).remove();
	    	       		} else if ($(value).text() == msgDuplicateVillage) {
	    	           		$(value).remove();
	    	       		}
	    	        });
	    	    }	
	    	});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/recharge/rechargeManage/form?option=edit&villageInfoId=${rechargeManage.villageInfo.id}">充值计划<shiro:hasPermission name="recharge:rechargeManage:edit">${option=='edit'?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="recharge:rechargeManage:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="rechargeManage" action="${ctx}/recharge/rechargeManage/save" method="post" class="form-horizontal">
		<input type="text" class="hide" id="hidProId"  value="${rechargeManage.villageInfo.addrPro}">
  		<input type="text" class="hide" id="hidCityId" value="${rechargeManage.villageInfo.addrCity}">
  		<input type="text" class="hide" id="hidAreaId" value="">
  		<input type="text" class="hide" id="hidVillageId" value="${rechargeManage.villageInfo.id}">
  		<input type="text" class="hide" id="hidRechargeMoney" name="rechargeMoney" value="testRechargeMoney">
  		<input type="text" class="hide" id="elemOption" name="option" value="${option}">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">选择楼盘：</label>
			<div class="controls">
				<c:choose>
					<c:when test="${option=='add'}">
						<select id="addrpro" name="addrPro" style="width: 120px" onchange="changeCity()">
							<option value="">全部省份</option>
						</select>
						<select id="addrcity" name="addrCity" style="width: 120px" onchange="changeVillage()">
								<option value="">全部城市</option>
						</select>
						<select id="addrarea" name="addrArea" style="width: 120px; display: none;">
								<option value="">全部区域</option>
						</select>
						<select id="addrVillage" name="villageInfo.id" style="width: 120px" class="required">
								<option value="">全部楼盘</option>
						</select>
						<span class="help-inline"><font color="red">*</font> </span>
					</c:when>
					<c:otherwise>
						<form:hidden path="villageInfo.id"/>
						<span class="help-inline"><font color="black">${rechargeManage.villageInfo.villageName}</font></span>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">充值面额：</label>
			<div id="rechargeMoneyDiv" class="controls">
					<c:choose>
				    	<c:when test="${option=='edit'}">
				    		<c:forEach items="${rechargeManage.rechargeManageList}" var="rechargeElem" varStatus="status">
								<span id="rechargeElemSpan">
									<span>充</span>
									<input id="rechargeMoney" name="tempRechargeMoney" class="input-mini" value="${rechargeElem.rechargeMoney}" maxLength="13"/>
									<span>元&nbsp;&nbsp;&nbsp;&nbsp;——&nbsp;&nbsp;&nbsp;&nbsp;赠送</span>
									<input id="giveMoney" name="tempGiveMoney"  class="input-mini" value="${rechargeElem.giveMoney}" maxLength="13"/>
									<span>元</span>
								</span>
								<br/>
							</c:forEach>
						</c:when>
					</c:choose>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="recharge:rechargeManage:edit"><input id="btnSubmit" class="btn btn-success" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
            <a id="btnCancel" href="${ctx}/recharge/rechargeManage/" class="btn btn-success"> 返 回</a>
		</div>
	</form:form>
</body>
</html>