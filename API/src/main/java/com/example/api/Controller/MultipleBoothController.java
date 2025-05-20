package com.example.api.Controller;

import com.example.api.DTO.Response.BoothOverviewResponse;
import com.example.api.DTO.Response.UserListResponse;
import com.example.api.Service.MultipleBoothService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "多筆攤位相關")
@RequestMapping("/multiple/booth")
@RestController
@RequiredArgsConstructor
public class MultipleBoothController {
    @Autowired
    private final MultipleBoothService multipleBoothService;



    @Operation(
            summary = "獲取所有攤位(概略)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得所有攤位(概略)",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = BoothOverviewResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/overview")
    public ResponseEntity<List<BoothOverviewResponse>> getAllBoothOverview(){
        System.out.println("BatchBoothController: getAllBoothOverview");
        List<BoothOverviewResponse> booths = multipleBoothService.getAllBoothOverview();
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }

    @Operation(
            summary = "獲取所有攤位(概略)-分頁版"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得所有攤位(概略)-分頁版",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = BoothOverviewResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/overview/page")
    public ResponseEntity<Page<BoothOverviewResponse>> getAllBoothOverviewPage(
            @Parameter(description = "頁數(第幾頁)", required = true)
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "數量(一頁幾筆資料)", required = true)
            @RequestParam(defaultValue = "5") Integer size
    ){
        System.out.println("BatchBoothController: getAllBoothOverviewPage >> "+page+", "+size);
        Page<BoothOverviewResponse> booths = multipleBoothService.getAllBoothOverviewPage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }


    @Operation(
            summary = "獲取某攤位的所有合作者",
            description = "合作者可共同編輯此攤位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該攤位的所有合作者",
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
        System.out.println("BatchBoothController: getAllCollaborator >> "+boothID);
        List<UserListResponse> collaborator = multipleBoothService.getAllCollaborator(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(collaborator);
    }


    @Operation(
            summary = "獲取某攤位的所有員工",
            description = "員工可在活動中發傳單等，但不能編輯攤位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該攤位的所有員工",
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
        System.out.println("BatchBoothController: getAllStaff >> "+boothID);
        List<UserListResponse> staff = multipleBoothService.getAllStaff(boothID);
        return ResponseEntity.status(HttpStatus.OK).body(staff);
    }


    @Operation(
            summary = "獲取有某tag的所有攤位",
            description = "只要符合部分字元即包含，關聯度排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該tag的所有攤位",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = BoothOverviewResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/by_tag")
    public ResponseEntity<List<BoothOverviewResponse>> getTagBoothOverview(
            @Parameter(description = "標籤名稱", required = true)
            @RequestParam String tagsName
    ){
        System.out.println("BatchBoothController: getTagBoothOverview >> "+tagsName);
        List<BoothOverviewResponse> booths = multipleBoothService.getTagBoothOverview(tagsName);
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }
}
