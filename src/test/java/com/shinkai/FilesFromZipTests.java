package com.shinkai;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


public class FilesFromZipTests extends TestBase {

    @Test
    void pfdTest() throws Exception {
        PDF pdf = getPdfFile("CSS_SelectorList");
        Assertions.assertTrue(pdf.text.contains("In CSS, selectors are patterns used to select the element"));
        Assertions.assertEquals("Brian Ireson", pdf.author);
    }

    @Test
    void xlsxTest() throws Exception {
        XLS xlsx = getXlsxFile("API_tests");

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
        List<String[]> content = getCsvContent("MOCK_DATA");
        Assertions.assertArrayEquals(new String[]{"id", "first_name", "last_name", "email", "gender", "ip_address"},
                content.get(0));
    }
}
