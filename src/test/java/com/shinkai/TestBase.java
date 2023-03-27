package com.shinkai;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import net.lingala.zip4j.ZipFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class TestBase {
    private final ClassLoader cl = TestBase.class.getClassLoader();

    @BeforeEach
    void unzipFiles() throws Exception {
        File zipFile = getZipFile();
        File unzipPath = getDestination(zipFile);
        try (ZipFile filesZip = new ZipFile(zipFile)) {
            filesZip.extractAll(String.valueOf(unzipPath));
        }
    }

    @AfterEach
    void removeUnzipped() throws Exception {
        deleteFolder(new File(Objects.requireNonNull(cl.getResource(getZipProperty("unzip.destination"))).getPath()));
    }

    private File getDestination(File zipFile) throws Exception {
        return new File(zipFile.getParent() + "/" + getZipProperty("unzip.destination"));
    }

    private File getZipFile() throws Exception {
        String zipArchive = getZipProperty("zip.file");
        URL zipURL = cl.getResource(zipArchive);
        assert zipURL != null;
        return new File(zipURL.toURI());
    }

    public PDF getPdfFile(String fileName) throws Exception {
        URL pdfURL = cl.getResource(getZipProperty("unzip.destination") + "/" + fileName + ".pdf");
        assert pdfURL != null;
        return new PDF(pdfURL);
    }

    public XLS getXlsxFile(String fileName) throws Exception {
        URL xlsxURL = cl.getResource(getZipProperty("unzip.destination") + "/" + fileName + ".xlsx");
        assert xlsxURL != null;
        return new XLS(xlsxURL);
    }

    public List<String[]> getCsvContent(String fileName) throws Exception {
        try (InputStream is = cl.getResourceAsStream(getZipProperty("unzip.destination") + "/" + fileName + ".csv")) {
            assert is != null;
            try (InputStreamReader isr = new InputStreamReader(is)) {
                CSVReader csvReader = new CSVReader(isr);
                return csvReader.readAll();
            }
        }
    }

    private static void deleteFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
            folder.delete();
        }
    }

    private String getZipProperty(String propName) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("src/test/resources/files-vars.properties"));
        return properties.getProperty(propName);
    }
}
