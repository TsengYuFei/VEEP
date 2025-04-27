package com.example.api.RowMapper;

import com.example.api.DTO.BoothEditDTO;
import com.example.api.Model.OpenMode;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BoothEditRowMapper implements RowMapper<BoothEditDTO> {
    @Override
    public BoothEditDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        BoothEditDTO booth = new BoothEditDTO();
        OpenMode openMode = OpenMode.valueOf(rs.getString("openMode"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = rs.getString("openStart");
        String end = rs.getString("openEnd");
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if(start != null) startTime = LocalDateTime.parse(start, formatter);
        if(end != null) endTime = LocalDateTime.parse(end, formatter);

        booth.setName(rs.getString("name"));
        booth.setAvatar(rs.getString("avatar"));
        booth.setIntroduction(rs.getString("introduction"));
        booth.setOpenMode(openMode);
        booth.setOpenStatus(rs.getBoolean("openStatus"));
        booth.setOpenStart(startTime);
        booth.setOpenEnd(endTime);
        booth.setMaxParticipants(rs.getInt("maxParticipants"));
        booth.setDisplay(rs.getBoolean("display"));

        return booth;
    }
}
