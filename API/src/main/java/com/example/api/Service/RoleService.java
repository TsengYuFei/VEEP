package com.example.api.Service;

import com.example.api.Entity.Role;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;



    public Role getRoleByName(String name) {
        System.out.println("RoleService: getRoleByName >> "+name);
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("找不到名稱為 <"+name+" > 的角色身分"));
    }


    public Role getRoleByID(Integer id) {
        System.out.println("RoleService: getRoleByID >> "+id);
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("找不到id為 <"+id+" > 的角色身分"));
    }
}
