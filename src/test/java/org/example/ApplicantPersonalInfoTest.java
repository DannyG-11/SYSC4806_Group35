package org.example;

import org.assertj.core.api.Assertions;
import org.example.models.ApplicantPersonalInfo;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicantPersonalInfoTest {

    private ApplicantPersonalInfo applicantPersonalInfo;

    @BeforeAll
    public void setUp() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "johndoe@cmail.carleton.ca";
        String phoneNumber = "1234567890";
        String address = "1125 Colonel By Drive, Ottawa, Ontario";
        applicantPersonalInfo = new ApplicantPersonalInfo(
                firstName, lastName, email, phoneNumber, address
        );
    }

    @AfterAll
    public void tearDown() {
        applicantPersonalInfo = null;
    }

    @Test
    public void getId() {
        Assertions.assertThat(applicantPersonalInfo.getId()).isNull();
    }

    @Test
    public void setAndGetId() {
        applicantPersonalInfo.setId(1L);
        assertEquals(1L, applicantPersonalInfo.getId());
    }

    @Test
    public void getFirstName() {
        assertEquals("John", applicantPersonalInfo.getFirstName());
    }

    @Test
    public void setFirstName() {
        applicantPersonalInfo.setFirstName("Johnny");
        assertEquals("Johnny", applicantPersonalInfo.getFirstName());
    }

    @Test
    public void getLastName() {
        applicantPersonalInfo.setLastName("Doe");
        assertEquals("Doe", applicantPersonalInfo.getLastName());
    }

    @Test
    public void setLastName() {
        applicantPersonalInfo.setLastName("James");
        assertEquals("James", applicantPersonalInfo.getLastName());
    }

    @Test
    public void getEmail() {
        applicantPersonalInfo.setEmail("johndoe@cmail.carleton.ca");
        assertEquals("johndoe@cmail.carleton.ca", applicantPersonalInfo.getEmail());
    }

    @Test
    public void setEmail() {
        applicantPersonalInfo.setEmail("johnnyjames@cmail.carleton.ca");
        assertEquals("johnnyjames@cmail.carleton.ca", applicantPersonalInfo.getEmail());
    }

    @Test
    public void getPhoneNumber() {
        assertEquals("1234567890", applicantPersonalInfo.getPhoneNumber());
    }

    @Test
    public void setPhoneNumber() {
        applicantPersonalInfo.setPhoneNumber("555");
        assertEquals("555", applicantPersonalInfo.getPhoneNumber());
    }

    @Test
    public void getAddress() {
        assertEquals("1125 Colonel By Drive, Ottawa, Ontario", applicantPersonalInfo.getAddress());
    }

    @Test
    public void setAddress() {
        applicantPersonalInfo.setAddress("Location");
        assertEquals("Location", applicantPersonalInfo.getAddress());
    }
}
