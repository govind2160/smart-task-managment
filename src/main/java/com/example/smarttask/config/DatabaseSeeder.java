package com.example.smarttask.config;

import com.example.smarttask.entity.*;
import com.example.smarttask.repository.ProjectRepository;
import com.example.smarttask.repository.TaskRepository;
import com.example.smarttask.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DatabaseSeeder(UserRepository userRepository, ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Seed Users (12 regular Indian users)
            User aarav = new User("Aarav Sharma", "aarav.sharma@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User aditi = new User("Aditi Patel", "aditi.patel@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User rahul = new User("Rahul Verma", "rahul.verma@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User priya = new User("Priya Iyer", "priya.iyer@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User ishaan = new User("Ishaan Gupta", "ishaan.gupta@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User ananya = new User("Ananya Sen", "ananya.sen@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User kabir = new User("Kabir Malhotra", "kabir.malhotra@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User diya = new User("Diya Joshi", "diya.joshi@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User arjun = new User("Arjun Rao", "arjun.rao@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User kavya = new User("Kavya Nair", "kavya.nair@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User rohan = new User("Rohan Mehta", "rohan.mehta@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User meera = new User("Meera Reddy", "meera.reddy@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User vivaan = new User("Vivaan Saxena", "vivaan.saxena@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User shruti = new User("Shruti Choudhury", "shruti.choudhury@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);

            // Seed Administrators (2 Indian admins)
            User amit = new User("Amit Sharma", "amit.sharma@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_ADMIN);
            User neha = new User("Neha Gupta", "neha.gupta@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_ADMIN);

            aarav = userRepository.save(aarav);
            aditi = userRepository.save(aditi);
            rahul = userRepository.save(rahul);
            priya = userRepository.save(priya);
            ishaan = userRepository.save(ishaan);
            ananya = userRepository.save(ananya);
            kabir = userRepository.save(kabir);
            diya = userRepository.save(diya);
            arjun = userRepository.save(arjun);
            kavya = userRepository.save(kavya);
            rohan = userRepository.save(rohan);
            meera = userRepository.save(meera);
            vivaan = userRepository.save(vivaan);
            shruti = userRepository.save(shruti);
            amit = userRepository.save(amit);
            neha = userRepository.save(neha);

            // Seed Projects (14 projects total)
            // Seed Projects (14 projects total)
            Project collegePortal = new Project("College Portal", "Web app for managing college task assignments.", ProjectStatus.ACTIVE, aarav);
            collegePortal.setDeadline(LocalDate.parse("2026-07-30"));

            Project smartTaskApp = new Project("Smart Task App", "Spring Boot backend task management backend.", ProjectStatus.PLANNED, aditi);
            smartTaskApp.setDeadline(LocalDate.parse("2026-08-15"));
            
            Project mobileApp = new Project("Mobile App Dev", "Cross-platform mobile client built with React Native.", ProjectStatus.ACTIVE, aarav);
            mobileApp.setDeadline(LocalDate.parse("2026-09-01"));
            mobileApp.getMembers().add(rahul);

            Project marketingCampaign = new Project("Marketing Campaign", "Launch campaign for the new product features.", ProjectStatus.ACTIVE, aditi);
            marketingCampaign.setDeadline(LocalDate.parse("2026-07-15"));
            marketingCampaign.getMembers().add(priya);
            marketingCampaign.getMembers().add(aarav);

            Project eCommerce = new Project("E-Commerce Webapp", "Online retail storefront with billing integrated.", ProjectStatus.ACTIVE, aarav);
            eCommerce.setDeadline(LocalDate.parse("2026-08-01"));
            eCommerce.getMembers().add(ananya);

            Project inventorySystem = new Project("Inventory System", "Enterprise storage and logistics inventory manager.", ProjectStatus.PLANNED, aditi);
            inventorySystem.setDeadline(LocalDate.parse("2026-09-15"));
            inventorySystem.getMembers().add(diya);

            Project hrPortal = new Project("HR Portal", "Internal staff recruitment and directory system.", ProjectStatus.ACTIVE, amit);
            hrPortal.setDeadline(LocalDate.parse("2026-08-20"));
            hrPortal.getMembers().add(kavya);

            Project crmDashboard = new Project("CRM Dashboard", "Customer relationship analytics dashboard.", ProjectStatus.ACTIVE, neha);
            crmDashboard.setDeadline(LocalDate.parse("2026-07-25"));
            crmDashboard.getMembers().add(meera);

            Project aiChatbot = new Project("AI Chatbot", "Intelligent customer service automated chatbot agent.", ProjectStatus.PLANNED, rahul);
            aiChatbot.setDeadline(LocalDate.parse("2026-10-01"));
            
            Project dataPipeline = new Project("Data Pipeline", "Real-time big data ETL streaming pipeline.", ProjectStatus.ACTIVE, ishaan);
            dataPipeline.setDeadline(LocalDate.parse("2026-08-30"));

            Project devOpsAuto = new Project("DevOps Automation", "CI/CD automated release pipeline orchestration.", ProjectStatus.ACTIVE, arjun);
            devOpsAuto.setDeadline(LocalDate.parse("2026-07-10"));

            Project billingEngine = new Project("Billing Engine", "Subscription billing plans and invoicing scheduler.", ProjectStatus.PLANNED, rohan);
            billingEngine.setDeadline(LocalDate.parse("2026-09-30"));
            billingEngine.getMembers().add(shruti);

            Project analyticsTool = new Project("Analytics Tool", "Data aggregation and visualization platform.", ProjectStatus.ACTIVE, vivaan);
            analyticsTool.setDeadline(LocalDate.parse("2026-08-05"));

            Project mobileWallet = new Project("Mobile Wallet", "Secure payments and digital wallet application.", ProjectStatus.ACTIVE, priya);
            mobileWallet.setDeadline(LocalDate.parse("2026-07-20"));
            mobileWallet.getMembers().add(kabir);

            Project swachhBharat = new Project("Swachh Bharat Cleanliness", "Community cleanliness campaign and waste tracking dashboard.", ProjectStatus.ACTIVE, aarav);
            swachhBharat.setDeadline(LocalDate.parse("2026-07-25"));
            swachhBharat.getMembers().add(aditi);

            Project farmerApp = new Project("Digital India Farmer App", "Portal for direct crop selling and weather updates for farmers.", ProjectStatus.ACTIVE, rahul);
            farmerApp.setDeadline(LocalDate.parse("2026-08-10"));
            farmerApp.getMembers().add(priya);
            farmerApp.getMembers().add(ishaan);

            Project roverSim = new Project("Chandrayaan Rover Sim", "Lunar terrain traversal simulation and telemetry viewer.", ProjectStatus.PLANNED, ishaan);
            roverSim.setDeadline(LocalDate.parse("2026-10-15"));
            roverSim.getMembers().add(arjun);

            Project handloomStore = new Project("Mitti Handloom Store", "E-commerce platform for rural Indian artisans and weavers.", ProjectStatus.ACTIVE, priya);
            handloomStore.setDeadline(LocalDate.parse("2026-09-05"));
            handloomStore.getMembers().add(ananya);

            collegePortal = projectRepository.save(collegePortal);
            smartTaskApp = projectRepository.save(smartTaskApp);
            mobileApp = projectRepository.save(mobileApp);
            marketingCampaign = projectRepository.save(marketingCampaign);
            eCommerce = projectRepository.save(eCommerce);
            inventorySystem = projectRepository.save(inventorySystem);
            hrPortal = projectRepository.save(hrPortal);
            crmDashboard = projectRepository.save(crmDashboard);
            aiChatbot = projectRepository.save(aiChatbot);
            dataPipeline = projectRepository.save(dataPipeline);
            devOpsAuto = projectRepository.save(devOpsAuto);
            billingEngine = projectRepository.save(billingEngine);
            analyticsTool = projectRepository.save(analyticsTool);
            mobileWallet = projectRepository.save(mobileWallet);
            swachhBharat = projectRepository.save(swachhBharat);
            farmerApp = projectRepository.save(farmerApp);
            roverSim = projectRepository.save(roverSim);
            handloomStore = projectRepository.save(handloomStore);

            // Seed Tasks (17 tasks total)
            Task task1 = new Task("Setup Schema", TaskStatus.COMPLETED, collegePortal, aarav, aarav);
            task1.setDeadline(LocalDate.parse("2026-06-10"));
            task1.setCreatedAt(LocalDateTime.of(2026, 6, 8, 10, 0, 0));

            Task task2 = new Task("Connect Database", TaskStatus.IN_PROGRESS, collegePortal, aarav, aarav);
            task2.setDeadline(LocalDate.parse("2026-06-25"));
            task2.setCreatedAt(LocalDateTime.of(2026, 6, 8, 11, 30, 0));

            Task task3 = new Task("Review Relationships", TaskStatus.PENDING, smartTaskApp, aditi, aditi);
            task3.setDeadline(LocalDate.parse("2026-06-12"));
            task3.setCreatedAt(LocalDateTime.of(2026, 6, 9, 9, 0, 0));

            Task task4 = new Task("Design Wireframes", TaskStatus.COMPLETED, mobileApp, aarav, rahul);
            task4.setDeadline(LocalDate.parse("2026-06-15"));
            task4.setCreatedAt(LocalDateTime.of(2026, 6, 5, 9, 0, 0));

            Task task5 = new Task("Setup Push Notifications", TaskStatus.IN_PROGRESS, mobileApp, aarav, aarav);
            task5.setDeadline(LocalDate.parse("2026-06-28"));
            task5.setCreatedAt(LocalDateTime.of(2026, 6, 6, 14, 0, 0));

            Task task6 = new Task("Draft Press Release", TaskStatus.PENDING, marketingCampaign, aditi, priya);
            task6.setDeadline(LocalDate.parse("2026-06-20"));
            task6.setCreatedAt(LocalDateTime.of(2026, 6, 11, 10, 0, 0));

            Task task7 = new Task("Social Media Graphics", TaskStatus.COMPLETED, marketingCampaign, aditi, aditi);
            task7.setDeadline(LocalDate.parse("2026-06-14"));
            task7.setCreatedAt(LocalDateTime.of(2026, 6, 10, 11, 0, 0));

            Task task8 = new Task("Setup Payment Gateway", TaskStatus.COMPLETED, eCommerce, aarav, ananya);
            task8.setDeadline(LocalDate.parse("2026-06-18"));
            task8.setCreatedAt(LocalDateTime.of(2026, 6, 10, 15, 0, 0));

            Task task9 = new Task("Inventory Schema Design", TaskStatus.PENDING, inventorySystem, aditi, diya);
            task9.setDeadline(LocalDate.parse("2026-06-24"));
            task9.setCreatedAt(LocalDateTime.of(2026, 6, 11, 16, 0, 0));

            Task task10 = new Task("Employee Onboarding Flow", TaskStatus.IN_PROGRESS, hrPortal, amit, kavya);
            task10.setDeadline(LocalDate.parse("2026-06-30"));
            task10.setCreatedAt(LocalDateTime.of(2026, 6, 12, 10, 0, 0));

            Task task11 = new Task("Sales Pipeline Charts", TaskStatus.COMPLETED, crmDashboard, neha, meera);
            task11.setDeadline(LocalDate.parse("2026-06-16"));
            task11.setCreatedAt(LocalDateTime.of(2026, 6, 9, 11, 0, 0));

            Task task12 = new Task("Integrate NLP Model", TaskStatus.PENDING, aiChatbot, rahul, rahul);
            task12.setDeadline(LocalDate.parse("2026-07-02"));
            task12.setCreatedAt(LocalDateTime.of(2026, 6, 14, 13, 0, 0));

            Task task13 = new Task("Kafka Cluster Setup", TaskStatus.IN_PROGRESS, dataPipeline, ishaan, ishaan);
            task13.setDeadline(LocalDate.parse("2026-06-29"));
            task13.setCreatedAt(LocalDateTime.of(2026, 6, 13, 14, 0, 0));

            Task task14 = new Task("Terraform Scripts", TaskStatus.COMPLETED, devOpsAuto, arjun, arjun);
            task14.setDeadline(LocalDate.parse("2026-06-12"));
            task14.setCreatedAt(LocalDateTime.of(2026, 6, 4, 9, 0, 0));

            Task task15 = new Task("Stripe API Integration", TaskStatus.PENDING, billingEngine, rohan, shruti);
            task15.setDeadline(LocalDate.parse("2026-06-27"));
            task15.setCreatedAt(LocalDateTime.of(2026, 6, 15, 10, 30, 0));

            Task task16 = new Task("Export PDF Reports", TaskStatus.IN_PROGRESS, analyticsTool, vivaan, vivaan);
            task16.setDeadline(LocalDate.parse("2026-06-26"));
            task16.setCreatedAt(LocalDateTime.of(2026, 6, 14, 11, 0, 0));

            Task task17 = new Task("Biometric Auth", TaskStatus.COMPLETED, mobileWallet, priya, kabir);
            task17.setDeadline(LocalDate.parse("2026-06-15"));
            task17.setCreatedAt(LocalDateTime.of(2026, 6, 7, 9, 30, 0));

            Task task18 = new Task("Organize Local Volunteer Drive", TaskStatus.COMPLETED, swachhBharat, aarav, aditi);
            task18.setDeadline(LocalDate.parse("2026-06-12"));
            task18.setCreatedAt(LocalDateTime.of(2026, 6, 8, 9, 0, 0));

            Task task19 = new Task("Translate UI to Hindi and Tamil", TaskStatus.IN_PROGRESS, farmerApp, rahul, priya);
            task19.setDeadline(LocalDate.parse("2026-07-05"));
            task19.setCreatedAt(LocalDateTime.of(2026, 6, 12, 10, 0, 0));

            Task task20 = new Task("Optimize Pathfinding Algorithms", TaskStatus.PENDING, roverSim, ishaan, arjun);
            task20.setDeadline(LocalDate.parse("2026-07-20"));
            task20.setCreatedAt(LocalDateTime.of(2026, 6, 15, 14, 30, 0));

            Task task21 = new Task("Onboard Weaver Cooperatives", TaskStatus.IN_PROGRESS, handloomStore, priya, ananya);
            task21.setDeadline(LocalDate.parse("2026-06-30"));
            task21.setCreatedAt(LocalDateTime.of(2026, 6, 10, 11, 0, 0));

            taskRepository.save(task1);
            taskRepository.save(task2);
            taskRepository.save(task3);
            taskRepository.save(task4);
            taskRepository.save(task5);
            taskRepository.save(task6);
            taskRepository.save(task7);
            taskRepository.save(task8);
            taskRepository.save(task9);
            taskRepository.save(task10);
            taskRepository.save(task11);
            taskRepository.save(task12);
            taskRepository.save(task13);
            taskRepository.save(task14);
            taskRepository.save(task15);
            taskRepository.save(task16);
            taskRepository.save(task17);
            taskRepository.save(task18);
            taskRepository.save(task19);
            taskRepository.save(task20);
            taskRepository.save(task21);

            System.out.println("====================================================================");
            System.out.println("Database seeded successfully with default users, projects, and tasks.");
            System.out.println("====================================================================");
        }
    }
}
