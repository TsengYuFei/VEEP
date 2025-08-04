package com.example.api.Controller;

import com.example.api.DTO.Request.BoothUpdateRequest;
import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.DTO.Request.BoothCreateRequest;
import com.example.api.DTO.Response.UserListResponse;
import com.example.api.Service.SingleBoothService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "單一攤位相關", description = "攤位內容與攤位本身的CRUD分開。")
@RequestMapping("/booth")
@RestController
@RequiredArgsConstructor
public class SingleBoothController {
    @Autowired
    private final SingleBoothService singleBoothService;



    @Operation(
            summary = "獲取攤位資訊(編輯用)",
            description = "用於攤位編輯頁面。"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得攤位資訊(編輯用)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoothEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到攤位"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/edit/{boothID}")
    public ResponseEntity<BoothEditResponse> getBoothEditByID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("SingleBoothController: getBoothEditByID >> "+boothID);
        BoothEditResponse booth = singleBoothService.getBoothEditByID(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(booth);
    }


    @Operation(
            summary = "新增攤位",
            description = "需由expo owner或collaborator新增並指定owner"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "成功新增攤位",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoothEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PreAuthorize("hasRole('FOUNDER') and (@expoSecurity.isOwner(#expoID) or @expoSecurity.isCollaborator(#expoID))")
    @PostMapping("/{expoID}/{userAccount}")
    public ResponseEntity<BoothEditResponse> createBooth(
            @Parameter(description = "攤位所屬展會的ID", required = true)
            @PathVariable Integer expoID,
            @Parameter(description = "攤位持有者的使用者帳號", required = true)
            @PathVariable String userAccount
    ){
        System.out.print("SingleBoothController: createBooth >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAccount = authentication.getName();
        System.out.println(currentAccount);

        Integer boothID = singleBoothService.createBooth(currentAccount, expoID, userAccount);
        BoothEditResponse booth = singleBoothService.getBoothEditByID(boothID);
        return ResponseEntity.status(HttpStatus.CREATED).body(booth);
    }


    @Operation(
            summary = "更新攤位",
            description = "用於攤位資料更新頁面。可更新除boothID、expoID、ownerAccount外之的欄位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新攤位資訊",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoothEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "輸入格式錯誤"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到攤位"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PreAuthorize("hasRole('FOUNDER') and (@boothSecurity.isOwner(#boothID) or @boothSecurity.isCollaborator(#boothID))")
    @PutMapping("/{boothID}")
    public ResponseEntity<BoothEditResponse> updateBoothByID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID,
            @Valid @RequestBody BoothUpdateRequest boothRequest
    ){
        System.out.println("SingleBoothController: updateBoothByID >> "+boothID);

        singleBoothService.updateBoothByID(boothID, boothRequest);
        BoothEditResponse booth = singleBoothService.getBoothEditByID(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(booth);
    }


    @Operation(summary = "刪除攤位")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "成功刪除攤位"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到攤位"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @PreAuthorize("hasRole('FOUNDER') and (@boothSecurity.isOwner(#boothID))")
    @DeleteMapping("delete/{boothID}")
    public ResponseEntity<?> deleteBoothByID(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("SingleBoothController: deleteBoothByID >> "+boothID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAccount = authentication.getName();

        singleBoothService.deleteBoothByID(boothID, userAccount);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            summary = "獲取所有合作者",
            description = "合作者可共同編輯此攤位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取所有合作者",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserListResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/collaborator/{boothID}")
    public ResponseEntity<List<UserListResponse>> getAllCollaborator(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("SingleBoothController: getAllCollaborator >> "+boothID);
        List<UserListResponse> collaborator = singleBoothService.getAllColList(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(collaborator);
    }


    @Operation(
            summary = "獲取所有員工",
            description = "員工可在活動中發傳單等，但不能編輯攤位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取所有員工",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserListResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/staff/{boothID}")
    public ResponseEntity<List<UserListResponse>> getAllStaff(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("SingleBoothController: getAllStaff >> "+boothID);
        List<UserListResponse> staff = singleBoothService.getAllStaff(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(staff);
    }
}
