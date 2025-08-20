package Efectura.stepDefs;

import Efectura.utilities.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static Efectura.utilities.BrowserUtils.isElementDisplayed;

public class GeneralStepDefs extends BaseStep {
    @Given("The user go to {string} environment")
    public void the_user_go_to_environment(String environment) {
        Driver.getDriver().get(ConfigurationReader.getProperty(environment));
    }

    @Given("The user login")
    public void theUserLogin(DataTable dataTable) {
        Map<String,String> mapParam = dataTable.asMap(String.class, String.class);
        pages.generalPage().getUserNameInput().sendKeys(mapParam.get("username"));
        pages.generalPage().getPasswordInput().sendKeys(mapParam.get("password"));
        pages.generalPage().getLoginButton().click();
        BrowserUtils.waitForVisibility(pages.generalPage().getUserMenuButton(),40);
        pages.generalPage().getWarningCloseButton().click();
        BrowserUtils.wait(1);
    }

    @Then("The user verify warning {string}")
    public void theUserVerifyWarning(String warningText) {
        BrowserUtils.wait(1);
        Assert.assertTrue("Uyarı Gelmedi", isElementDisplayed(pages.generalPage().getWarningElement()));

        Assert.assertEquals("Mesaj farklı",warningText,pages.generalPage().getWarningElement().getText());
    }

    @When("The user click assigned records tab")
    public void theUserClickAssignedRecordsTab() {
        pages.generalPage().getAssignedRecordsButton().click();
        BrowserUtils.wait(3);
    }


    @When("The user select {string} as salesRep")
    public void theUserSelectEmreUzunAsSalesRep(String salesRepOption) {
        BrowserUtils.wait(3);
        SelectFilterUtils sf = new SelectFilterUtils(Driver.getDriver(), Duration.ofSeconds(10));

        WebElement select = Driver.getDriver().findElement(By.id("salesRep"));

        sf.choose(select,salesRepOption);

    }

    @Given("The user logout")
    public void theUserLogout() {
        pages.generalPage().getUserMenuButton().click();
        pages.generalPage().getLogoutButton().click();
    }

    @Then("The user verify previous page button passive")
    public void theUserVerifyPreviousPageButtonPassive() {
        Assert.assertFalse("İlk Sayfada previous page butonu aktif!!",
                BrowserUtils.isButtonActive(pages.generalPage().getPreviousPageButton()));
    }

    @When("The user click next page button")
    public void theUserClickNextPageButton() {
        pages.generalPage().getNextPageButton().click();
    }

    @Then("The user verify previous page button active")
    public void theUserVerifyPreviousPageButtonActive() {
        Assert.assertTrue("İlk Sayfada değilken previous page butonu pasif!!",
                BrowserUtils.isButtonActive(pages.generalPage().getPreviousPageButton()));
    }

    @Then("The user verify next button active")
    public void theUserVerifyNextButtonActive() {
        BrowserUtils.wait(1);
        Assert.assertTrue("İlk Sayfadayken next page butonu pasif!!",
                BrowserUtils.isButtonActive(pages.generalPage().getNextPageButton()));
    }

    @When("The user go to last page")
    public void theUserGoToLastPage() {

        String paginateInfo = pages.generalPage().getPaginateInfoText().getText();
        int first = Integer.parseInt(paginateInfo.split(" ")[1]);
        int last = Integer.parseInt(paginateInfo.split(" ")[3]);

        while (first != last) {
            pages.generalPage().getNextPageButton().click();
            BrowserUtils.waitForVisibility(pages.generalPage().getPaginateInfoText(),60);
            paginateInfo = pages.generalPage().getPaginateInfoText().getText();
            first = Integer.parseInt(paginateInfo.split(" ")[1]);
        }

    }

    @Then("The user verify next button passive")
    public void theUserVerifyNextButtonPassive() {
        Assert.assertFalse("Son Sayfadayken next page butonu aktif!!",
                BrowserUtils.isButtonActive(pages.generalPage().getNextPageButton()));
    }

    @When("The user go to {string} on navbar")
    public void theUserGoToProspectOnNavbar(String tabName) {
        pages.generalPage().getNavBarTabs().stream()
                .filter(el -> el.getText().equals(tabName))
                .findFirst()
                .ifPresent(WebElement::click);
    }

    @When("The user click {string} button")
    public void theUserClickButton(String buttonName) {
        WebElement button = Driver.getDriver().
                findElement(By.xpath("//button[contains(text(),'" + buttonName + "')]"));

        BrowserUtils.wait(1);
        BrowserUtils.waitForVisibility(button, 45);
        BrowserUtils.waitForClickability(button, 45);
        BrowserUtils.adjustScreenSize(70,Driver.getDriver());

        button.click();
        BrowserUtils.wait(1);
    }

    @Then("The user verify form inputs clear")
    public void theUserVerifyFormInputsClear() {

        for (WebElement input : pages.formsPage().getFormInputs()) {

            Assert.assertEquals(input.getAttribute("id") + " input tewmizlenmemiş",
                    "", BrowserUtils.getValueInInputBox(input));

        }

    }

    @When("The user go to other tab")
    public void theUserGoToOtherTab() {
        Driver.getDriver().switchTo().window(Driver.getDriver().getWindowHandles().stream()
                .filter(h -> !h.equals(Driver.getDriver().getWindowHandle()))
                .findFirst().get());
        BrowserUtils.wait(3);

    }

    @When("The user open {string} info in form")
    public void theUserOpenProspectInfoInForm(String infoName) {
        WebElement infoTab = Driver.getDriver().findElement(By.xpath("//button[contains(.,'" + infoName + "')]"));
        infoTab.click();
    }

    @Then("The user verify required documents")
    public void theUserVerifyRequiredDocuments() {
        BrowserUtils.adjustScreenSize(50,Driver.getDriver());
        SelectFilterUtils sf = new SelectFilterUtils(Driver.getDriver(), Duration.ofSeconds(3));
        WebElement select = Driver.getDriver().findElement(By.id("companyType"));
        select.click();

        List<String> optionTexts = pages.generalPage().getCompanyTypeOptions().stream()
                .map(WebElement::getText).toList();

        sf.chooseRandom(select);
        BrowserUtils.wait(1);
        pages.generalPage().getRequiredDocumentsButton().click();

        for (String option : optionTexts) {
            sf.choose(select,option);
            List<String> actualReqDocuments = BrowserUtils.convertWebElementListToStringList
                    (pages.generalPage().getActualRequiredDocuments());

            if (option.equals("Adi Ortaklık") || option.equals("XB")) {
                Assert.assertTrue(BrowserUtils.areListsEqualIgnoreOrder(
                                actualReqDocuments,pages.generalPage().getAdiAndXbRequiredDocuments()));
            }
            if (option.equals("Şahıs")) {
                Assert.assertTrue(BrowserUtils.areListsEqualIgnoreOrder(
                        actualReqDocuments,pages.generalPage().getSahisRequiredDocuments()));
            }
            if (option.equals("Limited") || option.equals("Anonim")) {
                Assert.assertTrue(BrowserUtils.areListsEqualIgnoreOrder(
                        actualReqDocuments,pages.generalPage().getLimitedandAnonimRequiredDocuments()));
            }
            if (option.equals("Dernek")) {
                Assert.assertTrue(BrowserUtils.areListsEqualIgnoreOrder(
                        actualReqDocuments,pages.generalPage().getDernekRequiredDocuments()));
            }
            if (option.equals("Vakıf")) {
                Assert.assertTrue(BrowserUtils.areListsEqualIgnoreOrder(
                        actualReqDocuments,pages.generalPage().getVakifRequiredDocuments()));
            }

        }

    }

    @When("The user select {int} {int} {int} in {string}")
    public void theUserSelectInOfferDate(int day, int month, int year, String elementId) {
        BrowserUtils.wait(1);
        DateFilterUtils df = new DateFilterUtils(Driver.getDriver(), Duration.ofSeconds(10));
        df.selectCreatedAt(LocalDate.of(year, month, day),"//*[@id='" + elementId + "']");
        BrowserUtils.wait(2);

    }

    @When("The user wait {int} second")
    public void theUserWaitSecond(int secondAmount) {
        BrowserUtils.wait(secondAmount);
    }

    @When("The user enter {string} to taksit input")
    public void theUserEnterToTaksitInput(String value) {
        Driver.getDriver().findElement(By.id("installmentCount")).sendKeys(value);
    }

    @Then("The user verify minimum value warning")
    public void theUserVerifyMinimumValueWarning() {
        Assert.assertTrue("Eksi değerde uyarı gelmedi",
                BrowserUtils.isElementDisplayed(pages.generalPage().getMinimumValueWarning()));
    }

    @When("The user refresh the page")
    public void theUserRefreshThePage() {
        Driver.getDriver().navigate().refresh();
        BrowserUtils.wait(3);
    }

    @Then("The user verify revise reason {string} for {string}")
    public void theUserVerifyReviseReason(String expectedValue, String page) {

        if (page.equalsIgnoreCase("risk")) {
            Assert.assertEquals("revize nedeni kaydedilenden farklı",
                    expectedValue,pages.generalPage().getReviseReasonSelectedValueForRisk().getText());
        }
        if (page.equalsIgnoreCase("operation")) {
            Assert.assertEquals("revize nedeni kaydedilenden farklı",
                    expectedValue,pages.generalPage().getReviseReasonSelectedValueForOperation().getText());
        }


    }

    @When("The user enter {string} into explanation in {string}")
    public void theUserEnterIntoExplanation(String value, String page) {
        if (page.equalsIgnoreCase("risk")) {
            pages.generalPage().getExplanationInputForRisk().sendKeys(value);
        }
        if (page.equalsIgnoreCase("operation")) {
            pages.generalPage().getExplanationInput().sendKeys(value);
        }

    }

    @Then("The user verify explanation {string} for {string}")
    public void theUserVerifyExplanationTestNotu(String expectedValue, String page) {

        if (page.equalsIgnoreCase("risk")) {
            Assert.assertEquals("Note kısmı kaydedilenden farklı",
                    expectedValue,pages.generalPage().getExplanationInputForRisk().getText());
        }
        if (page.equalsIgnoreCase("operation")) {
            Assert.assertEquals("Note kısmı kaydedilenden farklı",
                    expectedValue,pages.generalPage().getExplanationInput().getText());
        }

    }
}
