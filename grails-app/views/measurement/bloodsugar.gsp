<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'bloodsugartable.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'font-awesome.min.css')}" type="text/css">
    <g:javascript src="jquery.js" />
    <style>
        body {
            width: 100%;
            margin-left: 0px;
            margin-right: 0px;
        }
    </style>
    <script type="text/javascript">
        $(function() {
            window.print();
        });
    </script>
</head>

<body>
<div class="content">
    <g:render template="bloodSugar" model="bloodSugarData"/>
</div>
</body>
</html>