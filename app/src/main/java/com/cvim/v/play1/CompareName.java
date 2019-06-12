package com.cvim.v.play1;

import java.io.File;
import java.util.Comparator;

public class CompareName implements Comparator<File> {

        @Override
        public int compare(File file1, File file2) {
            int flag = file1.getName().compareTo(file2.getName());
//            if (flag  == 1)
//            {
//                flag = -1;
//            }
            return flag;
        }
}
