package com.moudjames23.marksms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class Utils {

    private Utils() {
    }

    public static boolean isValidFileExtension(String fileName, String extension) {

        return fileName.endsWith(extension);
    }


    public static boolean isValidFileHeader(@NonNull String fileName, @NonNull String[] fileHeader)  {
        BufferedReader bufferedReader = null;

        try {
            FileReader fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);

            String header =  bufferedReader.readLine();

            fileReader.close();

            if (header != null)
            {
                String[] split = header.split(",");

                return Arrays.equals(split, fileHeader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
        }


        return false;
    }

}
