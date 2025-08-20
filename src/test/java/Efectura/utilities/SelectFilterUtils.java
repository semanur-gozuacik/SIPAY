package Efectura.utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class SelectFilterUtils {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Random rnd = new Random();

    public SelectFilterUtils(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
    }

    private final Duration SHORT = Duration.ofSeconds(2);
    private final Duration VERY_SHORT = Duration.ofMillis(800);

    private WebDriverWait shortWait() {
        WebDriverWait w = new WebDriverWait(driver, SHORT);
        w.pollingEvery(Duration.ofMillis(100));
        return w;
    }

    private void waitTransientOverlaysFast() {
        By overlays = By.cssSelector(".Toastify__toast, .toast, .modal-backdrop, [data-loading='true'], [aria-busy='true']");
        try {
            new WebDriverWait(driver, VERY_SHORT)
                    .until(ExpectedConditions.invisibilityOfElementLocated(overlays));
        } catch (TimeoutException ignored) {}
    }

    /**
     * Radix/Command palette Select: trigger -> panel -> option click
     * @param trigger   ör: web ele
     * @param valueOrText  ör: "cagri_merkezi"  veya  "Çağrı Merkezi"
     */
    public void choose(WebElement trigger, String valueOrText) {
        // 1) Trigger
        trigger = shortWait().until(ExpectedConditions.elementToBeClickable(trigger));
        String panelId = trigger.getAttribute("aria-controls");
        if (panelId == null || panelId.isBlank()) {
            throw new IllegalStateException("aria-controls (panel id) bulunamadı.");
        }

        // 2) Aç
        String state = trigger.getAttribute("data-state");
        if (state == null || state.equals("closed")) trigger.click();

        // 3) Panel görünür + hızlı overlay bekle
        By panel = By.xpath("//*[@id='" + panelId + "']");
        shortWait().until(ExpectedConditions.visibilityOfElementLocated(panel));
        waitTransientOverlaysFast();

        // 4) HIZLI YOL: arama inputuna yaz + Enter (çoğu kütüphane için en hızlısı)
        try {
            By input = By.id(getSearchInputId(panelId));
            WebElement search = shortWait().until(ExpectedConditions.elementToBeClickable(input));
            search.clear();
            search.sendKeys(valueOrText);
            search.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            // Fallback – doğrudan tıkla (data-value → görünür metin)
            if (!clickIfPresent(By.xpath("//*[@id='" + panelId + "']//*[@cmdk-item and @role='option' and " +
                    "translate(@data-value,'ABCDEFGHIJKLMNOPQRSTUVWXYZÇĞİÖŞÜ','abcdefghijklmnopqrstuvwxyzçğiöşü')='" +
                    toLowerTr(valueOrText) + "']"))) {

                clickIfPresent(By.xpath("//*[@id='" + panelId + "']//*[@cmdk-item and @role='option']" +
                        "[.//span[normalize-space()='" + valueOrText + "']]"));
            }
        }

        // 5) Kapanmayı kısa bekle
        shortWait().until(ExpectedConditions.invisibilityOfElementLocated(panel));
    }


    /** Rastgele bir option seçer (data-value ya da görünen metin önemli değil). */
    public String chooseRandom(WebElement trigger) {
        // 1) Trigger
        trigger = shortWait().until(ExpectedConditions.elementToBeClickable(trigger));
        String panelId = trigger.getAttribute("aria-controls");
        if (panelId == null || panelId.isBlank()) {
            throw new IllegalStateException("aria-controls (panel id) bulunamadı.");
        }

        // 2) Aç
        String state = trigger.getAttribute("data-state");
        if (state == null || state.equals("closed")) trigger.click();

        // 3) Panel
        By panel = By.id(panelId);
        shortWait().until(ExpectedConditions.visibilityOfElementLocated(panel));
        waitTransientOverlaysFast();

        // 4) JS ile TEK SEFERDE görünür ve seçilebilir option’ları çek (metin + element)
        String script =
                "const root=document.getElementById(arguments[0]);" +
                        "const items=[...root.querySelectorAll(\"[cmdk-item][role='option']\")]" +
                        " .filter(x=>!(x.getAttribute('aria-disabled')==='true'));" +
                        "return items.map(x=>({txt:x.textContent.trim(), el:x}));";

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> jsItems = (List<Map<String, Object>>)
                ((JavascriptExecutor)driver).executeScript(script, panelId);

        if (jsItems==null || jsItems.isEmpty()) {
            throw new NoSuchElementException("Seçilebilir option bulunamadı.");
        }

        // 5) Boş/placeholder metinleri tek PASS’te ele
        List<Map<String, Object>> filtered = new ArrayList<>(jsItems.size());
        for (Map<String, Object> it : jsItems) {
            String txt = String.valueOf(it.get("txt"));
            if (txt != null && !txt.isBlank() && !"Seçiniz...".equalsIgnoreCase(txt.trim())) {
                filtered.add(it);
            }
        }
        if (filtered.isEmpty()) throw new NoSuchElementException("Seçilebilir option bulunamadı.");

        // 6) Rastgele seç + JS click (tek komut, intercept riskini de düşürür)
        int idx = rnd.nextInt(filtered.size());
        Map<String, Object> pick = filtered.get(idx);
        String chosenText = String.valueOf(pick.get("txt"));

        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({block:'center'});", pick.get("el"));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", pick.get("el"));

        // 7) Kapanmayı kısa bekle
        shortWait().until(ExpectedConditions.invisibilityOfElementLocated(panel));

        return chosenText;
    }


    /* ---------------------- Yardımcılar ---------------------- */

    /** Kısa ömürlü overlay/animasyon/toast gibi engelleri bekler (varsa). */
    private void waitTransientOverlays() {
        // Projene göre buraya ek desenler ekleyebilirsin.
        By overlays = By.cssSelector(
                ".Toastify__toast, .toast, .modal-backdrop, [data-loading='true'], [aria-busy='true']"
        );
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.invisibilityOfElementLocated(overlays));
        } catch (TimeoutException ignored) {
            // Overlay yoksa/çekilmediyse sorun değil, aşağıda robustClick yine deneyecek.
        }
    }

    /** Panel içindeki bir option'ı güvenli şekilde tıklar. */
    private void robustClickInsidePanel(By panel, WebElement option) {
        try {
            // 1) Önce görünür alana getir
            bringIntoView(option);
            wait.until(ExpectedConditions.elementToBeClickable(option));
            // 2) Normal tıkla
            option.click();
        } catch (ElementNotInteractableException e) {
            // 3) Tekrar görünür yapmayı dene ve bir kez daha normal tıkla
            bringIntoView(option);
            try {
                wait.until(ExpectedConditions.elementToBeClickable(option));
                option.click();
            } catch (ElementNotInteractableException e2) {
                // 4) Son çare: JS ile tıkla
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
            }
        }
    }

    /** Option'ı en yakın scrollable ebeveyninde ve viewport'ta ortalayacak şekilde görünür yapar. */
    private void bringIntoView(WebElement el) {
        // a) En yakın scrollable parent'a doğru hafif kaydır
        ((JavascriptExecutor) driver).executeScript(
                "let ch=arguments[0], c=ch.parentElement;" +
                        "while(c && c !== document.body){" +
                        "  const s = getComputedStyle(c);" +
                        "  if(/auto|scroll/.test(s.overflowY)){" +
                        "    const box = ch.getBoundingClientRect();" +
                        "    const cb  = c.getBoundingClientRect();" +
                        "    const delta = box.top - (cb.top + cb.height/2);" +
                        "    c.scrollTop += delta;" +
                        "    break;" +
                        "  }" +
                        "  c = c.parentElement;" +
                        "}"
                , el);

        // b) Son olarak viewport merkezine al
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
    }


    private boolean clickIfPresent(By sel) {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(sel));
            el.click();
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Panel içindeki search input'un id’si: verdiğin HTML’de "radix-«rfo»"
    // Güvenli yol: panel içinde role=combobox input'u bul.
    private String getSearchInputId(String panelId) {
        WebElement input = driver.findElement(By.xpath(
                "//*[@id='" + panelId + "']//input[@role='combobox' or @cmdk-input]"));
        return input.getAttribute("id");
    }

    // Türkçe küçük harfe çevirme (CSS/XPath eşleşmeleri için)
    private String toLowerTr(String s) {
        return s == null ? "" : s
                .replace('I','ı').replace('İ','i')
                .toLowerCase(new java.util.Locale("tr","TR"));
    }
}
