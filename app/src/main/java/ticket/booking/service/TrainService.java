package ticket.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.stream.IntStream;

public class TrainService {

    List<Train> trains;

    private String TRAINS_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";

    private ObjectMapper objectMapper = new ObjectMapper();

    public TrainService() throws IOException {
        loadTrains();
    }

    private void loadTrains() throws IOException{
        File file = new File(TRAINS_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        trains = objectMapper.readValue(file, new TypeReference<List<Train>>() {});
    }

    public List<Train> findTrains(String sourceStation, String destinationStation) {
        return trains.stream().filter(train -> validTrain(train, sourceStation, destinationStation)).collect(Collectors.toList());
    }

    private Boolean validTrain(Train train, String sourceStation, String destinationStation) {
        List<String> stations = train.getStations();

        int sourceIndex = stations.indexOf(sourceStation);
        int destinationIndex = stations.indexOf(destinationStation);

        return sourceIndex!=-1 && destinationIndex!=-1 && sourceIndex < destinationIndex;
    }

    public void addTrain(Train newTrain) throws IOException {
        // check if train already exists
        Optional<Train> existingTrain = trains.stream().filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();
        if(existingTrain.isPresent()){
            // update existing train details
            updateTrain(newTrain);
            saveTrainsToFile();
        }else{
            // add new train to the list
            trains.add(newTrain);
            saveTrainsToFile();
        }
    }

    private void saveTrainsToFile() {
        try {
            File trainsFile = new File(TRAINS_PATH);
            objectMapper.writeValue(trainsFile, trains);
        }catch (IOException e) {
            System.out.println("Error saving trains to file: " + e.getMessage());
        }
    }

    private void updateTrain(Train newTrain) throws IOException {
        // Find the index of the existing train
        OptionalInt indexOpt = IntStream.range(0, trains.size())
                .filter(i -> trains.get(i).getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if(indexOpt.isPresent()){
            // Update the existing train details
            trains.set(indexOpt.getAsInt(), newTrain);
            saveTrainsToFile();
        }else {
            addTrain(newTrain);
        }
    }

}
