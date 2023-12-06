package service;

import javafx.util.Pair;
import model.Mode;
import model.ShowTitle;

import java.io.File;
import java.util.List;

public class StateMachine {
    public static File rootFolder;
    public static Mode mode = Mode.SELECT;


    public static ShowTitle show;
    public static List<Pair<File, File>> renames;
}
