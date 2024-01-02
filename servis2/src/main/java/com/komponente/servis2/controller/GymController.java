package com.komponente.servis2.controller;

import com.komponente.servis2.dto.GymDto;
import com.komponente.servis2.dto.validations.GymCreateDto;
import com.komponente.servis2.security.CheckSecurity;
import com.komponente.servis2.service.GymService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/gym")
public class GymController {

    // TODO
    // Implement BookController
    // Implement LoyaltyRewardsController
    // Implement TrainingSessionController

    @Autowired
    GymService gymService;


    @ApiOperation(value = "Add gym")
    @PostMapping("/add")
//    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<GymDto> addGym(@RequestBody @Valid GymCreateDto gymCreateDto) {
        return new ResponseEntity<>(gymService.add(gymCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get gym by id")
    @GetMapping("/{id}")
//    @CheckSecurity(roles = {"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<GymDto> getGymById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(gymService.get(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all gyms")
    @GetMapping
//    @CheckSecurity(roles = {"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Iterable<GymDto>> getAllGyms() {
        return new ResponseEntity<>(gymService.getAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Edit gym")
    @PutMapping("/{id}")
//    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<GymDto> editGym(@PathVariable("id") Long id, @RequestBody @Valid GymCreateDto gymCreateDto) {
        return new ResponseEntity<>(gymService.update(gymCreateDto,id), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete gym")
    @DeleteMapping("/{id}")
//    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<GymDto> deleteGym(@PathVariable("id") Long id) {
        return new ResponseEntity<>(gymService.delete(id), HttpStatus.OK);
    }
}
