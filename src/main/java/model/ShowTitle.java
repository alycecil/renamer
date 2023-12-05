package model;

public class ShowTitle {
    String title;
    Integer season;

    public ShowTitle(
        String title,
        Integer season
    ) {
        this.title = title;
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    @Override
    public String toString() {
        return "ShowTitle{" + "title='" + title + '\'' + ", season=" + season + '}';
    }
}
