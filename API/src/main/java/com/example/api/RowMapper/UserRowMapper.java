package com.example.api.RowMapper;

import com.example.api.DTO.UserDetailDTO;
import com.example.api.Model.Role;
import com.example.api.Model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserDetailDTO> {

    @Override
    public UserDetailDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDetailDTO user = new UserDetailDTO();
        Role role = Role.valueOf(rs.getString("role"));

        user.setName(rs.getString("name"));
        user.setUserAccount(rs.getString("userAccount"));
        user.setAvatar(rs.getString("avatar"));
        user.setBio(rs.getString("bio"));
        user.setBackground(rs.getString("background"));
        user.setFollowers(rs.getInt("followers"));
        user.setFollowing(rs.getInt("following"));
        user.setShowFollowers(rs.getBoolean("showFollowers"));
        user.setShowFollowing(rs.getBoolean("showFollowing"));
        user.setShowHistory(rs.getBoolean("showHistory"));
        user.setShowCurrentExpo(rs.getBoolean("showCurrentExpo"));
        user.setShowCurrentBooth(rs.getBoolean("showCurrentBooth"));
        user.setRole(role);
        return user;
    }
}
