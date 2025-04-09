package com.example.api.RowMapper;

import com.example.api.DTO.UserOverviewDTO;
import com.example.api.Model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserOverviewRowMapper implements RowMapper<UserOverviewDTO> {

    @Override
    public UserOverviewDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserOverviewDTO user = new UserOverviewDTO();
        Role role = Role.valueOf(rs.getString("role"));

        user.setName(rs.getString("name"));
        user.setUserAccount(rs.getString("userAccount"));
        user.setAvatar(rs.getString("avatar"));
        user.setRole(role);
        return user;
    }
}
