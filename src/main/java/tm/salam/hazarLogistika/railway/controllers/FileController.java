package tm.salam.hazarLogistika.railway.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/src/main/resources")
public class FileController {

    @Value("${upload.imagePath}")
    private String imagePath;

    @GetMapping(path = "/static/imageUsers/{file}",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("file")String fileName) throws IOException {

        File image=new File(imagePath+"/"+fileName);

        if(image.exists()){

            return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().
                    getContentType(image))).body(Files.readAllBytes(image.toPath()));
        }else{

            return null;
        }
    }
}