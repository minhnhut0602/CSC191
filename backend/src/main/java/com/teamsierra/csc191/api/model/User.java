package com.teamsierra.csc191.api.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User: scott
 * Date: 9/9/13
 * Time: 2:55 PM
 */

@Document(collection = "users")
public class User extends GenericModel 
{
    private String oauthId;
    private UserType type;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String token;
    private String avatarURL;
    private String phone;
    private boolean active;
    
    @JsonIgnore
    public String getOauthId() {
        return oauthId;
    }
    
    @JsonProperty(value = "oauthId")
    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }
    
    public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
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
    
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    
    @JsonProperty(value = "password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getToken() {
        return token;
    }
    
    @JsonProperty(value = "token")
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
                "id='" + this.getId() + '\'' +
                ", type='" + type + '\'' +
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
}
