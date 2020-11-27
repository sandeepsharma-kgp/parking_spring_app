package com.example.parking;

public interface ParkingService {
    ParkingCommand findCommand(String line);

    String executeCommand(ParkingCommand parkingCommand);
}
