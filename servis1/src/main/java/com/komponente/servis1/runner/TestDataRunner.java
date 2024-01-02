package com.komponente.servis1.runner;

import com.komponente.servis1.domain.*;
import com.komponente.servis1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private RoleRepository roleRepository;
    private AdminUserRepository adminUserRepository;
    private ClientUserRepository clientUserRepository;
    private EmailTypeRepository emailTypeRepository;
    @Autowired
    private ManagerUserRepository managerUserRepository;

    public TestDataRunner(RoleRepository roleRepository, AdminUserRepository adminUserRepository, EmailTypeRepository emailTypeRepository, ClientUserRepository clientUserRepository) {
        this.roleRepository = roleRepository;
        this.adminUserRepository = adminUserRepository;
        this.emailTypeRepository = emailTypeRepository;
        this.clientUserRepository = clientUserRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role roleUser = new Role("ROLE_USER", "User role");
        Role roleAdmin = new Role("ROLE_ADMIN", "Admin role");
        Role roleManager = new Role("ROLE_MANAGER", "Manager role");
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleManager);

        AdminUser admin = new AdminUser();
        admin.setEmail("admin@gmail.com");
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(roleAdmin);
        adminUserRepository.save(admin);

        ManagerUser manager = new ManagerUser();
        manager.setEmail("manager@gmail.com");
        manager.setUsername("manager");
        manager.setPassword("manager");
        manager.setFirstName("Manager");
        manager.setLastName("Manager");
        manager.setRole(roleManager);
        manager.setVerified("verified");
        manager.setEmploymentDate(java.time.LocalDate.of(2020, 1, 1));
        manager.setGymId(1L);
        managerUserRepository.save(manager);

        ClientUser client = new ClientUser();
        client.setEmail("nmarjanov6121rn@raf.rs");
        client.setUsername("Nemanja02");
        client.setPassword("Test1234");
        client.setFirstName("Nemanja");
        client.setLastName("Marjanov");
        client.setRole(roleUser);
        client.setVerified("verified");
        client.setDateOfBirth(java.time.LocalDate.of(2002, 1, 25));
        clientUserRepository.save(client);

        // EMAIL TYPES

        EmailType activation = new EmailType();
        activation.setType("ACTIVATION");
        activation.setSubject("Account activation");
        activation.setBody("Dear {firstName} {lastName},\n\n" +
                "Your account ({username}) has been created. Please click on the link below to activate it:\n\n" +
                "http://localhost:8080/api/client/verify/{token}\n\n" +
                "Regards,\n" +
                "Admin");
        emailTypeRepository.save(activation);

        EmailType passwordChanged = new EmailType();
        passwordChanged.setType("PASSWORD_CHANGED");
        passwordChanged.setSubject("Password changed");


        passwordChanged.setBody("Dear {firstName} {lastName},\n\n" +
                "Your password has been changed.\n\n" +
                "Regards,\n" +
                "Admin");
        emailTypeRepository.save(passwordChanged);

        EmailType trainingSessionBooked = new EmailType();
        trainingSessionBooked.setType("TRAINING_SESSION_BOOKED");
        trainingSessionBooked.setSubject("Training session booked");
        // body with price
        trainingSessionBooked.setBody("Dear {firstName} {lastName},\n\n" +
                "You have booked a training session for {trainingType} on {dayOfWeek} from {startTime} to {endTime}.\n\n" +
                "Price: {price} RSD\n\n" +
                "Regards,\n" +
                "Admin");
        emailTypeRepository.save(trainingSessionBooked);

        EmailType trainingSessionCancelled = new EmailType();
        trainingSessionCancelled.setType("TRAINING_SESSION_CANCELLED");
        trainingSessionCancelled.setSubject("Training session cancelled");
        trainingSessionCancelled.setBody("Dear {firstName} {lastName},\n\n" +
                "You have cancelled a training session for {trainingType} on {dayOfWeek} from {startTime} to {endTime}.\n\n" +
                "Regards,\n" +
                "Admin");
        emailTypeRepository.save(trainingSessionCancelled);

        EmailType trainingSessionCancelledByManager = new EmailType();
        trainingSessionCancelledByManager.setType("TRAINING_SESSION_CANCELLED_BY_MANAGER");
        trainingSessionCancelledByManager.setSubject("Training session cancelled by manager");
        trainingSessionCancelledByManager.setBody("Dear {firstName} {lastName},\n\n" +
                "Your training session for {trainingType} on {dayOfWeek} from {startTime} to {endTime} has been cancelled by manager.\n\n" +
                "Regards,\n" +
                "Admin");
        emailTypeRepository.save(trainingSessionCancelledByManager);

        EmailType trainingSessionCancelledByClient = new EmailType();
        trainingSessionCancelledByClient.setType("TRAINING_SESSION_CANCELLED_BY_CLIENT");
        trainingSessionCancelledByClient.setSubject("Training session cancelled by client");
        trainingSessionCancelledByClient.setBody("Dear manager,\n\n" +
                "Training session for {trainingType} on {dayOfWeek} from {startTime} to {endTime} has been cancelled by client.\n\n" +
                "Regards,\n" +
                "Admin");

        emailTypeRepository.save(trainingSessionCancelledByClient);

        EmailType trainingSessionBookedByClient = new EmailType();
        trainingSessionBookedByClient.setType("TRAINING_SESSION_BOOKED_BY_CLIENT");
        trainingSessionBookedByClient.setSubject("Training session booked by client");
        trainingSessionBookedByClient.setBody("Dear manager,\n\n" +
                "Training session for {trainingType} on {dayOfWeek} from {startTime} to {endTime} has been booked by client.\n\n" +
                "Regards,\n" +
                "Admin");
        emailTypeRepository.save(trainingSessionBookedByClient);

        EmailType trainingSessionReminder = new EmailType();
        trainingSessionReminder.setType("TRAINING_SESSION_REMINDER");
        trainingSessionReminder.setSubject("Training session reminder");
        trainingSessionReminder.setBody("Dear {firstName} {lastName},\n\n" +
                "You have a training session for {trainingType} on {dayOfWeek} from {startTime} to {endTime}.\n\n" +
                "Regards,\n" +
                "Admin");
        emailTypeRepository.save(trainingSessionReminder);



    }
}
