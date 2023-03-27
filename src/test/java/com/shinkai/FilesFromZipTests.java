package com.shinkai;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import net.lingala.zip4j.ZipFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;


public class FilesFromZipTests {

    private static final ClassLoader cl = FilesFromZipTests.class.getClassLoader();
    static String zipFile = "lesson10_files.zip";
    static String unzipFolder = "unzipped/";

    @BeforeAll
    static void unzipFiles() throws Exception {
        URL zipURL = cl.getResource(zipFile);
        assert zipURL != null;
        File zip = new File(zipURL.toURI());
        File unzipPath = new File(zip.getParent() + "/" + unzipFolder);
        try (ZipFile filesZip = new ZipFile(zip)) {
            filesZip.extractAll(String.valueOf(unzipPath));
        }
    }

    @Test
    void pfdTest() throws Exception {
        URL pdfURL = cl.getResource(unzipFolder + "CSS_SelectorList.pdf");
        assert pdfURL != null;
        PDF pdf = new PDF(pdfURL);
        Assertions.assertTrue(pdf.text.contains("In CSS, selectors are patterns used to select the element"));
        Assertions.assertEquals("Brian Ireson", pdf.author);
    }

    @Test
    void xlsxTest() throws Exception {

        URL xlsxURL = cl.getResource(unzipFolder + "API_tests.xlsx");
        assert xlsxURL != null;
        XLS xlsx = new XLS(xlsxURL);

        Assertions.assertEquals("https://t.me/protestinginfo",
                xlsx.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
        Assertions.assertEquals("Пример позитивного API-теста",
                xlsx.excel.getSheetAt(0).getRow(3).getCell(0).getStringCellValue());
        Assertions.assertEquals("Пример негативного API-теста",
                xlsx.excel.getSheetAt(0).getRow(17).getCell(0).getStringCellValue());
        Assertions.assertTrue(xlsx.excel.getSheetAt(0).getRow(0).getCell(3).getStringCellValue()
                .contains("https://petstore.swagger.io/"));
    }

    @Test
    void csvTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream(unzipFolder + "MOCK_DATA.csv")) {
            assert is != null;
            try (InputStreamReader isr = new InputStreamReader(is)) {
                CSVReader csvReader = new CSVReader(isr);
                List<String[]> content = csvReader.readAll();
                Assertions.assertArrayEquals(new String[]{"id", "first_name", "last_name", "email", "gender", "ip_address"},
                        content.get(0));
            }
        }
    }
}
