package main.java.main;

import javafx.application.Platform;
import main.java.control.ConfirmControl;
import main.java.control.UIControl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The file copier.
 * @author William Rosser.
 * @version 2.1.1
 */
public class Copier {
    /**
     * Copies files from the source folder defined by the UIControl.
     * The files are in the format: filePrefix_fileNum <br>
     * i.e. image_000, image_001, etc.
     * @param fileNums a string of raw numbers, which is broken up into individual file numbers.
     * @param filePrefix the shared prefix used by every file to be copied.
     * @return the number of files successfully copied.
     */
    public int copy(String fileNums, String filePrefix) {
        int numCopied = 0;
        File sourceFolder = UIControl.getFileLocation();
        if (sourceFolder == null) {
            return 0;
        }
        //examines the provided numbers.
        Pattern pattern = Pattern.compile("\\b[\\-\\=]?\\d+\\b");
        Matcher matcher = pattern.matcher(fileNums);
        ArrayList<String> matchList = new ArrayList<String>();
        while (matcher.find()) {
//            System.out.println(matcher.group());
            String s = matcher.group();
            if (!s.substring(0,1).equals("-") && !s.substring(0,1).equals("=")) {
                matchList.add(s);
//                System.out.println("Good");
            } else {
//                System.out.println("Bad");
            }
        }
        double progress;
        //finds the files.
        boolean rtSelected = UIControl.getRTSelected();
        boolean shSelected = UIControl.getSHSelected();
        String[] fileList = sourceFolder.list();
        ArrayList<Wrapper> truncFileList = new ArrayList<Wrapper>();
        //cuts off the file extensions.
        Pattern noExtension = Pattern.compile("\\.");
        for (int i = 0; i < fileList.length; i++) {
//            System.out.println(fileList[i]);
            Matcher noEMatcher = noExtension.matcher(fileList[i]);
            if (noEMatcher.find()) {
                String s = fileList[i].substring(0, noEMatcher.start());
                truncFileList.add(new Wrapper(s, i));
            }
            progress = ((double) i / fileList.length) * 0.05;
            double curProg = progress;
            Platform.runLater(() -> ConfirmControl.setProgress(curProg));
        }
        Platform.runLater(() -> ConfirmControl.setProgress(0.05));
        ArrayList<File> filesToMove = new ArrayList<>();

        //Decides formatting.

        String formatType = UIControl.getFormatType().toUpperCase();
        ArrayList<String> rejects = new ArrayList<String>();
        for (String s : matchList) {
            String nameToFind = format(filePrefix, s, formatType);
            for (Wrapper w : truncFileList) {
                if (rtSelected && w.text.equals(nameToFind + "rt")) {
                    filesToMove.add(new File(fileList[w.num]));
                    rejects.add(w.text + "sh");
                    rejects.add(w.text);
                } else if (shSelected && w.text.equals(nameToFind + "sh") && !rejects.contains(nameToFind + "sh")) {
                    filesToMove.add(new File(fileList[w.num]));
                    rejects.add(w.text);
                } else if (w.text.equals(nameToFind) && !rejects.contains(nameToFind)) {
                    filesToMove.add(new File(fileList[w.num]));
                }
            }
        }
        progress = 0.1;
        Platform.runLater(() -> ConfirmControl.setProgress(0.1));
        ConfirmControl.setProgress(progress);
        File dest = new File(sourceFolder.getAbsolutePath() + "\\" + "selected");
        try {
            Files.createDirectory(dest.toPath());
        } catch (IOException e) {
            System.out.println("");
        }

        for (int i = 0; i < filesToMove.size(); i++) {
            try {
                Path path = filesToMove.get(i).toPath();
//                System.out.println(path);
                File sourceFile = new File(sourceFolder.getAbsolutePath() + "\\" + path);
                File destFile = new File(dest.getAbsolutePath() + "\\" + path);
                Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                numCopied++;
            } catch (IOException e) {
                System.out.println("Could not move file.");
            }
            double curProg = progress + ((double) i / filesToMove.size()) * 0.9;
            Platform.runLater(() -> ConfirmControl.setProgress(curProg));
        }
        Platform.runLater(() -> ConfirmControl.setProgress(1));
        return numCopied;
    }

    /**
     * Formats the full filename based on the given parameters.
     * @param txt the text part of the file name
     * @param num the number part of the filename
     * @param fmat the format string.
     * @return
     */
    private String format(String txt, String num, String fmat) {
        String formatted = "";
        if (fmat.equals("TEXT_NUMBER")) {
            formatted = txt + "_" + num;
        } else if (fmat.equals("NUMBER_TEXT")) {
            formatted = num + "_" + txt;
        } else if (fmat.equals("TEXTNUMBER")) {
            formatted = txt + num;
        } else if (fmat.equals("NUMBERTEXT")) {
            formatted = num + txt;
        } else { // fmat == "number"
            formatted = num;
        }
        return formatted;
    }

    private class Wrapper {
        private String text;
        private int num;
        private Wrapper(String s, int i) {
            text = s;
            num = i;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Wrapper) return this.text.equals(((Wrapper) other).text);
            if (other instanceof String) return this.text.equals(other);
            return false;
        }
    }
}
