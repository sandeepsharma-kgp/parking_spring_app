package com.example.parking;

import javax.persistence.*;

@Entity
@Table(name = "parking_ticket")
public class ParkingTicket {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "slot_no")
    private Integer slotNo;

    @Column(name = "driver_age")
    private Integer driverAge;

    @Column(name = "vehicle_reg_no")
    private String vehicleRegNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getDriverAge() {
        return driverAge;
    }

    public void setDriverAge(Integer driverAge) {
        this.driverAge = driverAge;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo) {
        this.vehicleRegNo = vehicleRegNo;
    }
}
