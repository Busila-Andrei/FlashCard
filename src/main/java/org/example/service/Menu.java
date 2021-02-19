package org.example.service;

import org.example.domain.FlashCard;
import org.example.utils.ScannerUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Menu {


    FlashCard flashCard = new FlashCard();

    String importFile;
    String exportFile;

    ArrayList<String> console = new ArrayList<>();

    public Menu(String importFile, String exportFile) throws FileNotFoundException {
        this.importFile = importFile;
        importCardOnStart();
        this.exportFile = exportFile;
    }

    public void menu() throws IOException {
        MENU:
        for (; ; ) {
            String input = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
            System.out.println(input);
            String choice = ScannerUtils.nextLine();
            console.add(input);
            console.add(choice);
            switch (choice) {
                case "add":
                    add();
                    break;
                case "remove":
                    remove();
                    break;
                case "import":
                    importCard();
                    break;
                case "export":
                    exportCard();
                    break;
                case "ask":
                    ask();
                    break;
                case "log":
                    log();
                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                case "exit":
                    exportCardOnExit();
                    System.out.println("Bye bye!");
                    break MENU;
            }
        }
    }

    public void add() {
        System.out.println("The card:");
        console.add("The card:");
        String card = ScannerUtils.nextLine();
        console.add(card);
        String definition;
        if (verifyCard(card)) {
            System.out.println("The definition of the card:");
            console.add("The definition of the card:");
            definition = ScannerUtils.nextLine();
            console.add(definition);
            if (verifyDefinition(definition)) {
                LinkedHashMap<String, Integer> info = new LinkedHashMap<>();
                info.put(definition, 0);
                flashCard.setFlashCard(card, info);
                System.out.println("The pair (\"" + card + "\":\"" + definition + "\") has been added.\n");
                console.add("The pair (\"" + card + "\":\"" + definition + "\") has been added.\n");
            }
        }
    }

    public boolean verifyCard(String nameCard) {
        for (String card : flashCard.getFlashCard().keySet()) {
            if (card.equals(nameCard)) {
                System.out.println("The card \"" + nameCard + "\" already exists.\n");
                console.add("The card \"" + nameCard + "\" already exists.\n");
                return false;
            }
        }
        return true;
    }

    public boolean verifyDefinition(String nameDefinition) {
        for (LinkedHashMap<String, Integer> values : flashCard.getFlashCard().values()) {
            if (String.valueOf(values.keySet()).contains(nameDefinition)) {
                System.out.println("The definition \"" + nameDefinition + "\" already exists.\n");
                console.add("The definition \"" + nameDefinition + "\" already exists.\n");
                return false;
            }
        }
        return true;
    }

    public void remove() {
        System.out.println("Which card?");
        console.add("Which card?");
        String card = ScannerUtils.nextLine();
        console.add(card);
        if (existCard(card)) {
            flashCard.getFlashCard().remove(card);
            System.out.println("The card has been removed.\n");
            console.add("The card has been removed.\n");
        } else {
            System.out.println("Can't remove \"" + card + "\": there is no such card.\n");
            console.add("Can't remove \"" + card + "\": there is no such card.\n");
        }
    }

    public boolean existCard(String nameCard) {
        for (String card : flashCard.getFlashCard().keySet()) {
            if (card.equals(nameCard)) {
                return true;
            }
        }
        return false;
    }

    public void importCard() throws FileNotFoundException {
        String fileName;
        System.out.println("File name:");
        console.add("File name:");
        fileName = ScannerUtils.nextLine();
        console.add(fileName);

        File file;

        file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File not found.\n");
            console.add("File not found.\n");
        } else {
            Scanner scanner = new Scanner(file);
            int n = 0;
            while (scanner.hasNextLine()) {
                String cardName = scanner.nextLine();
                String definitionName = scanner.nextLine();
                int wrong = Integer.parseInt(scanner.nextLine());
                LinkedHashMap<String, Integer> value = new LinkedHashMap<>();
                value.put(definitionName, wrong);
                flashCard.setFlashCard(cardName, value);
                n++;
            }
            System.out.println(n + " cards have been loaded.\n");
            console.add(n + " cards have been loaded.\n");
            scanner.close();
        }
    }

    public void exportCard() throws IOException {
        String fileName;
        if (exportFile.equals("")) {
            System.out.println("File name:");
            console.add("File name:");
            fileName = ScannerUtils.nextLine();
            console.add(fileName);
        } else {
            fileName = exportFile;
        }

        File file;
        try {
            file = new File(fileName);
            if (!file.exists()) {
                Path path = Paths.get(fileName);
                Files.createFile(path);
            }
        } catch (FileNotFoundException e) {
            System.out.println();
        }
        file = new File(fileName);

        FileWriter fileWriter = new FileWriter(file);
        for (var entry : flashCard.getFlashCard().entrySet()) {
            for (var value : entry.getValue().entrySet()) {
                fileWriter.write(entry.getKey());
                fileWriter.write("\n");
                fileWriter.write(value.getKey());
                fileWriter.write("\n");
                fileWriter.write(String.valueOf(value.getValue()));
                fileWriter.write("\n");
            }
        }
        System.out.println(flashCard.getFlashCard().size() + " cards have been saved.\n");
        console.add(flashCard.getFlashCard().size() + " cards have been saved.\n");
        fileWriter.close();

    }

    public void ask() {
        System.out.println("How many times to ask?");
        console.add("How many times to ask?");
        int count = Integer.parseInt(ScannerUtils.nextLine());
        console.add(String.valueOf(count));
        while (count > 0) {
            for (String card : flashCard.getFlashCard().keySet()) {
                count = verifyAskDefinition(card, count);
                if (count == 0) {
                    break;
                }
            }
        }
        System.out.println();
        console.add("\n");

    }

    public int verifyAskDefinition(String card, int count) {
        String correctCard = "";
        System.out.println("Print the definition of \"" + card + "\":");
        console.add("Print the definition of \"" + card + "\":");
        String definition = ScannerUtils.nextLine();
        console.add(definition);
        LinkedHashMap<String, Integer> values = flashCard.getFlashCard().get(card);

        if (String.valueOf(values.keySet()).contains(definition)) {
            System.out.println("Correct!\n");
            console.add("Correct!\n");
        } else {
            boolean test1 = false;
            for (String nameCard : flashCard.getFlashCard().keySet()) {
                values = flashCard.getFlashCard().get(nameCard);
                if (String.valueOf(values.keySet()).contains(definition)) {
                    correctCard = nameCard;
                    test1 = true;
                    break;
                }
                values.replaceAll((k, v) -> v + 1);

            }

            definition = String.valueOf(flashCard.getFlashCard().get(card).keySet()).replace("[", "").replace("]", "");
            if (test1) {
                System.out.println("Wrong. The right answer is \"" + definition + "\", but your definition is correct for \"" + correctCard + "\".");
                console.add("Wrong. The right answer is \"" + definition + "\", but your definition is correct for \"" + correctCard + "\".");
            } else {
                System.out.println("Wrong. The right answer is \"" + definition + "\".");
                console.add("Wrong. The right answer is \"" + definition + "\".");
            }
        }
        count--;
        return count;
    }

    public void log() throws IOException {
        System.out.println("File name:");
        console.add("File name:");
        String fileName = ScannerUtils.nextLine();
        console.add(fileName);
        File file;
        try {
            file = new File(fileName);
            if (!file.exists()) {
                Path path = Paths.get(fileName);
                Files.createFile(path);
            }
        } catch (FileNotFoundException e) {
            System.out.println();
        }
        file = new File(fileName);

        FileWriter fileWriter = new FileWriter(file);
        for (var s : console) {
            fileWriter.write(s);
            fileWriter.write("\n");
        }
        System.out.println("The log has been saved.\n");
        console.add("The log has been saved.\n");
        fileWriter.close();
    }

    public void hardestCard() {
        int max = 0;
        for (var entry : flashCard.getFlashCard().entrySet()) {
            for (var value : entry.getValue().entrySet()) {
                if (value.getValue() > max) {
                    max = value.getValue();
                }
            }
        }
        int nr = 0;

        if (max == 0) {
            System.out.println("There are no cards with errors.");
            console.add("There are no cards with errors.");
        } else {
            for (var entry : flashCard.getFlashCard().entrySet()) {
                for (var value : entry.getValue().entrySet()) {
                    if (value.getValue() == max) {
                        nr++;
                    }
                }
            }
            if (nr == 1) {
                System.out.print("The hardest card is ");
                console.add("The hardest card is ");
            } else {
                System.out.print("The hardest cards are ");
                console.add("The hardest cards are ");
            }

            nr = 0;
            for (var entry : flashCard.getFlashCard().entrySet()) {
                for (var value : entry.getValue().entrySet()) {
                    if (value.getValue() == max && nr == 0) {
                        System.out.print("\"" + entry.getKey() + "\"");
                        console.add("\"" + entry.getKey() + "\"");
                        nr++;
                    } else if (value.getValue() == max) {
                        System.out.print(", \"" + entry.getKey() + "\"");
                        console.add(", \"" + entry.getKey() + "\"");
                        nr++;
                    }

                }
            }
            if (nr == 1) {
                System.out.println(". You have " + max + " errors answering it.\n");
                console.add(". You have " + max + " errors answering it.\n");
            } else {
                System.out.println(". You have " + max + " errors answering them.\n");
                console.add(". You have " + max + " errors answering them.\n");
            }
        }
    }

    public void resetStats() {
        for (var entry : flashCard.getFlashCard().entrySet()) {
            for (var value : entry.getValue().entrySet()) {
                value.setValue(0);
            }
        }
        System.out.println("Card statistics have been reset.");
        console.add("Card statistics have been reset.");
    }

    public void importCardOnStart() throws FileNotFoundException {
        String fileName;
        if (!importFile.equals("")) {
            fileName = importFile;

            File file;

            file = new File(fileName);
            if (!file.exists()) {
                System.out.println("File not found.\n");
                console.add("File not found.\n");
            } else {
                Scanner scanner = new Scanner(file);
                int n = 0;
                while (scanner.hasNextLine()) {
                    String cardName = scanner.nextLine();
                    String definitionName = scanner.nextLine();
                    int wrong = Integer.parseInt(scanner.nextLine());
                    LinkedHashMap<String, Integer> value = new LinkedHashMap<>();
                    value.put(definitionName, wrong);
                    flashCard.setFlashCard(cardName, value);
                    n++;
                }
                System.out.println(n + " cards have been loaded.\n");
                console.add(n + " cards have been loaded.\n");
                scanner.close();
            }
        }
    }

    public void exportCardOnExit() throws IOException {
        String fileName;
        if (!exportFile.equals("")) {
            fileName = exportFile;


            File file;
            try {
                file = new File(fileName);
                if (!file.exists()) {
                    Path path = Paths.get(fileName);
                    Files.createFile(path);
                }
            } catch (FileNotFoundException e) {
                System.out.println();
            }
            file = new File(fileName);

            FileWriter fileWriter = new FileWriter(file);
            for (var entry : flashCard.getFlashCard().entrySet()) {
                for (var value : entry.getValue().entrySet()) {
                    fileWriter.write(entry.getKey());
                    fileWriter.write("\n");
                    fileWriter.write(value.getKey());
                    fileWriter.write("\n");
                    fileWriter.write(String.valueOf(value.getValue()));
                    fileWriter.write("\n");
                }
            }
            System.out.println(flashCard.getFlashCard().size() + " cards have been saved.\n");
            console.add(flashCard.getFlashCard().size() + " cards have been saved.\n");
            fileWriter.close();

        }
    }

}
