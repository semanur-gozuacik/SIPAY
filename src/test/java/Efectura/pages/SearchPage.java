package Efectura.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

@Getter
public class SearchPage extends BasePage {

    @FindBy(xpath = "//input[contains(@id,'earch')]")
    private WebElement searchInput;

    @FindBy(xpath = "//*[contains(@id,'content-')]/div[3]/div[3]/div/div[1]/div[1]")
    private List<WebElement> recordTitles;

    @FindBy(xpath = "//*[contains(@id,'content-')]/div[3]/div[2]/div[1]/button")
    private WebElement dealStatusSelect;

    @FindBy(xpath = "/html/body/div[2]/div/div/div[1]/input")
    private WebElement dealStatusSearchInput;

    @FindBy(xpath = "//span[@class='truncate']")
    private List<WebElement> dealStatusOptions;

    @FindBy(xpath = "//*[contains(@id,'-content-')]/div[3]/div[3]/div/div/div[2]/div[2]/div[2]")
    private List<WebElement> dealStatusValuesResult;

    @FindBy(xpath = "//*[contains(@id,'-content-')]/div[3]/div[2]/div[1]/button[2]")
    private WebElement dealStatusResetButton;

    @FindBy(xpath = "//*[contains(@id,'-content-')]/div[3]/div[2]/div[1]/button/span")
    private WebElement selectedDealStatusText;

    @FindBy(xpath = "//*[contains(@id,'content-search')]/div[3]/div[1]/div/button[1]")
    private WebElement createDateSortButton;

    @FindBy(xpath = "//*[contains(@id,'-content-')]/div[3]/div[3]//div[1]/div[2]/div[1]/div[4]")
    private List<WebElement> createDateValues;

    @FindBy(xpath = "//*[contains(@id,'-content-')]/div[3]/div[2]/div[2]/div//button[2]")
    private WebElement createDateResetFilter;

    @FindBy(xpath = "//*[contains(@id,'-content-')]/div[3]/div[2]/div[2]//div/button/span")
    private WebElement selectedCreateDateText;

    @FindBy(xpath = "//*[@id='root']/div/main/div/div/div[1]/div/button")
    private WebElement createNewRecordButton;

    @FindBy(xpath = "//*[contains(@id,'-content-')]/div[1]/button")
    private WebElement searchButton;

    @FindBy(xpath = "//*[contains(@id,'content-')]/div[3]/div[3]/div/div[2]/button[1]")
    private List<WebElement> goToRelatedRecordButtons;

    @FindBy(xpath = "//*[contains(@id,'content-')]/div[3]/div[2]/div[2]/div//button")
    private WebElement createDateFilter;


    public static boolean isSortedAscending(List<String> dateStrings, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return IntStream.range(0, dateStrings.size() - 1)
                .allMatch(i -> {
                    LocalDateTime current = LocalDateTime.parse(dateStrings.get(i).split(" ")[2] + " "
                            + dateStrings.get(i).split(" ")[3], formatter);
                    LocalDateTime next = LocalDateTime.parse(dateStrings.get(i + 1).split(" ")[2] + " "
                            + dateStrings.get(i + 1).split(" ")[3], formatter);
                    return !current.isAfter(next);
                });
    }

    public static boolean isSortedDescending(List<String> dateStrings, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return IntStream.range(0, dateStrings.size() - 1)
                .allMatch(i -> {
                    LocalDateTime current = LocalDateTime.parse(dateStrings.get(i).split(" ")[2] + " "
                            + dateStrings.get(i).split(" ")[3], formatter);
                    LocalDateTime next = LocalDateTime.parse(dateStrings.get(i + 1).split(" ")[2] + " "
                            + dateStrings.get(i + 1).split(" ")[3], formatter);
                    return !current.isBefore(next);
                });
    }

}
