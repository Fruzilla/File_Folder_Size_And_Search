package com.company;

import java.util.Comparator;

public class FileTotalComparator implements Comparator<FileNode>{
    @Override
    public int compare(FileNode a, FileNode b){ //ascending order
        return a.getTotalSize() < b.getTotalSize() ? -1 : a.getTotalSize() == b.getTotalSize() ? 0 : 1;
    }
}
