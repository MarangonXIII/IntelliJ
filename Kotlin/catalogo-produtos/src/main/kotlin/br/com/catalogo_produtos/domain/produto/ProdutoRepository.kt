package br.com.catalogo_produtos.domain.produto

interface ProdutoRepository {
    fun findAll(): List<Produto>
}