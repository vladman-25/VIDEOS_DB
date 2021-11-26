package entities.actors;

import actor.ActorsAwards;
import entities.Database.Database;
import entities.videos.Movie;
import entities.videos.Show;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Map;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;
    private Double average;

    private int nrAwards;

    /**
     *
     * @return nr de premii
     */
    public int getNrAwards() {
        return nrAwards;
    }

    /**
     * calculeaza si seteaza nr total de premii
     */
    public void setNrAwards() {
        final int[] sum = {0};
        getAwards().forEach((key, value) -> sum[0] += value);
        this.nrAwards = sum[0];
    }
    /**
     * @param name nume
     * @param careerDescription descriere
     * @param filmography filme
     * @param awards premii

     */
    public Actor(final String name, final String careerDescription,
                 final ArrayList<String> filmography, final Map<ActorsAwards,
            Integer> awards) {
        this.setName(name);
        this.setCareerDescription(careerDescription);
        this.setFilmography(filmography);
        this.setAwards(awards);
    }

    /**
     *
     * @param actor actor
     */
    public Actor(final ActorInputData actor) {
        this.setName(actor.getName());
        this.setCareerDescription(actor.getCareerDescription());
        this.setFilmography(actor.getFilmography());
        this.setAwards(actor.getAwards());
    }

    /**
     *
     */
    public void getAverageGrade() {
        Double sum = 0.0;
        int nr = 0;
        for (Movie movie : Database.getDatabase().getMovies()) {
            if (movie.getCast().contains(getName()) && (movie.getRatings().size() != 0)) {
                Double x = movie.getAvgRating();
                if (x > 0.0) {
                    sum += x;
                    nr++;
                }
            }
        }
        for (Show show : Database.getDatabase().getShows()) {
            if (show.getCast().contains(getName())) {
                sum += show.getAvgRating();
                nr++;
                Double x = show.getAvgRating();
                if (x > 0.0) {
                    sum += x;
                    nr++;
                }
            }
        }
        if (sum == 0.0) {
            setAverage(0.0);
        }
        setAverage(sum / nr);
        //return sum/nr;
    }

    /**
     *
     * @return
     */
    public Double getAverage() {
        return average;
    }

    /**
     *
     * @param average
     */
    public void setAverage(final Double average) {
        this.average = average;
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
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param careerDescription
     */
    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    /**
     *
     * @return
     */
    public String getCareerDescription() {
        return this.careerDescription;
    }

    /**
     *
     * @param filmography
     */
    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getFilmography() {
        return this.filmography;
    }

    /**
     *
     * @param awards
     */
    public void setAwards(final Map<ActorsAwards, Integer> awards) {
        this.awards = awards;
    }

    /**
     *
     * @return
     */
    public Map<ActorsAwards, Integer> getAwards() {
        return this.awards;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Actor{" + "name='"
                + getName() + '\''
                + ", career_description='"
                + getCareerDescription()
                + '\'' + ", filmography="
                + getFilmography()
                + ", awards="
                + getAwards() + '}';
    }
}
