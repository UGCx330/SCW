<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="">
	<meta name="keys" content="">
	<meta name="author" content="">
	<base
			href="http://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath }/" />
	<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="css/font-awesome.min.css">
	<link rel="stylesheet" href="css/login.css">
	<script src="jquery/jquery-2.1.1.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
	<style>
	</style>
	<title>众筹网</title>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<div>
				<a class="navbar-brand" href="http://123.56.30.35" style="font-size: 32px;">创意-众筹网</a>
			</div>
		</div>
	</div>
</nav>

<div class="container">

	<form action="security/do/login.html" method="post" class="form-signin" role="form">
		<h2 class="form-signin-heading">
			<i class="glyphicon glyphicon-log-in"></i> 管理员登录
		</h2>
		<p>${requestScope.exception.message }</p>
		<p>${SPRING_SECURITY_LAST_EXCEPTION.message }</p>
		<div class="form-group has-success has-feedback">
			<input type="text" name="loginAcct" value="" class="form-control" id="inputSuccess4"
				   placeholder="请输入登录账号" autofocus> <span
				class="glyphicon glyphicon-user form-control-feedback"></span>
		</div>
		<div class="form-group has-success has-feedback">
			<input type="text" name="userPswd" value="" class="form-control" id="inputSuccess4"
				   placeholder="请输入登录密码" style="margin-top: 10px;"> <span
				class="glyphicon glyphicon-lock form-control-feedback"></span>
		</div>
		<button type="submit" class="btn btn-lg btn-success btn-block">登录</button>
	</form>
	<a href="http://123.56.30.35" style="width: 150px" class="btn btn-lg btn-success btn-block">返回首页</a>
</div>
</body>
</html>