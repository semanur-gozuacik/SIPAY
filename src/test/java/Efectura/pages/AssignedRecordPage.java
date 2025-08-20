package Efectura.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class AssignedRecordPage extends BasePage {

    @FindBy(xpath = "//*[contains(@id,'content-')]/div[1]/div/div[1]/div/div/label/div[2]")
    private WebElement assignedOrCreatedCheckBox;

}
