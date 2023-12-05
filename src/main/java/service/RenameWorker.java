package service;

import model.ShowTitle;

import javax.swing.*;
import java.io.File;

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

    public static void rename(JTextArea statusField) {
        //THIS IS THE CRUX


    }
}
