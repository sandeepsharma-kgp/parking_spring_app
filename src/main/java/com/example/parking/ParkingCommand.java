package com.example.parking;


public class ParkingCommand {

    private Integer slot;
    private String regNo;
    private Integer driverAge;
    private Integer totalSlots;
    private Command command;

    public enum Command {
        CREATE,
        PARK,
        GET_SLOT_BY_AGE,
        GET_SLOT_BY_REG_NO,
        LEAVE_SLOT,
        GET_REG_NO_BY_AGE,
        INVALID
    }

    @Override
    public String toString() {
        return "ParkingCommand{" +
                "slot=" + slot +
                ", regNo='" + regNo + '\'' +
                ", driverAge=" + driverAge +
                ", totalSlots=" + totalSlots +
                ", command=" + command +
                '}';
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public Integer getDriverAge() {
        return driverAge;
    }

    public void setDriverAge(Integer driverAge) {
        this.driverAge = driverAge;
    }

    public Integer getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
