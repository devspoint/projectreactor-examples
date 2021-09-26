package br.com.devspoint.projectreactorexamples

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

data class Pessoa(val nome: String)
data class Funcionario(val nome: String) {
    companion object {
        fun from(pessoa: Pessoa) = Funcionario(pessoa.nome)
    }
}

class PessoaRepository {
    companion object {
        fun findById(id: Long): Mono<Pessoa> = Mono.just(Pessoa("Felipe"))
        fun findAll(): Flux<Pessoa> = Flux.just(Pessoa("Felipe"), Pessoa("Diego"))
    }
}

class PessoaCacheRepository {
    companion object {
        fun findById(id: Long): Mono<Pessoa> = Mono.just(Pessoa("Felipe"))
        fun findAll(): Flux<Pessoa> = Flux.just(Pessoa("Felipe"), Pessoa("Diego"))
    }
}