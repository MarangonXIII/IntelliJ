package br.com.catalogo_produtos.domain.produto.imagem

import java.util.UUID

data class Imagem (
    val id: UUID = UUID.randomUUID(),
    val url: String
)
