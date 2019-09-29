package com.optimaize.test.task.service;

import com.optimaize.test.task.exceptions.InvalidInputValueException;
import com.optimaize.test.task.exceptions.NoAnyInputValuesException;
import com.optimaize.test.task.exceptions.ServerTooBusyException;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.format;

@Service
public class ShtukaServiceImpl implements ShtukaService {
    private static final int MAX_AMOUNT_OF_ATTEMPTS_SAVE = 30;
    private AtomicReference<ResultHolder> value = new AtomicReference<>();

    @Override
    public void saveNumber(String newNumber) {
        Double newValue;
        try {
            newValue = Double.valueOf(newNumber);
        } catch (NumberFormatException e) {
            throw new InvalidInputValueException(format("newNumber %s is not a double", newNumber));
        }

        for (int i = 0; i < MAX_AMOUNT_OF_ATTEMPTS_SAVE; i++) {
            ResultHolder resultHolder = value.get();
            ResultHolder newResultHolder;
            if (resultHolder == null) {
                newResultHolder = new ResultHolder(newValue, newValue, newValue, 1);
            } else {
                newResultHolder = new ResultHolder(
                        resultHolder.min < newValue ? resultHolder.min : newValue,
                        resultHolder.max > newValue ? resultHolder.max : newValue,
                        resultHolder.sum + newValue,
                        resultHolder.numberAmount + 1
                );
            }

            if (value.compareAndSet(resultHolder, newResultHolder)) {
                return;
            }
        }
        throw new ServerTooBusyException(format("tried to save %s times", MAX_AMOUNT_OF_ATTEMPTS_SAVE));
    }

    @Override
    public double getMin() {
        checkValuesExists();
        return value.get().min;
    }

    @Override
    public double getMax() {
        checkValuesExists();
        return value.get().max;
    }

    @Override
    public double getAverage() {
        checkValuesExists();
        return value.get().sum / value.get().numberAmount;
    }

    private void checkValuesExists() {
        if (value.get() == null) {
            throw new NoAnyInputValuesException("please enter at least one value");
        }
    }

    private static class ResultHolder {
        private final double min;
        private final double max;
        private final double sum;
        private final int numberAmount;

        public ResultHolder(double min, double max, double sum, int numberAmount) {
            this.min = min;
            this.max = max;
            this.sum = sum;
            this.numberAmount = numberAmount;
        }
    }
}
