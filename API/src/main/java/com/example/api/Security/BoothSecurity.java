package com.example.api.Security;

import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.Service.SingleBoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoothSecurity {
    @Autowired
    private final SingleBoothService singleBoothService;



    public boolean isOwner(Integer boothID) {
        System.out.println("BoothSecurity: isOwner >> " + boothID);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("BoothSecurity: 未登入或驗證失敗");
                return false;
            }
            String currentAccount = authentication.getName();

            BoothEditResponse booth = singleBoothService.getBoothEditByID(boothID);
            String ownerAccount = booth.getOwner();
            return currentAccount.equals(ownerAccount);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isCollaborator(Integer boothID){
        System.out.println("BoothSecurity: isCollaborator >> "+boothID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAccount = authentication.getName();

        List<String> colAccountList = singleBoothService.getAllColAccountList(boothID);
        return colAccountList.contains(currentAccount);
    }


    public boolean isStaff(Integer boothID){
        System.out.println("BoothSecurity: isStaff >> "+boothID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAccount = authentication.getName();

        List<String> staffAccountList = singleBoothService.getAllStaffAccountList(boothID);
        return staffAccountList.contains(currentAccount);
    }
}
