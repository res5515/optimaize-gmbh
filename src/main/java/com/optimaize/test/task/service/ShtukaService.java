package com.optimaize.test.task.service;

import com.optimaize.test.task.exceptions.InvalidInputValueException;
import com.optimaize.test.task.exceptions.NoAnyInputValuesException;
import com.optimaize.test.task.exceptions.ServerTooBusyException;

public interface ShtukaService {
    void saveNumber(String newNumber) throws InvalidInputValueException, ServerTooBusyException;
    double getMin() throws NoAnyInputValuesException;
    double getMax() throws NoAnyInputValuesException;
    double getAverage() throws NoAnyInputValuesException;
}
