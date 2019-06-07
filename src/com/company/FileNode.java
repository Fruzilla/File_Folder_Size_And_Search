package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class FileNode {
    //attributes to help in navigating
    private File this_file;
    private ArrayList<FileNode> child_dir;   //subdirectories
    private ArrayList<FileNode> child_files;      //files in this directory

    //meta data (ie. directory size)
    private long size;  //the size of the individual file, or the size of all files in that current directory (not including subdirectories)
    private long total_size;    //total size of a directory INCLUDING subdirectories

    public FileNode(String path){
        this_file = new File(path);
        child_dir = new ArrayList<FileNode>();
        child_files = new ArrayList<FileNode>();
        //find size
        size = 0;
        total_size = 0;

        if (this_file.isFile()) {   //file case
            size = this_file.length();
            //System.out.println(path + " is a file"); //debug
        }
        else{                       //directory case
            //System.out.println(path + " is a directory"); //debug
            String[] files = this_file.list();
            //System.out.println(files);
            try{
                files.equals(null);
            }
            catch(Exception e){
                return;
            }

            //find total file size of this directory
            //find subdirectories and add them as children
            //find files and add them as file children

            for(int i = 0; i < files.length; i++){
                File temp = new File(path + "/" + files[i]);
                if(temp.isFile()){
                    size += temp.length();
                    //total_size = size;
                    child_files.add(new FileNode(path + "/" + files[i]));
                }
                else{
                    FileNode child = new FileNode(path + "/" + files[i]);
                    child_dir.add(child);
                    total_size += child.getTotalSize();
                }
            }
        }
        total_size += size;
    }

    public FileNode(File file){
        this_file = file;
        //size

        //children
    }

    public long getSize(){
        return size;
    }

    public long getTotalSize(){
        return total_size;
    }

    public ArrayList<FileNode> getChildDirs(){
        return child_dir;
    }

    public ArrayList<FileNode> getChildFiles(){
        return child_files;
    }

    //TODO make this display the file size more accurately
    public String displaySize(long size){
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

    //finds the largest folders and files in the current directory
    //note that this does not inspect any files that may be in subdirectories.
    public void findLargest(int num_files){
        ArrayList<FileNode> largest_files = new ArrayList<FileNode>(num_files);
        ArrayList<FileNode> largest_dir = new ArrayList<FileNode>(num_files);

        //check files in root dir
        for(int i = 0; i < child_files.size(); i++){
            FileNode temp = child_files.get(i);
            if(largest_files.size() < num_files){   //if there's less than num_files items, add to list. When capacity is hit, sort.
                largest_files.add(temp);
                if(largest_files.size() == num_files){
                    Collections.sort(largest_files, new FileComparator());
                }
            }
            else{   //full capacity. If found file is larger than the smallest file, figure out where to insert it and do so. Use binary search and .remove(capacity-1), add(node, pos)
                if(temp.getSize() > largest_files.get(0).getSize()){
                    //binary search
                    int pos = BinarySearch(largest_files, 0, num_files-1, temp.getSize());
                    largest_files.remove(0);
                    largest_files.add(pos, temp);
                }
            }
        }
        if(largest_files.size() < num_files){ //if it never fills up, ensure that it still gets sorted
            Collections.sort(largest_files, new FileComparator());
        }

        //check subdirs
        for(int i = 0; i < child_dir.size(); i++){
            FileNode temp = child_dir.get(i);
            if(largest_dir.size() < num_files){   //if there's less than num_files items, add to list. When capacity is hit, sort.
                largest_dir.add(temp);
                if(largest_dir.size() == num_files){
                    Collections.sort(largest_dir, new FileTotalComparator());
                }
            }
            else{   //full capacity. If found file is larger than the smallest file, figure out where to insert it and do so. Use binary search and .remove(capacity-1), add(node, pos)
                if(temp.getTotalSize() > largest_dir.get(0).getTotalSize()){
                    //binary search
                    int pos = BinarySearch(largest_dir, 0, num_files-1, temp.getTotalSize());
                    largest_dir.remove(0);
                    largest_dir.add(pos, temp);
                }
            }
        }

        if(largest_dir.size() < num_files){
            Collections.sort(largest_dir, new FileTotalComparator());
        }

        System.out.println("Analysis of " + this_file.getPath());

        System.out.println("Largest Files:");
        for(int i = largest_files.size()-1; i >= 0; i--){
            System.out.println(largest_files.size() - i + ". " + largest_files.get(i).this_file.getName() + " - " + displaySize(largest_files.get(i).size));
        }

        System.out.println("\nLargest Directories:");
        for(int i = largest_dir.size()-1; i >= 0; i--){
            System.out.println(largest_dir.size() - i + ". " + largest_dir.get(i).this_file.getName() + " - " + displaySize(largest_dir.get(i).total_size));
        }
    }

    //TODO implement
    //find the largest files and directories of every subdirectory of the file node
    public void findLargestDeep(){

    }

    //TODO implement
    //a private helper function meant to recursively find the largest directories and files of all subdirectories
    private void findLargestDeepHelper(ArrayList<FileNode> largest_files, ArrayList<FileNode> largest_dir){

    }

    //binary search implementation to find where to insert
    private int BinarySearch(ArrayList<FileNode> files, int l, int r, long x){
        if(r >= l){
            int mid = l + (r-l)/2;

            //System.out.println("L R Mid: " + l + " " + r + " " + mid); //debug
            if(mid != 0){
                if(files.get(mid-1).getTotalSize() > x && files.get(mid+1).getTotalSize() <= x){
                    return mid;
                }
            }

            //if it's smaller than mid
            if(files.get(mid).getTotalSize() > x){
                return BinarySearch(files, l, mid-1, x);
            }

            //larger than mid
            return BinarySearch(files, mid+1, r, x);
        }
        return r;   //return -1;

    }

    public String toString(){
        String str = this_file.getPath() + "(Size: " + displaySize(size) + " | Total Size: " + displaySize(total_size) + ")\n\n";
        for(int i = 0; i < child_dir.size(); i++){
            str += child_dir.get(i).this_file.getPath() + "(Size: " + displaySize(child_dir.get(i).getSize()) + " | Total Size: " + displaySize(child_dir.get(i).getTotalSize()) + ")\n";

        }

        for(int i = 0; i < child_files.size(); i++){
            str += child_files.get(i).this_file.getName() + "(Size: " + displaySize(child_files.get(i).getSize()) + ")\n";
        }
        str += child_files.size() + " files, " + child_dir.size() + " directories";
        return str;
    }

    //TODO make a variation of toString that displays the contents and size of each file/subdir
}



