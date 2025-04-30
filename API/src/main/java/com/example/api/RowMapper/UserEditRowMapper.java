package com.example.api.RowMapper;

import com.example.api.DTO.Response.UserEditResponse;
import com.example.api.Entity.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserEditRowMapper implements RowMapper<UserEditResponse> {

    @Override
    public UserEditResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEditResponse user = new UserEditResponse();
        LocalDate day = LocalDate.parse(rs.getString("birthday"));
        Role role = Role.valueOf(rs.getString("role"));

        user.setName(rs.getString("name"));
        user.setTel(rs.getString("tel"));
        user.setMail(rs.getString("mail"));
        user.setAvatar(rs.getString("avatar"));
        user.setBirthday(day);
        user.setBio(rs.getString("bio"));
        user.setBackground(rs.getString("background"));
        user.setShowFollowers(rs.getBoolean("showFollowers"));
        user.setShowFollowing(rs.getBoolean("showFollowing"));
        user.setShowHistory(rs.getBoolean("showHistory"));
        user.setShowCurrentExpo(rs.getBoolean("showCurrentExpo"));
        user.setShowCurrentBooth(rs.getBoolean("showCurrentBooth"));
        user.setRole(role);

        return user;
    }
}
