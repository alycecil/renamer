package model;

public class EpisodeTitle {
    ShowTitle title;
    Integer episode;

    public EpisodeTitle(
        ShowTitle title,
        Integer episode
    ) {
        this.title = title;
        this.episode = episode;
    }

    public ShowTitle getTitle() {
        return title;
    }

    public void setTitle(ShowTitle title) {
        this.title = title;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    @Override
    public String toString() {
        return "EpisodeTitle{" + "title=" + title + ", episode=" + episode + '}';
    }
}
