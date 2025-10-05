package br.com.catalogo_produtos.domain.produto;

import br.com.catalogo_produtos.domain.produto.ProdutoStatus
import br.com.catalogo_produtos.domain.produto.imagem.Imagem
import java.util.UUID;

data class Produto (
    val id: UUID = UUID.randomUUID(),
    val sku: String,
    val nome: String,
    val descricao: String,
    val unidade: String,
    val peso: Double,
    val estoque: Double,
    val status: ProdutoStatus,
    val imagens: Set<Imagem>
)