package Efectura.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Getter
public class GeneralPage extends BasePage {

    @FindBy(xpath = "//input[@id='username']")
    private WebElement userNameInput;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//button[contains(@id,'trigger-assigned')]")
    private WebElement assignedRecordsButton;

    @FindBy(xpath = "//img[@alt='Sipay Logo']")
    private WebElement sipayLogo;

    //div[contains(@class,'Toastify__toast--')]
    @FindBy(xpath = "//div[contains(@class,'Toastify__toast--')]")
    private WebElement warningElement;

    @FindBy(xpath = "/html/body/div/div/main/section/div/div/button")
    private WebElement warningCloseButton;

    @FindBy(xpath = "//*[@id='root']/div/header/div/nav/div/div/button[2]")
    private WebElement logoutButton;

    @FindBy(xpath = "//*[@id='user-menu-button']/span[1]")
    private WebElement userMenuButton;

    @FindBy(xpath = "//div[contains(@id,'-content-')]/div[3]/div[3]/div")
    private WebElement tableNoContentText;

    @FindBy(xpath = "//*[contains(@id,'-content-')]/div[3]/div[4]/button[1]")
    private WebElement previousPageButton;

    @FindBy(xpath = "//*[contains(@id,'-content-')]/div[3]/div[4]/button[2]")
    private WebElement nextPageButton;

    @FindBy(xpath = "//*[contains(@id,'-content-')]/div[3]/div[4]/span")
    private WebElement paginateInfoText;

    @FindBy(xpath = "//*[@id='root']/div/header/div/nav/a")
    private List<WebElement> navBarTabs;

    @FindBy(xpath = "//input[@id='email']/following-sibling::p")
    private WebElement invalidEmailWarning;

    @FindBy(xpath = "//input[@id='phone']/following-sibling::p")
    private WebElement invalidPhoneWarning;

    @FindBy(xpath = "//*[@id=\"root\"]/div/main/div/div/div[2]/fieldset/div/div[5]/div/div/div/div[2]/div/div/p")
    private WebElement explanationInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/main/div/div/div[2]/fieldset/div/div[4]/div/div/div/div[2]/div/div")
    private WebElement explanationInputForRisk;

    @FindBy(xpath = "//input[@id='customerInfoForm']")
    private WebElement documentInput;

    @FindBy(xpath = "//button[@id='addDocumentButton']")
    private WebElement addDocumentButton;

    @FindBy(xpath = "//button[contains(text(),'Mevcut Dosyayı Kullan')]")
    private WebElement useAlreadyAddedFileButton;

    @FindBy(xpath = "/html/body/div[2]/div/div/div[2]/div/div/div/div")
    private List<WebElement> companyTypeOptions;

    @FindBy(xpath = "//div[2]/fieldset/div[4]/div[2]/div/div/div[1]/ul/li")
    private List<WebElement> actualRequiredDocuments;

    @FindBy(xpath = "//*[@id=\"root\"]/div/main/div/div/div[2]/fieldset/div[4]/div[1]/button/span")
    private WebElement requiredDocumentsButton;

    @FindBy(xpath = "//p[contains(text(),'Validasyon Minimum Değer')]")
    private WebElement minimumValueWarning;

    @FindBy(xpath = "//*[@id=\"reviseReasonOperation\"]/span")
    private WebElement reviseReasonSelectedValueForOperation;

    @FindBy(xpath = "//*[@id=\"reviseReason\"]/span")
    private WebElement reviseReasonSelectedValueForRisk;

    private final List<String> adiAndXbRequiredDocuments = List.of("Kimlik","Üye İşyeri Bilgi Formu","Oran Şablonu");
    private final List<String> sahisRequiredDocuments = List.of("Kimlik","Vergi Levhası","İmza Beyannamesi","İkametgah Belgesi","Üye İşyeri Bilgi Formu","Oran Şablonu");
    private final List<String> limitedandAnonimRequiredDocuments = List.of("Kimlik","Vergi Levhası","İmza Sirküleri","Ticaret Sicil Gazetesi","Üye İşyeri Bilgi Formu","Oran Şablonu");
    private final List<String> dernekRequiredDocuments = List.of("Kimlik","Vergi Kimlik Numarası","İmza Sirküleri","Dernek Tüzüğü","Üye İşyeri Bilgi Formu","Karar Defteri","Oran Şablonu");
    private final List<String> vakifRequiredDocuments = List.of("Kimlik","Vergi Kimlik Numarası","İmza Sirküleri","Vakıf Senedi","Üye İşyeri Bilgi Formu","Karar Defteri");





}
