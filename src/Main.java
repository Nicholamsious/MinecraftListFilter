import java.io.*;
import java.util.*;

enum FormattingMode {
    OBSIDIAN, REDDIT, TEXT
}

public class Main {
    private static List<String> excluding, rawFoodMatches, enchantments, potionItems, arrows, ominousBottles, goatHorns, finalLines, unstackablesMatch, unstackablesContain, stack16Match, stack16Contain;
    private static String inputFilesFolder = "MinecraftListFilter Input Files", outputFilesFolder = "MinecraftListFilter Output File(s)";
    private static File inputFilesFolderFile, itemsDirectory;
    private static String minecraftVersion;
    private static FormattingMode formattingMode;

    public static String formatFinalName(String finalNameToFormat, boolean includeMaximumStackAmounts) {
        if(includeMaximumStackAmounts) {
            int maximumStackAmount = getMaximumStackAmount(finalNameToFormat);
            if (maximumStackAmount > 1) {
                finalNameToFormat += " (Max x" + maximumStackAmount + ")";
            } else if (maximumStackAmount == 1) {
                finalNameToFormat += " (Unstackable)";
            }
        }
        if(formattingMode == FormattingMode.OBSIDIAN) {
            finalNameToFormat = finalNameToFormat.replaceAll(":", "-");
            finalNameToFormat = "- [[Minecraft " + finalNameToFormat + "]]";
        } else if(formattingMode == FormattingMode.REDDIT) {
            finalNameToFormat = "- " + finalNameToFormat;
        } else if(formattingMode == FormattingMode.TEXT) {
            finalNameToFormat = finalNameToFormat;
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
        filterFiles(itemsDirectory, false);
        formattingMode = FormattingMode.REDDIT;
        filterFiles(itemsDirectory, false);
        formattingMode = FormattingMode.TEXT;
        filterFiles(itemsDirectory, false);
        formattingMode = FormattingMode.OBSIDIAN;
        filterFiles(itemsDirectory, true);
        formattingMode = FormattingMode.REDDIT;
        filterFiles(itemsDirectory, true);
        formattingMode = FormattingMode.TEXT;
        filterFiles(itemsDirectory, true);
    }
    public static void filterFiles(File itemsDirectory, boolean includeMaximumStackAmounts) {
        File[] files = listFilesInDirectory(itemsDirectory);
        excluding = Arrays.asList("Spawn Egg",
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
            unstackablesMatch = readFile(inputFilesFolder + "/unstackablesmatch.txt");
            unstackablesContain = readFile(inputFilesFolder + "/unstackablescontain.txt");
            stack16Match = readFile(inputFilesFolder + "/stack16match.txt");
            stack16Contain = readFile(inputFilesFolder + "/stack16contain.txt");
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
                            finalName = formatFinalName(finalName, includeMaximumStackAmounts);
                            finalLines.add(finalName);
                        }
                        continue;
                    } else if (finalName.equals("Arrow")) {
                        for (String arrow : arrows) {
                            finalName = arrow;
                            finalName = formatFinalName(finalName, includeMaximumStackAmounts);
                            finalLines.add(finalName);
                        }
                        continue;
                    } else if (finalName.equals("Potion")) {
                        for (String potion : potionItems) {
                            finalName = potion;
                            finalName = formatFinalName(finalName, includeMaximumStackAmounts);
                            finalLines.add(finalName);
                        }
                        continue;
                    } else if (finalName.equals("Ominous Bottle")) {
                        for (String ominousBottle : ominousBottles) {
                            finalName = ominousBottle;
                            finalName = formatFinalName(finalName, includeMaximumStackAmounts);
                            finalLines.add(finalName);
                        }
                        continue;
                    } else if (finalName.equals("Goat Horn")) {
                        for (String goatHorn : goatHorns) {
                            finalName = goatHorn;
                            finalName = formatFinalName(finalName, includeMaximumStackAmounts);
                            finalLines.add(finalName);
                        }
                        continue;
                    } else if (finalName.contains("Copper")) {
                        if(!finalName.contains("Waxed") && !finalName.contains("Raw") && !finalName.contains("Ore") && !finalName.contains("Ingot")) {
                            finalName = "Unwaxed " + finalName;
                        }
                    } else if(finalName.equals("Experience Bottle")) {
                        finalName = "Bottle o' Enchanting";
                    } else if(finalName.equals("Jack O Lantern")) {
                        finalName = "Jack o'Lantern";
                    } else if(finalName.equals("Flower Banner Pattern")) {
                        finalName = "Flower Charge Banner Pattern";
                    } else if(finalName.equals("Creeper Banner Pattern")) {
                        finalName = "Creeper Charge Banner Pattern";
                    } else if(finalName.equals("Skull Banner Pattern")) {
                        finalName = "Skull Charge Banner Pattern";
                    } else if(finalName.equals("Mojang Banner Pattern")) {
                        finalName = "Thing Banner Pattern";
                    } else if(finalName.equals("Ender Eye")) {
                        finalName = "Eye of Ender";
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
                        finalName = formatFinalName(finalName, includeMaximumStackAmounts);
                        finalLines.add(finalName);
                    }
                }
                Collections.sort(finalLines);
                writeObtainableListToFile(outputFilesFolder, minecraftVersion, includeMaximumStackAmounts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeObtainableListToFile(String outputFilesFolder, String minecraftVersion, boolean includeMaximumStackAmounts) throws IOException {
        String fileVariant = "Null";
        String fileName = "";
        if(includeMaximumStackAmounts) {
            fileName += "(With stack sizes) ";
        }
        if(formattingMode == FormattingMode.OBSIDIAN) {
            fileName += "Obsidian";
        } else if(formattingMode == FormattingMode.REDDIT) {
            fileName += "Reddit";
        } else if(formattingMode == FormattingMode.TEXT) {
            fileName += "Raw-text";
        }
        fileName += "-formatted List of Minecraft Blocks and Items Obtainable in Survival "+minecraftVersion+".txt";
        File fileToWrite = new File(outputFilesFolder + "/"+fileName);
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
    public static int getMaximumStackAmount(String finalLine) {
        for(String potentialMatch : unstackablesMatch) {
            if(finalLine.equals(potentialMatch)) {
                return 1;
            }
        }
        for(String potentialMatch : stack16Match) {
            if(finalLine.equals(potentialMatch)) {
                return 16;
            }
        }
        for(String potentialSubstring : unstackablesContain) {
            if(finalLine.contains(potentialSubstring)) {
                return 1;
            }
        }
        for(String potentialSubstring : stack16Contain) {
            if(finalLine.contains(potentialSubstring)) {
                return 16;
            }
        }
        return 64;
    }
}