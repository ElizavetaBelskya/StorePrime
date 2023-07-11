package ru.tinkoff.storePrime.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tags(value = {
        @Tag(name = "Photos")
})
@RequestMapping("/photos")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public interface PhotoApi {


    @Operation(summary = "Добавление фотографии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Идентификатор фотографии",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }
            )
    })
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasAuthority('SELLER')")
    ResponseEntity<String> addPhoto(@RequestBody MultipartFile image) throws IOException;



    @Operation(summary = "Получение фотографии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "application/octet-stream")
            }),
            @ApiResponse(responseCode = "404", description = "Фотография не найдена")
    })
    @GetMapping("/{id}")
    ResponseEntity<ByteArrayResource> getPhoto(@PathVariable String id);


}
