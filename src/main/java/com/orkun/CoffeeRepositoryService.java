package com.orkun;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class CoffeeRepositoryService {

    private Map<Integer, Coffee> coffeeList = new HashMap<>();

    public CoffeeRepositoryService() {
        coffeeList.put(1, new Coffee(1, "Fernandez Espresso", "Colombia", 23));
        coffeeList.put(2, new Coffee(2, "La Scala Whole Beans", "Bolivia", 18));
        coffeeList.put(3, new Coffee(3, "Dak Lak Filter", "Vietnam", 25));
    }

    public List<Coffee> getAllCoffees() {
        return new ArrayList<>(coffeeList.values());
    }

    public Coffee getCoffeeById(Integer id) {
        return coffeeList.get(id);
    }

    public List<Coffee> getRecommendations(Integer id) {
        if (id == null) {
            return Collections.emptyList();
        }

        return coffeeList.values().stream()
                .filter(coffee -> !id.equals(coffee.id))
                .limit(2)
                .collect(Collectors.toList());
    }

    private AtomicLong counter = new AtomicLong(0);

    /**
     * We also added a @CircuitBreaker annotation with requestVolumeThreshold = 4.
     * CircuitBreaker.failureRatio is by default 0.5, and CircuitBreaker.delay is by default 5 seconds.
     * That means that a circuit breaker will open when 2 of the last 4 invocations failed
     * and it will stay open for 5 seconds.
     **/
    @CircuitBreaker(requestVolumeThreshold = 4)
    public Integer getAvailability(Coffee coffee) {
        maybeFail();
        return new Random().nextInt(30);
    }

    private void maybeFail() {
        // introduce some artificial failures
        final Long invocationNumber = counter.getAndIncrement();
        if (invocationNumber % 4 > 1) { // alternate 2 successful and 2 failing invocations
            throw new RuntimeException("Service failed.");
        }
    }
}
