package com.komponente.servis1.controller;
import com.komponente.servis1.dto.TokenRequestDto;
import com.komponente.servis1.dto.TokenResponseDto;
import com.komponente.servis1.dto.UserCreateDto;
import com.komponente.servis1.dto.UserDto;
import com.komponente.servis1.secutiry.CheckSecurity;
import com.komponente.servis1.service.impl.ClientUserServiceImpl;
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
@RequestMapping("/client")
public class ClientUserController {
    private ClientUserServiceImpl clientUserService;

    public ClientUserController(ClientUserServiceImpl clientUserService) {
        this.clientUserService = clientUserService;
    }

    @ApiOperation(value = "Get all client users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    @CheckSecurity(roles = {"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     Pageable pageable) {

        return new ResponseEntity<>(clientUserService.findAll(pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "Register client user")
    @PostMapping("/register")
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(clientUserService.add(userCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(clientUserService.login(tokenRequestDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Edit client user")
    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_USER", "ROLE_ADMIN"}, owners = {"clientOwnerCheckStrategy"})
    public ResponseEntity<UserDto> editUser(@PathVariable Long id, @RequestBody @Valid UserCreateDto userCreateDto, @RequestHeader("Authorization") String authorization) {
        return new ResponseEntity<>(clientUserService.edit(id, userCreateDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Verify client user")
    @GetMapping("/verify/{id}")
    public ResponseEntity<String> verifyUser(@PathVariable String id) {
        return new ResponseEntity<>(clientUserService.updateVerficationToken(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get client user")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(clientUserService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Increase client training count")
    @PutMapping("/increase/{id}")
    public ResponseEntity<UserDto> increaseTrainingCount(@PathVariable Long id) {
        return new ResponseEntity<>(clientUserService.increaseTrainingCount(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Decrease client training count")
    @PutMapping("/decrease/{id}")
    public ResponseEntity<UserDto> decreaseTrainingCount(@PathVariable Long id) {
        return new ResponseEntity<>(clientUserService.decreaseTrainingCount(id), HttpStatus.OK);
    }

}