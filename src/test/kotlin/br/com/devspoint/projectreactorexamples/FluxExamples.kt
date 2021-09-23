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
}