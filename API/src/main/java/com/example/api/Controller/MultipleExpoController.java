package com.example.api.Controller;

import com.example.api.DTO.Response.ExpoOverviewResponse;
import com.example.api.Service.MultipleExpoService;
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

@Tag(name = "多筆展會相關")
@RequestMapping("/multiple/expo")
@RestController
@RequiredArgsConstructor
public class MultipleExpoController {
    @Autowired
    private final MultipleExpoService multipleExpoService;



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
        System.out.println("MultipleExpoController: getAllExpoOverview");
        List<ExpoOverviewResponse> expos = multipleExpoService.getAllExpoOverview();
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
        System.out.println("MultipleExpoController: getAllExpoOverviewPage >> "+page+", "+size);
        Page<ExpoOverviewResponse> expos = multipleExpoService.getAllExpoOverviewPage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(expos);
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
        System.out.println("MultipleExpoController: getTagExpoOverview >> "+tagsName);
        List<ExpoOverviewResponse> expos = multipleExpoService.getTagExpoOverview(tagsName);
        return ResponseEntity.status(HttpStatus.OK).body(expos);
    }
}
