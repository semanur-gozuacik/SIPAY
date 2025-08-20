package Efectura.stepDefs;

import Efectura.utilities.BrowserUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import static Efectura.utilities.BrowserUtils.isElementDisplayed;

public class AssignedRecordsStepDefs extends BaseStep {

    @Then("The user verify assigned records tab")
    public void theUserVerifyAssignedRecordsTab() {
        Assert.assertTrue("Deal Status Filtresi Yok", isElementDisplayed(pages.searchPage().getDealStatusSelect()));
        Assert.assertTrue("Create Date Filtresi Yok", isElementDisplayed(pages.searchPage().getCreateDateFilter()));
        Assert.assertTrue("Atanan ve oluşturan Toggle Yok", isElementDisplayed(pages.assignedRecordPage().getAssignedOrCreatedCheckBox()));
        Assert.assertTrue("İlgili Kayda Git Butonu Yok",
                isElementDisplayed(pages.searchPage().getGoToRelatedRecordButtons().get(0)));
    }

    @When("The user click assignedOrCreatedCheckbox")
    public void theUserClickAssignedOrCreatedCheckbox() {
        BrowserUtils.wait(1);
        pages.assignedRecordPage().getAssignedOrCreatedCheckBox().click();
    }
}
