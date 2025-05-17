package com.java.ne_starter.enumerations.plate;
import lombok.Getter;

@Getter
public enum PlateStatus {
    AVAILABLE("AVAILABLE"),  // Plate exists but not assigned
    ASSIGNED("ASSIGNED"),   // Plate is assigned to a vehicle/owner
    RETIRED("RETIRED");

    public final String status;

    PlateStatus(String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return status;
    }
}
