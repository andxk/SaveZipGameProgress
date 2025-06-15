import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static final String DEF_PATH = "D://GamesJava//savegames//";

    public static void saveGame(GameProgress gp, String path) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static String getSavePath(int n) {
        return DEF_PATH + "save" + n + ".dat";
    }


    public static void zipFiles(String zipPath, List<String> files) {
        try (FileOutputStream fos = new FileOutputStream(zipPath);
                ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String fin : files) {
                try (FileInputStream fis = new FileInputStream(fin)) {
                    byte[] buff = new byte[fis.available()];
                    fis.read(buff);
//                    ZipEntry entry = new ZipEntry(fin);
                    ZipEntry entry = new ZipEntry(fin.substring(fin.lastIndexOf("//")+1));
                    zos.putNextEntry(entry);
                    zos.write(buff);
                    zos.closeEntry();
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void deleteFiles(List<String> files) {
        for (String fname : files) {
            File f = new File(fname);
            if (f.delete()) {
                System.out.println("Файл '" + f.getName() + "' удален!");
            }
            else {
                System.out.println("Ошибка. Файл '" + f.getName() + "' не может быть удален!");
            }
        }
    }



    public static void main(String[] args) {

        GameProgress[] gps = {
                new GameProgress(100, 50, 5, 10.5),
                new GameProgress(85, 15, 4, 1.2),
                new GameProgress(40, 30, 1, 0.5)
        };

        int n = 0;
        for (GameProgress gp : gps) {
            saveGame(gp, getSavePath(n++));
        }

        List<String> savedNames = new ArrayList<>();
        for (int i = 0; i < gps.length; i++) {
            savedNames.add(getSavePath(i));
        }

        String zipFileName = DEF_PATH + "saved.zip";
        zipFiles(zipFileName, savedNames);

        if (new File(zipFileName).exists()) {
            deleteFiles(savedNames);
        }

    }


}
