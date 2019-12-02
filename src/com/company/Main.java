package com.company;

import java.io.File;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class Main {

    public static void main(String[] args) {
        //test();
        //findFile("C:/Users/Myself/Desktop/Temp");
        //System.out.println(displaySize(63410));

        //findFileNode("C:/Users/Myself/Downloads");
        //findFileNode("C:/Users/Myself/Desktop/Temp");
        //findFileNode("C:/Users/Myself/Desktop/Temp/bsnes-mercury-2017.06.28_b68bd0a");

        //findFileNodeDeep("C:/Users/Myself/Desktop/Temp");
        //findFileNodeDeep("C:/Users/Myself/Music");
        //findFileNodeDeep("C:/Program Files (x86)/Steam/SteamApps/common");
        //findFileNodeDeep("C:/");

        searchFile("C:/Users/Myself/Desktop/Temp", "favicon.ico");
        searchFile("C:/Users/Myself/Desktop/Temp", "favicon");

        //main_interface();
    }

    public static void test(){
        File test = new File("C:/Users/Myself/Desktop/Temp");
        System.out.println(test.list());
        System.out.println(test.toString());
        System.out.println(test.isDirectory());
        System.out.println(test.getParent());

        String[] files = test.list();
        for(int i = 0; i < files.length; i++){
            long div = 1024; //kb -> mb -> gb -> tb -> pb ect.
            File temp = new File(test.toString() + "/" + files[i]);
            //file.toString prints full path, file.getName prints the file name only
            System.out.println(temp.getName() + "\t" + temp.length()/div);
        }
    }

    public static void findFile(String path){
        System.out.println(path);
        File root = new File(path);
        String[] files = root.list();
        for(int i = 0; i < files.length; i++){
            File temp = new File(path + "/" + files[i]);
            if(temp.isDirectory()){
                findFile(temp.toString());
            }
            else{ //if it's a file
                System.out.println("\t" + temp.getName() + "\t" + displaySize(temp.length()));
            }
        }
    }

    public static void findFileNode(String path){
        FileNode root = new FileNode(path);
        System.out.println("The size is " + root.displaySize(root.getSize()) + " / " + root.displaySize(root.getTotalSize()) + " total");
        System.out.println("TOSTRING:");
        System.out.println(root.toString());
        //TODO display run time
        root.findLargest(10);
    }

    public static void findFileNodeDeep(String path){
        FileNode root = new FileNode(path);
        System.out.println("The size is " + root.displaySize(root.getSize()) + " / " + root.displaySize(root.getTotalSize()) + " total");
        System.out.println("TOSTRING:");
        System.out.println(root.toString());
        //TODO display run time
        root.findLargestDeep(10);
    }

    public static void searchFile(String path, String file){
        System.out.println("Searching for " + file);
        FileNode root = new FileNode(path);
        long startTime = System.currentTimeMillis();
        root.searchForFile(file);
        long stopTime = System.currentTimeMillis();
        System.out.println("Search time: " + showRunTime(startTime, stopTime));
    }

    public static String displaySize(long size){
        long temp = size;
        int counter = 0;
        long div = 1024;
        String result = size + " bytes";
        String[] post = {"kb", "mb", "gb", "tb", "pb"};
        while(temp > div){
            temp = temp / div;
            result = temp + " " + post[counter];
            counter++;
        }
        return result;
    }

    public static void main_interface(){
        //ask user for path
        File chosen_dir = new File(System.getProperty("user.home"));
        final JFileChooser fc = new JFileChooser();

        //Not currently used, but code to open a file's folder in the OS's file explorer
        /*
        Desktop d = null;
        File test = new File("C:/");
        try {
            if (Desktop.isDesktopSupported()) {
                d= Desktop.getDesktop();
                d.open(test);
            } else {
                System.out.println("desktop is not supported");
            }
        }
        catch (IOException e){  }
         */

        fc.setCurrentDirectory(chosen_dir);
        fc.setDialogTitle("Select a directory");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);

        if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            File temp = fc.getSelectedFile();
            chosen_dir = temp;
        }
        else{
            System.out.println("No file selected! Exiting program...");
            return;
        }

        System.out.println("Analyzing directory " + chosen_dir.getPath());
        long startTime = System.currentTimeMillis();
        FileNode dir = new FileNode(chosen_dir);
        long stopTime = System.currentTimeMillis();
        System.out.println("Time to analyze: " + showRunTime(startTime, stopTime));
        System.out.println(dir.toString());

        // ask for number of files/number of folders

        System.out.println("How many files to search for?");
        Scanner sc = new Scanner(System.in);
        int num_files = sc.nextInt();

        //System.out.println("How many directories to search for?");
        //int num_dir = sc.nextInt();

        System.out.println("Searching for " + num_files + " biggest files/directories");
        startTime = System.currentTimeMillis();
        dir.findLargestDeep(num_files);
        stopTime = System.currentTimeMillis();
        System.out.println("Time to analyze: " + showRunTime(startTime, stopTime));

        //ask if they want the file structure saved to file
        //ask if they want analysis saved to file

        //gui with different options available?
    }

    public static String showRunTime(long start, long stop){
        String[] postfix = {"ms", "sec", "min", "hr"};
        int counter = 0;
        double dif = stop - start;
        if (dif >= 1000){
            dif = dif / 1000;
            counter++;
            while(dif > 60 && counter <= 3){    //this shouldn't take 60 hours but if it does, we came prepared.
                dif = dif / 60;
                counter++;
            }
        }
        return String.format("%.3f", dif) + " " + postfix[counter];
    }
}
