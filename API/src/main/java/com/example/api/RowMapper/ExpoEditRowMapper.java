package com.example.api.RowMapper;

import com.example.api.DTO.ExpoEditDTO;
import com.example.api.Model.OpenMode;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ExpoEditRowMapper implements RowMapper<ExpoEditDTO> {

    @Override
    public ExpoEditDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExpoEditDTO expo = new ExpoEditDTO();
        OpenMode openMode = OpenMode.valueOf(rs.getString("openMode"));
        LocalDateTime start = LocalDateTime.parse(rs.getString("openStart"));
        LocalDateTime end = LocalDateTime.parse(rs.getString("openEnd"));

        expo.setName(rs.getString("name"));
        expo.setAvatar(rs.getString("avatar"));
        expo.setPrice(rs.getInt("price"));
        expo.setIntroduction(rs.getString("introduction"));
        expo.setOpenMode(openMode);
        expo.setOpenStatus(rs.getBoolean("openStatus"));
        expo.setOpenStart(start);
        expo.setOpenEnd(end);
        expo.setAccessCode(rs.getString("accessCode"));
        expo.setMaxParticipants(rs.getInt("maxParticipants"));
        expo.setShow(rs.getBoolean("isShow"));

        return expo;
    }
}
