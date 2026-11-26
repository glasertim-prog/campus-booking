package de.tyma.campusbooking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    private User createUser() {
        return new User("Tyma", "Tyma@example.com", 1);
    }

    @Test
    void createUserWithValidValues() {
        User user = createUser();
        assertEquals("Tyma", user.getName());
        assertEquals("Tyma@example.com", user.getEmail());
        assertEquals(1, user.getUser_id());
    }

    @Test
    void rejectsNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(null, "Tyma@example.com", 1);
        });
    }

    @Test
    void rejectsEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("", "Tyma@example.com", 1);
        });
    }

    @Test
    void rejectsSpacebarOnlyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(" ", "Tyma@example.com", 1);
        });
    }

    @Test
    void rejectsNullEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Tyma", null, 1);
        });
    }

    @Test
    void rejectsEmptyEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Tyma", "", 1);
        });
    }

    @Test
    void rejectsNothingBeforAtEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Tyma", "@example.com", 1);
        });
    }

    @Test
    void rejectsSpacedEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Tyma", "Tyma @example.com", 1);
        });
    }

    @Test
    void rejectsNoATSymbolEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Tyma", "wrong.example.com", 1);
        });
    }

    @Test
    void rejectsTwoATSymbolEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Tyma", "wrong@@example.com", 1);
        });
    }

    @Test
    void rejectsNullNameKeepsPreviousValue() {
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.setName(null);
        });

        assertEquals("Tyma", user.getName());
    }

    @Test
    void rejectsEmptyNameKeepsPreviousValue() {
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.setName("");
        });

        assertEquals("Tyma", user.getName());
    }

    @Test
    void rejectsSpacebarOnlyNameKeepsPreviousValue() {
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.setName(" ");
        });

        assertEquals("Tyma", user.getName());
    }

    @Test
    void rejectsNullEmailKeepsPreviousValue() {
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail(null);
        });

        assertEquals("Tyma@example.com", user.getEmail());
    }

    @Test
    void rejectsNothingBeforeAtEmailKeepsPreviousValue() {
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail("@example.com");
        });

        assertEquals("Tyma@example.com", user.getEmail());
    }

    @Test
    void rejectsEmptyEmailKeepsPreviousValue() {
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail("");
        });

        assertEquals("Tyma@example.com", user.getEmail());
    }

    @Test
    void rejectsSpacedEmailKeepsPreviousValue() {
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail("Tyma @example.com");
        });

        assertEquals("Tyma@example.com", user.getEmail());
    }

    @Test
    void rejectsNoATSymbolEmailKeepsPreviousValue() {
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail("wrong.example.com");
        });

        assertEquals("Tyma@example.com", user.getEmail());
    }

    @Test
    void rejectsTwoATSymbolEmailKeepsPreviousValue() {
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail("wrong@@example.com");
        });

        assertEquals("Tyma@example.com", user.getEmail());
    }

    @Test
    void acceptValidEmail() {
        User user = createUser();
        user.setEmail("Tyma2@example.com");
        assertEquals("Tyma2@example.com", user.getEmail());
    }

    @Test
    void acceptValidName() {
        User user = createUser();
        user.setName("Tyma2");
        assertEquals("Tyma2", user.getName());
    }
}
