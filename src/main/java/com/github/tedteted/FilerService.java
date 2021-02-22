package com.github.tedteted;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.micronaut.context.annotation.Context;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Context
public class FilerService {

    String fileName = "test-file.txt";

    @PostConstruct
    private void createFile() {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String appendNameToFile(String name) throws FileNotFoundException {

        if (!checkNameExists(fileName, name)) {

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
                String writeable = name + "\r\n";
                fileOutputStream.write(writeable.getBytes());
                fileOutputStream.close();
                return "OK: " + name;

            } catch (Exception exception) {
                System.out.println(exception);
                return "NOK: " + name;
            }
        }
             else{
                return "OK file exists";
            }
    }

        public String removeNameFromFile(String name) throws IOException {

            if (checkNameExists(fileName, name)) {

                File file = new File(fileName);
                File temp = File.createTempFile("file", ".txt", file.getParentFile());

                String charset = "UTF-8";
                String delete = name;

                String line;

                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp), charset));

                while ((line = reader.readLine()) != null) {

                    if (!line.trim().equals(delete)) {

                        writer.println(line);
                        writer.flush();
                    }
                }
                reader.close();
                writer.close();
                file.delete();
                temp.renameTo(file);

                return "OK removed";
            }
            else{
                return "OK doesn't exists";
            }

        }

        public String listAllNamesFromFile() {

            List<String> results = null;

            try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
                results = lines.collect(Collectors.toList());

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                final ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                String jsonString = mapper.writeValueAsString(results);
                System.out.println(jsonString);
                return jsonString;

            } catch (Exception exception) {
                System.out.println(exception);
                return null;
            }
        }

    public boolean checkNameExists(String fileName, String name) throws FileNotFoundException {

        boolean nameExists = false;
        int count = 0;

        Scanner scanner = new Scanner(new FileInputStream(fileName));
        while(scanner.hasNextLine()) {

            String line = scanner.nextLine();
            System.out.println(line);
            if(line.contains(name)) {

                nameExists = true;
                count = count + 1;
            }
        }

        return nameExists;
    }

}