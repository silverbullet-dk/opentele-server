package pages

import geb.Page

class LoginPage extends Page {
    static url = "login/auth"
    static at = { title == "Log ind til KIH Datamonitorering" }

    static content = {
        loginForm { $("form") }
        loginButton { $("input", value: "Log ind") }
    }
}
