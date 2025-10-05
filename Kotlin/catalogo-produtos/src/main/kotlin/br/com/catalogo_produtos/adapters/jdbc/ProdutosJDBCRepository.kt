package br.com.catalogo_produtos.adapters.jdbc

import br.com.catalogo_produtos.domain.produto.Produto
import br.com.catalogo_produtos.domain.produto.ProdutoRepository

class ProdutosJDBCRepository: ProdutoRepository {
    override fun findAll(): List<Produto> {

    }
}