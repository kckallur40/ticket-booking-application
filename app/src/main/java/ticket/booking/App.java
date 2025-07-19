package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.service.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.util.*;

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
                Train trainSelectedForBooking = new Train();
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
                        int index = 1;
                        for(Train train : trains) {
                            System.out.println(index+" Train ID: "+train.getTrainId());
                            for(Map.Entry<String,String> entry: train.getStationTimes().entrySet()){
                                System.out.println("Station: " + entry.getKey() + ", Time: " + entry.getValue());
                            }
                        }
                        System.out.println("Select the train ID to book:");
                        trainSelectedForBooking = trains.get(scn.nextInt() - 1);
                        break;
                    case 5:
                        System.out.println("Select the seat...");
                        List<List<Integer>> seats = trainSelectedForBooking.getSeats();
                        for(List<Integer> seatRow : seats) {
                            for(Integer seat : seatRow) {
                                System.out.print(seat + " ");
                            }
                            System.out.println();
                        }
                        System.out.println("Enter the seat number to book:");
                        System.out.println("Enter row");
                        int row = scn.nextInt();
                        System.out.println("Enter column");
                        int column = scn.nextInt();
                        System.out.println("Booking seat at row " + row + ", column " + column + "...");
                        Boolean bookingStatus = userBookingService.bookSeat(trainSelectedForBooking, row, column);
                        if(bookingStatus) {
                            System.out.println("Seat booked successfully!");
                        } else {
                            System.out.println("Failed to book seat. Please try again.");
                        }
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