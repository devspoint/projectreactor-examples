package br.com.devspoint.projectreactorexamples

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

class MonoExamples {

    @Test
    fun `creating mono`() {
        val monoJust = Mono.just(System.currentTimeMillis())
        val monoEmpty = Mono.empty<String>()
        val monoJustOrEmpty = Mono.justOrEmpty(Optional.empty<String>())
        val monoDefer = Mono.defer { Mono.just(System.currentTimeMillis()) }

        println("Assinando eventos criado com o Just")
        monoJust.subscribe(System.out::println)
        Thread.sleep(500L)
        monoJust.subscribe(System.out::println)

        println("Assinando eventos criado com o Defer")
        monoDefer.subscribe(System.out::println)
        Thread.sleep(500L)
        monoDefer.subscribe(System.out::println)

        println("Assinando eventos criado com o Empty e JustOrEmpty")
        monoEmpty.subscribe { println("não passei aqui") }
        monoJustOrEmpty.subscribe { println("não passei aqui") }
    }

    @Test
    fun `doOnNext`() {
        Mono.just("Olá")
            .doOnNext { println("$it mundo!") }
            .doOnNext { println("$it leitor!") }
            .subscribe()
    }

    @Test
    fun `transforming an item`() {
        val pessoa = Pessoa("Felipe")
        Mono.just(pessoa)
            .map { Funcionario.from(pessoa) }
            .doOnNext { println(it) }
            .subscribe()
    }

    @Test
    fun `transforming an item to a new mono`() {
        val idPessoa = 0L

        val monoMap: Mono<Mono<Pessoa>> = Mono
            .just(idPessoa)
            .map { PessoaRepository.findById(it) }

        val monoFlatMap: Mono<Pessoa> = Mono
            .just(idPessoa)
            .flatMap { PessoaRepository.findById(it) }
    }

    @Test
    fun `handling empty mono`() {
        val idPessoa = 0L

        val monoSwitchIfEmpty = Mono.just(idPessoa)
            .flatMap { PessoaCacheRepository.findById(idPessoa) }
            .doOnNext { println("Não passo por aqui pois nenhum item foi emitido") }
            .switchIfEmpty(
                Mono.defer { PessoaRepository.findById(idPessoa) }
            )
            .subscribe(System.out::println)

        val monoDefaultIfEmpty = Mono.just(idPessoa)
            .flatMap { PessoaCacheRepository.findById(idPessoa) }
            .doOnNext { println("Não passo por aqui pois nenhum item foi emitido") }
            .defaultIfEmpty(Pessoa("default"))
            .subscribe(System.out::println)
    }


    @Test
    fun `mono to flux`() {
        val pessoas = listOf(
            Pessoa("Felipe"),
            Pessoa("Diego")
        )

        val fluxFromMonoFlatMapMany: Flux<Pessoa> = Mono.just(pessoas)
            .flatMapMany { Flux.fromIterable(it) }

        val fluxFromMonoFlatMapIterable: Flux<Pessoa> = Mono.just(pessoas)
            .flatMapIterable { it }
    }

    @Test
    fun `mono error`() {
        val monoError: Mono<Pessoa> = Mono.just(Funcionario("Fulano"))
            .flatMap { Mono.error<Funcionario>(Exception("Erro no Cadastro")) }
            .doOnError { println("Mostrar mensagem de erro: $it") }
            .onErrorResume { Mono.just(Funcionario("Ciclano")) }
            .map { Pessoa(it.nome) }
    }
}


