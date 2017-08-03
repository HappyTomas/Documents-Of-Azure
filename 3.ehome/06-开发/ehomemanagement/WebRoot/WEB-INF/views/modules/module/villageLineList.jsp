<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>楼盘信息产品线管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
    $(document).ready(function() {
        fillPro();
    });
    function page(n, s) {
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }
    function elemSetMondule() {
        if (!$("#selectElemId").val()) {
            alertx("请选择要设置的行");
            return false;
        } else {
            var elemId = $("#selectElemId").val();
            $("#elemEditId").attr("href", $("#elemEditId").attr("href") + elemId);
            return true;
        }
    }
</script>
</head>
<body>
    <ul class="nav nav-tabs">
        <li>
            <span><a href="${ctx}/module/villageLine/">产品管理 </a>> <a href="${ctx}/module/villageLine/">设置管理</a></span>
        </li>
    </ul>
    <form:form id="searchForm" modelAttribute="villageLine" action="${ctx}/module/villageLine/" method="post" class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
        <input type="text" class="hide" id="hidProId" value="${villageLine.villageInfo.addrPro }">
        <input type="text" class="hide" id="hidCityId" value="${villageLine.villageInfo.addrCity }">
        <input type="text" class="hide" id="hidVillageId" value="${villageLine.villageInfoId }">
        <ul class="ul-form">
            <li class="btns">
                <form:select path="villageInfo.propertyCompanyId" class="input-small">
                    <form:option value="" label="全部物业" />
                    <form:options items="${propertyCompanyMap}" htmlEscape="false" />
                </form:select>
            </li> 
            <li class="btns">
                <select id="addrpro" name="villageInfo.addrPro" style="width: 120px" onchange="changeCity()">
                    <option value="">全部省份</option>
                </select>
            </li>
            <li class="btns">
                <select id="addrcity" name="villageInfo.addrCity" style="width: 120px" onchange="changeVillage()">
                    <option value="">全部城市</option>
                </select>
            </li>
            <li class="btns">
                <select id="addrVillage" name="villageInfoId" style="width: 120px">
                    <option value="">全部楼盘</option>
                </select>
            </li>
            <li class="btns">
                <form:select path="setState" class="input-medium">
                    <form:option value="" label="设置状态" />
                    <form:options items="${fns:getDictList('setState')}" itemLabel="label" itemValue="value" htmlEscape="false" />
                </form:select>
            </li>

            <li class="btns">
                <form:select path="productLine" class="input-medium">
                    <form:option value="" label="产品线" />
                    <form:options items="${fns:getDictList('product_line')}" itemLabel="label" itemValue="value" htmlEscape="false" />
                </form:select>
            </li>
            <li class="btns">
                 <form:input path="villageInfo.villageName" placeholder="小区名字" htmlEscape="false" maxlength="64" class="input-small" />
            </li>
            <li class="btns">
                <input id="btnSubmit" class="btn btn-success" type="submit" value="查询" />
            </li>
            <li class="clearfix"></li>
        </ul>
    </form:form>
    <ul style="margin: 10px;">
        <li>
            <shiro:hasPermission name="module:villageLine:setModule">
                <a id="elemEditId" href="${ctx}/module/villageLine/form?id=" onclick="return elemSetMondule()" class="btn btn-primary">设置模块</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="module:villageLine:batchSetModule">
                <a href="${ctx}/module/villageLine/batchFrom" class="btn btn-primary" >批量设置模块</a>
            </shiro:hasPermission>
        </li>
    </ul>
    <input id="selectElemId" type="hidden" value="" />
    <sys:message content="${message}" />
    <table id="contentTable" class="table table-bordered table-condensed">
        <thead>
            <tr>
                <th>序号</th>
                <th>物业公司</th>
                <th>楼盘名称</th>
                <th>产品线</th>
                <th>设置时间</th>
                <th>设置状态</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${page.list}" var="villageLine" varStatus="status">
                <tr onClick="selectElem(this)">
                    <td>${status.count}<input id="elemId" type="hidden" value="${villageLine.id}" />
                    </td>
                    <td>${villageLine.villageInfo.propertyCompanyName }</td>
                    <td>${villageLine.villageInfo.villageName}</td>
                    <td>${fns:getDictLabel(villageLine.productLine, 'product_line', '')}</td>
                    <td><fmt:formatDate value="${villageLine.setTime}" pattern="yyyy-MM-dd" /></td>
                    <td>${fns:getDictLabel(villageLine.setState, 'set_state', '')}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <div class="pagination">${page}</div>
</body>
</html>