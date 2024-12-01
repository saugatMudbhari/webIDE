package com.share.user.language.usecase;

import com.share.user.language.dto.UserWriteLanguage;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ChooseLanguageUseCase {

    public String selectLanguage(UserWriteLanguage userWriteLanguage) {
        String lang = userWriteLanguage.writeLanguage().toLowerCase().replaceAll("\\s", "");
        return ChooseCpp(lang, userWriteLanguage.code());
    }

    private String ChooseCpp(String selectedLanguage, String logic) {
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
        String fileExtension = switch (selectedLanguage) {
            case "C" -> "c";
            case "C++" -> "cpp";
            case "Java" -> "java";
            case "Python" -> "py";
            case "JavaScript", "js" -> "js";
            case "PHP" -> "php";
            case "Kotlin" -> "kt";
            default -> "txt";
        };
        Path fileLocation = Path.of(System.getProperty("user.dir"), "language_folder", randomClassName+"."+ fileExtension);
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
}
