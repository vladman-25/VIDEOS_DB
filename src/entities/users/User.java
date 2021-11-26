package entities.users;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String username;
    private String subscription;
    private Map<String, Integer> history;
    private ArrayList<String> favourite;

    private int nrRatings;

    /**
     *
     * @return
     */
    public int getNrRatings() {
        return nrRatings;
    }

    /**
     *
     * @param nrRatings
     */
    public void setNrRatings(final int nrRatings) {
        this.nrRatings = nrRatings;
    }

    /**
     *
     * @param username
     * @param subscription
     * @param history
     * @param favourite
     */
    public User(final String username, final String subscription, final Map<String,
            Integer> history, final ArrayList<String> favourite) {
        this.setUsername(username);
        this.setSubscription(subscription);
        this.setHistory(history);
        this.setFavourite(favourite);
        this.setNrRatings(0);
    }

    /**
     *
     * @param user
     */
    public User(final UserInputData user) {
        this.setUsername(user.getUsername());
        this.setSubscription(user.getSubscriptionType());
        this.setHistory(user.getHistory());
        this.setFavourite(user.getFavoriteMovies());
        this.setNrRatings(0);
    }

    /**
     *
     * @param username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     *
     * @param subscription
     */
    public void setSubscription(final String subscription) {
        this.subscription = subscription;
    }

    /**
     *
     * @param history
     */
    public void setHistory(final Map<String, Integer> history) {
        this.history = history;
    }

    /**
     *
     * @param favourite
     */
    public void setFavourite(final ArrayList<String> favourite) {
        this.favourite = favourite;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    public String getSubscription() {
        return subscription;
    }

    /**
     *
     * @return
     */
    public Map<String, Integer> getHistory() {
        return history;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getFavourite() {
        return favourite;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "User{" + "username='"
                + username + '\'' + ", subscription='"
                + subscription + '\'' + ", history="
                + history + ", favourite=" + favourite + '}';
    }
}
