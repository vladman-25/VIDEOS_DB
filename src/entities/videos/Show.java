package entities.videos;

import entertainment.Season;
import entities.Database.Database;
import entities.users.User;
import fileio.SerialInputData;

import java.util.ArrayList;

public class Show extends Video {
    private int numberOfSeasons;
    private ArrayList<Season> seasons;
    private Double rating;
    private int nrFavorite;
    private int duration;

    /**
     *
     * @return
     */
    public int getDuration() {
        return duration;
    }

    /**
     *
     */
    public void setDuraion() {
        int sum = 0;
        for (Season season : getSeasons()) {
            sum += season.getDuration();
        }
        this.duration = sum;
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
     * @param numberOfSeasons
     * @param seasons
     */
    public Show(final String name, final int year, final ArrayList<String> cast,
                final ArrayList<String> genres, final int numberOfSeasons,
                final ArrayList<Season> seasons) {
        super(name, year, cast, genres);
        this.setNumberOfSeasons(numberOfSeasons);
        this.setSeasons(seasons);
    }

    /**
     *
     * @param serial
     */
    public Show(final SerialInputData serial) {
        super(serial.getTitle(), serial.getYear(), serial.getCast(), serial.getGenres());
        this.setNumberOfSeasons(serial.getNumberSeason());
        this.setSeasons(serial.getSeasons());
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
    public Double getAvgRating() {
        Double sum = 0.0;
        int nr = 0;
        boolean ok = false;
        for (Season season: seasons) {
            Double sumSeason = 0.0;
            int nrSeason = 0;
            if (season.getRatings().size() != 0) {
                ok = true;
                for (Double rating: season.getRatings()) {
                    sumSeason += rating;
                    nrSeason++;
                }
                sum += sumSeason / nrSeason;
                nr++;
            } else {
                nr++;
            }
        }
        if (!ok) {
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
    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    /**
     *
     * @param numberOfSeasons
     */
    public void setNumberOfSeasons(final int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    /**
     *
     * @return
     */
    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    /**
     *
     * @param seasons
     */
    public void setSeasons(final ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Show{" + " title= "
                + super.getName() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n " + ", number_of_seasons="
                + getNumberOfSeasons() + ", seasons=" + getSeasons() + '}';
    }
}
