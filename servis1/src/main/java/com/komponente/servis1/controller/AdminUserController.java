package com.komponente.servis1.controller;

import com.komponente.servis1.dto.TokenRequestDto;
import com.komponente.servis1.dto.TokenResponseDto;
import com.komponente.servis1.dto.UserCreateDto;
import com.komponente.servis1.dto.UserDto;
import com.komponente.servis1.service.impl.AdminUserServiceImpl;
import com.komponente.servis1.secutiry.CheckSecurity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private final AdminUserServiceImpl adminUserService;

    public AdminUserController(AdminUserServiceImpl adminUserService) {
        this.adminUserService = adminUserService;
    }

    @ApiOperation(value = "Get all  admin users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     Pageable pageable) {

        return new ResponseEntity<>(adminUserService.findAll(pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "Register admin user")
    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(adminUserService.add(userCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(adminUserService.login(tokenRequestDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Enable user")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @PostMapping("/enable/{userId}")
    public ResponseEntity<Void> enableUser(@RequestHeader("Authorization") String authorization,@PathVariable Long userId) {
        adminUserService.enableUser(userId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Disable user")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @PostMapping("/disable/{userId}")
    public ResponseEntity<Void> disableUser(@RequestHeader("Authorization") String authorization,@PathVariable Long userId) {
        System.out.println("disableUser " + userId);
        adminUserService.disableUser(userId);
        return ResponseEntity.ok().build();
    }
}
