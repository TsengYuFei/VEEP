package com.example.api.RowMapper;

import com.example.api.DTO.ExpoEditDTO;
import com.example.api.Model.OpenMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExpoEditRowMapper implements RowMapper<ExpoEditDTO> {

    @Override
    public ExpoEditDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExpoEditDTO expo = new ExpoEditDTO();
        OpenMode openMode = OpenMode.valueOf(rs.getString("openMode"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = rs.getString("openStart");
        String end = rs.getString("openEnd");
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if(start != null) startTime = LocalDateTime.parse(start, formatter);
        if(end != null) endTime = LocalDateTime.parse(end, formatter);

        expo.setName(rs.getString("name"));
        expo.setAvatar(rs.getString("avatar"));
        expo.setPrice(rs.getInt("price"));
        expo.setIntroduction(rs.getString("introduction"));
        expo.setOpenMode(openMode);
        expo.setOpenStatus(rs.getBoolean("openStatus"));
        expo.setOpenStart(startTime);
        expo.setOpenEnd(endTime);
        expo.setAccessCode(rs.getString("accessCode"));
        expo.setMaxParticipants(rs.getInt("maxParticipants"));
        expo.setDisplay(rs.getBoolean("display"));

        return expo;
    }
}
