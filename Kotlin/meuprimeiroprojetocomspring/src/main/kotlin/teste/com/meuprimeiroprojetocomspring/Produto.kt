package teste.com.meuprimeiroprojetocomspring

import java.util.UUID


data class Produto(
    val id: UUID,
    val nome: String,
    val descricao: String,
)