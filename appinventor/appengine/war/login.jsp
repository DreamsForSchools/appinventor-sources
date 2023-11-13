<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page import="com.google.appinventor.server.util.UriBuilder"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<%
   String error = StringEscapeUtils.escapeHtml4(request.getParameter("error"));
   String useGoogleLabel = (String) request.getAttribute("useGoogleLabel");
   String locale = StringEscapeUtils.escapeHtml4(request.getParameter("locale"));
   String redirect = StringEscapeUtils.escapeHtml4(request.getParameter("redirect"));
   String repo = StringEscapeUtils.escapeHtml4((String) request.getAttribute("repo"));
   String autoload = StringEscapeUtils.escapeHtml4((String) request.getAttribute("autoload"));
   String galleryId = StringEscapeUtils.escapeHtml4((String) request.getAttribute("galleryId"));
   String newGalleryId = StringEscapeUtils.escapeHtml4(request.getParameter("ng"));
   if (locale == null) {
       locale = "en";
   }

%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache"/>
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"/>
    <meta HTTP-EQUIV="expires" CONTENT="0"/>
    <link href="https://fonts.googleapis.com/css?family=Rubik&amp;display=swap" rel="stylesheet">
    <title>DFS - AppMaker</title>
  </head>
<body>
  <div id="main">
    <img alt="AppMaker Logo" src="/static/images/appmaker.png" class="logo"/>
    <h3>Build your own apps with the Dreams for Schools App Maker Platform.<br/>
      It allows anyone to create simple, beautiful and powerful mobile apps.</h3>
    
    <!-- Sign In With Google -->
    <%    if (useGoogleLabel != null && useGoogleLabel.equals("true")) { %>
      <a href="<%= new UriBuilder("/login/google").add("locale", locale).add("repo", repo).add("galleryId", galleryId).add("redirect", redirect).build() %>" 
          style="text-decoration:none;">
      <img alt="Google SignIn" src="/static/images/btn_google_signin_dark_normal_web@2x.png" id="signInWithGoogle"/>
    </a></br>
    <%    } %>
    
    <footer class="footer">
      <span class="text-muted">Powered by 
        <a href="https://www.dreamsforschools.org/" target="_blank" class="footer-link">
          <img src="/static/images/d4s.jpeg" alt="d4s-icon" align="middle" class="icon"> Dreams for Schools
        </a>
      </span>
    </footer>
  </div>
</body>

<style>
body {
  font-family: Rubik, Roboto, Helvetica, Arial, sans-serif;
}

div#main {
  position: absolute;
  top:  45%;
  left: 50%;
  transform: translate(-50%,-50%);
  text-align: center;
}

h1 { margin-top: 0; font-size: 3em; }

.logo { width: 50%; }

#signInWithGoogle { width: 35%; padding: 1em 0; }

#signInWithGoogle:hover { filter: brightness(95%); }

.footer { color: #6c757d; padding-top: 2em;  }
.icon { width: 2em; height: 2em; padding: 0 0.5em 1em 0.5em; }

.footer-link {
  text-decoration: none !important;
  color: #6c757d;
}
</style>

</html>