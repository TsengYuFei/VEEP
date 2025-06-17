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
            summary = "以標籤獲取展會(模糊搜尋)",
            description = "只要符合部分字元即包含，關聯度排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該標籤的所有展會",
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
    public ResponseEntity<List<ExpoOverviewResponse>> getExpoOverviewByTag(
            @Parameter(description = "標籤名稱", required = true)
            @RequestParam String tagName
    ){
        System.out.println("MultipleExpoController: getExpoOverviewByTag >> "+tagName);
        List<ExpoOverviewResponse> expos = multipleExpoService.getExpoOverviewByTag(tagName);
        return ResponseEntity.status(HttpStatus.OK).body(expos);
    }


    @Operation(
            summary = "以展會名稱及描述獲取展會(模糊搜尋)",
            description = "只要符合部分字元即包含，關聯度排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該展會名稱或描述的所有展會",
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
    @GetMapping("/by_name_and_intro")
    public ResponseEntity<List<ExpoOverviewResponse>> getExpoOverviewByNameAndIntro(
            @Parameter(description = "搜尋關鍵字", required = true)
            @RequestParam String keyword
    ){
        System.out.println("MultipleExpoController: getExpoOverviewByNameAndIntro >> "+keyword);
        List<ExpoOverviewResponse> expos = multipleExpoService.getExpoOverviewByNameAndIntro(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(expos);
    }


    @Operation(
            summary = "獲取display為true的所有展會(概略)",
            description = "display >>　是否顯示於推薦頁面"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功display為true的所有展會(概略)",
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
    @GetMapping("/overview/is_display")
    public ResponseEntity<List<ExpoOverviewResponse>> getDisplayExpoOverview(){
        System.out.println("BatchExpoController: getDisplayExpoOverview");
        List<ExpoOverviewResponse> expos = multipleExpoService.getDisplayExpoOverview();
        return ResponseEntity.status(HttpStatus.OK).body(expos);
    }
}
