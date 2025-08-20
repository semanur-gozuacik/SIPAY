package Efectura.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Getter
public class FormsPage extends BasePage {

    @FindBy(xpath = "//fieldset/div/div/div/button[@aria-haspopup='dialog']")
    private List<WebElement> formSelects;

    @FindBy(xpath = "//div/div/input[contains(@placeholder,'Değer Giriniz')]")
    private List<WebElement> formInputs;

    @FindBy(xpath = "//*[@id='root']/div/main/div/div/div[2]/form/div[2]/button[2]")
    private WebElement formSaveButton;

    @FindBy(xpath = "//*[@id='root']/div/main/div/div/div[2]/form/div[2]/button[3]")
    private WebElement formCompleteButton;

}
