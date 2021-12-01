<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="commons/taglib-header.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8"/>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" scope="application"/>


    <meta name="viewport" content="width=device-width,user-scalable=no"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="${_csrf.headerName}" content="${_csrf.token}"/>
    <link rel="shortcut icon" href="${contextPath}/static/favicon.ico"/>

    <title>首页 | CAS-Client-Demo</title>

    <link href="${contextPath}/static/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>

</head>
<body>
<nav class="navbar navbar-default navbar-static-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="${contextPath}/"><img alt="Brand" style="max-height: 25px;"
                                                                src="${contextPath}/static/images/logo2.png"></a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <p class="navbar-text">CAS-Client</p>

            <form action="${contextPath}/signout" class="navbar-form navbar-right" role="search" method="post"
                  onsubmit="return confirm('确认全局退出?(IDP认证平台将同时退出, 当使用统一登录界面时有效)')">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="force" value="true"/>
                <button type="submit" class="btn btn-warning">全局退出</button>
            </form>
            <form action="${contextPath}/signout" class="navbar-form navbar-right" role="search" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-default">退出</button>
            </form>
            <p class="navbar-text pull-right"><a
                    href="">${SPRING_SECURITY_CONTEXT.authentication.principal.username}</a></p>
        </div>
    </div>
</nav>

<div class="container">

    <h3>CAS-Client-Demo is work!</h3>
    <hr/>
    <p>
        Username: ${username}
    </p>
    <p>
        ValidFromDate: ${validFromDate}
    </p>
    <p>
        AuthenticationDate: ${authenticationDate}
    </p>


    <div>
        <hr/>
        <p class="text-muted text-center">
            &copy; 2016-2021 IDsManager.com | Ver. ${currVersion}
        </p>
    </div>
</div>



</body>
</html>
