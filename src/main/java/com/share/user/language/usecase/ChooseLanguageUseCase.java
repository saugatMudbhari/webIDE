package com.share.user.language.usecase;

import com.share.user.language.dto.UserWriteLanguage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ChooseLanguageUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ChooseLanguageUseCase.class);

    public String selectLanguage(UserWriteLanguage userWriteLanguage) {
        String lang = userWriteLanguage.writeLanguage().toLowerCase().replaceAll("\\s", "");
        return ChooseLanguage(lang, userWriteLanguage.code());
    }

    private String ChooseLanguage(String selectedLanguage, String logic) {
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        java.util.Random rand = new java.util.Random();
        if (logic == null || logic.trim().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be null or empty");
        }
        StringBuilder classNameBuilder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = rand.nextInt(lexicon.length());
            classNameBuilder.append(lexicon.charAt(index));
        }

        String randomClassName = classNameBuilder.toString();
        String fileExtension = switch (selectedLanguage.toLowerCase()) {
            case "c" -> "c";
            case "c++" -> "cpp";
            case "java" -> "java";
            case "python" -> "py";
            case "javaScript", "js" -> "js";
            case "php" -> "php";
            case "kotlin" -> "kt";
            default -> "txt";
        };
        Path fileLocation = Path.of(System.getProperty("user.dir"), "language_folder", randomClassName + "." + fileExtension);
        executeProgram(fileExtension);
        try {
            Files.createDirectories(fileLocation.getParent());
            try (FileWriter fileWriter = new FileWriter(fileLocation.toFile())) {
                fileWriter.write(logic);
            }
            return "";
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return "";
        }
    }


    public String executeProgram(String fileExt) {
        try {
            Process process = new ProcessBuilder("python", "--version").start();

            // Read the output of the command
//        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println(reader.readLine());
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            LOG.info("->>>>>>>>>>>>>>>>>>{}", output);
            return output.toString();
        } catch (IOException e) {
            LOG.error("IOException occurred while executing the program", e);
            throw new RuntimeException("Error executing program", e);
        }
    }
}
