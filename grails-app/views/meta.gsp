<%--
  Created by IntelliJ IDEA.
  User: msurrow
  Date: 1/28/13
  Time: 2:07 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>OpenTele isAlive</title>
</head>
<body>
    <table>
    <g:each in="${info}" var='row'>
        <tr>
            <td style="width: 160px;">${row.key}</td>
            <td>${row.value}</td>
        </tr>
    </g:each>
    </table>
</body>
</html>