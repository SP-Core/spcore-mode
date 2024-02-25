package spcore.appapi.helpers;

import java.io.File;

public class PathHelper {
    public static String combine(String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }

    public static File combineFile(String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2;
    }
    public static String combine(String path1, String path2, String path3)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        File file3 = new File(file2, path3);
        return file3.getAbsolutePath();
    }
}
