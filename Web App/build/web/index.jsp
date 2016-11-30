<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">
        <!--<link rel="icon" href="../../favicon.ico"> -->

        <title>Flexible Query Answering System</title>

        <!-- Bootstrap core CSS -->
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link href="css/ie10-viewport-bug-workaround.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="css/sticky-footer-navbar.css" rel="stylesheet">

        <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
        <!--[if lt IE 9]><script src="js/ie8-responsive-file-warning.js"></script><![endif]-->
        <script src="js/ie-emulation-modes-warning.js"></script>

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
        <link href="css/styles.css" rel="stylesheet">
    </head>

    <body>

        <!-- Fixed navbar -->
        <nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">Flexible Query Answering System</a>
                </div>
                <div id="navbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="#">Home</a></li>
                    </ul>
                </div><!--/.nav-collapse -->
            </div>
        </nav>

        <!-- Begin page content -->
        <div class="container-fluid">
            <div class="page-header">
                <h1>FAQS with Heart Disease Data Set</h1>
            </div>
            <div class="row">
                <form id="ajaxform" action="FAQS" role="form" method="post">
                    <div class="col-sm-6 col-sm-offset-3">
                        <div class="form-group">
                            <label for="comment">Failing Query</label>
                            <textarea name="query" class="form-control" rows="5" id="query"></textarea>
                        </div>
                    </div>
                    <div class="col-sm-6 col-sm-offset-3">
                        <button id="submitButton" type="submit" class="btn btn-default pull-right">Submit</button>
                    </div>
                </form>  
            </div>
            <div class="row">
                <p class="col-sm-6 col-sm-offset-3 text-muted">Sample Query: age >= 58 ^ sex >0  ^ cp > 2 ^ trestbps >120 ^ chol < 300</p>
            </div>
            <div id="relaxedQuery" class="row hide">
                <div class="col-sm-6 col-sm-offset-3">
                     <div class="form-group">
                            <label for="comment">Relaxed Query</label>
                            <p id="relaxedQueryOutput" class="lead"></p>
                        </div>
                </div>
            </div>
            <div id="result" class="row hide">
                <div class="col-sm-6 col-sm-offset-3">
                     <div class="form-group">
                            <label for="comment">Result rows</label>
                            <p id="resultOutput" class="lead"></p>
                        </div>
                </div>
            </div>
            <div id="rules" class="row hide">
                <div class="col-sm-6 col-sm-offset-3">
                     <div class="form-group">
                            <label for="comment">Rules</label>
                            <p id="rulesOutput" class="lead"></p>
                        </div>
                </div>
            </div>
            <div class="row">
                <h3><a href="http://archive.ics.uci.edu/ml/machine-learning-databases/heart-disease/heart-disease.names" target="_blank">Data Set Information:</a></h3>
                <p class="lead">
                    This database contains 76 attributes, but all published experiments refer to using a subset of 14 of them. 
                    In particular, the Cleveland database is the only one that has been used by ML researchers to 
                    this date. The "goal" field refers to the presence of heart disease in the patient. It is integer valued from 0 (no presence) to 4. 
                    Experiments with the Cleveland database have concentrated on simply attempting to distinguish presence (values 1,2,3,4) from absence (value 0). 
                    The names and social security numbers of the patients were recently removed from the database, replaced with dummy values. 
                    One file has been "processed", that one containing the Cleveland database. All four unprocessed files also exist in this directory. 
                    To see Test Costs (donated by Peter Turney), please see the folder "Costs"
                </p>
            </div>
            <div class="row">
                <h3>Attribute Information:</h3>
                <h4>Only 14 attributes used:</h4>
                <p class="lead">
                <ul>
                    <li>#3 (age):- Max:77 Min:29 </li>
                    <li>#4 (sex):- Max:1(male) Min:0(female) </li>
                    <li>#9 (cp):- Max:4 Min:1 </li>
                    <li>#10 (trestbps):- Max:200 Min:94 </li>
                    <li>#12 (chol):- Max:564 Min:126 </li>
                    <li>#16 (fbs):- Max:1 Min:0 </li>
                    <li>#19 (restecg):- Max:2 Min:0 </li>
                    <li>#32 (thalach):- Max:202 Min:71 </li>
                    <li>#38 (exang):- Max:1 Min:0 </li>
                    <li>#40 (oldpeak):- Max:6.2 Min:0 </li>
                    <li>#41 (slope):- Max:3 Min:1 </li>
                    <li>#44 (ca):- Max:3 Min:0 </li>
                    <li>#51 (thal):- Max:7 Min:3 </li>
                    <li>#58 (num) (the predicted attribute):- Max:4 Min:0 </li>
                </ul> 
                </p>
            </div>
        </div>

        <footer class="footer">
            <div class="container pull-right">
                <p class="text-muted pull-right" style="color:white">ITCS - 6155 Summer-II 2016</p>
            </div>
        </footer>


        <!-- Bootstrap core JavaScript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="js/ie10-viewport-bug-workaround.js"></script>
        <script src="js/scripts.js"></script>    
    </body>
</html>
