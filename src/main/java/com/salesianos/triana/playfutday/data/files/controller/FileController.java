package com.salesianos.triana.playfutday.data.files.controller;

import com.salesianos.triana.playfutday.data.files.dto.FileResponse;
import com.salesianos.triana.playfutday.data.files.service.StorageService;
import com.salesianos.triana.playfutday.data.files.utils.MediaTypeUrlResource;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;
/*

    @PostMapping("/upload/files")
    public ResponseEntity<?> upload(@RequestPart("files") MultipartFile[] files) {
        List<FileResponse> result = Arrays.stream(files)
                .map(this::uploadFile)
                .toList();

        return ResponseEntity
                //.created(URI.create(response.getUri()))
                .status(HttpStatus.CREATED)
                .body(result);
    }*/

    @Operation(summary = "Este método se encarga de subir un nueva imagen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha agregado correctamente la imagen",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                    messi.jpg
                                    }
                                    """))}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petición de POST a otra distinta, ejemplo GET",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "No estas agregando la imagen",
                    content = @Content)
    })
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) {

        FileResponse response = uploadFile(file);

        return ResponseEntity.created(URI.create(response.getUri())).body(response);
    }

    private FileResponse uploadFile(MultipartFile file) {
        String name = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        return FileResponse.builder()
                .name(name)
                .size(file.getSize())
                .type(file.getContentType())
                .uri(uri)
                .build();
    }

    @Operation(summary = "Este obtiene la imagen de posts por su nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Sa han devuelto la imagen correctamente",
                    content = {@Content(mediaType = "application/json"
                    )}),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petición de GET a otra distinta, ejemplo POST",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Estas intentando pasar cuerpo a la petición que no lo requiere", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado la imagen", content = @Content),
            @ApiResponse(responseCode = "401", description = "No estas logeado", content = @Content),
    })
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        MediaTypeUrlResource resource =
                (MediaTypeUrlResource) storageService.loadAsResource(filename);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", resource.getType())
                .body(resource);
    }

}
