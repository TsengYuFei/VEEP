package com.example.api.Dao;

import com.example.api.DTO.UserAllInformationDTO;
import com.example.api.DTO.UserDetailDTO;
import com.example.api.DTO.UserOverviewDTO;
import com.example.api.Request.UserCreateRequest;
import com.example.api.Request.UserUpdateRequest;
import com.example.api.RowMapper.UserAllInformationRowMapper;
import com.example.api.RowMapper.UserDetailRowMapper;
import com.example.api.RowMapper.UserOverviewRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public UserDetailDTO getUserDetailByAccount(String account){
        System.out.println("UserDao: getUserDetailByAccount >> "+account);
        String sql = "SELECT name, userAccount, avatar, bio, background," +
                "showFollowers, showFollowing, showHistory, showCurrentExpo, showCurrentBooth, role " +
                "FROM user " +
                "WHERE userAccount = :account";

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);

        List<UserDetailDTO> userList = namedParameterJdbcTemplate.query(sql, map, new UserDetailRowMapper());

        if(!userList.isEmpty()) return userList.get(0);
        else return null;
    }


    public UserOverviewDTO getUserOverviewByAccount(String account){
        System.out.println("UserDao: getUserOverviewByAccount >> "+account);
        String sql = "SELECT name, userAccount, avatar, role FROM user WHERE userAccount = :account";

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);

        List<UserOverviewDTO> userList = namedParameterJdbcTemplate.query(sql, map, new UserOverviewRowMapper());

        if(!userList.isEmpty()) return userList.get(0);
        else return null;
    }


    public UserAllInformationDTO getUserAllInformationByAccount(String account){
        System.out.println("UserDao: getUserAllInformationByAccount >> "+account);
        String sql = "SELECT name, tel, mail, avatar, birthday, bio, background," +
                "showFollowers, showFollowing, showHistory, showCurrentExpo, showCurrentBooth, role " +
                "FROM user " +
                "WHERE userAccount = :account";

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);

        List<UserAllInformationDTO> userList = namedParameterJdbcTemplate.query(sql, map, new UserAllInformationRowMapper());

        if(!userList.isEmpty()) return userList.get(0);
        else return null;
    }


    public String createUser(UserCreateRequest request){
        System.out.println("UserDao: createUser");
        String sql = "INSERT INTO user (name, userAccount, password, tel, mail, avatar, birthday) " +
                "VALUES (:name, :userAccount, :password, :tel, :mail, :avatar, :birthday)";

        Map<String, Object> map = new HashMap<>();
        map.put("name", request.getName());
        map.put("userAccount", request.getUserAccount());
        map.put("password", request.getPassword());
        map.put("tel", request.getTel());
        map.put("mail", request.getMail());
        map.put("avatar", request.getAvatar());
        map.put("birthday", request.getBirthday());

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));

        return map.get("userAccount").toString();
    }


    public void updateUserByAccount(String account, UserUpdateRequest request){
        System.out.println("UserDao: updateUserByAccount >> "+account);
        String sql = "UPDATE user SET name = :name, tel = :tel, mail = :mail, avatar = :avatar," +
                " birthday = :birthday, bio = :bio, background = :background, showFollowers = :showFollowers," +
                " showFollowing = :showFollowing, showHistory = :showHistory," +
                " showCurrentExpo = :showCurrentExpo, showCurrentBooth = :showCurrentBooth, role = :role" +
                " WHERE userAccount = :account";

        Map<String, Object> map = new HashMap<>();

        if(request.getAvatar().isBlank()) map.put("avatar", null);
        else map.put("avatar", request.getAvatar());

        if(request.getBackground().isBlank()) map.put("background", null);
        else map.put("background", request.getBackground());

        map.put("name", request.getName());
        map.put("tel", request.getTel());
        map.put("mail", request.getMail());
        map.put("birthday", request.getBirthday());
        map.put("bio", request.getBio());
        map.put("showFollowers", request.isShowFollowers());
        map.put("showFollowing", request.isShowFollowing());
        map.put("showHistory", request.isShowHistory());
        map.put("showCurrentExpo", request.isShowCurrentExpo());
        map.put("showCurrentBooth", request.isShowCurrentExpo());
        map.put("role", request.getRole().toString());
        map.put("account", account);

        namedParameterJdbcTemplate.update(sql, map);
    }


    public void deleteUserByAccount(String account){
        System.out.println("UserDao: updateUser >> "+account);
        String sql = "DELETE FROM user WHERE userAccount = :account";

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);

        namedParameterJdbcTemplate.update(sql, map);
    }
}