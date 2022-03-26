package fiit.mtaa.frontend.data.model

import java.io.Serializable

data class User (private val login: String, private val password: String): Serializable{
    private val id: Long? = null
    private val user_role: String = "guest"

    fun verify (login: String, password: String): Boolean{
        return (this.login == login && this.password == password)
    }

    fun getLogin(): String {
        return this.login
    }

    fun getRole(): String {
        return this.user_role
    }

    fun getId(): Long? {
        return this.id
    }
}