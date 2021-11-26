package entities.videos;

import entities.Database.Database;
import entities.users.User;
import fileio.ShowInput;

import java.util.ArrayList;

public class Video {

    private String name;
    private int year;
    private ArrayList<String> cast;
    private ArrayList<String> genres;

    private Double rating;

    /**
     *
     * @return
     */
    public Double getRating() {
        return rating;
    }

    private int nrFavorite;
    private int nrViews;

    /**
     *
     */
    public void nrViewsCalc() {
        int sum = 0;
        for (User user: Database.getDatabase().getUsers()) {
            if (user.getHistory().containsKey(getName())) {

                sum += user.getHistory().get(getName());
            }
        }
        setNrViews(sum);
    }

    /**
     *
     * @return
     */
    public int getNrViews() {
        return nrViews;
    }

    /**
     *
     * @param nrViews
     */
    public void setNrViews(final int nrViews) {
        this.nrViews = nrViews;
    }

    /**
     *
     */
    public void nrFavoriteCalc() {
        int nr = 0;
        for (User user: Database.getDatabase().getUsers()) {
            if (user.getFavourite().contains(getName())) {
                nr++;
            }
        }
        setNrFavorite(nr);
    }

    /**
     *
     * @return
     */
    public int getNrFavorite() {
        return nrFavorite;
    }

    /**
     *
     * @param nrFavorite
     */
    public void setNrFavorite(final int nrFavorite) {
        this.nrFavorite = nrFavorite;
    }

    /**
     *
     * @param rating
     */
    public void setRating(final Double rating) {
        this.rating = rating;
    }

    /**
     *
     * @param name
     * @param year
     * @param cast
     * @param genres
     */
    public Video(final String name, final int year, final ArrayList<String> cast,
                 final ArrayList<String> genres) {
        this.setName(name);
        this.setYear(year);
        this.setCast(cast);
        this.setGenres(genres);
    }

    /**
     *
     * @param video
     */
    public Video(final ShowInput video) {
        this.setName(video.getTitle());
        this.setYear(video.getYear());
        this.setCast(video.getCast());
        this.setGenres(video.getGenres());
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getCast() {
        return cast;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @param year
     */
    public void setYear(final int year) {
        this.year = year;
    }

    /**
     *
     * @param cast
     */
    public void setCast(final ArrayList<String> cast) {
        this.cast = cast;
    }

    /**
     *
     * @param genres
     */
    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

}
