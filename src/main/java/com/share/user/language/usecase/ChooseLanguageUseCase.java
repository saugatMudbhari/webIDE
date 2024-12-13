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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChooseLanguageUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ChooseLanguageUseCase.class);

    public String selectLanguage(UserWriteLanguage userWriteLanguage) {
        String lang = userWriteLanguage.writeLanguage();
        if (lang == null || lang.trim().isEmpty()) {
            LOG.error("Received null or empty value for writeLanguage");
            throw new IllegalArgumentException("Language cannot be null or empty");
        }
        lang = lang.toLowerCase().replaceAll("\\s", "");
        return chooseLanguage(lang, userWriteLanguage.code(), userWriteLanguage.Parameters());
    }

    private String chooseLanguage(String selectedLanguage, String logic, List<String> parameters) {
        if (logic == null || logic.trim().isEmpty()) {
            throw new IllegalArgumentException("Logic cannot be null or empty");
        }

        String fileExtension = switch (selectedLanguage.toLowerCase()) {
            case "c" -> "c";
            case "c++" -> "cpp";
            case "java" -> "java";
            case "python" -> "py";
            case "javascript", "js" -> "js";
            case "php" -> "php";
            case "kotlin" -> "kt";
            default -> "txt";
        };

        String fileName = generateFileName(selectedLanguage, logic, fileExtension);

        Path fileLocation = Path.of(System.getProperty("user.dir"), "language_folder", fileName);
        try {
            Files.createDirectories(fileLocation.getParent());
            try (FileWriter fileWriter = new FileWriter(fileLocation.toFile())) {
                fileWriter.write(logic);
            }
            return executeProgram(fileLocation, selectedLanguage, parameters);
        } catch (IOException e) {
            LOG.error("Error creating or writing to file: {}", e.getMessage(), e);
            return "Error creating or executing program.";
        }
    }

    private String executeProgram(Path filePath, String language, List<String> arguments) {
        try {
            ProcessBuilder processBuilder;
            switch (language.toLowerCase()) {
                case "python" -> processBuilder = buildCommand("python", filePath, arguments);
                case "javascript", "js" -> processBuilder = buildCommand("node", filePath, arguments);
                case "php" -> processBuilder = buildCommand("php", filePath, arguments);
                case "java" -> processBuilder = prepareJavaExecution(filePath, arguments);
                case "c" -> processBuilder = prepareCExecution(filePath, arguments);
                case "c++" -> processBuilder = prepareCppExecution(filePath, arguments);
                default -> throw new UnsupportedOperationException("Unsupported language: " + language);
            }

            processBuilder.redirectErrorStream(true);
            return readProcessOutput(processBuilder.start());
        } catch (Exception e) {
            LOG.error("Error while executing program for language {}: {}", language, e.getMessage(), e);
            return "Error executing program.";
        }
    }

    private ProcessBuilder buildCommand(String interpreter, Path filePath, List<String> arguments) {
        List<String> command = new ArrayList<>();
        command.add(interpreter);
        command.add(filePath.toString());
        command.addAll(arguments);
        return new ProcessBuilder(command);
    }

    private ProcessBuilder prepareJavaExecution(Path filePath, List<String> arguments) throws IOException, InterruptedException {
        String code = Files.readString(filePath);
        String className = extractMainClassName(code);
        if (className == null || className.isEmpty()) {
            throw new RuntimeException("No class with public static void main found.");
        }

        Path parentDirectory = filePath.getParent();
        Path correctedFilePath = parentDirectory.resolve(className + ".java");
        Files.writeString(correctedFilePath, code);

        ProcessBuilder compileBuilder = new ProcessBuilder("javac", correctedFilePath.toString());
        compileBuilder.directory(parentDirectory.toFile());
        if (compileBuilder.start().waitFor() != 0) {
            throw new RuntimeException("Java compilation failed.");
        }

        List<String> command = new ArrayList<>(List.of("java", "-cp", parentDirectory.toString(), className));
        command.addAll(arguments);
        return new ProcessBuilder(command).directory(parentDirectory.toFile());
    }

    private ProcessBuilder prepareCExecution(Path filePath, List<String> arguments) throws IOException, InterruptedException {
        String fileName = filePath.getFileName().toString().replace(".c", "");
        Path compiledFile = filePath.getParent().resolve(fileName + ".out");
        ProcessBuilder compileBuilder = new ProcessBuilder("gcc", filePath.toString(), "-o", compiledFile.toString());
        if (compileBuilder.start().waitFor() != 0) {
            throw new RuntimeException("C compilation failed.");
        }

        List<String> command = new ArrayList<>(List.of(compiledFile.toString()));
        command.addAll(arguments);
        return new ProcessBuilder(command).directory(filePath.getParent().toFile());
    }

    private ProcessBuilder prepareCppExecution(Path filePath, List<String> arguments) throws IOException, InterruptedException {
        String fileName = filePath.getFileName().toString().replace(".cpp", "");
        Path compiledFile = filePath.getParent().resolve(fileName + ".out");
        ProcessBuilder compileBuilder = new ProcessBuilder("g++", filePath.toString(), "-o", compiledFile.toString());
        if (compileBuilder.start().waitFor() != 0) {
            throw new RuntimeException("C++ compilation failed.");
        }

        List<String> command = new ArrayList<>(List.of(compiledFile.toString()));
        command.addAll(arguments);
        return new ProcessBuilder(command).directory(filePath.getParent().toFile());
    }

    private String readProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        }
    }

    private String extractMainClassName(String code) {
        String content = code;
        String regex = "(?s)(?:public\\s+)?class\\s+(\\w+)\\s*\\{[^}]*?public\\s+static\\s+void\\s+main\\s*\\(";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String className = matcher.group(1);
            System.out.println("Class containing 'public static void main': " + className);
            return className;
        } else {
            System.out.println("No class found containing 'public static void main'.");
            throw new IllegalArgumentException("No class containing 'public static void main' found.");
        }

    }

    private String generateFileName(String selectedLanguage, String logic, String fileExtension) {
        if ("java".equalsIgnoreCase(selectedLanguage)) {
            String className = extractMainClassName(logic);
            if (className == null || className.isEmpty()) {
                throw new IllegalArgumentException("No class with public static void main found in Java logic");
            }
            return className + "." + fileExtension;
        } else {
            final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
            StringBuilder fileNameBuilder = new StringBuilder();
            java.util.Random rand = new java.util.Random();
            for (int i = 0; i < 8; i++) {
                fileNameBuilder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            return fileNameBuilder.toString() + "." + fileExtension;
        }
    }
}

