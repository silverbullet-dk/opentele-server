package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.Permission
import org.opentele.server.model.Role
import org.opentele.server.model.RolePermission
import org.opentele.server.model.UserRole
import org.opentele.server.core.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class RoleController {

    def roleService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    @Secured(PermissionName.ROLE_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }
    @Secured(PermissionName.ROLE_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [roleInstanceList: Role.list(params), roleInstanceTotal: Role.count()]
    }
    @Secured(PermissionName.ROLE_CREATE)
    def create(Long id) {
        def permissions = []
        if (id != null) {
            permissions = RolePermission.findAllByRole(Role.get(id))*.permission
        }
        [roleInstance: new Role(params), permissions: permissions]
    }

    @Secured(PermissionName.ROLE_CREATE)
    def save() {
        def roleInstance = new Role()
        if (!roleService.updateRole(roleInstance, params.authority, params.permissionIds)) {
            def permissionIds = params.list('permissionIds')
            def permissions = Permission.getAll(permissionIds)
            render(view: "create", model: [roleInstance: roleInstance, permissions: permissions])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'role.label')])
        redirect(action: "show", id: roleInstance.id)
    }
    @Secured(PermissionName.ROLE_READ)
    def show() {
        def roleInstance = Role.get(params.id)
        if (!roleInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'role.label')])
            redirect(action: "list")
            return
        }

        def permissions = RolePermission.findAllByRole(roleInstance)*.permission
        [roleInstance: roleInstance, permissions: permissions]
    }
    @Secured(PermissionName.ROLE_WRITE)
    def edit() {
        def roleInstance = Role.get(params.id)
        if (!roleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'role.label')])
            redirect(action: "list")
            return
        }

        def permissions = RolePermission.findAllByRole(roleInstance)*.permission

        [roleInstance: roleInstance, permissions: permissions]
    }
    @Secured(PermissionName.ROLE_WRITE)
    def update() {
        def roleInstance = Role.get(params.id)
        if (!roleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'role.label')])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (roleInstance.version > version) {
                roleInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'role.label')] as Object[])
                def permissionIds = params.list('permissionIds')
                def permissions = Permission.getAll(permissionIds)
                render(view: "edit", model: [roleInstance: roleInstance, permissions: permissions])
                return
            }
        }

        //Check that we do not try to change the name of the patient role
        if (roleInstance.authority.equals("Patient") && !params.authority.equals("Patient")) {
            roleInstance.errors.rejectValue("authority", "cannot.change.patient.role.authority")
            def permissionIds = params.list('permissionIds')
            def permissions = Permission.getAll(permissionIds)
            render(view: "edit", model:  [roleInstance: roleInstance, permissions: permissions])
            return
        }

        if (!roleService.updateRole(roleInstance, params.authority, params.permissionIds)) {
            def permissionIds = params.list('permissionIds')
            def permissions = Permission.getAll(permissionIds)
            render(view: "edit", model: [roleInstance: roleInstance, permissions: permissions])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'role.label')])
        redirect(action: "show", id: roleInstance.id)
    }

    @Secured(PermissionName.ROLE_DELETE)
    def delete() {
        def roleInstance = Role.get(params.id)
        if (!roleInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'role.label')])
            redirect(action: "list")
            return
        }

        //Check that we do not try to delete the  patient role
        if (roleInstance.authority.equals("Patient")) {
            if(session.lastAction.equals("edit")) {
                roleInstance.errors.rejectValue("authority", "cannot.delete.patient.role")
                render(view: "edit", model:  [roleInstance: roleInstance])
                return
            } else if (session.lastAction.equals("show")) {
                flash.message = message(code: 'cannot.delete.patient.role', args: [message(code: 'role.label')])
                redirect(action: "show", id: roleInstance.id)
                return
            } else { //assume list
                flash.message = message(code: 'cannot.delete.patient.role', args: [message(code: 'role.label')])
                redirect(action: "list")
                return
            }
        }

        UserRole.removeAll(roleInstance)
        RolePermission.removeAll(roleInstance)

        try {
            roleInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'role.label')])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'role.label')])
            redirect(action: "show", id: params.id)
        }
    }
}
