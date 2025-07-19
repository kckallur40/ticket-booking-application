package ticket.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;

    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService() throws IOException {
        loadUser();
    }

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUser();
    }

    private void loadUser() throws IOException {
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public Boolean loginUser(){

        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equalsIgnoreCase(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getPassword());
        }).findFirst();

        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
        }catch (IOException e){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void saveUserListToFile() throws IOException{
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public void fetchBooking(){
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId) throws IOException {

        List<Ticket> ticketList = user.getTicketsBooked();
        Optional<Ticket> ticketToCancel = ticketList.stream().filter(ticket -> ticketId.equals(ticket.getTicketId())).findFirst();
        if(ticketToCancel.isPresent()){
            // found ticket
            Ticket ticketToRemoveFromList = ticketToCancel.get();
            ticketList.remove(ticketToRemoveFromList);
            userList.stream().filter(user1 -> user1.equals(user)).findFirst().get().setTicketsBooked(ticketList);
            saveUserListToFile();
            return Boolean.TRUE;
        }else{
            // not found
            return Boolean.FALSE;
        }
    }

    public Boolean bookSeat(Train train, int row, int col){
        try{
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if(row >=0 && row < seats.size() && col>=0 && col<seats.get(row).size()){
                if(seats.get(row).get(col) == 0){
                    // seat is available
                    seats.get(row).set(col, 1); // mark as booked
                    train.setSeats(seats);
                    trainService.addTrain(train); // update train in the service
                    return Boolean.TRUE;
                }else{
                    System.out.println("Seat already booked.");
                    return Boolean.FALSE;
                }
            }else{
                System.out.println("Invalid row or column.");
                return Boolean.FALSE; // invalid row or column
            }

        }catch (IOException e){
            System.out.println("Error while booking seat: " + e.getMessage());
            return Boolean.FALSE;
        }
    }

    public List<Train> searchTrains(String sourceStation, String destinationStation) {
        try{
            TrainService trainService = new TrainService();
            return trainService.findTrains(sourceStation, destinationStation);
        }catch (Exception e){
            System.out.println("Error while searching trains: " + e.getMessage());
        }

        return null;
    }
}






















