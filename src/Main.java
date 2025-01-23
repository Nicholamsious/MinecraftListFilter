import java.io.*;
import java.util.*;

public class Main {
    private static List<String> excluding, enchantments, potionItems, arrows, ominousBottles, goatHorns, finalLines;

    public static String formatFinalName(String finalNameToFormat) {
        return "- " + finalNameToFormat + "";
    }

    public static File[] listFilesInDirectory(File itemsDirectory) {
        return itemsDirectory.listFiles();
    }

    public static void main(String[] args) {
        printLine("Hello!");
        runMinecraftListFilter();
    }
    public static void runMinecraftListFilter() {
        String inputFilesFolder = "MinecraftListFilter Input Files", outputFilesFolder = "MinecraftListFilter Output File(s)";
        File inputFilesFolderFile = new File(inputFilesFolder), itemsDirectory = new File(inputFilesFolder + "/items");
        if(!inputFilesFolderFile.exists() || !itemsDirectory.exists()) {
            printLine("ERROR! items directory not found in folder " + inputFilesFolder +" (which might not exist either). Stopping.");
            return;
        }
        File[] files = listFilesInDirectory(itemsDirectory);
        String minecraftVersion = getUserInputString("What minecraft version (in numbers and dots)? ");
        excluding = Arrays.asList("Spawn Egg",
                "Egg",
                "Jigsaw",
                "Player Head",
                "Barrier",
                "Bedrock",
                "Command Block",
                "Air",
                "Debug Stick",
                "Dirt Path",
                "End Portal Frame",
                "Farmland",
                "Frogspawn",
                "Infested",
                "Knowledge Book",
                "Petrified Oak Slab",
                "Structure Block",
                "Structure Void",
                "Spawner",
                "Vault",
                "Splash Potion",
                "Potion",
                "Lingering Potion");
        try {
            enchantments = readFile(inputFilesFolder + "/enchantedbooks.txt");
            potionItems = readFile(inputFilesFolder + "/potions.txt");
            arrows = readFile(inputFilesFolder + "/arrows.txt");
            ominousBottles = readFile(inputFilesFolder + "/ominousbottles.txt");
            goatHorns = readFile(inputFilesFolder + "/goathorns.txt");
            finalLines = new ArrayList<>();
            if (files != null) {
                String name;
                String[] words;
                String finalName;
                for (File file : files) {
                    name = file.getName();
                    name = name.replaceAll("_", " ");
                    name = name.substring(0, name.length() - 5);
                    finalName = "";
                    words = name.split(" ");
                    for (String word : words) {
                        finalName += String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1, word.length()) + " ";
                    }
                    finalName = finalName.substring(0, finalName.length() - 1);
                    finalName = finalName.replaceAll("Tnt", "TNT");
                    finalName = finalName.replaceAll("Writable Book", "Book and Quill");
                    finalName = finalName.replaceAll("Repeater", "Redstone Repeater");
                    finalName = finalName.replaceAll("Comparator", "Redstone Comparator");
                    if (finalName.equals("Enchanted Book")) {
                        for (String enchantment : enchantments) {
                            finalName = "Enchanted Book of " + enchantment;
                            finalName = formatFinalName(finalName);
                            finalLines.add(finalName);
                        }
                        continue;
                    }
                    if (finalName.equals("Arrow")) {
                        for (String arrow : arrows) {
                            finalName = arrow;
                            finalName = formatFinalName(finalName);
                            finalLines.add(finalName);
                        }
                        continue;
                    }
                    if (finalName.equals("Potion")) {
                        for (String potion : potionItems) {
                            finalName = potion;
                            finalName = formatFinalName(finalName);
                            finalLines.add(finalName);
                        }
                        continue;
                    }
                    if (finalName.equals("Ominous Bottle")) {
                        for (String ominousBottle : ominousBottles) {
                            finalName = ominousBottle;
                            finalName = formatFinalName(finalName);
                            finalLines.add(finalName);
                        }
                        continue;
                    }
                    if (finalName.equals("Goat Horn")) {
                        for (String goatHorn : goatHorns) {
                            finalName = goatHorn;
                            finalName = formatFinalName(finalName);
                            finalLines.add(finalName);
                        }
                        continue;
                    }
                    boolean skipAhead = false;
                    if (!finalName.startsWith("Enchanted Book")) {
                        for (String badSubstring : excluding) {
                            if (finalName.contains(badSubstring))
                                skipAhead = true;
                        }
                    }
                    finalName = formatFinalName(finalName);
                    if (!skipAhead)
                        finalLines.add(finalName);
                }
                File fileToWrite = new File(outputFilesFolder + "/List of Minecraft Blocks and Items Obtainable in Survival "+minecraftVersion+".txt");
                if(!fileToWrite.exists())
                    fileToWrite.createNewFile();
                writeToFile(fileToWrite, finalLines, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void printLine(String string) {
        System.out.println(string);
    }
    public static void print(String string) {
        System.out.print(string);
    }
    public static String getUserInputString(String prompt) {
        Scanner scanner = new Scanner(System.in);
        print(prompt);
        String line = scanner.nextLine();
        while(line.isBlank()) {
            printLine("This can't be blank. Please retry.");
            print(prompt);
            line = scanner.nextLine();
        }
        return line;
    }
    public static List<String> readFile(String filenameOrPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(filenameOrPath)));
        List<String> lines = new ArrayList<>();
        String line = br.readLine();
        while(line != null) {
            lines.add(line);
            line = br.readLine();
        }
        br.close();
        return lines;
    }
    public static void writeToFile(File file, List<String> lines, boolean append) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, append));
        if(append) {
            for (String line : lines) {
                bw.write(line);
            }
        } else {
            String string = String.join("\n",lines);
            bw.write(string);
        }
        bw.flush();
        bw.close();
    }
}