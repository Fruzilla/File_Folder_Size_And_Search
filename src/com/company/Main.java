package com.company;

import java.io.File;

public class Main {

    public static void main(String[] args) {
	// write your code here
        //test();
        //findFile("C:/Users/Myself/Desktop/Temp");
        //System.out.println(displaySize(63410));
        //findFileNode("C:/Users/Myself/Downloads");
        //findFileNode("C:/Users/Myself/Desktop/Temp");
        //findFileNode("C:/Users/Myself/Desktop/Temp/bsnes-mercury-2017.06.28_b68bd0a");

        //findFileNodeDeep("C:/Users/Myself/Desktop/Temp");
        findFileNodeDeep("C:/Users/Myself/Music");
        //findFileNodeDeep("C:/");



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

    public static String displaySize(long size){
        long temp = size;
        int counter = 0;
        long div = 1024;
        String result = size + " bytes";
        String[] post = {"kb", "mb", "gb", "pb"};
        while(temp > div){
            temp = temp / div;
            result = temp + " " + post[counter];
            counter++;
        }
        return result;
    }
}
