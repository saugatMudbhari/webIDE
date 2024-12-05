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
import java.util.ArrayList;
import java.util.List;

@Service
public class ChooseLanguageUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ChooseLanguageUseCase.class);

    public String selectLanguage(UserWriteLanguage userWriteLanguage) {
        String lang = userWriteLanguage.writeLanguage().toLowerCase().replaceAll("\\s", "");
        return ChooseLanguage(lang, userWriteLanguage.code(), userWriteLanguage.Parameters());
    }

    private String ChooseLanguage(String selectedLanguage, String logic, List<String> parameters) {
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
        try {
            Files.createDirectories(fileLocation.getParent());
            try (FileWriter fileWriter = new FileWriter(fileLocation.toFile())) {
                fileWriter.write(logic);
            }
            return executeProgram(fileLocation, selectedLanguage, parameters);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return "";
        }
    }


    public String executeProgram(Path filePath, String language, List<String> arguments) {
        try {
            ProcessBuilder processBuilder;
            switch (language.toLowerCase()) {
                case "python" -> {
                    List<String> command = new ArrayList<>();
                    command.add("python");
                    command.add(filePath.toString());
                    if (!arguments.isEmpty()) {
                        command.addAll(arguments);
                    }
                    processBuilder = new ProcessBuilder(command);
                }
                case "javascript", "js" -> {
                    List<String> command = new ArrayList<>();
                    command.add("node");
                    command.add(filePath.toString());
                    if (!arguments.isEmpty()) {
                        command.addAll(arguments);
                    }
                    processBuilder = new ProcessBuilder(command);
                }
                case "php" -> {
                    List<String> command = new ArrayList<>();
                    command.add("php");
                    command.add(filePath.toString());
                    if (!arguments.isEmpty()) {
                        command.addAll(arguments);
                    }
                    processBuilder = new ProcessBuilder(command);
                }
                case "java" -> {
                    String className = filePath.getFileName().toString().replace(".java", "");
                    ProcessBuilder compileBuilder = new ProcessBuilder("javac", filePath.toString());
                    compileBuilder.directory(filePath.getParent().toFile());
                    compileBuilder.start().waitFor();

                    List<String> command = new ArrayList<>();
                    command.add("java");
                    command.add(className);
                    if (!arguments.isEmpty()) {
                        command.addAll(arguments);
                    }
                    processBuilder = new ProcessBuilder(command);
                    processBuilder.directory(filePath.getParent().toFile());
                }
                case "c" -> {
                    String fileName = filePath.getFileName().toString().replace(".c", "");
                    Path compiledFile = filePath.getParent().resolve(fileName + ".out");
                    ProcessBuilder compileBuilder = new ProcessBuilder("gcc", filePath.toString(), "-o", compiledFile.toString());
                    compileBuilder.directory(filePath.getParent().toFile());
                    compileBuilder.start().waitFor(); // Compile C file

                    // Run the compiled C program with arguments
                    List<String> command = new ArrayList<>();
                    command.add(compiledFile.toString());
                    if (!arguments.isEmpty()) {
                        command.addAll(arguments);
                    }
                    processBuilder = new ProcessBuilder(command);
                    processBuilder.directory(filePath.getParent().toFile());
                }
                case "c++" -> {
                    String fileName = filePath.getFileName().toString().replace(".cpp", "");
                    Path compiledFile = filePath.getParent().resolve(fileName + ".out");
                    ProcessBuilder compileBuilder = new ProcessBuilder("g++", filePath.toString(), "-o", compiledFile.toString());
                    compileBuilder.directory(filePath.getParent().toFile());
                    compileBuilder.start().waitFor();

                    List<String> command = new ArrayList<>();
                    command.add(compiledFile.toString());
                    if (!arguments.isEmpty()) {
                        command.addAll(arguments);
                    }
                    processBuilder = new ProcessBuilder(command);
                    processBuilder.directory(filePath.getParent().toFile());
                }
                default ->
                        throw new UnsupportedOperationException("Execution for language " + language + " is not supported.");
            }

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the process output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                return output.toString();
            }
        } catch (Exception e) {
            LOG.error("Error while executing program for language {}: {}", language, e.getMessage());
            return "Error executing program.";
        }
    }
}
