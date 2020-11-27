package com.example.parking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {

    ParkingTicket findBySlotNo(Integer slotNo);

    List<ParkingTicket> findByDriverAge(Integer driverAge);

    ParkingTicket findByVehicleRegNo(String regNo);
}
