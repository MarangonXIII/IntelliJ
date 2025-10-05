package teste.com.meuprimeiroprojetocomspring

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.sql.ResultSet
import java.util.*

@RestController
class MeuPrimeiroController(
    private val namedParameterJdbcOperations: NamedParameterJdbcOperations
) {

    @GetMapping("/ping")
    fun ping(): String {
        return "PONG";
    }

    @GetMapping("/produtos")
    fun produtos(): List<Produto> {
        /*val produtos = listOf(
            Produto(
                id = UUID.randomUUID(),
                nome = "Demon Souls",
                descricao = "The Father"
            ),
            Produto(
                id = UUID.randomUUID(),
                nome = "Dark Souls 1",
                descricao = "GOAT"
            ),
            Produto(
                id = UUID.randomUUID(),
                nome = "Dark Souls 2",
                descricao = "WOAT"
            ),
            Produto(
                id = UUID.randomUUID(),
                nome = "Dark Souls 3",
                descricao = "Greatest"
            ),
            Produto(
                id = UUID.randomUUID(),
                nome = "Bloodborne",
                descricao = "Greatest of PS4"
            ),
            Produto(
                id = UUID.randomUUID(),
                nome = "Sekiro",
                descricao = "GOTY"
            ),
            Produto(
                id = UUID.randomUUID(),
                nome = "Elden Ring",
                descricao = "GOTY 2"
            )
        )*/

        val produtos = namedParameterJdbcOperations.query("select id, nome, descricao from produto", { rs, rows ->
            Produto(
                id = UUID.fromString(rs.getString("id")),
                nome = rs.getString("nome"),
                descricao = rs.getString("descricao")
            )
        })

        return produtos
    }
}