package com.example.api.Security;

import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.Entity.Expo;
import com.example.api.Service.ExpoHelperService;
import com.example.api.Service.SingleExpoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpoSecurity {
    private final ExpoHelperService expoHelperService;
    private final SingleExpoService singleExpoService;



    public boolean isOwner(Integer expoID) {
        System.out.println("ExpoSecurity: isOwner >> " + expoID);
        expoHelperService.getExpoByID(expoID);
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("ExpoSecurity: 未登入或驗證失敗");
                return false;
            }
            String currentAccount = authentication.getName();

            ExpoEditResponse expo = singleExpoService.getExpoEditByID(expoID);
            String ownerAccount = expo.getOwner();
            return currentAccount.equals(ownerAccount);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isCollaborator(Integer expoID){
        System.out.println("ExpoSecurity: isCollaborator >> " + expoID);
        expoHelperService.getExpoByID(expoID);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("ExpoSecurity: 未登入或驗證失敗");
                return false;
            }
            String currentAccount = authentication.getName();

            List<String> colAccountList = singleExpoService.getAllColAccountList(expoID);
            if (colAccountList == null) return false;
            return colAccountList.contains(currentAccount);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
