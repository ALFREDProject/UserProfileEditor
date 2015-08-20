package eu.alfred.personalization_manager.db_administrator.model;

/**
 * Login data for local usage only
 */
public class LocalLoginData {
    private String userId;
    private String username;
    private String password;

    public LocalLoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LocalLoginData(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public LocalLoginData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
