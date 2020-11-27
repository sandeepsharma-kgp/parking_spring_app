package com.example.parking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
public class ParkingController {

    @Autowired
    ParkingService parkingService;

    @GetMapping("/parking")
    public ResponseEntity<Object> getDetails(@RequestParam String filepath) {
        BufferedReader input;
        String line = "";
        StringBuilder result = new StringBuilder("");
        try {
            input = new BufferedReader(new FileReader(filepath));
            line = input.readLine();
            while (line != null) {
                ParkingCommand parkingCommand = parkingService.findCommand(line);
                result.append(parkingService.executeCommand(parkingCommand)).append("\r\n");
                // read next line
                line = input.readLine();
            }
            input.close();
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            out.append(result);
            out.close();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
