package com.example.api.Controller;

import com.example.api.DTO.Request.BoothCoordinateUpdateRequest;
import com.example.api.DTO.Request.BoothCreateRequest;
import com.example.api.DTO.Request.BoothUpdateRequest;
import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.DTO.Response.ContentResponse;
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
    @PostMapping("/create/{expoID}")
    public ResponseEntity<BoothEditResponse> createBooth(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Valid @RequestBody BoothCreateRequest boothRequest
    ){
        System.out.print("SingleBoothController: createBooth >> ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAccount = authentication.getName();
        System.out.println(currentAccount);

        Integer boothID = singleBoothService.createBooth(currentAccount, expoID, boothRequest);
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


    @Operation(
            summary = "更新攤位座標",
            description = "只能由expo owner或collaborator更新"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新攤位座標",
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
    @PreAuthorize("hasRole('FOUNDER') and (@expoSecurity.isOwner(#expoID) or @expoSecurity.isCollaborator(#expoID))")
    @PutMapping("update/coordinate/{expoID}")
    public ResponseEntity<BoothEditResponse> updateBoothCoordinateByID(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Valid @RequestBody BoothCoordinateUpdateRequest boothRequest
    ){
        System.out.println("SingleBoothController: updateBoothCoordinateByID >> "+expoID);

        singleBoothService.updateBoothCoordinateByID(expoID, boothRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(
            summary = "刪除攤位",
            description = "刪除封面圖片、所有攤位內容、所有booth & content log"
    )
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
                    responseCode = "404",
                    description = "找不到攤位"
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
                    responseCode = "404",
                    description = "找不到攤位"
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


    @Operation(
            summary = "用expoID和座標獲取是否存在攤位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得是否存在攤位",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到展會"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/has_booth_or_not/{expoID}/{coordinateX}/{coordinateY}")
    public ResponseEntity<Boolean> hasBoothByExpoIDAndCoordinate(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID,
            @Parameter(description = "X座標", required = true)
            @PathVariable Integer coordinateX,
            @Parameter(description = "Y座標", required = true)
            @PathVariable Integer coordinateY
    ){
        System.out.println("SingleBoothController: hasBoothByExpoIDAndCoordinate >> "+expoID+", "+coordinateX+", "+coordinateY);

        Boolean hasBooth = singleBoothService.hasBoothByExpoIDAndCoordinate(expoID, coordinateX, coordinateY);
        return ResponseEntity.status(HttpStatus.OK).body(hasBooth);
    }


    @Operation(
            summary = "獲取所有攤位內容"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取所有攤位內容",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ContentResponse.class)
                            )
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
    @GetMapping("/content/{boothID}")
    public ResponseEntity<List<ContentResponse>> getAllContent(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("SingleBoothController: getAllContent >> "+boothID);
        List<ContentResponse> content = singleBoothService.getAllContentList(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(content);
    }


    @Operation(
            summary = "獲取攤位是否開放中"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得攤位是否開放中",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
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
    @GetMapping("/is_opening_or_not/{boothID}")
    public ResponseEntity<Boolean> getOpeningOrNot(
            @Parameter(description = "攤位ID", required = true)
            @PathVariable Integer boothID
    ){
        System.out.println("SingleBoothController: getOpeningOrNot >> "+boothID);

        Boolean isOpening = singleBoothService.isOpening(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(isOpening);
    }
}
