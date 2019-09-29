package com.optimaize.test.task.service;

import com.optimaize.test.task.exceptions.NoAnyInputValuesException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShtukaServiceImplTest {

    private static int AMOUNT_OF_INPUT_VALUES = 1_000_000;
    private ShtukaServiceImpl service;
    private double[] inputValues;

    @BeforeEach
    public void init() {
        service = new ShtukaServiceImpl();
        inputValues = generateArray(AMOUNT_OF_INPUT_VALUES);
    }

    @Test
    public void testEmptyValues() {
        Assertions.assertThrows(NoAnyInputValuesException.class, () -> service.getMax());
        Assertions.assertThrows(NoAnyInputValuesException.class, () -> service.getMin());
        Assertions.assertThrows(NoAnyInputValuesException.class, () -> service.getAverage());
    }

    @Test
    public void testShtukaOneThread() {
        Arrays.stream(inputValues);
        OptionalDouble min = Arrays.stream(inputValues).min();
        OptionalDouble max = Arrays.stream(inputValues).max();
        OptionalDouble average = Arrays.stream(inputValues).average();
        System.out.println(min.getAsDouble());
        System.out.println(max.getAsDouble());
        System.out.println(roundDouble(average.getAsDouble()));

        Arrays.stream(inputValues).forEach(newValue -> service.saveNumber(String.valueOf(newValue)));
        assertEquals(min.getAsDouble(), service.getMin());
        assertEquals(max.getAsDouble(), service.getMax());
        assertEquals(roundDouble(average.getAsDouble()), roundDouble(service.getAverage()));
    }

    @Test
    public void testShtukaMultiThread() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        Arrays.stream(inputValues).forEach(newValue -> processAsync(String.valueOf(newValue), threadPoolExecutor)) ;
        await().atMost(5, SECONDS).until(() -> threadPoolExecutor.getQueue().size() == 0);

        OptionalDouble min = Arrays.stream(inputValues).min();
        OptionalDouble max = Arrays.stream(inputValues).max();
        OptionalDouble average = Arrays.stream(inputValues).average();
        assertEquals(min.getAsDouble(), service.getMin());
        assertEquals(max.getAsDouble(), service.getMax());
        assertEquals(roundDouble(average.getAsDouble()), roundDouble(service.getAverage()));

        shutdownExecutor(threadPoolExecutor, 1000);
    }

    private void processAsync(String newValue, ThreadPoolExecutor threadPoolExecutor) {
        threadPoolExecutor.execute(() -> service.saveNumber(newValue));
    }

    private void shutdownExecutor(ExecutorService executor, int millis) {
        try {
            executor.shutdown();
            executor.awaitTermination(millis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {

        } finally {
            executor.shutdownNow();
        }
    }

    public static double[] generateArray(int size) {
        DoubleStream stream = DoubleStream.generate(() -> (Math.random() * 1_000_000));
        return stream.limit(size).toArray();
    }

    private double roundDouble(double value) {
        return Math.round(value * 1_000_000) / 1_000_000.0;
    }
}
