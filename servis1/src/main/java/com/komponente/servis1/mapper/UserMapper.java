package com.komponente.servis1.mapper;

import com.komponente.servis1.domain.*;
import com.komponente.servis1.dto.EmailDto;
import com.komponente.servis1.dto.UserCreateDto;
import com.komponente.servis1.dto.UserDto;
import com.komponente.servis1.exception.NotFoundException;
import com.komponente.servis1.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setUserType(user.getRole().getName());
        userDto.setActive(user.isActive());
        userDto.setVerified(user.getVerified());



        if (user instanceof ClientUser) {
            ClientUser clientUser = (ClientUser) user;
            userDto.setMembershipNumber(clientUser.getMembershipNumber());
            userDto.setVerified(clientUser.getVerified());
            userDto.setTrainingCount(clientUser.getScheduledTrainings());
        } else if (user instanceof ManagerUser) {
            ManagerUser gymManagerUser = (ManagerUser) user;
            userDto.setGymId(gymManagerUser.getGymId());
            userDto.setEmploymentDate(gymManagerUser.getEmploymentDate());
        }

        return userDto;
    }

    public User userCreateDtoToUser(UserCreateDto userCreateDto) {
        User user;
        switch (userCreateDto.getUserType().toLowerCase()) {
            case "admin":
                user = new AdminUser();
                break;
            case "client":
                ClientUser clientUser = new ClientUser();
                clientUser.setMembershipNumber(userCreateDto.getMembershipNumber());
                user = clientUser;
                user.setRole(roleRepository.findRoleByName("ROLE_USER")
                        .orElseThrow(() -> new NotFoundException("Role 'ROLE_USER' not found")));
                break;
            case "manager":
                ManagerUser ManagerUser = new ManagerUser();
                ManagerUser.setGymId(userCreateDto.getGymId());
                ManagerUser.setEmploymentDate(userCreateDto.getEmploymentDate());
                user = ManagerUser;
                user.setRole(roleRepository.findRoleByName("ROLE_MANAGER")
                        .orElseThrow(() -> new NotFoundException("Role 'ROLE_MANAGER' not found")));
                break;
            default:
                user = new User();
        }

        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setDateOfBirth(userCreateDto.getDateOfBirth());





        return user;
    }

    public EmailDto userDtoToEmailDto(UserDto userDto, EmailType emailType, String token) {
        User user;
        System.out.println(userDto.getUserType());
        switch (userDto.getUserType()) {
            case "ROLE_ADMIN":
                user = new AdminUser();
                break;
            case "ROLE_USER":
                ClientUser clientUser = new ClientUser();
                clientUser.setMembershipNumber(userDto.getMembershipNumber());
                user = clientUser;
                System.out.println(user);
                break;
            case "ROLE_MANAGER":
                ManagerUser ManagerUser = new ManagerUser();
                ManagerUser.setGymId(userDto.getGymId());
                ManagerUser.setEmploymentDate(userDto.getEmploymentDate());
                user = ManagerUser;
                break;
            default:
                user = new User();
        }

        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setRole(roleRepository.findRoleByName("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Role 'ROLE_USER' not found")));

        user.setVerified(token);
        EmailDto emailDto = new EmailDto();
        System.out.println(userDto.getMembershipNumber());
        emailDto.setSubject(replaceVariables(emailType.getSubject(), user));
        emailDto.setBody(replaceVariables(emailType.getBody(), user));
        emailDto.setEmail(user.getEmail());
        emailDto.setFirstName(user.getFirstName());
        emailDto.setLastName(user.getLastName());
        emailDto.setToken(token);

        return emailDto;
    }

    public EmailDto createEmailDtoToUser(UserCreateDto userCreateDto, EmailType emailType, String token) {
        User user;
        switch (userCreateDto.getUserType().toLowerCase()) {
            case "admin":
                user = new AdminUser();
                break;
            case "client":
                ClientUser clientUser = new ClientUser();
                clientUser.setMembershipNumber(userCreateDto.getMembershipNumber());
                user = clientUser;
                break;
            case "manager":
                ManagerUser ManagerUser = new ManagerUser();
                ManagerUser.setGymId(userCreateDto.getGymId());
                ManagerUser.setEmploymentDate(userCreateDto.getEmploymentDate());
                user = ManagerUser;
                break;
            default:
                user = new User();
        }

        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setDateOfBirth(userCreateDto.getDateOfBirth());
        user.setRole(roleRepository.findRoleByName("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Role 'ROLE_USER' not found")));

        user.setVerified(token);
        EmailDto emailDto = new EmailDto();
        emailDto.setSubject(replaceVariables(emailType.getSubject(), user));
        emailDto.setBody(replaceVariables(emailType.getBody(), user));
        emailDto.setEmail(user.getEmail());

        return emailDto;
    }

    public String replaceVariables(String body, User user) {
        body = body.replace("{firstName}", user.getFirstName());
        body = body.replace("{lastName}", user.getLastName());
        body = body.replace("{username}", user.getUsername());
        body = body.replace("{email}", user.getEmail());
        if (user instanceof ClientUser && ((ClientUser) user).getDateOfBirth() != null) {
            body = body.replace("{dateOfBirth}", user.getDateOfBirth().toString());
        }
        if (user instanceof ClientUser && ((ClientUser) user).getMembershipNumber() != null) {
            body = body.replace("{membershipNumber}", ((ClientUser) user).getMembershipNumber());
        }
        if (user instanceof ManagerUser && ((ManagerUser) user).getGymId() != null)
            body = body.replace("{gymName}", ((ManagerUser) user).getGymId().toString());
        if (user instanceof ManagerUser && ((ManagerUser) user).getEmploymentDate() != null)
            body = body.replace("{employmentDate}", ((ManagerUser) user).getEmploymentDate().toString());
        body = body.replace("{token}", user.getVerified());
        return body;
    }
}
