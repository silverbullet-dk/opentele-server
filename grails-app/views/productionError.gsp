<!doctype html>
<html>
<head>
<title>Grails Runtime Exception</title>
<meta name="layout" content="main">
<link rel="stylesheet"
	href="${resource(dir: 'css', file: 'errors.css')}" type="text/css">
<style>
    #leftmenu {
        visibility: hidden;
    }
    h2 {
        margin-left: 0px;
    }
</style>
</head>
<body>
    <h2>Uventet fejl - kl. ${new java.util.Date().format("HH:mm dd/MM/yyyy")}</h2>
    <p>En uventet fejl er sket.</p>
    <p>Fejlmeld dette. Registrer venligst hvilke handlinger du foretog dig for at nå hertil.
    Registrer venligst den benyttede browser. Vedhæft gerne skærmbillede.</p>
</body>
</html>