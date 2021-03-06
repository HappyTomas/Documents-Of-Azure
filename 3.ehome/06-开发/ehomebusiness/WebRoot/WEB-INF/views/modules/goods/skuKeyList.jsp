<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商品规格名称管理</title>
	<meta name="decorator" content="default"/>
    <style>
    .table tbody tr.trerror>td {
        background-color: #f2dede !important;
    }
    .commonbtn1 {
        font-family: MicrosoftYaHei;
        font-size: 14px;
        color: #60C1F7;
        background: #FFFFFF;
        border: 1px solid #60C1F7;
        border-radius: 6px;
        height:30px;
    }
    .commonbtn1:hover {
        font-family: MicrosoftYaHei;
        font-size: 14px;
        color: #60C1F7;
        background: #F1FAFF;
        border: 1px solid #60C1F7;
        border-radius: 6px;
        height:30px;
    }
    </style>
	<script type="text/javascript">
    $(document).ready(function() {
    	// 特殊字符检测
        $.validator.addMethod("skuKeyName", function(value) {
            var patrn=/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
            return patrn.test(value);
        }, "请输入中文，英文或数字");
        // 规格项必输CHECK
        $.validator.addMethod("skuKeyNameRequired", function(value) {
            if (value == "") {
            	return false;
            } else {
            	return true;
            }
        }, "请输入规格");
        // 名称重复检测
        $.validator.addMethod("skuvaluenamecheck", function(value) {
        	var flg = true;
        	if (value == "") {
        		return flg;
        	}
        	temp_value = new Array();
            $(".skuvaluenamecheck").each(function(index){
            	if (this.value != "") {
                    if (temp_value.indexOf(this.value) != -1 && (this.value == value)) {
                        flg = false;
                    } else {
                        temp_value.push(this.value);
                    }
            	}
            });
            return flg;
        }, "该规格项名称与其他规格项名称重复请修改");
        //$("#name").focus();
        $("#inputForm").validate({
        	rules: {
        		name: {
        			required: true,
        			maxlength: 12,
        			skuKeyName: true
        		}
        	},
        	messages: {
                name: {
                    required: "请输入规格名称",
                    maxlength: "规格名称不能超过12个字"
                }
        	},
            submitHandler: function(form){
                var flg = true;
                top.$.jBox.tip.mess = null;
                $(".skuValueName").each(function(){
                    var patrn=/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
                    if (!patrn.test(this.value)) {
                        alertx("规格项请输入中文，英文或数字");
                        flg = false;
                    }
                });
                if (!flg) {
                	return;
                } else {
                    loading('正在提交，请稍等...');
                    form.submit();
                }

            },
            errorContainer: "#messageBox",
            errorPlacement: function(error, element) {
                $("#messageBox").text("输入有误，请先更正。");
                if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                    error.appendTo(element.parent().parent());
                } else {
                	if ($("#msg_"+element[0].id)[0] != undefined) {
                		error.insertAfter($("#msg_"+element[0].id)[0]);
                	} else {
                		error.insertAfter(element);
                	}
                }
            }
        });
    });
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
       	return false;
    }
        function addRow(list, idx, tpl, row){
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list+idx).find("select").each(function(){
                $(this).val($(this).attr("data-value"));
            });
            $(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
                var ss = $(this).attr("data-value").split(',');
                for (var i=0; i<ss.length; i++){
                    if($(this).val() == ss[i]){
                        $(this).attr("checked","checked");
                    }
                }
            });
            $("#skuValueList").find("tr").each(function(){
                $(this).find("input[name=btnAdd]").hide();
            });
            if ($("#skuValueList").find("tr").length != 20) {
                var html = $("#skuValueList").find("tr:last").find("td:last").html();
                html = html + '<input name="btnAdd" style="width:30px;" class="commonbtn1" type="button" value="+" onclick=';
                html = html + '"addRow(';
                html = html + "'#skuValueList', skuValueRowIdx, skuValueTpl);skuValueRowIdx = skuValueRowIdx + 1;";
                html = html + '"/>';
                $("#skuValueList").find("tr:last").find("td:last").html(html);
            }
            if ($("#skuValueList").find("tr").length == 1) {
                var html = $("#skuValueList").find("tr:first").find("td:last").find("input[name=btnDel]").hide();
            } else {
                var html = $("#skuValueList").find("tr:first").find("td:last").find("input[name=btnDel]").show();
            }
        }
        function delRow(obj, prefix){
            var id = $(prefix+"_id");
            var delFlag = $(prefix+"_delFlag");
            if (id.val() == ""){
                $(obj).parent().parent().remove();
            }else if(delFlag.val() == "0"){
                delFlag.val("1");
                $(obj).val("÷").attr("title", "撤销删除");
                $(obj).parent().parent().addClass("trerror");
            }else if(delFlag.val() == "1"){
                delFlag.val("0");
                $(obj).val("-").attr("title", "删除");
                $(obj).parent().parent().removeClass("trerror");
            }
            $("#skuValueList").find("tr").each(function(){
                $(this).find("input[name=btnAdd]").hide();
            });
            $("#skuValueList").find("tr").each(function(){
                $(this).find("input[name=btnAdd]").hide();
            });
            if ($("#skuValueList").find("tr").length != 20) {
                var html = $("#skuValueList").find("tr:last").find("td:last").html();
                html = html + '<input name="btnAdd" style="width:30px;" class="commonbtn1" type="button" value="+" onclick=';
                html = html + '"addRow(';
                html = html + "'#skuValueList', skuValueRowIdx, skuValueTpl);skuValueRowIdx = skuValueRowIdx + 1;";
                html = html + '"/>';
                $("#skuValueList").find("tr:last").find("td:last").html(html);
            }
            if ($("#skuValueList").find("tr").length == 1) {
            	var html = $("#skuValueList").find("tr:first").find("td:last").find("input[name=btnDel]").hide();
            } else {
            	var html = $("#skuValueList").find("tr:first").find("td:last").find("input[name=btnDel]").show();
            }
        }
</script>
</head>
<body>
	<sys:message content="${message}"/>
	<div>
        <p><span class="common-breadcrumb">商品管理&nbsp;>&nbsp;商品规格</span></p>
    </div>
	<div id="left" style="float:left;width:60%; overflow-x:auto;height:100%;">
            <div>
                <p><span>商品规格</span></p>
            </div>
        <div >
	        <table id="contentTable" class="table table-striped table-bordered table-condensed" style="width:97%;">
	            <!--<thead>
                    <tr>
                    <th>规格名称</th>
                    <c:set var="firstSkuKeyList" value="${page.list[0]}"/>
                    <c:set var="firstSkuValueList" value="${firstSkuKeyList.skuValueList}"/>
                    <c:set var="maxCols" value="${firstSkuValueList.size()}"/>
                    <th colspan="${maxCols}">规格项</th>
                    <shiro:hasPermission name="goods:skuKey:edit"><th>操作</th></shiro:hasPermission>
                </tr>
                </thead> -->
		        <tbody>
		        <c:forEach items="${page.list}" var="skuKey">
			        <tr>
				        <td>${skuKey.name}:</td>
				        <c:forEach items="${skuKey.skuValueList}" var="skuValue">
				        <td>${skuValue.name}</td>
				        </c:forEach>
				        <shiro:hasPermission name="goods:skuKey:edit"><td>
                            <input id="btuElemEdit" type="button" class="commonsmallbtn" value="编辑" style="width:40px;" onclick="top.$.jBox.tip.mess = null;window.location.href='${ctx}/goods/skuKey/form?id=${skuKey.id}'">
                            <div style="margin:0 auto;height:3px;"></div>
                            <input id="btuElemDelete" type="button" class="commonsmallbtn" value="删除" style="width:40px;" onclick="top.$.jBox.tip.mess = null;return confirmx('确认要删除该商品规格名称吗？', '${ctx}/goods/skuKey/delete?id=${skuKey.id}')">
				        </td></shiro:hasPermission>
			        </tr>
		        </c:forEach>
		        </tbody>
	        </table>
	    </div>
	</div>
	    <div id="center" style="float:left ;  width:2%;  height:100%;">
            <p><span> </span></p>
        </div>
        <div id="right" style="float:left ;  width:38%; height:100%;">
            <div>
                <p><span>新增/编辑规格</span></p>
            </div>
            <div>
                    <form:form id="inputForm" modelAttribute="skuKey" action="${ctx}/goods/skuKey/save" method="post" class="form-horizontal">
        <form:hidden path="id"/>
        <div class="control-group">
            <label class="control-label"  style="width:90px"><span class="help-inline"><font color="red">*</font></span>规格名称</label>
            <div class="controls"  style="margin-left:100px">
                <form:input path="name" htmlEscape="false" maxlength="12" placeholder="请输入规格名称" class="input-xlarge"/>
                <br/>
                <span>规格名称可以是中文、英文或数字，不超过12个字符。</span>
            </div>
        </div>
            <div class="control-group">
                <label class="control-label"  style="width:90px"><span class="help-inline"><font color="red">*</font></span>规格列表</label>
                <div class="controls"  style="margin-left:80px">
                    <table id="contentTable" class="table table-striped table-bordered table-condensed"  style="width:308px">
                        <tbody id="skuValueList">
                        </tbody>
                        <tfoot>
                            <tr><td colspan="3"><span>可输入中文、英文或数字，不超过6个字符。</span></td></tr>
                        </tfoot>
                    </table>
                    <script type="text/template" id="skuValueTpl">//<!--
                        <tr id="skuValueList{{idx}}">
                            <td class="hide">
                                <input id="skuValueList{{idx}}_id" name="skuValueList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
                                <input id="skuValueList{{idx}}_delFlag" name="skuValueList[{{idx}}].delFlag" type="hidden" value="0"/>
                                <input id="skuValueList{{idx}}_sortOrder" name="skuValueList[{{idx}}].sortOrder" type="hidden" value="{{idx}}"/>
                            </td>
                            <td>
                                <input id="skuValueList{{idx}}_name" name="skuValueList[{{idx}}].name" type="text" value="{{row.name}}" placeholder="请输入规格" maxlength="6" class="input-small skuKeyNameRequired skuKeyName skuvaluenamecheck"/>
                                <span id="msg_skuValueList{{idx}}_name"></span>
                            </td>
                            <td class="text-center" style="width:70px">
                                {{#delBtn}}<input name="btnDel" class="commonbtn1" style="width:30px;" value="&minus;" onclick="delRow(this, '#skuValueList{{idx}}')" type="button" title="删除">{{/delBtn}}
                            </td>
                        </tr>//-->
                    </script>
                    <script type="text/javascript">
                        var skuValueRowIdx = 0, skuValueTpl = $("#skuValueTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                        $(document).ready(function() {
                            var data = ${fns:toJson(skuKey.skuValueList)};
                            for (var i=0; i<data.length; i++){
                                addRow('#skuValueList', skuValueRowIdx, skuValueTpl, data[i]);
                                skuValueRowIdx = skuValueRowIdx + 1;
                            }
                            if (data.length == 0) {
                            	addRow('#skuValueList', skuValueRowIdx, skuValueTpl);
                                skuValueRowIdx = skuValueRowIdx + 1;
                            }
                            $("#skuValueList").find("tr").each(function(){
                                $(this).find("input[name=btnAdd]").hide();
                            });
                            if ($("#skuValueList").find("tr").length != 20) {
                                var html = $("#skuValueList").find("tr:last").find("td:last").html();
                                html = html + '<input name="btnAdd" style="width:30px;" class="commonbtn1" type="button" value="+" onclick=';
                                html = html + '"addRow(';
                                html = html + "'#skuValueList', skuValueRowIdx, skuValueTpl);skuValueRowIdx = skuValueRowIdx + 1;";
                                html = html + '"/>';
                                $("#skuValueList").find("tr:last").find("td:last").html(html);
                            }
                        });
                    </script>
                </div>
            </div>
        <div class="form-actions" style="background:#FFFFFF;border-top:0;">
            <input id="btnSubmit" class="commonbtn1" style="width:80px" type="submit" value="保存规格"/>&nbsp;
        </div>
    </form:form>
                </div>
        </div>
</body>
</html>