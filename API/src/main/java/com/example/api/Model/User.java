package com.example.api.Model;


import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class User {
    private String name;
    private String userAccount;
    private String password;
    private String tel;
    private String mail;
    private String avatar;
    private Date birthday;
    private String bio;
    private String background;
    private Integer followers;
    private Integer following;
    private Integer[] expo;
    private String[] booth;
    private boolean showFollowers;
    private boolean showFollowing;
    private boolean showHistory;
    private boolean showCurrentExpo;
    private boolean showCurrentBooth;
    private Role role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Integer[] getExpo() {
        return expo;
    }

    public void setExpo(Integer[] expo) {
        this.expo = expo;
    }

    public String[] getBooth() {
        return booth;
    }

    public void setBooth(String[] booth) {
        this.booth = booth;
    }

    public boolean isShowFollowers() {
        return showFollowers;
    }

    public void setShowFollowers(boolean showFollowers) {
        this.showFollowers = showFollowers;
    }

    public boolean isShowFollowing() {
        return showFollowing;
    }

    public void setShowFollowing(boolean showFollowing) {
        this.showFollowing = showFollowing;
    }

    public boolean isShowHistory() {
        return showHistory;
    }

    public void setShowHistory(boolean showHistory) {
        this.showHistory = showHistory;
    }

    public boolean isShowCurrentExpo() {
        return showCurrentExpo;
    }

    public void setShowCurrentExpo(boolean showCurrentExpo) {
        this.showCurrentExpo = showCurrentExpo;
    }

    public boolean isShowCurrentBooth() {
        return showCurrentBooth;
    }

    public void setShowCurrentBooth(boolean showCurrentBooth) {
        this.showCurrentBooth = showCurrentBooth;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
