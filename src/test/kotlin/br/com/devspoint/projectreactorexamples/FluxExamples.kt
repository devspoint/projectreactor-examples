package br.com.devspoint.projectreactorexamples

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.justOrEmpty
import java.util.*

class FluxExamples {

    @Test
    fun `creating flux`() {
        val fluxJust = Flux.just(
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )

        val fluxEmpty = Flux.empty<String>()

        val fluxDefer = Flux.defer { Flux.just(System.currentTimeMillis()) }

        println("Assinando eventos criado com o Just")
        fluxJust.subscribe(System.out::println)
        Thread.sleep(500L)

        println("Assinando eventos criado com o Defer")
        fluxDefer.subscribe(System.out::println)
        Thread.sleep(500L)

        println("Assinando eventos criado com o Empty e JustOrEmpty")
        fluxEmpty.subscribe { println("não passei aqui") }
    }

    @Test
    fun `doOnNext`() {
        Flux.just(
            "Felipe",
            "Diego",
            "Luana"
        )
        .doOnNext { println("$it passou aqui.") }
        .doOnNext { println("$it passou aqui também.") }
        .doOnComplete { println("Mas nenhum deles passou aqui.") }
        .subscribe()
    }

    @Test
    fun `transforming an item`() {
        Flux.just(
            Pessoa("Felipe"),
            Pessoa("Diego")
        )
        .map { Funcionario.from(it) }
        .doOnNext { println(it) }
        .subscribe()
    }

    @Test
    fun `transforming an item to a new mono`() {
        val fluxMap: Flux<Flux<Pessoa>> = Flux.just("item")
                .map { PessoaRepository.findAll() }

        val fluxFlatMap: Flux<Pessoa> = Flux.just("item")
            .flatMap { PessoaRepository.findAll() }
    }

    @Test
    fun `handling empty flux`() {
        val idPessoa = 0L

        val fluxSwitchIfEmpty = Flux.just(idPessoa)
            .flatMap { PessoaCacheRepository.findAll() }
            .doOnNext { println("Não passo por aqui pois nenhum item foi emitido") }
            .switchIfEmpty(
                Flux.defer { PessoaRepository.findAll() }
            )
            .subscribe(System.out::println)

        val fluxDefaultIfEmpty = Flux.just(idPessoa)
            .flatMap { PessoaCacheRepository.findAll() }
            .doOnNext { println("Não passo por aqui pois nenhum item foi emitido") }
            .defaultIfEmpty(Pessoa("default"))
            .subscribe(System.out::println)
    }

    @Test
    fun `flux to mono`() {
        val pessoas = listOf(
            Pessoa("Felipe"),
            Pessoa("Diego")
        )

        val monoFromFluxCollectList: Mono<List<Pessoa>> = Flux.just(
            Pessoa("Felipe"), Pessoa("Diego"))
        .collectList()

        val monoFromFluxNext: Mono<Pessoa> = Flux.just(
            Pessoa("Felipe"), Pessoa("Diego"))
        .next()

        val monoFromFluxLast: Mono<Pessoa> = Flux.just(
            Pessoa("Felipe"), Pessoa("Diego"))
        .last()

        Flux.just(
            Pessoa("Felipe"), Pessoa("Diego"))
        .reduce { pessoa1, pessoa2 -> Pessoa("Felipe Diego") }
            .subscribe { println(it) }
    }

    @Test
    fun `flux error`() {
        val fluxError: Flux<Pessoa> = Flux.just(Funcionario("Fulano"))
            .flatMap { Mono.error<Funcionario>(Exception("Erro no Cadastro")) }
            .doOnError { println("Mostrar mensagem de erro: $it") }
            .onErrorResume { Mono.just(Funcionario("Ciclano")) }
            .map { Pessoa(it.nome) }
    }
}