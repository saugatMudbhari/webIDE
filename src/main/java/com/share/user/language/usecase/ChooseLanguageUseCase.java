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
        if (logic == null || logic.trim().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be null or empty");
        }
        String fileExtension = switch (selectedLanguage) {
            case "C" -> "c";
            case "C++" -> "cpp";
            case "Java" -> "java";
            case "Python" -> "py";
            case "JavaScript","js" -> "js";
            case "PHP" -> "php";
            case "Kotlin" -> "kt";
            default -> "txt";
        };
        Path fileLocation = Path.of(System.getProperty("user.dir"), "language_folder", "output."+fileExtension);
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
