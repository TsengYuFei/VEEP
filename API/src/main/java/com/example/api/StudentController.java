package com.example.api;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StudentController {
//    @PostMapping("/students")
//    public String create(@RequestBody Student student) {
//        return "執行 create student 操作";
//    }
//
//    @GetMapping("/students/{studentId}")
//    public String read(@PathVariable Integer studentId){
//        return "執行 get student 操作";
//    }
//
//    @PutMapping("/students/{studentId}")
//    public String update(@RequestBody Student student, @PathVariable Integer studentId){
//        return "執行 update student 操作";
//    }
//
//    @DeleteMapping("/students/{studentId}")
//    public String delete(@PathVariable Integer studentId){
//        return "執行 delete student 操作";
//    }

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @RequestMapping("/students")
    public String insert(@RequestBody Student student) {
        String sql = "INSERT INTO student(id, name) VALUES (:studentId, :studentName)";
        Map<String, Object> map = new HashMap<>();

        map.put("studentId", student.getId());
        map.put("studentName", student.getName());

        namedParameterJdbcTemplate.update(sql, map);
        return "執行 INSERT SQL 指令";
    }
}
