<html>
<head>
<title>Welcome</title>
<link href="webjars/bootstrap/3.3.6/css/bootstrap.min.css"
rel="stylesheet">
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
  <br/>
  <p id="welcome" style="color: blue"></p>
</div>
<div class="container">
  <table id="accountTable" class="table table-row-border" style="width:100%">
    <caption>Account Details </caption>
    <thead>
    <tr>
    <th>ID</th>
    <th>Account Type</th>
    <th>Account Status</th>
    <th>Balance</th>
    </tr>
    </thead>
  </table>
</div>
   <div class="container">
    <table id="detailTable" class="table table-striped" style="width:100%">
    <caption>Your Last 10 transactions </caption>
    <thead>
        <th>ID</th>
        <th>Account ID</th>
        <th>Transaction ON</th>
        <th>Body</th>
        <th>Transaction Type</th>
        </tr>
    </thead>
    </table>
    <div>
<a id="backButton" class="btn btn-default" href="/">Back</a>
</div>
<script src="webjars/jquery/1.9.1/jquery.min.js"></script>
    <script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link type="text/css" rel="stylesheet" th:href="@{https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css}" />
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>
    <script type="text/javascript">

      $(document).ready(function(){
        $.ajax({
          url: "http://localhost:8080/api/transactions/accounts/"+${accountId},
          type: "GET",
          dataType: "json"
        }).done(function(data){
          console.log(data);
          $(document).ready(function() {
            $('#detailTable').dataTable( {
              searching: false,
              paging: false,
              data: data,
              "columns": [
                {"data": "id"},
                {"data": "accountId"},
                { "data": "transactionOn" },
                { "data": "body" },
                { "data": "transactionType"}
              ]
            });
          });
        });

        $.ajax({
          url: "http://localhost:8080/api/accounts/"+${accountId},
          type: "GET",
          dataType: "json"
        }).done(function(data){
          var wel = "Welcome "+ data[0].user.username + "!!!"
          $('p#welcome').text(wel);

          $(document).ready(function() {
            $('#accountTable').dataTable( {
              searching: false,
              paging: false,
              bInfo: false,
              data: data,
              "columns": [
                {"data": "id"},
                { "data": "accountType"},
                { "data": "accountStatus" },
                { "data": "balance" }
              ]
            });
          });
        });

      });
</script>
</div>
</body>
</html>
