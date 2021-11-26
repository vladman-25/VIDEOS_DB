package entities.Database;

import common.Constants;
import entities.actors.Actor;
import entities.users.User;
import entities.videos.Movie;
import entities.videos.Show;
import entities.videos.Video;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static utils.Utils.stringToAwards;


public final class Database {

    private static final Database INSTANCE = new Database();
    private ArrayList<Actor> actors = new ArrayList<Actor>();
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<ActionInputData> commandsData = new ArrayList<ActionInputData>();
    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private ArrayList<Show> shows = new ArrayList<Show>();

    private Database() {
    }

    public static Database getDatabase() {
        return INSTANCE;
    }

    /**
     *
     */
    public void destroyDatabase() {
        this.actors = new ArrayList<Actor>();
        this.users = new ArrayList<User>();
        this.commandsData = new ArrayList<ActionInputData>();
        this.movies = new ArrayList<Movie>();
        this.shows = new ArrayList<Show>();
    }

    /**
     *
     * @param input
     */
    public void transferFromInput(final Input input) {
        if (input.getActors() != null) {
            for (ActorInputData x : input.getActors()) {
                actors.add(new Actor(x));
            }
        }
        if (input.getUsers() != null) {
            for (UserInputData x : input.getUsers()) {
                users.add(new User(x));
            }
        }
        if (input.getMovies() != null) {
            for (MovieInputData x : input.getMovies()) {
                movies.add(new Movie(x));
            }
        }
        if (input.getSerials() != null) {
            for (SerialInputData x : input.getSerials()) {
                shows.add(new Show(x));
            }
        }
        if (input.getCommands() != null) {
            this.commandsData = new ArrayList<ActionInputData>(input.getCommands());
        }
    }

    /**
     *
     * @param action
     * @return
     */
    public String commandFavorite(final ActionInputData action) {
        String msg = null;
        for (User user : Database.getDatabase().getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getHistory().containsKey(action.getTitle())) {
                    if (!user.getFavourite().contains(action.getTitle())) {
                        user.getFavourite().add(action.getTitle());
                        msg = "success -> " + action.getTitle() + " was added as "
                                + "favourite";

                    } else {
                        msg = "error -> " + action.getTitle() + " is already in "
                                + "favourite list";
                    }
                } else {
                    msg = "error -> " + action.getTitle() + " is not seen";

                }
            }
        }
        return msg;
    }
    /**
     *
     * @param action
     * @return
     */
    public String commandView(final ActionInputData action) {
        String msg = null;
        for (User user : Database.getDatabase().getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                if (!user.getHistory().containsKey(action.getTitle())) {
                    user.getHistory().put(action.getTitle(), 1);
                } else {
                    user.getHistory().put(action.getTitle(),
                            user.getHistory().get(action.getTitle()) + 1);
                }
                msg = "success -> " + action.getTitle() + " was viewed with total "
                        + "views of " + user.getHistory().get(action.getTitle());

            }
        }
        return msg;
    }
    /**
     *
     * @param action
     * @return
     */
    public String commandRating(final ActionInputData action) {
        String msg = null;
        if (action.getSeasonNumber() == 0) {
            for (Movie movie : Database.getDatabase().getMovies()) {
                if (movie.getName().equals(action.getTitle())) {
                    boolean ok = true;
                    for (User user : Database.getDatabase().getUsers()) {
                        if (user.getUsername().equals(action.getUsername())) {
                            if (!user.getHistory().containsKey(action.getTitle())) {
                                msg = "error -> " + action.getTitle()
                                        + " is not seen";
                                ok = false;
                            }
                        }
                    }
                    if (ok) {
                        if (movie.getRatings().containsKey(action.getUsername())) {
                            msg = "error -> " + action.getTitle() + " has "
                                    + "been already rated";
                        } else {
                            movie.getRatings().put(action.getUsername(),
                                    action.getGrade());
                            msg = "success -> " + action.getTitle() + " was rated "
                                    + "with "
                                    + action.getGrade() + " by " + action.getUsername();
                            for (User user: Database.getDatabase().getUsers()) {
                                if (user.getUsername().equals(action.getUsername())) {
                                    user.setNrRatings(user.getNrRatings() + 1);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (Show show : Database.getDatabase().getShows()) {
                if (show.getName().equals(action.getTitle())) {
                    boolean ok = true;
                    for (User user : Database.getDatabase().getUsers()) {
                        if (user.getUsername().equals(action.getUsername())) {
                            if (!user.getHistory().containsKey(action.getTitle())) {
                                msg = "error -> " + action.getTitle()
                                        + " is not seen";
                                ok = false;
                            }
                        }
                    }
                    if (ok) {

                        if (show.getSeasons().get(action.getSeasonNumber() - 1).
                                getRaters().contains(action.getUsername())) {
                            msg = "error -> " + action.getTitle() + " has "
                                    + "been already rated";

                        } else {

                            show.getSeasons().get(action.getSeasonNumber() - 1).
                                    getRatings().add(action.getGrade());
                            show.getSeasons().get(action.getSeasonNumber() - 1).
                                    getRaters().add(action.getUsername());
                            msg = "success -> " + action.getTitle()
                                    + " was rated with " + action.getGrade()
                                    + " by " + action.getUsername();
                            for (User user : Database.getDatabase().getUsers()) {
                                if (user.getUsername().equals(action.getUsername())) {
                                    user.setNrRatings(user.getNrRatings() + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        return msg;
    }

    /**
     *
     * @param action
     * @return
     */
    public String queryActorsAverage(final ActionInputData action) {
        for (Actor actor : Database.getDatabase().getActors()) {
            actor.getAverageGrade();
        }

        ArrayList<Actor> result = new ArrayList<Actor>();
        for (Actor actor : Database.getDatabase().getActors()) {
            if (actor.getAverage() > 0.0) {
                result.add(actor);
            }
        }

        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (Double.compare(o1.getAverage(), o2.getAverage()) < 0) {
                return c2;
            }
            if (Double.compare(o1.getAverage(), o2.getAverage()) > 0) {
                return c1;
            }
            return c1 * (o1.getName().compareTo(o2.getName()));
        });


        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();

    }
    /**
     *
     * @param action
     * @return
     */
    public String queryActorsAwards(final ActionInputData action) {
        ArrayList<Actor> result = new ArrayList<Actor>();

        for (Actor actor : Database.getDatabase().getActors()) {
            boolean ok = true;
            actor.setNrAwards();
            for (String awards : action.getFilters().get(3)) {
                if (!actor.getAwards().containsKey(stringToAwards(awards))) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                result.add(actor);
            }
        }

        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (o1.getNrAwards() < o2.getNrAwards()) {
                return c2;
            }
            if (o2.getNrAwards() < o1.getNrAwards()) {
                return c1;
            }
            return c1 * (o1.getName().compareTo(o2.getName()));
        });

        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();
    }
    /**
     *
     * @param action
     * @return
     */
    public String queryActorsFilterdesc(final ActionInputData action) {
        ArrayList<Actor> result = new ArrayList<Actor>();
        for (Actor actor : Database.getDatabase().getActors()) {
            boolean ok = true;
            for (String word : action.getFilters().get(2)) {
                String updatedDescription =
                        actor.getCareerDescription().replaceAll("[^a-zA-Z ]",
                                " ");
                if (updatedDescription.toLowerCase().contains(" " + word + " ")) {
                    ok = true;
                } else {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                result.add(actor);
            }
        }
        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (o1.getName().compareTo(o2.getName()) < 0) {
                return c2;
            }
            if (o1.getName().compareTo(o2.getName()) > 0) {
                return c1;
            }
            return 0;
        });
        StringBuilder msg = new StringBuilder("Query result: [");



        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();
    }

    /**
     *
     * @param action
     * @return
     */
    public String queryMoviesRatings(final ActionInputData action) {
        ArrayList<Movie> result = new ArrayList<Movie>();

        for (Movie movie : Database.getDatabase().getMovies()) {
            movie.getAvgRating();
        }

        for (Movie movie : Database.getDatabase().getMovies()) {
            if (movie.getRating() > 0.0) {
                if (action.getFilters().get(0).contains(String.valueOf(movie.
                        getYear())) && movie.getGenres().contains(action.
                        getFilters().get(1).get(0))) {
                    result.add(movie);
                }
            }
        }
        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (Double.compare(o1.getRating(), o2.getRating()) < 0) {
                return c2;
            }
            if (Double.compare(o1.getRating(), o2.getRating()) > 0) {
                return c1;
            }
            return Integer.compare(o1.getName().compareTo(o2.getName()), 0);
        });
        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();
    }
    /**
     *
     * @param action
     * @return
     */
    public String queryShowsRatings(final ActionInputData action) {
        ArrayList<Show> result = new ArrayList<Show>();
        for (Show show : Database.getDatabase().getShows()) {
            show.getAvgRating();
        }

        for (Show show : Database.getDatabase().getShows()) {
            if (show.getRating() > 0.0) {
                boolean ok = false;
                if (action.getFilters().get(0).get(0) != null) {
                    if (action.getFilters().get(0).contains(String.
                            valueOf(show.getYear()))) {
                        ok = true;
                    }
                }
                if (ok && (action.getFilters().get(1).get(0) != null)) {
                    ok = show.getGenres().contains(action.getFilters().
                            get(1).get(0));
                }
                if (action.getFilters().get(0).get(0) == null) {
                    if (action.getFilters().get(1).get(0) != null) {
                        if (show.getGenres().contains(action.getFilters().
                                get(1).get(0))) {
                            ok = true;
                        }
                    } else {
                        ok = true;
                    }
                }

                if (ok) {
                    result.add(show);
                }
            }
        }
        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (Double.compare(o1.getRating(), o2.getRating()) < 0) {
                return c2;
            }
            if (Double.compare(o1.getRating(), o2.getRating()) > 0) {
                return c1;
            }
            return c1 * (o1.getName().compareTo(o2.getName()));
        });
        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();
    }
    /**
     *
     * @param action
     * @return
     */
    public String queryMoviesFavorites(final ActionInputData action) {
        ArrayList<Video> result = new ArrayList<Video>();

        ArrayList<Video> videos = new ArrayList<Video>(Database.getDatabase().
                getMovies());

        for (Video video : videos) {
            video.nrFavoriteCalc();
            if (video.getNrFavorite() > 0) {
                boolean ok = false;
                if (action.getFilters().get(0).get(0) != null) {
                    if (action.getFilters().get(0).contains(String.
                            valueOf(video.getYear()))) {
                        ok = true;
                    }
                }
                if (ok && (action.getFilters().get(1).get(0) != null)) {
                    ok = video.getGenres().contains(action.
                            getFilters().get(1).get(0));
                }
                if (action.getFilters().get(0).get(0) == null) {
                    if (action.getFilters().get(1).get(0) != null) {
                        if (video.getGenres().contains(action.
                                getFilters().get(1).get(0))) {
                            ok = true;
                        }
                    } else {
                        ok = true;
                    }
                }

                if (ok) {
                    result.add(video);
                }
            }
        }
        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (o1.getNrFavorite() < o2.getNrFavorite()) {
                return c2;
            }
            if (o1.getNrFavorite() > o2.getNrFavorite()) {
                return c1;
            }
            return c1 * (o1.getName().compareTo(o2.getName()));
        });
        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();
    }
    /**
     *
     * @param action
     * @return
     */
    public String queryShowsFavorites(final ActionInputData action) {
        ArrayList<Video> result = new ArrayList<Video>();

        ArrayList<Video> videos = new ArrayList<Video>(Database.getDatabase().getShows());

        for (Video video : videos) {
            video.nrFavoriteCalc();
            if (video.getNrFavorite() > 0) {
                boolean ok = false;
                if (action.getFilters().get(0).get(0) != null) {
                    if (action.getFilters().get(0).contains(String.
                            valueOf(video.getYear()))) {
                        ok = true;
                    }
                }
                if (ok && (action.getFilters().get(1).get(0) != null)) {
                    ok = video.getGenres().contains(action.
                            getFilters().get(1).get(0));
                }
                if (action.getFilters().get(0).get(0) == null) {
                    if (action.getFilters().get(1).get(0) != null) {
                        if (video.getGenres().contains(action.
                                getFilters().get(1).get(0))) {
                            ok = true;
                        }
                    } else {
                        ok = true;
                    }
                }

                if (ok) {
                    result.add(video);
                }
            }
        }
        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (o1.getNrFavorite() < o2.getNrFavorite()) {
                return c2;
            }
            if (o1.getNrFavorite() > o2.getNrFavorite()) {
                return c1;
            }
            return c1 * (o1.getName().compareTo(o2.getName()));
        });
        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();
    }
    /**
     *
     * @param action
     * @return
     */
    public String queryMoviesLongest(final ActionInputData action) {
        ArrayList<Movie> result = new ArrayList<Movie>();
        for (Movie movie : Database.getDatabase().getMovies()) {
            boolean ok = false;
            if (action.getFilters().get(0).get(0) != null) {
                if (action.getFilters().get(0).contains(String.
                        valueOf(movie.getYear()))) {
                    ok = true;
                }
            }
            if (ok && (action.getFilters().get(1).get(0) != null)) {
                ok = movie.getGenres().contains(action.getFilters().
                        get(1).get(0));
            }
            if (action.getFilters().get(0).get(0) == null) {
                if (action.getFilters().get(1).get(0) != null) {
                    if (movie.getGenres().contains(action.getFilters().
                            get(1).get(0))) {
                        ok = true;
                    }
                } else {
                    ok = true;
                }
            }

            if (ok) {
                result.add(movie);
            }
        }
        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (o1.getDuration() < o2.getDuration()) {
                return c2;
            }
            if (o1.getDuration() > o2.getDuration()) {
                return c1;
            }
            return c1 * (o1.getName().compareTo(o2.getName()));
        });
        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();
    }
    /**
     *
     * @param action
     * @return
     */
    public String queryShowsLongest(final ActionInputData action) {
        ArrayList<Show> result = new ArrayList<Show>();

        for (Show show : Database.getDatabase().getShows()) {
            show.setDuraion();
            if (action.getFilters().get(0).contains(String.
                    valueOf(show.getYear())) && show.getGenres().
                    contains(action.getFilters().get(1).get(0))) {
                result.add(show);
            }
        }
        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (o1.getDuration() < o2.getDuration()) {
                return c2;
            }
            if (o1.getDuration() > o2.getDuration()) {
                return c1;
            }
            return Integer.compare(o1.getName().compareTo(o2.getName()), 0);
        });
        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();
    }
    /**
     *
     * @param action
     * @return
     */
    public String queryMoviesMostviewed(final ActionInputData action) {
        ArrayList<Video> result = new ArrayList<Video>();

        ArrayList<Video> videos = new ArrayList<Video>(Database.getDatabase().
                getMovies());

        for (Video video : videos) {
            video.nrViewsCalc();
            if (video.getNrViews() > 0) {
                boolean ok = false;
                if (action.getFilters().get(0).get(0) != null) {
                    if (action.getFilters().get(0).contains(String.
                            valueOf(video.getYear()))) {
                        ok = true;
                    }
                }
                if (ok && (action.getFilters().get(1).get(0) != null)) {
                    ok = video.getGenres().contains(action.getFilters().
                            get(1).get(0));
                }
                if (action.getFilters().get(0).get(0) == null) {
                    if (action.getFilters().get(1).get(0) != null) {
                        if (video.getGenres().contains(action.getFilters().
                                get(1).get(0))) {
                            ok = true;
                        }
                    } else {
                        ok = true;
                    }
                }

                if (ok) {
                    result.add(video);
                }
            }
        }
        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (o1.getNrViews() < o2.getNrViews()) {
                return c2;
            }
            if (o1.getNrViews() > o2.getNrViews()) {
                return c1;
            }
            return c1 * (o1.getName().compareTo(o2.getName()));
        });
        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();
    }
    /**
     *
     * @param action
     * @return
     */
    public String queryShowsMostviewed(final ActionInputData action) {
        ArrayList<Video> result = new ArrayList<Video>();

        ArrayList<Video> videos = new ArrayList<Video>(Database.getDatabase().
                getShows());

        for (Video video : videos) {
            video.nrViewsCalc();
            if (video.getNrViews() > 0) {
                boolean ok = false;
                if (action.getFilters().get(0).get(0) != null) {
                    if (action.getFilters().get(0).contains(String.
                            valueOf(video.getYear()))) {
                        ok = true;
                    }
                }
                if (ok && (action.getFilters().get(1).get(0) != null)) {
                    ok = video.getGenres().contains(action.getFilters().
                            get(1).get(0));
                }
                if (action.getFilters().get(0).get(0) == null) {
                    if (action.getFilters().get(1).get(0) != null) {
                        if (video.getGenres().contains(action.getFilters().
                                get(1).get(0))) {
                            ok = true;
                        }
                    } else {
                        ok = true;
                    }
                }

                if (ok) {
                    result.add(video);
                }
            }
        }
        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (o1.getNrViews() < o2.getNrViews()) {
                return c2;
            }
            if (o1.getNrViews() > o2.getNrViews()) {
                return c1;
            }
            return c1 * (o1.getName().compareTo(o2.getName()));
        });
        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getName());
            }
        }
        msg.append("]");
        return msg.toString();
    }

    /**
     *
     * @param action
     * @return
     */
    public String queryUsers(final ActionInputData action) {
        ArrayList<User> result = new ArrayList<User>();

        for (User user: Database.getDatabase().getUsers()) {
            if (user.getNrRatings() > 0) {
                result.add(user);
            }
        }
        result.sort((o1, o2) -> {
            int c1 = 1, c2 = -1;
            if (action.getSortType().equals(Constants.DESC)) {
                c1 = -1;
                c2 = 1;
            }
            if (o1.getNrRatings() < o2.getNrRatings()) {
                return c2;
            }
            if (o1.getNrRatings() > o2.getNrRatings()) {
                return c1;
            }
            return c1 * (o1.getUsername().compareTo(o2.getUsername()));
        });
        StringBuilder msg = new StringBuilder("Query result: [");
        if (result.size() < action.getNumber()) {
            for (int i = 0; i < result.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getUsername());
            }
        } else {
            for (int i = 0; i < action.getNumber(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(result.get(i).getUsername());
            }
        }
        msg.append("]");
        return msg.toString();
    }

    /**
     *
     * @param action
     * @return
     */
    public String recommendationStandard(final ActionInputData action) {
        ArrayList<Video> videos = new ArrayList<Video>();
        videos.addAll(Database.getDatabase().getMovies());
        videos.addAll(Database.getDatabase().getShows());
        String msg;
        boolean ok = true;
        StringBuilder msgBuilder = new StringBuilder("StandardRecommendation result: ");
        for (User user: Database.getDatabase().getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                for (Video video: videos) {
                    if (!user.getHistory().containsKey(video.getName()) && ok) {
                        msgBuilder.append(video.getName());
                        ok = false;
                    }
                }
            }
        }
        msg = msgBuilder.toString();
        if (ok) {
            msg = "StandardRecommendation cannot be applied!";
        }
        return msg;
    }
    /**
     *
     * @param action
     * @return
     */
    public String recommendationBestunseen(final ActionInputData action) {
        ArrayList<Video> videos = new ArrayList<Video>();
        for (Movie movie: Database.getDatabase().getMovies()) {
            movie.getAvgRating();
            if (movie.getRating() >= 0) {
                videos.add(movie);
            }
        }
        for (Show show: Database.getDatabase().getShows()) {
            show.getAvgRating();
            if (show.getRating() >= 0) {
                videos.add(show);
            }
        }
        videos.sort((o1, o2) -> {
            int c1 = -1, c2 = 1;
            if (Double.compare(o1.getRating(), o2.getRating()) < 0) {
                return c2;
            }
            if (Double.compare(o1.getRating(), o2.getRating()) > 0) {
                return c1;
            }
            return 0;
        });
        StringBuilder msg = new StringBuilder("BestRatedUnseenRecommendation result: ");
        boolean ok = true;
        for (User user: Database.getDatabase().getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                for (Video video: videos) {
                    if (!user.getHistory().containsKey(video.getName()) && ok) {
                        msg.append(video.getName());
                        ok = false;
                    }
                }
            }
        }
        if (ok) {
            msg = new StringBuilder("BestRatedUnseenRecommendation cannot be applied!");
        }
        return msg.toString();
    }
    /**
     *
     * @param action
     * @return
     */
    public String recommendationSearch(final ActionInputData action) {
        //movie.getGenres().contains(action.getFilters().get(1).get(0))
        ArrayList<Video> videos = new ArrayList<Video>();
        for (User user: Database.getDatabase().getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getSubscription().equals(Constants.PREMIUM)) {
                    for (Movie movie : Database.getDatabase().getMovies()) {
                        if (movie.getGenres().contains(action.getGenre())
                                && !user.getHistory().containsKey(movie.getName())) {

                            movie.getAvgRating();
                            videos.add(movie);
                        }
                    }
                    for (Show show : Database.getDatabase().getShows()) {
                        if (show.getGenres().contains(action.getGenre())
                                && !user.getHistory().containsKey(show.getName())) {
                            show.getAvgRating();
                            videos.add(show);
                        }
                    }
                }
            }
        }
        if (videos.size() == 0) {
            return "SearchRecommendation cannot be applied!";
        } else {
            videos.sort((o1, o2) -> {
                int c1 = -1, c2 = 1;
                if (Double.compare(o1.getRating(), o2.getRating()) > 0) {
                    return c2;
                }
                if (Double.compare(o1.getRating(), o2.getRating()) < 0) {
                    return c1;
                }
                return o1.getName().compareTo(o2.getName());
            });
            StringBuilder msg = new StringBuilder("SearchRecommendation result: [");
            for (int i = 0; i < videos.size(); i++) {
                if (i != 0) {
                    msg.append(", ");
                }
                msg.append(videos.get(i).getName());
            }
            msg.append("]");
            return msg.toString();
        }
    }
    /**
     *
     * @param action
     * @return
     */
    public String recommendationPopular(final ActionInputData action) {
        boolean ok = true;
        for (User user: Database.getDatabase().getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                if (!user.getSubscription().equals(Constants.PREMIUM)) {
                    ok = false;
                }
            }
        }
        if (ok) {

            ArrayList<Video> videos = new ArrayList<Video>();
            videos.addAll(Database.getDatabase().getMovies());
            videos.addAll(Database.getDatabase().getShows());
            for (Video video: videos) {
                int sum = 0;
                for (User user: Database.getDatabase().getUsers()) {
                    if (user.getHistory().containsKey(video.getName())) {
                        sum += user.getHistory().get(video.getName());

                    }
                }

                video.setNrViews(sum);
            }


            HashMap<String, Integer> genreViews = new HashMap<String, Integer>();
            for (Movie movie : Database.getDatabase().getMovies()) {

                for (String genre : movie.getGenres()) {
                    if (!genreViews.containsKey(genre)) {
                        genreViews.put(genre, movie.getNrViews());
                    } else {
                        genreViews.put(genre, genreViews.get(genre)
                                + movie.getNrViews());
                    }

                }
            }
            for (Show show : Database.getDatabase().getShows()) {
                for (String genre : show.getGenres()) {
                    if (!genreViews.containsKey(genre)) {
                        genreViews.put(genre, show.getNrViews());
                    } else {
                        genreViews.put(genre, genreViews.get(genre)
                                + show.getNrViews());
                    }

                }
            }

            Map<String, Integer> sortedGenres = genreViews.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            ArrayList<String> showsWatchedByUser = new ArrayList<String>();
            String recommendedShow = null;

            for (User user : Database.getDatabase().getUsers()) {
                if (user.getUsername().equals(action.getUsername())) {
                    showsWatchedByUser.addAll(user.getHistory().keySet());
                }
            }

            for (String genre : sortedGenres.keySet()) {
                for (Movie movie : Database.getDatabase().getMovies()) {
                    if (!showsWatchedByUser.contains(movie.getName())) {
                        if (movie.getGenres().contains(genre)) {
                            recommendedShow = movie.getName();
                            break;
                        }
                    }
                }

                if (recommendedShow == null) {
                    for (Show serial : Database.getDatabase().getShows()) {
                        if (!showsWatchedByUser.contains(serial.getName())) {
                            if (serial.getGenres().contains(genre)) {
                                recommendedShow = serial.getName();
                                break;
                            }
                        }
                    }
                }

                if (recommendedShow != null) {
                    break;
                }
            }
            String msg;
            if (recommendedShow != null) {
                msg = "PopularRecommendation result: " + recommendedShow;
            } else {
                msg = "PopularRecommendation cannot be applied!";
            }
            return msg;

        } else {
            return "PopularRecommendation cannot be applied!";
        }
    }
    /**
     *
     * @param action
     * @return
     */
    public String recommendationFavorite(final ActionInputData action) {
        ArrayList<Video> videos = new ArrayList<Video>();
        for (User user: Database.getDatabase().getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getSubscription().equals(Constants.PREMIUM)) {
                    for (Movie movie : Database.getDatabase().getMovies()) {
                        if (!user.getHistory().containsKey(movie.getName())) {
                            movie.nrFavoriteCalc();
                            if (movie.getNrFavorite() > 0) {
                                videos.add(movie);
                            }
                        }
                    }
                    for (Show show : Database.getDatabase().getShows()) {
                        if (!user.getHistory().containsKey(show.getName())) {
                            show.nrFavoriteCalc();
                            if (show.getNrFavorite() > 0) {
                                videos.add(show);
                            }
                        }
                    }
                }
            }
        }
        if (videos.size() == 0) {
            return "FavoriteRecommendation cannot be applied!";
        } else {
            videos.sort((o1, o2) -> {
                int c1 = -1, c2 = 1;
                if (o1.getNrFavorite() < o2.getNrFavorite()) {
                    return c2;
                }
                if (o1.getNrFavorite() > o2.getNrFavorite()) {
                    return c1;
                }
                return 0;
            });
            boolean ok = true;
            StringBuilder msg = new StringBuilder("FavoriteRecommendation result: ");
            for (User user: Database.getDatabase().getUsers()) {
                if (user.getUsername().equals(action.getUsername())) {
                    for (Video video: videos) {
                        if (!user.getHistory().containsKey(video.getName()) && ok) {
                            msg.append(video.getName());
                            ok = false;
                        }
                    }
                }
            }
            return msg.toString();
        }
    }






    /**
     *
     * @param fileWriter
     * @return
     * @throws IOException
     */
    public JSONArray executeActions(final Writer fileWriter) throws IOException {
        JSONObject obj = null;
        JSONArray arrayResult = new JSONArray();
        for (ActionInputData action : Database.getDatabase().getCommandsData()) {
            String msg = null;
            if (action.getActionType().equals(Constants.COMMAND)) {
                if (action.getType().equals(Constants.FAVORITE)) {
                    msg = commandFavorite(action);
                }
                if (action.getType().equals(Constants.VIEW)) {
                    msg = commandView(action);
                }
                if (action.getType().equals(Constants.RATING)) {
                    msg = commandRating(action);
                }
            }
            if (action.getActionType().equals(Constants.QUERY)) {
                if (action.getObjectType().equals(Constants.ACTORS)) {
                    if (action.getCriteria().equals(Constants.AVERAGE)) {
                        msg = queryActorsAverage(action);
                    }
                    if (action.getCriteria().equals(Constants.AWARDS)) {
                        msg = queryActorsAwards(action);
                    }
                    if (action.getCriteria().equals(Constants.FILTER_DESCRIPTIONS)) {
                        msg = queryActorsFilterdesc(action);
                    }

                }
                if (action.getObjectType().equals(Constants.MOVIES)) {
                    if (action.getCriteria().equals(Constants.RATINGS)) {
                        msg = queryMoviesRatings(action);
                    }
                    if (action.getCriteria().equals(Constants.FAVORITE)) {
                        msg = queryMoviesFavorites(action);
                    }
                    if (action.getCriteria().equals(Constants.LONGEST)) {
                        msg = queryMoviesLongest(action);
                    }
                    if (action.getCriteria().equals(Constants.MOST_VIEWED)) {
                        msg = queryMoviesMostviewed(action);
                    }
                }
                if (action.getObjectType().equals(Constants.SHOWS)) {
                    if (action.getCriteria().equals(Constants.RATINGS)) {
                        msg = queryShowsRatings(action);
                    }
                    if (action.getCriteria().equals(Constants.FAVORITE)) {
                        msg = queryShowsFavorites(action);
                    }
                    if (action.getCriteria().equals(Constants.LONGEST)) {
                        msg = queryShowsLongest(action);
                    }
                    if (action.getCriteria().equals(Constants.MOST_VIEWED)) {
                        msg = queryShowsMostviewed(action);
                    }
                }
                if (action.getObjectType().equals(Constants.USERS)) {
                    if (action.getCriteria().equals(Constants.NUM_RATINGS)) {
                        msg = queryUsers(action);
                    }
                }
            }
            if (action.getActionType().equals(Constants.RECOMMENDATION)) {
                if (action.getType().equals(Constants.STANDARD)) {
                    msg = recommendationStandard(action);
                }
                if (action.getType().equals(Constants.BEST_UNSEEN)) {
                    msg = recommendationBestunseen(action);
                }
                if (action.getType().equals(Constants.SEARCH)) {
                    msg = recommendationSearch(action);
                }
                if (action.getType().equals(Constants.POPULAR)) {
                    msg = recommendationPopular(action);
                }
                if (action.getType().equals(Constants.FAVORITE)) {
                    msg = recommendationFavorite(action);
                }
            }
            obj = fileWriter.writeFile(action.getActionId(), "", msg);
            arrayResult.add(obj);

        }
        return arrayResult;

    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(final ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(final ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Show> getShows() {
        return shows;
    }

    public void setShows(final ArrayList<Show> shows) {
        this.shows = shows;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(final ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public ArrayList<ActionInputData> getCommandsData() {
        return commandsData;
    }

    public void setCommandsData(final ArrayList<ActionInputData> commandsData) {
        this.commandsData = commandsData;
    }
}
