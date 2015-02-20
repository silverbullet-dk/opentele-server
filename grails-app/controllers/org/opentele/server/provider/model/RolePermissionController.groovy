package org.opentele.server.provider.model


import org.opentele.server.model.RolePermission
import org.springframework.dao.DataIntegrityViolationException

class RolePermissionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [rolePermissionInstanceList: RolePermission.list(params), rolePermissionInstanceTotal: RolePermission.count()]
    }

    def create() {
        [rolePermissionInstance: new RolePermission(params)]
    }

    def save() {
        def rolePermissionInstance = new RolePermission(params)
        if (!rolePermissionInstance.save(flush: true)) {
            render(view: "create", model: [rolePermissionInstance: rolePermissionInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'rolePermission.label', default: 'RolePermission'), rolePermissionInstance.id])
        redirect(action: "show", id: rolePermissionInstance.id)
    }

    def show() {
        def rolePermissionInstance = RolePermission.get(params.id)
        if (!rolePermissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rolePermission.label', default: 'RolePermission'), params.id])
            redirect(action: "list")
            return
        }

        [rolePermissionInstance: rolePermissionInstance]
    }

    def edit() {
        def rolePermissionInstance = RolePermission.get(params.id)
        if (!rolePermissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rolePermission.label', default: 'RolePermission'), params.id])
            redirect(action: "list")
            return
        }

        [rolePermissionInstance: rolePermissionInstance]
    }

    def update() {
        def rolePermissionInstance = RolePermission.get(params.id)
        if (!rolePermissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rolePermission.label', default: 'RolePermission'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (rolePermissionInstance.version > version) {
                rolePermissionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'rolePermission.label', default: 'RolePermission')] as Object[],
                        "Another user has updated this RolePermission while you were editing")
                render(view: "edit", model: [rolePermissionInstance: rolePermissionInstance])
                return
            }
        }

        rolePermissionInstance.properties = params

        if (!rolePermissionInstance.save(flush: true)) {
            render(view: "edit", model: [rolePermissionInstance: rolePermissionInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'rolePermission.label', default: 'RolePermission'), rolePermissionInstance.id])
        redirect(action: "show", id: rolePermissionInstance.id)
    }

    def delete() {
        def rolePermissionInstance = RolePermission.get(params.id)
        if (!rolePermissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rolePermission.label', default: 'RolePermission'), params.id])
            redirect(action: "list")
            return
        }

        try {
            rolePermissionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'rolePermission.label', default: 'RolePermission'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'rolePermission.label', default: 'RolePermission'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
