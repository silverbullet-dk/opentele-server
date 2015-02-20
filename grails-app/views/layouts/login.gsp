<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title><g:layoutTitle default="Grails" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}">
%{--<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">--}%
%{--<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">--}%
<link rel="stylesheet" href="${resource(dir: 'css', file: 'login_style.css')}" type="text/css">
%{--<g:layoutHead />--}%
%{--<r:layoutResources />--}%
</head>
<body>

    <div class="content">
        <div id="logo" role="banner">
            <a href="${createLink(uri: '/')}"><img src="${resource(dir: 'images', file: 'logo.png')}" alt="OpenTele" /></a>
        </div>
        <div id="loginBox">
            <g:layoutBody />
        </div>
    </div>

    <div class="footer" role="contentinfo">
        <div class="version">
            Version ${grailsApplication.metadata['app.version'] } (${grails.util.Environment.getCurrent()?.getName()})
        </div>
    </div>

		%{--<div id="spinner" class="spinner" style="display: none;">--}%
			%{--<g:message code="spinner.alt" default="Loading&hellip;" />--}%
		%{--</div>--}%
		%{--<g:javascript library="application" />--}%
		%{--<r:layoutResources />--}%
</body>
</html>
