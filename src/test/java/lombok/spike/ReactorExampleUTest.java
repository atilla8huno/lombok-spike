package lombok.spike;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ReactorExampleUTest {

    Person atilla = new Person("Atilla", "Barros");
    Person daniela = new Person("Daniela", "Carvalho");
    Person joao = new Person("Lucio", "Barros");
    Person pedro = new Person("Maria", "Silva");

    @Test
    public void monoTest() {
        // creates new person mono
        // Mono is a zero-or-one object
        Mono<Person> personMono = Mono.just(atilla);

        // gets person object from mono publisher
        Person person = personMono.block();

        // output name
        log.info(person.sayMyName());
    }

    @Test
    public void monoTransform() {
        // crates a mono of Person
        Mono<Person> personMono = Mono.just(daniela);

        // transform the person in PersonCmd
        PersonCmd cmd = personMono
                .map(PersonCmd::new)
                .block();

        // output name
        log.info(cmd.sayMyName());
    }

    @Test(expected = NullPointerException.class)
    public void monoFilter() {
        // creates a mono of Person
        Mono<Person> personMono = Mono.just(joao);

        // filter a person obj where the first name starts with Foo and doesn't find it
        Person foo = personMono
                .filter(person -> person.getFirstName().contains("Foo"))
                .block();

        // attemp to output name
        log.info(foo.sayMyName());
    }

    @Test
    public void fluxTest() {
        // creates a reactive collection (Flux)
        // Flux is a zero-or-many object
        Flux<Person> peopleFlux = Flux.just(atilla, daniela, joao, pedro);

        // subscribe to receive the date (async)
        peopleFlux.subscribe(person -> log.info(person.sayMyName()));
    }

    @Test
    public void fluxFilter() {
        // creates a reactive collection (Flux)
        Flux<Person> peopleFlux = Flux.just(atilla, daniela, joao, pedro);

        peopleFlux
                .filter(person -> person.getFirstName().equals(atilla.getFirstName()))
                .subscribe(person -> log.info(person.sayMyName()));
    }

    @Test
    public void fluxDelayWithNoOutput() {
        // creates a reactive collection (Flux)
        Flux<Person> peopleFlux = Flux.just(atilla, daniela, joao, pedro);

        // delays for 2 seconds, then the execution terminates before it gets back
        peopleFlux
                .delayElements(Duration.ofSeconds(2))
                .subscribe(person -> log.info(person.sayMyName()));
    }

    @Test
    public void fluxDelayWaitToOutput() throws InterruptedException {
        // with CountDownLatch we can cause a thread to block until other threads have completed
        // basically the parameter 1 tells how many times the method countDown should be invoked to resume the thread
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // creates a reactive collection (Flux)
        Flux<Person> peopleFlux = Flux.just(atilla, daniela, joao, pedro);

        // delays 1 second for each element
        peopleFlux
                .delayElements(Duration.ofSeconds(1))
                // on complete call countDown to resume the thread
                .doOnComplete(countDownLatch::countDown)
                .subscribe(person -> log.info(person.sayMyName()));

        countDownLatch.await();
    }

    @Test
    public void fluxDelayWithFilter() throws InterruptedException {
        // with CountDownLatch we can cause a thread to block until other threads have completed
        // basically the parameter 1 tells how many times the method countDown should be invoked to resume the thread
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // creates a reactive collection (Flux)
        Flux<Person> peopleFlux = Flux.just(atilla, daniela, joao, pedro);

        peopleFlux
                // delays 1 second for each element
                .delayElements(Duration.ofSeconds(1))
                .filter(person -> person.getLastName().equals("Barros"))
                // on complete call countDown to resume the thread
                .doOnComplete(countDownLatch::countDown)
                .subscribe(person -> log.info(person.sayMyName()));

        countDownLatch.await();
    }
}
