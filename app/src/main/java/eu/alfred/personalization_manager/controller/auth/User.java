package eu.alfred.personalization_manager.controller.auth;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String email;
    private String password;
    private String userId;
    private String accessToken;
    private String error;
    private List<String> roles = null;

    public User() {
        roles = new ArrayList<String>();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        roles = new ArrayList<String>();
    }

    public User(String firstName, String email, String password, String role) {
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        roles = new ArrayList<String>();
        roles.add(role);
    }

    public User(String firstName, String middleName, String lastName, String email, String userId, String accessToken) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;
        this.accessToken = accessToken;
    }

    public User(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        String ret = "";
        if (firstName != null) {
            ret += firstName.trim();
        }
        if (middleName != null && !middleName.trim().isEmpty()) {
            ret += " " + middleName.trim();
        }
        if (lastName != null) {
            ret += " " + lastName.trim();
        }
        return ret.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getRoles() {
        if (roles == null) {
            roles = new ArrayList<String>();
        }
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void addRole(String role) {
        if (roles == null) {
            roles = new ArrayList<String>();
        }
        roles.add(role);
    }

    @Override
    public String toString() {
        String rolesStr = "(NULL)";
        if (roles != null) {
            rolesStr = "";
            for (int i = 0; i < roles.size(); i++) {
                if (i == roles.size() - 1) {
                    rolesStr += roles.get(i);
                } else {
                    rolesStr += roles.get(i) + ", ";
                }
            }
        }
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", completeName='" + getName() + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userId='" + userId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", error='" + error + '\'' +
                ", roles=" + rolesStr +
                '}';
    }
}