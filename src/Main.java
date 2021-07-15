import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        String pathDir = "D://Games//savegames//";
        String zipFile = "zip_output.zip";

        GameProgress[] gameProgresses = {
                new GameProgress(90, 9, 1, 100.0),
                new GameProgress(110, 11, 2, 200.0),
                new GameProgress(100, 10, 1, 150.0)
        };

        List<String> pathFiles = new ArrayList<>();
        for (int i = 0; i < gameProgresses.length; i++) {
            pathFiles.add(pathDir + "save" + (i + 1) + ".dat");
            saveGame(pathFiles.get(i), gameProgresses[i]);
        }

        zipFiles(pathDir + zipFile, pathFiles);

        clearNotZip(pathDir, zipFile);
    }

    private static void saveGame(String gamePath, GameProgress gameProgress) {
        try (FileOutputStream fout = new FileOutputStream(gamePath);
             ObjectOutputStream oout = new ObjectOutputStream(fout)) {
            oout.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void zipFiles(String fileZip, List<String> pathFiles) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileZip))) {
            for (int i = 0; i < pathFiles.size(); i++) {
                File myFile = new File(pathFiles.get(i).toString());

                ZipEntry zipEntry = new ZipEntry(myFile.getName());
                zos.putNextEntry(zipEntry);

                FileInputStream fis = new FileInputStream(pathFiles.get(i));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zos.write(buffer);

                zos.closeEntry();
                fis.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void clearNotZip(String pathDir, String zipFile) {
        File dir = new File(pathDir);
        if (dir.isDirectory()) {
            for (File item : dir.listFiles()) {
                if (!item.isDirectory()) {
                    if (!item.getName().equals(zipFile)) {
                        item.delete();
                    }
                }
            }
        }
    }
}
