package com.example.parking;

import org.springframework.stereotype.Service;

import java.util.PriorityQueue;

@Service
public class ParkingSlotHelper {

    PriorityQueue<Integer> parkingPriorityQueue;

    void cleanAndCreateParkingSlot(int size) {
        parkingPriorityQueue = new PriorityQueue<>();
        for(int i=1;i<=size;i++)
            parkingPriorityQueue.add(i);
    }

    public Integer getMinSlotNo() {
        return parkingPriorityQueue.peek();
    }

    public void removeSlotFromHeap(Integer slot) {
        parkingPriorityQueue.remove(slot);
    }

    public void addSlotToMinHeap(Integer slot) {
        parkingPriorityQueue.add(slot);
    }
}
