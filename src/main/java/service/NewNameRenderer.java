package service;

import model.EpisodeTitle;
import model.ShowTitle;

import java.text.NumberFormat;

public class NewNameRenderer {
    public static String render(EpisodeTitle episodeTitle){
        ShowTitle show = episodeTitle.getTitle();
        String title = show.getTitle();
        Integer season = show.getSeason();
        Integer episode = episodeTitle.getEpisode();

        if(title == null || season == null || episode == null) return null;
        //Naruto (2002) - S01E01

        return title + " - S"+renderDigit(season, 2)+"E"+renderDigit(episode, 2);//TODO
    }

    private static String renderDigit(
        Integer season,
        int length
    ) {

        return String.format("%0"+length+"d", season);
    }

}
