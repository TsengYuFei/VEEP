package com.example.api.Controller;

import com.example.api.DTO.Response.BoothOverviewResponse;
import com.example.api.DTO.Response.ExpoOverviewResponse;
import com.example.api.DTO.Response.UserListResponse;
import com.example.api.Service.BatchExpoService;
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

@Tag(name = "批量展會相關")
@RequestMapping("/batch/expo")
@RestController
@RequiredArgsConstructor
public class BatchExpoController {
    @Autowired
    private final BatchExpoService batchExpoService;



    @Operation(
            summary = "獲取所有展會(概略)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得所有展會(概略)",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ExpoOverviewResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/overview")
    public ResponseEntity<List<ExpoOverviewResponse>> getAllExpoOverview(){
        System.out.println("BatchExpoController: getAllExpoOverview");
        List<ExpoOverviewResponse> expos = batchExpoService.getAllExpoOverview();
        return ResponseEntity.status(HttpStatus.OK).body(expos);
    }

    @Operation(
            summary = "獲取所有展會(概略)-分頁版"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功取得所有展會(概略)-分頁版",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ExpoOverviewResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/overview/page")
    public ResponseEntity<Page<ExpoOverviewResponse>> getAllExpoOverviewPage(
            @Parameter(description = "頁數(第幾頁)", required = true)
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "數量(一頁幾筆資料)", required = true)
            @RequestParam(defaultValue = "5") Integer size
    ){
        System.out.println("BatchExpoController: getAllExpoOverviewPage >> "+page+", "+size);
        Page<ExpoOverviewResponse> expos = batchExpoService.getAllExpoOverviewPage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(expos);
    }


    @Operation(
            summary = "獲取某展會的所有合作者",
            description = "合作者可共同編輯此攤位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該展會的所有合作者",
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
    @GetMapping("/collaborator/{expoID}")
    public ResponseEntity<List<UserListResponse>> getAllCollaborator(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("BatchExpoController: getAllCollaborator >> "+expoID);
        List<UserListResponse> collaborator = batchExpoService.getAllCollaborator(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(collaborator);
    }


    @Operation(
            summary = "獲取某展會黑名單中的所有使用者",
            description = "黑名單中的使用者不可進入此展會"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該展會黑名單中的所有使用者",
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
    @GetMapping("/blacklist/{expoID}")
    public ResponseEntity<List<UserListResponse>> getAllBlack(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("BatchExpoController: getAllBlack >> "+expoID);
        List<UserListResponse> blacklisted = batchExpoService.getAllBlack(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(blacklisted);
    }


    @Operation(
            summary = "獲取某展會白名單中的所有使用者",
            description = "白名單中的使用者不可進入此展會"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該展會白名單中的所有使用者",
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
    @GetMapping("/whitelist/{expoID}")
    public ResponseEntity<List<UserListResponse>> getAllWhite(
            @Parameter(description = "展會ID", required = true)
            @PathVariable Integer expoID
    ){
        System.out.println("BatchExpoController: getAllWhite >> "+expoID);
        List<UserListResponse> whitelisted = batchExpoService.getAllWhite(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(whitelisted);
    }


    @Operation(
            summary = "獲取有某tag的所有展會",
            description = "只要符合部分字元即包含，關聯度排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該tag的所有展會",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ExpoOverviewResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "伺服器錯誤"
            )
    })
    @GetMapping("/by_tag")
    public ResponseEntity<List<ExpoOverviewResponse>> getTagExpoOverview(
            @Parameter(description = "標籤名稱", required = true)
            @RequestParam String tagsName
    ){
        System.out.println("BatchExpoController: getTagExpoOverview >> "+tagsName);
        List<ExpoOverviewResponse> expos = batchExpoService.getTagExpoOverview(tagsName);
        return ResponseEntity.status(HttpStatus.OK).body(expos);
    }


    @Operation(
            summary = "獲取某展會中的所有攤位"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取該展會中的所有攤位",
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
    @GetMapping("/booths/{expoID}")
    public ResponseEntity<List<BoothOverviewResponse>> getAllBoothOverview(
            @Parameter(description = "展會ID", required = true)
            @RequestParam Integer expoID
    ){
        System.out.println("BatchExpoController: getAllBoothOverview >> "+expoID);
        List<BoothOverviewResponse> booths = batchExpoService.getAllBoothOverview(expoID);
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }
}
