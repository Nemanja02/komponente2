package com.komponente.servis1.controller;

import com.komponente.servis1.dto.EmailDto;
import com.komponente.servis1.dto.EmailTypeDto;
import com.komponente.servis1.dto.EmailTypeRequestDto;
import com.komponente.servis1.service.impl.EmailServiceImpl;
import com.komponente.servis1.secutiry.CheckSecurity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/email")
public class EmailController {

    EmailServiceImpl emailService;

    public EmailController(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @ApiOperation(value = "Get all email types")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<EmailTypeDto>> getAllEmailTypes(@RequestHeader("Authorization") String authorization,
                                                               Pageable pageable) {
        return new ResponseEntity<>(emailService.findAll(pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "Edit email type")
    @PutMapping("/{id}")
    public ResponseEntity<EmailTypeDto> editEmail(@PathVariable Long id, @RequestBody @Valid EmailTypeDto emailTypeDto) {
        return new ResponseEntity<>(emailService.update(id, emailTypeDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Get email type by id")
    @GetMapping("/{id}")
    public ResponseEntity<EmailTypeDto> getEmail(@PathVariable Long id) {
        return new ResponseEntity<>(emailService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get email by type")
    @PostMapping("/type/{type}")
    public ResponseEntity<EmailDto> getEmailByType(@PathVariable String type, @RequestBody @Valid EmailTypeRequestDto emailTypeRequestDto) {
        return new ResponseEntity<>(emailService.findByType(type, emailTypeRequestDto.getClientId(), emailTypeRequestDto.getManagerId()), HttpStatus.OK);
    }
}
