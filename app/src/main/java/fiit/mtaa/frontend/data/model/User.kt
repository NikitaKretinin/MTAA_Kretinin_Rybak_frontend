package fiit.mtaa.frontend.data.model

import java.io.Serializable

class User (private val login: String, private val password: String): Serializable{
    private val id: Long? = null
    private val userRole: String? = null
    fun printUserInfo() {
        println("User $id:\n login: $login,\n password: $password,\n role: $userRole\n")
    }
    fun verify (login: String, password: String): Boolean{
        return (this.login == login && this.password == password)
    }
    fun getLogin(): String {
        return this.login
    }
}