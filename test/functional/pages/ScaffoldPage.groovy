package pages

import geb.Page

class ScaffoldPage extends Page {
    static content = {
        heading { $("h1") }
        message { $("div.message").text() }
        logoutLink (to: LogoutPage) { $("a", text: "Log ud")}
    }
}
