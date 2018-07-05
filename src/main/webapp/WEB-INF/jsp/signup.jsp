<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.w3.org/1999/xhtml">
<head>
	<title>ATBANK</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

	<link rel="stylesheet" type="text/css"
				href="webjars/bootstrap/3.3.6/css/bootstrap.min.css" />

	<link rel="stylesheet" th:href="@{/css/main.css}"
				href="../../css/main.css" />

</head>
<body>

<nav class="navbar navbar-inverse">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">ATBANK</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
		</div>
	</div>
</nav>

<div class="container">
	<div class="row">
		<div class="col-md-6 col-md-offset-3">
			<h1>Please sign up </h1>
			<div class="form-group">
				<p id="errorMsg" style="color: red"></p>
				<label for="username">Username</label>:
				<input type="text"
							 id="username"
							 name="username"
							 class="form-control input-sm "
							 width="30px"
							 autofocus="autofocus"
							 placeholder="Username">
			</div>
			<div class="form-group">
				<label for="password">Password</label>:
				<input type="password"
							 id="password"
							 name="password"
							 class="form-control input-sm "
							 width="30px"
							 placeholder="Password">
			</div>
			<div class="form-group">
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3">
						<input type="submit"
									 name="signup-submit"
									 id="signup-submit"
									 class="form-control btn btn-info"
									 value="Sign Up">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>

<script src="webjars/jquery/1.9.1/jquery.min.js"></script>
<script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link type="text/css" rel="stylesheet" th:href="@{https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css}" />
<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript">

  $(document).ready(function() {
    $('errorMsg').hide()
  });

  $('#signup-submit').click(function() {
    var username = $("input#username").val();
    var password = $("input#password").val()
		$.ajax({
		 traditional: true,
		 url: "http://localhost:8080/api/users/",
		 dataType: "json",
		 type: "POST",
			data: "{\"username\":\"" + username+ "\", \"password\":\"" + password+ "\"}",
		 contentType: "application/json; charset=utf-8",
		 success: function (data) {
		 var dat = data;
		 if (dat.hasOwnProperty("code")) {
		 $('p#errorMsg').show();
		 $('p#errorMsg').text(dat.message);
		 } else {

       $.ajax({
         traditional: true,
         url: "http://localhost:8080/api/accounts/",
         dataType: "json",
         type: "POST",
         data: "{\"userId\":\"" + dat.id+ "\", \"amount\":\"" + 100+ "\"}",
         contentType: "application/json; charset=utf-8",
         success: function (data) {
           var dat = data;
           if (dat.hasOwnProperty("code")) {
             $('p#errorMsg').show();
             $('p#errorMsg').text(dat.message);
           } else {
             window.location.href = "/login";
           }
         },
         error: function (data) {
           console.log("failure"+ data);
         }
       });
		 }
		 },
		 error: function (data) {
		 console.log("failure"+ data);
		 }
		 });
  });

</script>
</body>
</html>
