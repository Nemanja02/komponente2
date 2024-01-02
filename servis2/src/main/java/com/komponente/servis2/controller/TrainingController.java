package com.komponente.servis2.controller;

import com.komponente.servis2.dto.BookingDto;
import com.komponente.servis2.dto.PricingDto;
import com.komponente.servis2.dto.TrainingSessionDto;
import com.komponente.servis2.dto.TrainingTypeDto;
import com.komponente.servis2.dto.validations.*;
import com.komponente.servis2.security.CheckSecurity;
import com.komponente.servis2.service.BookingService;
import com.komponente.servis2.service.PricingService;
import com.komponente.servis2.service.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/training")
public class TrainingController {

    TrainingService trainingService;
    PricingService pricingService;
    BookingService bookingService;

    public TrainingController(TrainingService trainingService, PricingService pricingService, BookingService bookingService) {
        this.trainingService = trainingService;
        this.pricingService = pricingService;
        this.bookingService = bookingService;
    }


    @ApiOperation(value = "Get all training types")
    @GetMapping("/types")
    public ResponseEntity<Iterable<TrainingTypeResponseDto>> getAllTrainingTypes() {
        return new ResponseEntity<>(trainingService.getAllTypesResponse(), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Get training types by id")
    @GetMapping("/types/{id}")
    public ResponseEntity<TrainingTypeResponseDto> getTrainingTypeById(@PathVariable Long id) {
        return new ResponseEntity<>(trainingService.getTypeResponse(id), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Create training type")
    @PostMapping("/types")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TrainingTypeDto> createTrainingType(@RequestBody @Valid TrainingTypeCreateDto trainingTypeDto) {
        return new ResponseEntity<>(trainingService.addType(trainingTypeDto), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Update training type")
    @PutMapping("/types/{id}")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TrainingTypeDto> updateTrainingType(@PathVariable("id") Long id, @RequestBody @Valid TrainingTypeCreateDto trainingTypeDto) {
        return new ResponseEntity<>(trainingService.updateType(trainingTypeDto, id), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Delete training type")
    @DeleteMapping("/types/{id}")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TrainingTypeDto> deleteTrainingType(@PathVariable("id") Long id) {
        return new ResponseEntity<>(trainingService.deleteType(id), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Get all pricings")
    @GetMapping("/pricings")
    public ResponseEntity<Iterable<PricingDto>> getAllPricings() {
        return new ResponseEntity<>(pricingService.getAll(), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Get pricing by id")
    @GetMapping("/pricings/{id}")
    public ResponseEntity<PricingDto> getPricingById(Long id) {
        return new ResponseEntity<>(pricingService.get(id), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Create pricing")
    @PostMapping("/pricings")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<PricingDto> createPricing(@RequestBody @Valid PricingCreateDto pricingDto) {
        return new ResponseEntity<>(pricingService.add(pricingDto), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Update pricing")
    @PutMapping("/pricings/{id}")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<PricingDto> updatePricing(@PathVariable("id") Long id, @RequestBody @Valid PricingCreateDto pricingDto) {
        return new ResponseEntity<>(pricingService.update(pricingDto, id), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Delete pricing")
    @DeleteMapping("/pricings/{id}")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<PricingDto> deletePricing(@PathVariable("id") Long id) {
        return new ResponseEntity<>(pricingService.delete(id), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Add training session")
    @PostMapping("/session")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TrainingSessionDto> addSession(@RequestBody @Valid TrainingSessionCreateDto trainingSessionCreateDto) {
        return new ResponseEntity<>(trainingService.addSession(trainingSessionCreateDto), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Get all training sessions")
    @GetMapping("/session")
    public ResponseEntity<Iterable<TrainingSessionDto>> getAllSessions() {
        return new ResponseEntity<>(trainingService.getAllSessions(), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Get all training sessions by gym")
    @GetMapping("/session/gym/{id}")
    public ResponseEntity<Iterable<TrainingSessionResponseDto>> getAllSessionsByGym(@PathVariable("id") Long id) {
        return new ResponseEntity<>(trainingService.getAllSessionsByGym(id), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Get training session by id")
    @GetMapping("/session/{id}")
    public ResponseEntity<TrainingSessionDto> getSessionById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(trainingService.getSession(id), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Update training session")
    @PutMapping("/session/{id}")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TrainingSessionDto> updateSession(@PathVariable("id") Long id, @RequestBody @Valid TrainingSessionCreateDto trainingSessionCreateDto) {
        return new ResponseEntity<>(trainingService.updateSession(trainingSessionCreateDto, id), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Delete training session")
    @DeleteMapping("/session/{id}")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TrainingSessionDto> deleteSession(@PathVariable("id") Long id) {
        return new ResponseEntity<>(trainingService.deleteSession(id), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Book session")
    @PostMapping("/session/book")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TrainingSessionDto> bookSession(@RequestBody @Valid BookingCreateDto bookingCreateDto) {
        return new ResponseEntity<>(bookingService.bookSession(bookingCreateDto), org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Cancel session")
    @PostMapping("/session/cancel")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TrainingSessionDto> cancelSession(@RequestBody @Valid BookingCreateDto bookingCreateDto) {
        bookingService.cancelBooking(bookingCreateDto);
        return new ResponseEntity<>(null, org.springframework.http.HttpStatus.OK);
    }

    @ApiOperation(value = "Cancel session by manager")
    @PostMapping("/session/cancel/{id}")
    //@CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TrainingSessionDto> cancelSessionByManager(@PathVariable("id") Long id) {
        trainingService.cancelSession(id);
        return new ResponseEntity<>(null, org.springframework.http.HttpStatus.OK);
    }
}
