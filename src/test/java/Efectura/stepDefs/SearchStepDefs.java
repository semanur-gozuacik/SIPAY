package Efectura.stepDefs;

import Efectura.pages.SearchPage;
import Efectura.utilities.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SearchStepDefs extends BaseStep {

    @When("The user enter {string} to search input")
    public void theUserEnterSenaToSearchInput(String searchInput) {
        pages.searchPage().getSearchInput().sendKeys(searchInput + Keys.ENTER);
        BrowserUtils.wait(2);
    }

    @Then("The user verify record titles with {string}")
    public void theUserVerifyRecordTitlesWithSena(String searchValue) {

        System.out.println(pages.searchPage().getRecordTitles().stream()
                .map(WebElement::getText).collect(Collectors.toList()));

        boolean allMatch = pages.searchPage().getRecordTitles().stream()
                .map(WebElement::getText)
                .allMatch(title -> title.toLowerCase().contains(searchValue.toLowerCase()));

//        Assert.assertTrue("Tüm Title'lar ilgili arama kelimesini içermiyor",allMatch);

    }

    @When("The user select {string} in deal status filter")
    public void theUserSelectProspectInDealStatusFilter(String expectedDealStatusValue) {
        pages.searchPage().getDealStatusSelect().click();
        pages.searchPage().getDealStatusSearchInput().sendKeys(expectedDealStatusValue);

        pages.searchPage().getDealStatusOptions().stream()
                .filter(el -> el.getText().trim().equals(expectedDealStatusValue))
                .findFirst()
                .ifPresent(WebElement::click);

        BrowserUtils.wait(1);
        BrowserUtils.waitForVisibility(pages.searchPage().getGoToRelatedRecordButtons().get(0),45);

    }

    @Then("The user verify deal status filter with {string}")
    public void theUserVerifyDealStatusFilterWithProspect(String expectedStatus) {

        System.out.println(pages.searchPage().getDealStatusValuesResult().stream()
                .map(WebElement::getText).collect(Collectors.toList()));

        boolean allMatch = pages.searchPage().getDealStatusValuesResult().stream()
                .map(WebElement::getText)
                .allMatch(title -> title.toLowerCase().contains(expectedStatus.toLowerCase()));

        Assert.assertTrue("Deal Durumları Filtredeki Aramayla Eşleşmiyor",allMatch);
    }

    @When("The user click deal status reset button")
    public void theUserClickDealStatusResetButton() {
        pages.searchPage().getDealStatusResetButton().click();
    }

    @And("The user verify Reset button func for deal status filter")
    public void theUserVerifyResetButtonFuncForDealStatusFilter() {
        System.out.println("Reset Sonrası Deal filter: " + pages.searchPage().getSelectedDealStatusText().getText());
        Assert.assertEquals("Deal Durumu Resetlenmedi","Deal Durumu",
                pages.searchPage().getSelectedDealStatusText().getText());
    }

    @When("The user click create date sort button")
    public void theUserClickCreateDateSortButton() {
        pages.searchPage().getCreateDateSortButton().click();
        BrowserUtils.wait(3);
    }

    @Then("The user verify create date descending sort")
    public void theUserVerifyCreateDateDescendingSort() {
        List<String> createDates = pages.searchPage().getCreateDateValues().stream()
                .map(WebElement::getText).toList();

        System.out.println("Desc: " + createDates);
        Assert.assertTrue(SearchPage.isSortedDescending(createDates,"dd-MM-yyyy HH:mm:ss"));

    }

    @Then("The user verify create date ascending sort")
    public void theUserVerifyCreateDateAscendingSort() {
        List<String> createDates = pages.searchPage().getCreateDateValues().stream()
                .map(WebElement::getText).toList();

        System.out.println("Asc: " + createDates);

        Assert.assertTrue(SearchPage.isSortedAscending(createDates,"dd-MM-yyyy HH:mm:ss"));
    }

    @When("The user select date filter {int} {int} {int}")
    public void theUserSelectDateFilter(int day, int month, int year) {
        BrowserUtils.wait(1);
        DateFilterUtils df = new DateFilterUtils(Driver.getDriver(), Duration.ofSeconds(10));
        String locate = "//button[.//span[normalize-space()='Oluşturulma Tarihi']]";
        df.selectCreatedAt(LocalDate.of(year, month, day),locate);
        BrowserUtils.wait(2);
    }

    @Then("The user verify create date filter {int} {int} {int}")
    public void theUserVerifyCreateDateFilter(int day, int month, int year) {
        List<String> createDates = pages.searchPage().getCreateDateValues().stream()
                .map(WebElement::getText).toList();

        boolean allMatch;

        for (String createDate : createDates) {
            System.out.println(createDate.split(" ")[2]);

            LocalDate expectedDate = LocalDate.of(year, month, day);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate actualDate = LocalDate.parse(createDate.split(" ")[2], formatter);

            if (!expectedDate.equals(actualDate)) {
                allMatch = false;
                break;
            }

        }

    }

    @When("The user click create date reset button")
    public void theUserClickCreateDateResetButton() {
        pages.searchPage().getCreateDateResetFilter().click();
    }

    @Then("The user verify Reset button func for create date filter")
    public void theUserVerifyResetButtonFuncForCreateDateFilter() {
        System.out.println("Reset Sonrası Create Date: " + pages.searchPage().getSelectedCreateDateText().getText());

        Assert.assertEquals("Create Date Sıfırlanmadı",
                "Oluşturulma Tarihi",pages.searchPage().getSelectedCreateDateText().getText());
    }

    @When("The user click create new record button")
    public void theUserClickCreateNewRecordButton() {
        pages.searchPage().getCreateNewRecordButton().click();
    }

    String randomValue;
    @When("The user fill and save the form")
    public void theUserFillAndSaveTheForm() {
        SelectFilterUtils sf = new SelectFilterUtils(Driver.getDriver(), Duration.ofSeconds(10));

        randomValue = UUID.randomUUID().toString();
        System.out.println(randomValue);

        for (WebElement select : pages.formsPage().getFormSelects()) {
            if (select.getAttribute("id").equals("salesRep"))
                continue;
            sf.chooseRandom(select);
        }

        for (WebElement input : pages.formsPage().getFormInputs()) {
            input.sendKeys(randomValue);
        }

        pages.formsPage().getFormSaveButton().click();
        BrowserUtils.wait(1);
        Driver.getDriver().get("https://sipayapp.efectura.com/login");
        BrowserUtils.wait(2);

    }

    @Then("The user verify record is created")
    public void theUserVerifyRecordIsCreated() {
        BrowserUtils.wait(2);
        System.out.println("Filtre Öncesi random: " + randomValue);

        for (char c : randomValue.toCharArray()) {
            pages.searchPage().getSearchInput().sendKeys(Character.toString(c));
//            BrowserUtils.wait((int) 0.5); // 50ms gecikme1
        }

        BrowserUtils.wait(1);
        pages.searchPage().getSearchButton().click();
        BrowserUtils.wait(1);

        System.out.println(pages.searchPage().getRecordTitles().stream()
                .map(WebElement::getText).collect(Collectors.toList()));

        boolean allMatch = pages.searchPage().getRecordTitles().stream()
                .map(WebElement::getText)
                .allMatch(title -> title.toLowerCase().contains(randomValue.toLowerCase()));

        boolean isCreated = pages.searchPage().getRecordTitles().size() > 0;

        Assert.assertTrue("Title'lar içinde yeni oluşturulan bulunamadı",allMatch && isCreated);

    }

    @When("The user click related record button at row {int}")
    public void theUserClickRelatedRecordButtonAtRow(int rowNumber) {
        pages.searchPage().getGoToRelatedRecordButtons().get(rowNumber).click();
    }

    @Then("The user verify {string} form is open")
    public void theUserVerifyFormIsOpen(String formName) {
        BrowserUtils.wait(2);
        Assert.assertEquals(formName, Driver.getDriver().
                findElement(By.xpath("//*[@id='root']/div/main/div/div/div[1]//h3")).getText());

    }

    @Then("The user verify table no result")
    public void theUserVerifyTableNoResult() {
        BrowserUtils.wait(1);
        BrowserUtils.waitForVisibility(pages.searchPage().getSearchInput(),30);

        for (char c : randomValue.toCharArray()) {
            pages.searchPage().getSearchInput().sendKeys(Character.toString(c));
        }
        pages.searchPage().getSearchButton().click();
        BrowserUtils.wait(1);

        Assert.assertEquals("Kendisine atalı olmayan kaydı görüyor",
                "Kayıt bulunamadı",pages.generalPage().getTableNoContentText().getText());

    }

    @Then("The user verify table with record")
    public void theUserVerifyTableWithRecord() {

        BrowserUtils.wait(1);

        for (char c : randomValue.toCharArray()) {
            pages.searchPage().getSearchInput().sendKeys(Character.toString(c));
        }
        pages.searchPage().getSearchButton().click();
        BrowserUtils.wait(1);

        System.out.println(pages.searchPage().getRecordTitles().stream()
                .map(WebElement::getText).collect(Collectors.toList()));

        boolean allMatch = pages.searchPage().getRecordTitles().stream()
                .map(WebElement::getText)
                .allMatch(title -> title.toLowerCase().contains(randomValue.toLowerCase()));

        Assert.assertTrue("Kendine Atalı Kaydı Göremiyor!! ",allMatch);

    }

    @Then("The user verify table no result for created case")
    public void theUserVerifyTableNoResultForCreatedCase() {
        BrowserUtils.wait(2);
        BrowserUtils.waitForVisibility(pages.searchPage().getSearchInput(),30);

        for (char c : randomValue.toCharArray()) {
            pages.searchPage().getSearchInput().sendKeys(Character.toString(c));
        }
        pages.searchPage().getSearchButton().click();
        BrowserUtils.wait(3);

        Assert.assertEquals("Kendisinin oluşturmadığı kaydı benim oluşturduklarım listesinde görüyor",
                "Kayıt bulunamadı",pages.generalPage().getTableNoContentText().getText());
    }

    @When("The user fill the form partially")
    public void theUserFillTheFormPartially() {

        SelectFilterUtils sf = new SelectFilterUtils(Driver.getDriver(), Duration.ofSeconds(10));

        randomValue = UUID.randomUUID().toString();
        System.out.println(randomValue);

        for (WebElement select : pages.formsPage().getFormSelects()) {
            if (select.getAttribute("id").equals("salesRep") || select.getAttribute("id").equals("source"))
                continue;
            sf.chooseRandom(select);
        }

        for (WebElement input : pages.formsPage().getFormInputs()) {
            input.sendKeys(randomValue);
        }

    }

    String validPhone;
    String validEmail;
    @When("The user fill the prospect form")
    public void theUserFillTheProspectForm() {
        SelectFilterUtils sf = new SelectFilterUtils(Driver.getDriver(), Duration.ofSeconds(1));

        randomValue = UUID.randomUUID().toString();
        System.out.println(randomValue);

        for (WebElement select : pages.formsPage().getFormSelects()) {
            if (select.getAttribute("id").equals("salesRep") || select.getAttribute("id").equals("prospectDurum"))
                continue;
            sf.chooseRandom(select);
        }

        sf.choose(Driver.getDriver().findElement(By.id("prospectDurum")),"Ulaşılamadı");

        List<String> needFormatInputs = List.of("phone", "email", "estVolume");

        for (WebElement input : pages.formsPage().getFormInputs()) {
            if (!needFormatInputs.contains(input.getAttribute("id"))) {
                input.sendKeys(randomValue);
            }
            if (input.getAttribute("id").equals("phone")) {
                validPhone = BrowserUtils.generateTurkishMobileNumber();
                input.sendKeys(validPhone);
            }
            if (input.getAttribute("id").equals("email")) {
                validEmail = BrowserUtils.generateRandomEmail();
                input.sendKeys(validEmail);
            }
            if (input.getAttribute("id").equals("estVolume")) {
                input.sendKeys("10000000");
            }

        }

    }

    @When("The user fill the prospect form with invalid mail and phone")
    public void theUserFillTheProspectFormWithInvalidMailAndPhone() {

        SelectFilterUtils sf = new SelectFilterUtils(Driver.getDriver(), Duration.ofSeconds(10));

        randomValue = UUID.randomUUID().toString();
        System.out.println(randomValue);

        for (WebElement select : pages.formsPage().getFormSelects()) {
            if (select.getAttribute("id").equals("salesRep"))
                continue;
            sf.chooseRandom(select);
        }

        List<String> needFormatInputs = List.of("phone", "email", "estVolume");

        for (WebElement input : pages.formsPage().getFormInputs()) {
            if (!needFormatInputs.contains(input.getAttribute("id"))) {
                input.sendKeys(randomValue);
            }
            if (input.getAttribute("id").equals("phone")) {
                input.sendKeys(BrowserUtils.generateTurkishMobileNumber() + "x");
            }
            if (input.getAttribute("id").equals("email")) {
                input.sendKeys(BrowserUtils.generateRandomEmail().split("@")[0]);
            }
            if (input.getAttribute("id").equals("estVolume")) {
                input.sendKeys("10000");
            }

        }

    }

    @Then("The user verify invalid email and phone warning")
    public void theUserVerifyInvalidEmailAndPhoneWarning() {

        Assert.assertEquals("Geçersiz Mail Uyarısı Yazmadı","Lütfen geçerli bir e-posta adresi giriniz",
                pages.generalPage().getInvalidEmailWarning().getText());

        Assert.assertEquals("Geçersiz Telefon Uyarısı Yazmadı","Lütfen geçerli bir telefon numarası giriniz",
                pages.generalPage().getInvalidPhoneWarning().getText());

    }

    @Then("The user verify old values")
    public void theUserVerifyOldValues() {
        Assert.assertEquals("Telefon farklı",validPhone,
                BrowserUtils.getValueInInputBox(Driver.getDriver().findElement(By.id("phone"))));

        Assert.assertEquals("Email farklı",validEmail,
                BrowserUtils.getValueInInputBox(Driver.getDriver().findElement(By.id("email"))));

        Assert.assertEquals("Aylık Toplam POS Hacmi farklı","10.000.000",
                BrowserUtils.getValueInInputBox(Driver.getDriver().findElement(By.id("estVolume"))));

        Assert.assertEquals("İletişim Takibi Ulaşılamadı yapıldıktan sonra Onaylandı Olmadı", "Onaylandı",
                Driver.getDriver().findElement(By.xpath("//button[@id='prospectDurum']/span")).getText());

    }

    String selectOption;
    @When("The user select {string} in {string}")
    public void theUserSelectOptionInSelectElement(String option,String selectId) {
        selectOption = option;
        SelectFilterUtils sf = new SelectFilterUtils(Driver.getDriver(), Duration.ofSeconds(3));
        
        sf.choose(Driver.getDriver().findElement(By.id(selectId)),option);
        
    }

    @Then("The user verify other attributes display")
    public void theUserVerifyOtherAttributesDisplay() {
        List<String> expectedIds = List.of("singlePaymentRate", "installmentUsageRate",
                "debitUsageRate", "creditUsageRate", "frequentlyUsedInstallment");

        for (String id : expectedIds) {
            Assert.assertTrue(id + "'li element gelmedi",
                    BrowserUtils.isElementDisplayed(Driver.getDriver().findElement(By.id(id))));

            if (selectOption.equals("Fiziki POS")) {
                Assert.assertTrue("vb507 idli element gelmedi",
                        BrowserUtils.isElementDisplayed(Driver.getDriver().findElement(By.id("vb507"))));
            }

        }


    }

    @When("The user fill the lead form")
    public void theUserFillTheLeadForm() {
        // frequentlyUsedInstallment
        // rivalInfo, memberCompanyType
        List<String> requiredLeadSelectIds = List.of("rivalInfo", "memberCompanyType");

        SelectFilterUtils sf = new SelectFilterUtils(Driver.getDriver(), Duration.ofSeconds(2));

        randomValue = UUID.randomUUID().toString();
        System.out.println(randomValue);

        for (String selectId : requiredLeadSelectIds) {
            sf.chooseRandom(Driver.getDriver().findElement(By.id(selectId)));
        }

        if (BrowserUtils.isElementDisplayed(Driver.getDriver().findElement(By.id("frequentlyUsedInstallment")))) {
            sf.chooseRandom(Driver.getDriver().findElement(By.id("frequentlyUsedInstallment")));
        }
        if (BrowserUtils.isElementDisplayed(Driver.getDriver().findElement(By.id("vb507")))) {
            sf.chooseRandom(Driver.getDriver().findElement(By.id("vb507")));
        }

        for (WebElement input : pages.formsPage().getFormInputs()) {
            try {
                input.sendKeys("50");
            } catch (org.openqa.selenium.ElementNotInteractableException e) {
                break;
            }

        }


    }

    @When("The user upload documents")
    public void theUserUploadDocuments() {
        SelectFilterUtils sf = new SelectFilterUtils(Driver.getDriver(), Duration.ofSeconds(3));
        BrowserUtils.adjustScreenSize(55,Driver.getDriver());
        pages.generalPage().getRequiredDocumentsButton().click();

        List<String> actualReqDocuments = BrowserUtils.convertWebElementListToStringList
                (pages.generalPage().getActualRequiredDocuments());

        for (String actualReqDocument : actualReqDocuments) {
            BrowserUtils.wait(1);
            pages.generalPage().getDocumentInput().sendKeys(ConfigurationReader.getProperty("filePath"));
            BrowserUtils.wait(1);
            sf.choose(Driver.getDriver().findElement(By.id("BELGE_TURU")), actualReqDocument);
            pages.generalPage().getAddDocumentButton().click();
            BrowserUtils.wait(2);
            pages.generalPage().getUseAlreadyAddedFileButton().click();
            BrowserUtils.waitForVisibility(pages.generalPage().getWarningElement(), 20);
            Assert.assertEquals("Döküman başarıyla eklendi", pages.generalPage().getWarningElement().getText());

        }


    }
}
