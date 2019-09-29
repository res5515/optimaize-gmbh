package com.optimaize.test.task.controllers;

import com.optimaize.test.task.exceptions.InvalidInputValueException;
import com.optimaize.test.task.exceptions.NoAnyInputValuesException;
import com.optimaize.test.task.exceptions.ServerTooBusyException;
import com.optimaize.test.task.responses.BaseResponse;
import com.optimaize.test.task.responses.Responses;
import com.optimaize.test.task.responses.ResultResponse;
import com.optimaize.test.task.service.ShtukaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
@RequestMapping("/shtuka")
public class ShtukaController {
    private static final Logger log = LoggerFactory.getLogger(ShtukaController.class);

    private final ShtukaService shtukaService;

    public ShtukaController(ShtukaService shtukaService) {
        this.shtukaService = shtukaService;
    }

    @PostMapping("/save")
    public ResponseEntity<BaseResponse> saveNumber(@RequestParam String number) {
        try {
            shtukaService.saveNumber(number);
        } catch (InvalidInputValueException e) {
            log.error("could not save the number", e);
            return new ResponseEntity(new BaseResponse(Responses.NOT_NUMBER), HttpStatus.OK);
        } catch (ServerTooBusyException e) {
            return new ResponseEntity(new BaseResponse(Responses.SERVER_IS_TOO_BUSY), HttpStatus.OK);
        }
        return new ResponseEntity(new BaseResponse(Responses.OK), HttpStatus.OK);
    }

    @GetMapping("/min")
    public ResponseEntity<BaseResponse> getMin() {
        return calculateResult(() -> shtukaService.getMin());
    }

    @GetMapping("/max")
    public ResponseEntity<BaseResponse> getMax() {
        return calculateResult(() -> shtukaService.getMax());
    }

    @GetMapping("/avg")
    public ResponseEntity<BaseResponse> getAvg() {
        return calculateResult(() -> shtukaService.getAverage());
    }

    private ResponseEntity<BaseResponse> calculateResult(Supplier<Double> consumer) {
        double result;
        try {
            result = consumer.get();
        } catch (NoAnyInputValuesException e) {
            log.error("could not calculate the result", e);
            return new ResponseEntity(new BaseResponse(Responses.NO_ANY_INPUT_VALUES), HttpStatus.OK);
        }
        return new ResponseEntity(new ResultResponse(Responses.OK, result), HttpStatus.OK);
    }
}
