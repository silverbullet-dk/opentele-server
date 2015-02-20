<r:script>
    $.post('${createLink(controller: "meta", action: "registerCurrentPage")}', {currentController: '${controllerName}', currentAction: '${actionName}'});
</r:script>
