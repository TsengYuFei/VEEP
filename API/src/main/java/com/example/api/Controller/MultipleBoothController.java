package com.example.api.Controller;

import com.example.api.DTO.Response.BoothOverviewResponse;
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
        System.out.println("MultipleBoothController: getAllBoothOverviewPage >> "+page+", "+size);
        Page<BoothOverviewResponse> booths = multipleBoothService.getAllBoothOverviewPage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }


    @Operation(
            summary = "以標籤獲取攤位(模糊搜尋)",
            description = "只要符合部分字元即包含，關聯度排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該標籤的所有展會",
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
    @GetMapping("/overview/by_tag")
    public ResponseEntity<List<BoothOverviewResponse>> getBoothOverviewByTag(
            @Parameter(description = "標籤名稱", required = true)
            @RequestParam String tagName
    ){
        System.out.println("MultipleBoothController: getBoothOverviewByTag >> "+tagName);
        List<BoothOverviewResponse> booths = multipleBoothService.getBoothOverviewByTag(tagName);
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }


    @Operation(
            summary = "以攤位名稱及描述獲取攤位(模糊搜尋)",
            description = "只要符合部分字元即包含，關聯度排序"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功獲取含該攤位名稱或描述的所有攤位",
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
    @GetMapping("/overview/by_name_and_intro")
    public ResponseEntity<List<BoothOverviewResponse>> getBoothOverviewByNameAndIntro(
            @Parameter(description = "搜尋關鍵字", required = true)
            @RequestParam String keyword
    ){
        System.out.println("MultipleBoothController: getBoothOverviewByNameAndIntro >> "+keyword);
        List<BoothOverviewResponse> booths = multipleBoothService.getBoothOverviewByNameAndIntro(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }


    @Operation(
            summary = "獲取display為true的所有攤位(概略)",
            description = "display >>　是否顯示於推薦頁面"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功display為true的所有攤位(概略)",
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
    @GetMapping("/overview/is_display")
    public ResponseEntity<List<BoothOverviewResponse>> getDisplayBoothOverview(){
        System.out.println("BatchBoothController: getDisplayBoothOverview");
        List<BoothOverviewResponse> booths = multipleBoothService.getDisplayBoothOverview();
        return ResponseEntity.status(HttpStatus.OK).body(booths);
    }
}
