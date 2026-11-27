package de.tyma.campusbooking.model;

public class Room {

    private String RoomNr;
    private final int Room_id;
    private int Capacity;

    public Room(String RoomNr, int Room_id, int Capacity) {
        setRoomNr(RoomNr);
        this.Room_id = Room_id;
        setCapacity(Capacity);

    }

    public String getRoomNr() {
        return RoomNr;
    }

    public int getRoom_id() {
        return Room_id;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setRoomNr(String NewRoomNr) throws IllegalArgumentException{
        if(NewRoomNr == null|| NewRoomNr.isBlank()) {
            throw new IllegalArgumentException("RoomNr cant be empty");
        }
        RoomNr = NewRoomNr;
    }

    public void setCapacity(int NewCapacity) throws IllegalArgumentException{
        if(NewCapacity < 1) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        Capacity = NewCapacity;
    }
}