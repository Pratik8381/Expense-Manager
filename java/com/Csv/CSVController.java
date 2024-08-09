package com.Csv;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/expenses/csv")
public class CSVController {

    @Autowired
    private CSVFileService csvFileService;

    @PostMapping("/generate")
    public String generateCSV() {
        csvFileService.generateCSV();
        return "CSV file generation initiated!";
    }

    @PostMapping("/upload/csv")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) throws CsvValidationException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a CSV file to upload", HttpStatus.BAD_REQUEST);
        }

        int count = csvFileService.importCSV(file);
        return ResponseEntity.ok("Successfully imported " + count + " expenses from CSV file");
    }
}
