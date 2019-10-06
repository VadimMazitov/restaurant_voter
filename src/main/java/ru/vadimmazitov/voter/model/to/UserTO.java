package ru.vadimmazitov.voter.model.to;

import ru.vadimmazitov.voter.HasEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

//TODO Delete
public class UserTO extends BaseTO implements HasEmail, Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 5, message = "length should be more than 5")
    private String password;

    @NotBlank
    @Size(max = 100)
    private String name;

    public UserTO() {
    }

    public UserTO(Integer id, String email, String password, String name) {
        super(id);
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Override
    public String getEmail() {
        return this.email;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
