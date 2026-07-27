package org.example.freighthandler.door;


//status of door/trailer
public enum DoorStatus {
    UNLOAD,//to unload and prevent loading
    LOAD, //to load prevent unloading on accident
    EMPTY,
    CLOSED_ARRIVED
}
