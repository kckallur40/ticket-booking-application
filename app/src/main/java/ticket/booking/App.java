package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.service.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class App {

    public static void main(String[] args) {
        System.out.println("Running Ticket Booking Application...");
        UserBookingService userBookingService;
        Scanner scn = new Scanner(System.in);
        try {
            userBookingService = new UserBookingService();
            int count = 0;
            while (count != 7) {
                System.out.println("1. Register User");
                System.out.println("2. Login User");
                System.out.println("3. Fetch Bookings");
                System.out.println("4. Search Trains");
                System.out.println("5. Book a seat");
                System.out.println("6. Cancel my booking");
                System.out.println("7. Exit");
                count = scn.nextInt();
                scn.nextLine(); // Consume newline

                switch (count) {
                    case 1:
                        System.out.println("Enter your name:");
                        String name = scn.nextLine();
                        System.out.println("Enter your password:");
                        String password = scn.nextLine();
                        userBookingService.signUp(new User(name, password, UserServiceUtil.hashPassword(password), new ArrayList<>(), UUID.randomUUID().toString()));
                        System.out.println("User registered successfully!");
                        break;
                    case 2:
                        System.out.println("Enter your name:");
                        String loginName = scn.nextLine();
                        System.out.println("Enter your password:");
                        String loginPassword = scn.nextLine();
                        User user = new User(loginName, loginPassword, UserServiceUtil.hashPassword(loginPassword), new ArrayList<>(), UUID.randomUUID().toString());
                        userBookingService = new UserBookingService(user);
                        break;
                    case 3:
                        System.out.println("Fetching bookings...");
                        userBookingService.fetchBooking();
                        break;
                    case 4:
                        System.out.println("Type the source station:");
                        String sourceStation = scn.nextLine();
                        System.out.println("Type the destination station:");
                        String destinationStation = scn.nextLine();
                        System.out.println("Searching trains from " + sourceStation + " to " + destinationStation + "...");
                        List<Train> trains = userBookingService.searchTrains(sourceStation, destinationStation);
                        System.out.println("Trains found: "+ trains.size());
                        System.out.println("Available trains: "+ trains);
                        break;
                    case 5:
                        // Call view tickets method
                        break;
                    case 6:
                        count = 7; // Exit the loop
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}