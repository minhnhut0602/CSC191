package com.teamsierra.csc191.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User: scott
 * Date: 9/9/13
 * Time: 2:55 PM
 */

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String oauthId;
    private String group;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String token;
    private String avatarURL;
    private String phone;
    private boolean active;
    private GenericModel.UserType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOauthId() {
        return oauthId;
    }

    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

	public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", group='" + group + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", phone='" + phone + '\'' +
                ", active='" + active + '\'' +
                '}';
    }

    public GenericModel.UserType getType()
    {
        return type;
    }

    public void setType(GenericModel.UserType type)
    {
        this.type = type;
    }
}
