<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>版本管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var checkFlag;
		$(document).ready(function() {
			checkProductLine();
			//$("#name").focus();
			$.validator.addMethod("checkProductLine",function(value,element,params){
					return (value != null && value != "");
			},"请输入产品线名称");
			$.validator.addMethod("checkSystemType",function(value,element,params){
					if (("disabled" != $(element).attr("disabled") && "true" != $(element).attr("disabled")) 
							&& (value == null || value == "")) {
						if ($("#productLine").val() == null || $("#productLine").val() == "") {
							return true;
						} else {
							return false;
						}
					} else {
						return true;
					}
			},"请输入产品线名称");
			$("#inputForm").validate({
				ignore:[],
				submitHandler: function(form){
					checkEdition();
					if (checkFlag && setSoftwarePreview()) {
						loading('正在提交，请稍等...');
						form.submit();
					}
				},
				rules : {
					productLine : {
						checkProductLine:"param"
					},
					systemType : {
						checkSystemType:"param"
					}
				},
				messages : {
				   editionName : {
					  required : "请输入版本名称"
				   },
				   editionNo : {
					  required : "请输入版本号"
				   },
				   editionInstruction : {
					 required : "请输入版本说明"
				   },
				   selectedFile : {
						required : "请上传软件"
					}
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else if (element.is(".product-line")) {
						error.insertAfter(element.parent().children(":last-child"));
					} else if (element.is("#selectedFile")) {
						error.appendTo(element.parent().find("span.help-inline"));
					} else {
						error.appendTo(element.parent());
					}
				}
			});
			
	    	 // 手动清除产品线必填提示
			$("#productLine").change(function(){
			    checkProductLine();
    			cleanProductLineMsg();
	    	});
	    	 
			//手动清除产品线必填提示
			$("#systemType").change(function(){
    			cleanProductLineMsg();
	    	});
	    	
			// 手动清除软件必须上传提示
			$("#btn_file").change(function(){
	    		var msgBtnFile = "请上传软件";
	    	    var $labelError = $("#btn_file").parent().children("span.help-inline").eq(0).children("label.error");
	    	    if ($labelError.size() != 0) {
	    	        $.each($labelError,function(index,value,array){
	    	       		if ($(value).text() == msgBtnFile) {
	    	            	$(value).remove();
	    	       		}
	    	        });
	    	    }
	    	});
		});
		
		// 手动清除产品线必填提示
		function cleanProductLineMsg() {
	    	var msgProductLine = "请输入产品线名称";
		    var $labelError = $("#productLine").parent().children("label.error");
		    if ($labelError.size() != 0) {
		        $.each($labelError,function(index,value,array){
// 		            alert($(value).text() == msgProductLine );
// 		            alert(($("#productLine").val() != undefined && $("#productLine").val() != ""));
// 		            alert(($("#systemType").attr("disabled") == undefined && $("#systemType").val() != undefined && $("#systemType").val() != ""));
// 		            alert($("#systemType").attr("disabled") != undefined);
// 		            alert(($("#productLine").val() != undefined && $("#productLine").val() != "")
// 		     		               && (($("#systemType").attr("disabled") == undefined && $("#systemType").val() != undefined && $("#systemType").val() != "") 
// 		     		                       || $("#systemType").attr("disabled") != undefined));
		       		if ($(value).text() == msgProductLine 
		               && ($("#productLine").val() != undefined && $("#productLine").val() != "")
		               && (($("#systemType").attr("disabled") == undefined && $("#systemType").val() != undefined && $("#systemType").val() != "") 
		                       || $("#systemType").attr("disabled") != undefined)) {
		            	$(value).remove();
		       		}
		        });
		    }
		}
		
		// 判定产品类型下拉框是否可用
		function checkProductLine() {
			var selectedLabel = $("#productLine").find("option:selected").html();
			if ((selectedLabel) && (selectedLabel.lastIndexOf("APP") + 3) != selectedLabel.length) {
				 $("#systemType").prop("disabled",true);
				 $("#systemType").prev().children().eq(0).children().eq(0).html("");
			} else {
				$("#systemType").removeAttr("disabled");
			}
		}
		
		function synchroEditionSoft(elem){
			setSoftwarePreview();
			$("#selectedFile").val($(elem).val());
		}
		
		function checkEdition(){
			var checkFlag;
			checkFlag = $.ajax({
				url : "${ctx}/edition/editionManage/checkEdition",
				async : false,
				dataType : "json",
				data : {
					"id" : $("#id").val(),
					"productLine" : $("#productLine").val(),
					"systemType" : $("#systemType").val(),
					"editionName" : $("#editionName").val(),
					"editionNo" : $("#editionNo").val()
				},
				success : function(result){
					if (result == false) {
						alertx("该软件版本已存在，请勿重复添加");
						fail_function();
						return;
					}
					success_function();
					return;
			    }
			});
		}
		
		function success_function(){
			checkFlag = true;
		}
		
		function fail_function(){
			checkFlag = false;
		}
	</script>
	
	<style type="text/css">
	   label.undisplay{
	   		display:none;
	   }
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
<%-- 		<li><a href="${ctx}/edition/editionManage/">版本列表</a></li> --%>
		<li class="active"><a href="${ctx}/edition/editionManage/form?id=${editionManage.id}"><shiro:hasPermission name="edition:editionManage:edit">${not empty editionManage.id?'修改':'上传'}</shiro:hasPermission><shiro:lacksPermission name="edition:editionManage:edit">查看</shiro:lacksPermission>版本</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="editionManage" action="${ctx}/edition/editionManage/save" method="post" enctype="multipart/form-data" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">产品线：</label>
			<div class="controls">
				<form:select path="productLine" class="input-xlarge product-line">
					<form:option value="" label="" />
					<form:options items="${fns:getDictList('product_line')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<form:select path="systemType" class="input-medium product-line">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('prod_sys_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">版本名称：</label>
			<div class="controls">
				<form:input path="editionName" htmlEscape="false" maxlength="32" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">版本号：</label>
			<div class="controls">
				<form:input path="editionNo" htmlEscape="false" maxlength="16" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">版本说明：</label>
			<div class="controls">
				<form:textarea path="editionInstruction" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">上传软件：</label>
			<div class="control-group" id="localFile">
			<div class="controls">
				<input type="file" name="file" id="btn_file" onchange="synchroEditionSoft(this)">
				<input type="hidden" id="selectedFile" name="selectedFile" value="${editionManage.fileUrl}" class="required">
				<span class="help-inline">
					<font color="red">*      </font>
					<font color="black">(</font>
					<font color="red">格式APK</font>
					<font color="black">)</font>
				</span>
				<br/>
				<c:choose>
					 <c:when test="${not empty editionManage.fileUrl}">
					 	<label id="filePreview" onclick="openBrowse();" >已上传的文件：${fileName}</label>
					 </c:when>
					 <c:otherwise>
					 	<label id="filePreview"  style="display:none"></label>
					 </c:otherwise>
				</c:choose>
				<br/>
				<c:choose>
					 <c:when test="${not empty editionManage.fileUrl}">
					 	<label id="fileSize" onclick="openBrowse();" >软件大小：${fileSize}M</label>
					 </c:when>
					 <c:otherwise>
					 	<label id="fileSize"  style="display:none"></label>
					 </c:otherwise>
				</c:choose>
              </div>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="edition:editionManage:edit"><input id="btnSubmit" class="btn btn-success" type="submit" value="保 存" />&nbsp;</shiro:hasPermission>
            <a id="btnCancel" href="${ctx}/edition/editionManage/" class="btn btn-success"> 返 回</a>
		</div>
	</form:form>
</body>
</html>