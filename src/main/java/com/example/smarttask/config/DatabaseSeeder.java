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
            // Seed Users (4 regular Indian users + 1 admin)
            User aarav = new User("Aarav Sharma", "aarav.sharma@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User aditi = new User("Aditi Patel", "aditi.patel@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User rahul = new User("Rahul Verma", "rahul.verma@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User priya = new User("Priya Iyer", "priya.iyer@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_USER);
            User amit = new User("Amit Sharma", "amit.sharma@example.com", "$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.", Role.ROLE_ADMIN);

            aarav = userRepository.save(aarav);
            aditi = userRepository.save(aditi);
            rahul = userRepository.save(rahul);
            priya = userRepository.save(priya);
            amit = userRepository.save(amit);

            // Seed Projects (4 projects total)
            Project swachhBharat = new Project("Swachh Bharat Cleanliness", "Community cleanliness campaign and waste tracking dashboard.", ProjectStatus.ACTIVE, aarav);
            swachhBharat.setDeadline(LocalDate.parse("2026-07-25"));
            swachhBharat.getMembers().add(aditi);

            Project farmerApp = new Project("Digital India Farmer App", "Portal for direct crop selling and weather updates for farmers.", ProjectStatus.ACTIVE, rahul);
            farmerApp.setDeadline(LocalDate.parse("2026-08-10"));
            farmerApp.getMembers().add(priya);

            Project roverSim = new Project("Chandrayaan Rover Sim", "Lunar terrain traversal simulation and telemetry viewer.", ProjectStatus.PLANNED, amit);
            roverSim.setDeadline(LocalDate.parse("2026-10-15"));
            roverSim.getMembers().add(aarav);

            Project handloomStore = new Project("Mitti Handloom Store", "E-commerce platform for rural Indian artisans and weavers.", ProjectStatus.ACTIVE, priya);
            handloomStore.setDeadline(LocalDate.parse("2026-09-05"));
            handloomStore.getMembers().add(rahul);

            swachhBharat = projectRepository.save(swachhBharat);
            farmerApp = projectRepository.save(farmerApp);
            roverSim = projectRepository.save(roverSim);
            handloomStore = projectRepository.save(handloomStore);

            // Seed Tasks (6 tasks total)
            Task task1 = new Task("Organize Local Volunteer Drive", TaskStatus.COMPLETED, swachhBharat, aarav, aditi);
            task1.setDeadline(LocalDate.parse("2026-06-12"));
            task1.setCreatedAt(LocalDateTime.of(2026, 6, 8, 9, 0, 0));

            Task task2 = new Task("Translate UI to Hindi and Tamil", TaskStatus.IN_PROGRESS, farmerApp, rahul, priya);
            task2.setDeadline(LocalDate.parse("2026-07-05"));
            task2.setCreatedAt(LocalDateTime.of(2026, 6, 12, 10, 0, 0));

            Task task3 = new Task("Optimize Pathfinding Algorithms", TaskStatus.PENDING, roverSim, amit, aarav);
            task3.setDeadline(LocalDate.parse("2026-07-20"));
            task3.setCreatedAt(LocalDateTime.of(2026, 6, 15, 14, 30, 0));

            Task task4 = new Task("Onboard Weaver Cooperatives", TaskStatus.IN_PROGRESS, handloomStore, priya, rahul);
            task4.setDeadline(LocalDate.parse("2026-06-30"));
            task4.setCreatedAt(LocalDateTime.of(2026, 6, 10, 11, 0, 0));

            Task task5 = new Task("Design Crop Selling Interface", TaskStatus.COMPLETED, farmerApp, rahul, rahul);
            task5.setDeadline(LocalDate.parse("2026-06-18"));
            task5.setCreatedAt(LocalDateTime.of(2026, 6, 10, 15, 0, 0));

            Task task6 = new Task("Procure waste collection equipment", TaskStatus.PENDING, swachhBharat, aarav, aarav);
            task6.setDeadline(LocalDate.parse("2026-06-24"));
            task6.setCreatedAt(LocalDateTime.of(2026, 6, 11, 16, 0, 0));

            taskRepository.save(task1);
            taskRepository.save(task2);
            taskRepository.save(task3);
            taskRepository.save(task4);
            taskRepository.save(task5);
            taskRepository.save(task6);

            System.out.println("====================================================================");
            System.out.println("Database seeded successfully with reduced Indian sample data.");
            System.out.println("====================================================================");
        }
    }
}
