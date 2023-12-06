package service;

import javafx.util.Pair;
import model.EpisodeTitle;
import model.Extensions;
import model.ShowTitle;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * D:\TV Shows\Naruto (2002)\Season 1\
 * D:\TV Shows\X\Season Y\
 * then z is it's pace in the orignal alphabetical order with 2 digits so 1 is 01
 * Y also uses 2 digits, so the naming scheme would be X - SYEZ
 * or Naruto (2002) - S01E01
 */
public class RenameWorker {
    public static ShowTitle getShowTitleAndSeason() {
        if (StateMachine.rootFolder == null) { return null; }
        File season = StateMachine.rootFolder;
        File show = season.getParentFile();
        return new ShowTitle(ParseShow.parse(show.getName()), ParseSeason.parse(season.getName()));

    }

    public static List<Pair<File, File>> prepRenames(JTextArea statusField) {
        if (StateMachine.rootFolder == null) { return null; }

        //THIS IS THE CRUX


        File[] files = StateMachine.rootFolder.listFiles((dir, name) -> {
            return !StringUtils.isBlank(getValidExtension(name));
        });

        if (files == null || files.length == 0) {
            statusField.append("\r\nNo valid files");
            return null;
        }

        Arrays.sort(files, Comparator.naturalOrder());


        int index = 1;
        ShowTitle show = StateMachine.show;

        List<Pair<File, File>> renames = new ArrayList<>(files.length);
        for (File file : files) {
            EpisodeTitle ep = new EpisodeTitle(show, index);
            String name = NewNameRenderer.render(ep);
            index++;


            String extension = getValidExtension(file.getName());

            String fileName = name + extension;

            File finalFile = new File(StateMachine.rootFolder, fileName);
            if (finalFile.exists()) {
                throw new IllegalStateException(
                    "Hey uhm this failed partially before or something?\r\nfile name=" + finalFile.getAbsolutePath());
            }

            renames.add(new Pair<>(file, finalFile));
        }

        StateMachine.renames = renames;
        return renames;
    }

    private static String getValidExtension(
        String name
    ) {

        for (String s : Extensions.valid) {
            if (name.endsWith(s)) {
                return s;
            }
        }
        return null;
    }
}
