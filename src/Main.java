import java.io.*;
import java.util.*;

enum FormattingMode {
    OBSIDIAN, REDDIT, TEXT
}

public class Main {
    private static List<String> excluding, rawFoodMatches, enchantments, potionItems, arrows, ominousBottles, goatHorns, finalLines;
    private static String inputFilesFolder = "MinecraftListFilter Input Files", outputFilesFolder = "MinecraftListFilter Output File(s)";
    private static File inputFilesFolderFile, itemsDirectory;
    private static String minecraftVersion;
    private static FormattingMode formattingMode;

    public static String formatFinalName(String finalNameToFormat) {
        if(formattingMode == FormattingMode.OBSIDIAN) {
            return "- [[Minecraft " + finalNameToFormat + "]]";
        } else if(formattingMode == FormattingMode.REDDIT) {
            return "- " + finalNameToFormat;
        } else if(formattingMode == FormattingMode.TEXT) {
            return finalNameToFormat;
        }
        return finalNameToFormat;
    }

    public static File[] listFilesInDirectory(File itemsDirectory) {
        return itemsDirectory.listFiles();
    }

    public static void main(String[] args) {
        printLine("Hello!");
        runMinecraftListFilter();
    }
    public static void runMinecraftListFilter() {
        inputFilesFolderFile = new File(inputFilesFolder);
        itemsDirectory = new File(inputFilesFolder + "/items");
        if(!inputFilesFolderFile.exists() || !itemsDirectory.exists()) {
            printLine("ERROR! items directory not found in folder " + inputFilesFolder +" (which might not exist either). Stopping.");
            return;
        }
        minecraftVersion = getUserInputString("What minecraft version (in numbers and dots)? ");
        formattingMode = FormattingMode.OBSIDIAN;
        filterFiles(itemsDirectory);
        formattingMode = FormattingMode.REDDIT;
        filterFiles(itemsDirectory);
        formattingMode = FormattingMode.TEXT;
        filterFiles(itemsDirectory);
    }
    public static void filterFiles(File itemsDirectory) {
        File[] files = listFilesInDirectory(itemsDirectory);
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
            rawFoodMatches = readFile(inputFilesFolder + "/rawfoodmatches.txt");
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
                    if(finalName.equals("Light")) {
                        skipAhead = true;
                    }
                    if (!finalName.startsWith("Enchanted Book")) {
                        for (String badSubstring : excluding) {
                            if (finalName.contains(badSubstring))
                                skipAhead = true;
                        }
                    }
                    if (!skipAhead) {
                        for(String rawFoodMatch : rawFoodMatches) {
                            if(finalName.equals(rawFoodMatch)) {
                                finalName = "Raw " + finalName;
                            }
                        }
                        finalName = formatFinalName(finalName);
                        finalLines.add(finalName);
                    }
                }
                Collections.sort(finalLines);
                writeObtainableListToFile(outputFilesFolder, minecraftVersion);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeObtainableListToFile(String outputFilesFolder, String minecraftVersion) throws IOException {
        String fileVariant = "Null";
        if(formattingMode == FormattingMode.OBSIDIAN) {
            fileVariant = "Obsidian";
        } else if(formattingMode == FormattingMode.REDDIT) {
            fileVariant = "Reddit";
        } else if(formattingMode == FormattingMode.TEXT) {
            fileVariant = "Raw-text";
        }
        File fileToWrite = new File(outputFilesFolder + "/"+fileVariant+"-formatted List of Minecraft Blocks and Items Obtainable in Survival "+minecraftVersion+".txt");
        if(!fileToWrite.exists())
            fileToWrite.createNewFile();
        writeToFile(fileToWrite, finalLines, false);
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