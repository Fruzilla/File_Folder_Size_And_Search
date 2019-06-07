package com.company;

import java.util.Comparator;

public class FileComparator implements Comparator<FileNode>{
    @Override
    public int compare(FileNode a, FileNode b){ //ascending order
        return a.getSize() < b.getSize() ? -1 : a.getSize() == b.getSize() ? 0 : 1;
    }
}
