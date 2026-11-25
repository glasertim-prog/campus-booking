package de.tyma.campusbooking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoomTest {

    private Room createTestRoom() {
        return new Room("B-501", 1, 25);
    }

    @Test
    void createRoomWithValidValues() {
        Room room = createTestRoom();

        assertEquals("B-501", room.getRoomNr());
        assertEquals(1, room.getRoom_id());
        assertEquals(25, room.getCapacity());
    }

    @Test
    void rejectsZeroCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Room("B-501", 1, 0);
        });
    }

    @Test
    void rejectsNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Room("B-501", 1, -1);
        });
    }

    @Test
    void rejectsEmptyRoomNr() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Room("", 1, 25);
        });
    }

    @Test
    void rejectsSpacebarOnlyRoomNr() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Room("  ", 1, 25);
        });
    }

    @Test
    void rejectsNullRoomNr() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Room(null, 1, 25);
        });
    }

    @Test
    void rejectsZeroCapacityKeepsPreviousValue() {
        Room room = createTestRoom();
        assertThrows(IllegalArgumentException.class, () -> {
            room.setCapacity(0);
        });

        assertEquals(25, room.getCapacity());

    }

    @Test
    void setValidCapacity() {
        Room room = createTestRoom();
        room.setCapacity(30);
        assertEquals(30, room.getCapacity());

    }

    @Test
    void setLowestValidCapacity() {
        Room room = createTestRoom();
        room.setCapacity(1);
        assertEquals(1, room.getCapacity());
    }

    @Test
    void setValidRoomNr(){
        Room room = createTestRoom();
        room.setRoomNr("A-200");
        assertEquals("A-200", room.getRoomNr());
    }

    @Test
    void rejectsNullRoomNrKeepsPreviousValue() {
        Room room = createTestRoom();
        assertThrows(IllegalArgumentException.class, () -> {
            room.setRoomNr(null);
        });

        assertEquals("B-501", room.getRoomNr());
    }

    @Test
    void rejectsEmptyRoomNrKeepsPreviousValue() {
        Room room = createTestRoom();
        assertThrows(IllegalArgumentException.class, () -> {
            room.setRoomNr("");
        });

        assertEquals("B-501", room.getRoomNr());
    }

    @Test
    void rejectsSpacebarRoomNrKeepsPreviousValue() {
        Room room = createTestRoom();
        assertThrows(IllegalArgumentException.class, () -> {
            room.setRoomNr("  ");
        });

        assertEquals("B-501", room.getRoomNr());
    }

}