<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Iron Geek 2015</title>
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
	$(function(){
	});

	$( document ).ready(function() {
		$( "#uploadFile" ).click( "click", function() {
		  if ($('#csvData').val() != "") {
			  var formData = new FormData();
		      //formData.append('isOverview', 0);
			  formData.append('csvFile', $('#csvData')[0].files[0]);

			  $.ajax({
	       		  type: "POST",
	       		  url: "/CloudComputingWeb/report",
	       		  enctype:"multipart/form-data",
	       		  data: formData,
	       		  cache: false,
	    	      contentType: false,
	    	      processData: false,
	              //dataType: "json",
	         	  success: function (data) {
	         		 alert("Go to Report page");
	         	  },
	  		      error: function(xhr, status, exception) {
	  		      }
		  	  });
		  }
	    });

		$( "#defaultData" ).click( "click", function() {
		      
			 $.ajax({
	       		  type: "POST",
	       		  url: "/CloudComputingWeb/data",
	       		  data: "filename=IronGeekCloudInputData.csv",
	              dataType: "json",
	         	  success: function (data) {
	         		 alert("Go to Report page");
	         	  },
	  		      error: function(xhr, status, exception) {
	  		      }
		  	  });
		});

		$( "#default10Data" ).click( "click", function() {
			 $.ajax({
	       		  type: "POST",
	       		  url: "/CloudComputingWeb/data",
	       		  data: "filename=out_10_IronGeekCloudInputData.csv",
	              dataType: "json",
	         	  success: function (data) {
	         		 alert("Go to Report page");
	         	  },
	  		      error: function(xhr, status, exception) {
	  		      }
		  	  });
		});

		$( "#default50Data" ).click( "click", function() {
			 $.ajax({
	       		  type: "POST",
	       		  url: "/CloudComputingWeb/data",
	       		  data: "filename=out_50_IronGeekCloudInputData.csv",
	              dataType: "json",
	         	  success: function (data) {
	         		 alert("Go to Report page");
	         	  },
	  		      error: function(xhr, status, exception) {
	  		      }
		  	  });
		});

		$( "#default100Data" ).click( "click", function() {
			 $.ajax({
	       		  type: "POST",
	       		  url: "/CloudComputingWeb/data",
	       		  data: "filename=out_100_IronGeekCloudInputData.csv",
	              dataType: "json",
	         	  success: function (data) {
	         		 alert("Go to Report page");
	         	  },
	  		      error: function(xhr, status, exception) {
	  		      }
		  	  });
		});

		$( "#default500Data" ).click( "click", function() {
			 $.ajax({
	       		  type: "POST",
	       		  url: "/CloudComputingWeb/data",
	       		  data: "filename=out_500_IronGeekCloudInputData.csv",
	              dataType: "json",
	         	  success: function (data) {
	         		 alert("Go to Report page");
	         	  },
	  		      error: function(xhr, status, exception) {
	  		      }
		  	  });
		});

		$( "#default1000Data" ).click( "click", function() {
			 $.ajax({
	       		  type: "POST",
	       		  url: "/CloudComputingWeb/data",
	       		  data: "filename=out_1000_IronGeekCloudInputData.csv",
	              dataType: "json",
	         	  success: function (data) {
	         		 alert("Go to Report page");
	         	  },
	  		      error: function(xhr, status, exception) {
	  		      }
		  	  });
		});
	});
	//gcloud compute copy-files ExplodeFile.class instance-1:/home/lpf66fpl --zone us-central1-f
</script>
</head>
<body>
<h2>Cloud Computing</h2>
<h4>Upload CSV Data File:</h4>
<input id="csvData" type="file" accept="application/csv" style="width: 300px;" />
<input id="uploadFile" type="button" value="Upload" />
<br /><br /><br />
<!--  <input id="refreshReport" type="button" value="Reports" />
<div id="reportFolder"></div>-->
<a href="/CloudComputingWeb/report" target="_blank">Report page</a>	
<br /><br /><br />
<a href="/CloudComputingWeb/data" target="_blank">Uploaded Data Files</a>
<br /><br /><br />
<input id="defaultData" type="button" value="Use IronGeekCloudInputData.csv" />
<br /><br />
<input id="default10Data" type="button" value="Use out_10_IronGeekCloudInputData.csv" />
<br /><br />
<input id="default50Data" type="button" value="Use out_50_IronGeekCloudInputData.csv" />
<br /><br />
<input id="default100Data" type="button" value="Use out_100_IronGeekCloudInputData.csv" />
<br /><br />
<input id="default500Data" type="button" value="Use out_500_IronGeekCloudInputData.csv" />
<br /><br />
<input id="default1000Data" type="button" value="Use out_1000_IronGeekCloudInputData.csv" />
<br /><br />
</body>
</html>