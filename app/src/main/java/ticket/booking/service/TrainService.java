package ticket.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {

    List<Train> trains;

    public TrainService() throws IOException {
        loadTrains();
    }

    private void loadTrains() throws IOException{
        File file = new File("app/src/main/java/ticket/booking/localDb/trains.json");
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
}
