package com.moudjames23.marksms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {


    @Test
    void itShouldCheckRealFileExtension() {
        //Given
        String fileName = "test.txt";
        String extension = ".txt";

        //When
        //Then
        assertTrue(Utils.isValidFileExtension(fileName, extension));
    }

    @Test
    void itShouldCheckFakeFileExtension() {
        //Given
        String fileName = "test.txt";
        String extension = ".pdf";

        //When
        //Then
        assertFalse(Utils.isValidFileExtension(fileName, extension));
    }

    @Test
    void itShouldValidHeader() {
        //Given
        String fileName = "src/test/resources/customers.csv";
        String[] header = {"Nom", "Phone", "Email", "Groupe"};

        //When
        //Then
        assertTrue(Utils.isValidFileHeader(fileName, header));
    }

    @Test
    void itShouldInvalidHeader() {
        //Given
        String fileName = "src/test/resources/bad_customers.csv";
        String[] header = {"Nom", "Phone", "Email", "Groupe", "Pays"};

        //When
        //Then
        assertFalse(Utils.isValidFileHeader(fileName, header));
    }

}
