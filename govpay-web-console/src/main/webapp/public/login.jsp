<!DOCTYPE html>
<%@page import="it.govpay.web.utils.Utils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>${it.govpay.console.appTitle} v${project.version}</title>
    <!-- Bootstrap core CSS -->
    <link href="styles/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="styles/login.css" rel="stylesheet">

<%
String msg = request.getParameter("msg"); 
String msgString = null;
String msgType = null;
    		
if(StringUtils.isNotEmpty(msg)){
	if(msg.equals("lo")){ // logout Ok
		msgString =  "govpay.logoutOk";
		msgType= "alert alert-info";
	}else if(msg.equals("le")){ // credenziali sconosciute nell'application server
		msgString = "govpay.credenzialiNonValide";
		msgType= "alert alert-danger";
	}else if(msg.equals("se")) { // sessione scaduta
		msgString = "govpay.sessioneScaduta";
		msgType= "alert alert-warning";
	}else { // utente sconosciuto in govpay
		msgString = "govpay.utenteNonAutorizzato";
		msgType= "alert alert-danger";
	}
}
    		
%>

</head>
<body>

<div class="container">

    <form class="form-signin" action="j_security_check" method=post>
    	<div class="form-signin-heading text-center">
	    	<h2 class="disply-inline">${it.govpay.console.appTitle} </h2>
	    	<h4 class="disply-inline">v${project.version}</h4>
    	</div>
        
        <% if(StringUtils.isNotEmpty(msg)){ %>
        	<div class="<%=msgType %>" role="alert">
        		<p><%=Utils.getInstance().getMessageFromResourceBundle(msgString) %></p>
        	</div>
        <% } %>
        <label for="inputEmail" class="sr-only">Username</label>
        <input type="text" id="inputEmail" class="form-control" placeholder="Username" required autofocus
               name="j_username">
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="Password" required
               name="j_password">
        <p></p>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
    </form>

</div>

</body>
</html>
