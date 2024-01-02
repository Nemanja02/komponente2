package com.komponente.servis1.controller;

import com.komponente.servis1.dto.TokenRequestDto;
import com.komponente.servis1.dto.TokenResponseDto;
import com.komponente.servis1.dto.UserCreateDto;
import com.komponente.servis1.dto.UserDto;
import com.komponente.servis1.secutiry.CheckSecurity;
import com.komponente.servis1.service.impl.ManagerUserServiceImpl;
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
@RequestMapping("/manager")
public class ManagerUserController {
    private ManagerUserServiceImpl managerUserService;

    public ManagerUserController(ManagerUserServiceImpl clientUserService) {
        this.managerUserService = clientUserService;
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
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     Pageable pageable) {

        return new ResponseEntity<>(managerUserService.findAll(pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "Register client user")
    @PostMapping("/register")
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(managerUserService.add(userCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(managerUserService.login(tokenRequestDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Verify manager user")
    @GetMapping("/verify/{id}")
    public ResponseEntity<String> verifyUser(@PathVariable String id) {
        return new ResponseEntity<>(managerUserService.updateVerficationToken(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get manager user by id")
    @GetMapping("/{id}")
//    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        return new ResponseEntity<>(managerUserService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Update manager user")
    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userCreateDto, @RequestHeader("Authorization") String authorization) {
        return new ResponseEntity<>(managerUserService.updateUser(id, userCreateDto), HttpStatus.OK);
    }

}
