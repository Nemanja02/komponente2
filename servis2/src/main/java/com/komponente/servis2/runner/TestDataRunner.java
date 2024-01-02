package com.komponente.servis2.runner;

import com.komponente.servis2.entity.Gym;
import com.komponente.servis2.entity.Pricing;
import com.komponente.servis2.entity.TrainingType;
import com.komponente.servis2.repository.GymRepository;
import com.komponente.servis2.repository.PricingRepository;
import com.komponente.servis2.repository.TrainingTypeRepository;
import com.komponente.servis2.service.LoyaltyService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {
    private GymRepository gymRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private PricingRepository pricingRepository;
    @Autowired
    private LoyaltyService loyaltyService;

    public TestDataRunner(GymRepository gymRepository, TrainingTypeRepository trainingTypeRepository, PricingRepository pricingRepository) {
        this.gymRepository = gymRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.pricingRepository = pricingRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Gym gym = new Gym();
        gym.setName("Sala 1");
        gym.setDescription("Sala za trening");
        gym.setManagerId(1L);
        gym.setNumberOfTrainers(5);
        gym = gymRepository.save(gym);

        TrainingType powerlifting = new TrainingType("Powerlifting", false);
        TrainingType kalisetnics = new TrainingType("Kalistetika", false);
        TrainingType pilates = new TrainingType("Pilates", true);
        TrainingType yoga = new TrainingType("Yoga", true);


        powerlifting = trainingTypeRepository.save(powerlifting); // error here
        yoga = trainingTypeRepository.save(yoga);
        kalisetnics = trainingTypeRepository.save(kalisetnics);
        pilates = trainingTypeRepository.save(pilates);

        gym.getTrainingTypes().add(powerlifting);
        gym.getTrainingTypes().add(yoga);
        gym.getTrainingTypes().add(kalisetnics);
        gym.getTrainingTypes().add(pilates);
        gymRepository.save(gym);


        Pricing powerliftingPricing = new Pricing();
        powerliftingPricing.setTrainingType(powerlifting);
        powerliftingPricing.setPrice(1000);
        pricingRepository.save(powerliftingPricing);

        Pricing yogaPricing = new Pricing();
        yogaPricing.setTrainingType(yoga);
        yogaPricing.setPrice(500);
        pricingRepository.save(yogaPricing);

        Pricing kalisetnicsPricing = new Pricing();
        kalisetnicsPricing.setTrainingType(kalisetnics);
        kalisetnicsPricing.setPrice(500);
        pricingRepository.save(kalisetnicsPricing);

        Pricing pilatesPricing = new Pricing();
        pilatesPricing.setTrainingType(pilates);
        pilatesPricing.setPrice(750);
        pricingRepository.save(pilatesPricing);

        loyaltyService.addReward(gym, 3L, "Popust od 10% na svaki treci trening", 10L);
    }
}
