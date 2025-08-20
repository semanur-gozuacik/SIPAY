package Efectura.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@RunWith(Cucumber.class)
@CucumberOptions(
        // Test sonuçlarının raporlanmasını sağlayan Cucumber ayarları
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "rerun:target/rerun.txt"
        },
        // Hangi senaryoların çalıştırılacağını belirten etiket
        tags ="@spy",
        features ="src/test/resources/features",
        glue  = "Efectura/stepDefs",
        dryRun = false
)


public class TestRunner {
    // Tüm senaryoların çalıştırılmasının ardından yapılacak işlemler
    @AfterClass
    public static void

    teardown() {
        File reportOutputDirectory = new File("target/cucumber-reports");
        generateReport(reportOutputDirectory.getAbsolutePath());
    }
    // Cucumber raporlarını üreten metot
    public static void generateReport(String cucumberOutputPath) {
        Collection<File> jsonFiles = FileUtils.listFiles(new File(cucumberOutputPath), new String[] {"json"}, true);
        List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));

        // HTML raporlarını oluşturur ve hedef dizine kaydeder
        Configuration config = new Configuration(new File("target"), "Semaaa");
        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
        reportBuilder.generateReports();
    }
}
