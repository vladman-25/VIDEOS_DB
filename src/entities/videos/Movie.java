package entities.videos;

import entities.Database.Database;
import entities.users.User;
import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Movie extends Video {
    private int duration;
    private Map<String, Double> ratings;
    private Double rating;
    private int nrFavorite;

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
     * @return
     */
    public Double getRating() {
        return rating;
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
     * @param duration
     */
    public Movie(final String name, final int year, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int duration) {
        super(name, year, cast, genres);
        this.setDuration(duration);
        this.setRatings();
    }

    /**
     *
     * @param movie
     */
    public Movie(final MovieInputData movie) {
        super(movie.getTitle(), movie.getYear(), movie.getCast(), movie.getGenres());
        this.setDuration(movie.getDuration());
        this.setRatings();
    }

    /**
     *
     * @return
     */
    public int getUserFavorite() {
        int nr = 0;
        for (User user: Database.getDatabase().getUsers()) {
            if (user.getFavourite().contains(getName())) {
                nr++;
            }
        }
        setNrFavorite(nr);
        return nr;
    }

    /**
     *
     * @return
     */
    public Double getAvgRating() {
        Double sum = 0.0;
        int nr = 0;
        boolean ok = false;
        for (Map.Entry<String, Double> entry : getRatings().entrySet()) {
            ok = true;
            sum += entry.getValue();
            nr++;
        }
        if (getRatings().size() == 0) {
            setRating(0.0);
            return 0.0;
        }
        setRating(sum / nr);
        return sum / nr;
    }

    /**
     *
     * @return
     */
    public int getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     */
    public void setDuration(final int duration) {
        this.duration = duration;
    }

    /**
     *
     * @return
     */
    public Map<String, Double> getRatings() {
        return ratings;
    }

    /**
     *
     */
    public void setRatings() {
        this.ratings = new HashMap<String, Double>();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Movie{" + " title= "
                + super.getName() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n " + "duration=" + duration + '}';
    }
}
