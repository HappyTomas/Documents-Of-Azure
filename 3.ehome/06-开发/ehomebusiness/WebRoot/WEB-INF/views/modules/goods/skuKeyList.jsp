<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商品规格名称管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
    $(document).ready(function() {
        jQuery.validator.addMethod("skuKeyName", function(value, element,params) {
            var patrn=/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
            return this.optional(element) || (patrn.test(value));
        }, "请输入中文，英文或数字");
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
                    error.insertAfter(element);
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
            if(idx > 19) {
                alertx("最多可以增加20条规格");
                return;
            }
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
        }
        function delRow(obj, prefix){
            var id = $(prefix+"_id");
            var delFlag = $(prefix+"_delFlag");
            if (id.val() == ""){
                $(obj).parent().parent().remove();
            }else if(delFlag.val() == "0"){
                delFlag.val("1");
                $(obj).html("&divide;").attr("title", "撤销删除");
                $(obj).parent().parent().addClass("error");
            }else if(delFlag.val() == "1"){
                delFlag.val("0");
                $(obj).html("&times;").attr("title", "删除");
                $(obj).parent().parent().removeClass("error");
            }
        }
</script>
</head>
<body>
	<sys:message content="${message}"/>
	<div id="left" style="float:left ;  width:40%;  height:100%;">
            <div>
                <p><span>分类列表</span></p>
            </div>
        <div >
	        <table id="contentTable" class="table table-striped table-bordered table-condensed">
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
    				        <a href="${ctx}/goods/skuKey/form?id=${skuKey.id}">修改</a>
					        <a href="${ctx}/goods/skuKey/delete?id=${skuKey.id}" onclick="return confirmx('确认要删除该商品规格名称吗？', this.href)">删除</a>
				        </td></shiro:hasPermission>
			        </tr>
		        </c:forEach>
		        </tbody>
	        </table>
	    </div>
	</div>
	        <div id="center" style="float:left ;  width:5%;  height:100%;">
            <p><span> </span></p>
        </div>
        <div id="right" style="float:left ;  width:55%; height:100%;">
            <div>
                <p><span>新增/编辑分类</span></p>
            </div>
            <div>
                    <form:form id="inputForm" modelAttribute="skuKey" action="${ctx}/goods/skuKey/save" method="post" class="form-horizontal">
        <form:hidden path="id"/>
        <div class="control-group">
            <label class="control-label"  style="width:90px">商品规格</label>
            <div class="controls"  style="margin-left:100px">
                <form:input path="name" htmlEscape="false" maxlength="12" placeholder="请输入规格名称" class="input-xlarge"/>
                <span class="help-inline"><font color="red">*</font></span><br/>
                <span>分类名称可以是中文、英文或数字，不超过12个字符。</span>
            </div>
        </div>
            <div class="control-group">
                <label class="control-label"  style="width:90px">新增/编辑规格</label>
                <div class="controls"  style="margin-left:80px">
                    <table id="contentTable" class="table table-striped table-bordered table-condensed"  style="width:388px">
                        <thead>
                            <tr>
                                <th class="hide"></th>
                                <th style="width:90px">规格列表值名称</th>
                                <shiro:hasPermission name="goods:skuKey:edit"><th style="width:10px">&nbsp;</th></shiro:hasPermission>
                            </tr>
                        </thead>
                        <tbody id="skuValueList">
                        </tbody>
                        <shiro:hasPermission name="goods:skuKey:edit"><tfoot>
                            <tr><td colspan="3"><a href="javascript:" onclick="addRow('#skuValueList', skuValueRowIdx, skuValueTpl);skuValueRowIdx = skuValueRowIdx + 1;" class="btn">+</a></td></tr>
                            <tr><td colspan="3"><span>可输入中文、英文或数字，不超过6个字符。</span></td></tr>
                        </tfoot></shiro:hasPermission>
                    </table>
                    <script type="text/template" id="skuValueTpl">//<!--
                        <tr id="skuValueList{{idx}}">
                            <td class="hide">
                                <input id="skuValueList{{idx}}_id" name="skuValueList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
                                <input id="skuValueList{{idx}}_delFlag" name="skuValueList[{{idx}}].delFlag" type="hidden" value="0"/>
                                <input id="skuValueList{{idx}}_sortOrder" name="skuValueList[{{idx}}].sortOrder" type="hidden" value="{{idx}}"/>
                            </td>
                            <td>
                                <input id="skuValueList{{idx}}_name" name="skuValueList[{{idx}}].name" type="text" value="{{row.name}}" placeholder="请输入规格" maxlength="6" class="input-small required skuValueName"/>
                                <span class="help-inline"><font color="red">*</font> </span>
                            </td>
                            <shiro:hasPermission name="goods:skuKey:edit"><td class="text-center" style="width:10px">
                                {{#delBtn}}<span style="font-size: 20px;font-weight: bold;line-height: 20px;color: #A9A9A9;text-shadow: 0 1px 0 #ffffff;" onclick="delRow(this, '#skuValueList{{idx}}')" title="删除">&minus;</span>{{/delBtn}}
                            </td></shiro:hasPermission>
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
                        });
                    </script>
                </div>
            </div>
        <div class="form-actions">
            <shiro:hasPermission name="goods:skuKey:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保存规格"/>&nbsp;</shiro:hasPermission>
        </div>
    </form:form>
                </div>
        </div>
</body>
</html>