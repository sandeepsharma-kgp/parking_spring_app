package com.example.parking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    ParkingTicketRepository parkingTicketRepository;

    @Autowired
    ParkingSlotHelper parkingSlotHelper;

    @Override
    public ParkingCommand findCommand(String line) {
        Pattern createPattern = Pattern.compile("Create_parking_lot\\s+(.*[^0-9]|)(1000|[1-9]\\d{0,2})([^0-9].*|)$");
        Pattern parkPattern = Pattern.compile("Park\\s+([A-Z]{2}-[0-9]{2}-[A-Z]{2}-[0-9]{4})\\s+driver_age\\s+([1-9]\\d{0,1})$");
        Pattern getSlotByDriverAgePattern = Pattern.compile("Slot_numbers_for_driver_of_age\\s+([1-9]\\d{0,1})$");
        Pattern getSlotByCarNoPattern = Pattern.compile("Slot_number_for_car_with_number\\s+([A-Z]{2}-[0-9]{2}-[A-Z]{2}-[0-9]{4})$");
        Pattern leavePattern = Pattern.compile("Leave\\s+(.*[^0-9]|)(1000|[1-9]\\d{0,2})([^0-9].*|)$");
        Pattern getVehicleRegForDriverAgePattern = Pattern.compile("Vehicle_registration_number_for_driver_of_age\\s+([1-9]\\d{0,1})$");
        Matcher createPatternMatcher = createPattern.matcher(line);
        Matcher parkPatternMatcher = parkPattern.matcher(line);
        Matcher getSlotByDriverAgePatternMatcher = getSlotByDriverAgePattern.matcher(line);
        Matcher getSlotByRegNoPatternMatcher = getSlotByCarNoPattern.matcher(line);
        Matcher leavePatternMatcher = leavePattern.matcher(line);
        Matcher getVehicleRegForDriverAgePatternMatcher = getVehicleRegForDriverAgePattern.matcher(line);

        ParkingCommand parkingCommand = new ParkingCommand();
        parkingCommand.setCommand(ParkingCommand.Command.INVALID);

        if(createPatternMatcher.find()) {
            parkingCommand.setCommand(ParkingCommand.Command.CREATE);
            parkingCommand.setTotalSlots(Integer.valueOf(createPatternMatcher.group(2)));
        }
        if(parkPatternMatcher.find()) {
            parkingCommand.setCommand(ParkingCommand.Command.PARK);
            parkingCommand.setRegNo(parkPatternMatcher.group(1));
            parkingCommand.setDriverAge(Integer.valueOf(parkPatternMatcher.group(2)));
        }
        if(getSlotByDriverAgePatternMatcher.find()) {
            parkingCommand.setCommand(ParkingCommand.Command.GET_SLOT_BY_AGE);
            parkingCommand.setDriverAge(Integer.valueOf(getSlotByDriverAgePatternMatcher.group(1)));
        }
        if(getSlotByRegNoPatternMatcher.find()) {
            parkingCommand.setCommand(ParkingCommand.Command.GET_SLOT_BY_REG_NO);
            parkingCommand.setRegNo(getSlotByRegNoPatternMatcher.group(1));
        }
        if(leavePatternMatcher.find()) {
            parkingCommand.setCommand(ParkingCommand.Command.LEAVE_SLOT);
            parkingCommand.setSlot(Integer.valueOf(leavePatternMatcher.group(2)));
        }
        if(getVehicleRegForDriverAgePatternMatcher.find()) {
            parkingCommand.setCommand(ParkingCommand.Command.GET_REG_NO_BY_AGE);
            parkingCommand.setDriverAge(Integer.valueOf(getVehicleRegForDriverAgePatternMatcher.group(1)));
        }

        return  parkingCommand;
    }

    @Override
    public String executeCommand(ParkingCommand parkingCommand) {
        switch (parkingCommand.getCommand()) {
            case CREATE:
                return cleanAndCreateParkingSpace(parkingCommand.getTotalSlots());
            case PARK:
                return parkTheVehicle(parkingCommand.getRegNo(),parkingCommand.getDriverAge());
            case GET_SLOT_BY_AGE:
                return getAllSlotsByDriverAge(parkingCommand.getDriverAge());
            case GET_SLOT_BY_REG_NO:
                return getSlotByRegNo(parkingCommand.getRegNo());
            case LEAVE_SLOT:
                return clearSlot(parkingCommand.getSlot());
            case GET_REG_NO_BY_AGE:
                return getAllRegNoByDriverAge(parkingCommand.getDriverAge());
            default:
                return "Command not valid!!";
        }
    }

    private String getAllRegNoByDriverAge(Integer driverAge) {
        List<ParkingTicket> ticketsByDriverAge = parkingTicketRepository.findByDriverAge(driverAge);
        if(ticketsByDriverAge.isEmpty())
            return null;
        StringBuilder regNos = new StringBuilder();
        for(int i=0;i<ticketsByDriverAge.size();i++) {
            regNos.append(ticketsByDriverAge.get(i).getSlotNo());
            if(i<ticketsByDriverAge.size()-1)
                regNos.append(",");
        }
        return regNos.toString();
    }

    private String clearSlot(Integer slot) {
        ParkingTicket parkingTicket = parkingTicketRepository.findBySlotNo(slot);
        parkingTicketRepository.delete(parkingTicket);
        parkingSlotHelper.addSlotToMinHeap(slot);
        return "Slot number " +parkingTicket.getSlotNo() + " vacated," +
                " the car with vehicle registration number " + parkingTicket.getVehicleRegNo() +
                " left the space," +
                " the driver of the car was of age " + parkingTicket.getDriverAge();
    }

    private String getSlotByRegNo(String regNo) {
        ParkingTicket parkingTicket = parkingTicketRepository.findByVehicleRegNo(regNo);
        if(parkingTicket==null)
            return null;
        return parkingTicket.getSlotNo().toString();
    }

    private String getAllSlotsByDriverAge(Integer driverAge) {
        StringBuilder slots = new StringBuilder();
        List<ParkingTicket> ticketsByDriverAge = parkingTicketRepository.findByDriverAge(driverAge);
        if(ticketsByDriverAge.isEmpty())
            return null;
        for(int i=0;i<ticketsByDriverAge.size();i++) {
            slots.append(ticketsByDriverAge.get(i).getSlotNo());
            if(i<ticketsByDriverAge.size()-1)
                slots.append(",");
        }
        return slots.toString();
    }

    private String parkTheVehicle(String regNo, Integer driverAge) {
        Integer slot = parkingSlotHelper.getMinSlotNo();
        if(slot!=null) {
            parkingSlotHelper.removeSlotFromHeap(slot);
            return createParkingTicket(regNo,driverAge,slot);
        }
        return "Slot not available";
    }

    private String createParkingTicket(String regNo, Integer driverAge, Integer slot) {
        ParkingTicket ticketBySlotNo = parkingTicketRepository.findBySlotNo(slot);
        if(ticketBySlotNo != null)
            return slot + " already alloted";
        ParkingTicket parkingTicket = new ParkingTicket();
        parkingTicket.setVehicleRegNo(regNo);
        parkingTicket.setDriverAge(driverAge);
        parkingTicket.setSlotNo(slot);
        parkingTicketRepository.save(parkingTicket);
        return "Car with vehicle registration number " + parkingTicket.getVehicleRegNo()
                + " has been parked at slot number " + slot;
    }

    String cleanAndCreateParkingSpace(int totalSlots) {
        parkingTicketRepository.deleteAll();
        parkingSlotHelper.cleanAndCreateParkingSlot(totalSlots);
        return "Created parking of " + totalSlots + " slots";
    }
}
