package Efectura.utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DateFilterUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public DateFilterUtils(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
    }

    /**
     * Senin HTML'ine özgü:
     *  - Trigger:  //button[.//span[normalize-space()='Oluşturulma Tarihi']]
     *  - Panel ID: trigger.getAttribute("aria-controls")
     *  - Gün butonları: <button aria-label="14 Ağustos 2025" ...>
     *  - Ay okları:    [aria-label*='Sonraki'], [aria-label*='Önceki'] (TR) veya 'next'/'previous' (EN fallback)
     */
    public void selectCreatedAt(LocalDate targetDate,String locate) {
        // 1) Trigger butonunu bul
        By triggerSel = By.xpath(locate);
        WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(triggerSel));

        // Panel id'sini al
        String panelId = trigger.getAttribute("aria-controls");
        if (panelId == null || panelId.isBlank()) {
            throw new IllegalStateException("Date panel id (aria-controls) bulunamadı.");
        }

        // 2) Paneli aç
        String state = trigger.getAttribute("data-state");
        if (state == null || state.equals("closed")) {
            trigger.click();
        }

        // Panel görünür olsun
        By panelSel = By.cssSelector("#" + cssEscape(panelId));
        WebElement panel = wait.until(ExpectedConditions.visibilityOfElementLocated(panelSel));

        // 3) Hedef gün için TR yerelinde aria-label hazırla: "14 Ağustos 2025"
        Locale tr = new Locale("tr", "TR");
        DateTimeFormatter dayLabel = DateTimeFormatter.ofPattern("d MMMM yyyy", tr);
        String label = targetDate.format(dayLabel);

        // 4) Gün görünüyorsa direkt tıkla; görünmüyorsa aylar arasında gezin
        if (!clickDayIfPresent(panel, label)) {
            // Ay oklarını bul
            By nextBtnSel = By.cssSelector("#" + cssEscape(panelId) + " [aria-label*='Sonraki'],#" + cssEscape(panelId) + " [aria-label*='next']");
            By prevBtnSel = By.cssSelector("#" + cssEscape(panelId) + " [aria-label*='Önceki'],#" + cssEscape(panelId) + " [aria-label*='previous']");
            WebElement nextBtn = safeFind(nextBtnSel);
            WebElement prevBtn = safeFind(prevBtnSel);

            // Hedef ay yönünü tahmin et (bugünden fark)
            int monthDiff = (int) ChronoUnit.MONTHS.between(YearMonth.now(), YearMonth.from(targetDate));
            int maxHops = 36; // güvenlik

            if (monthDiff >= 0 && nextBtn != null) {
                for (int i = 0; i <= monthDiff && i < maxHops; i++) {
                    if (clickDayIfPresent(panel, label)) return;
                    nextBtn.click();
                    waitShort();
                }
            } else if (monthDiff < 0 && prevBtn != null) {
                for (int i = 0; i <= Math.abs(monthDiff) && i < maxHops; i++) {
                    if (clickDayIfPresent(panel, label)) return;
                    prevBtn.click();
                    waitShort();
                }
            }

            // Yön tahmini başarısızsa birkaç deneme daha yap
            for (int i = 0; i < 12; i++) {
                if (nextBtn != null) { nextBtn.click(); waitShort(); if (clickDayIfPresent(panel, label)) return; }
            }
            for (int i = 0; i < 24; i++) {
                if (prevBtn != null) { prevBtn.click(); waitShort(); if (clickDayIfPresent(panel, label)) return; }
            }

            throw new TimeoutException("Hedef gün bulunamadı: " + label);
        }
    }

    private boolean clickDayIfPresent(WebElement panel, String dayAriaLabel) {
        try {
            By daySel = By.cssSelector("button[aria-label*='" + cssEscape(dayAriaLabel) + "']");
            WebElement dayBtn = panel.findElement(daySel);
            wait.until(ExpectedConditions.elementToBeClickable(dayBtn)).click();
            return true;
        } catch (NoSuchElementException | TimeoutException ignored) {
            return false;
        }
    }

    private WebElement safeFind(By sel) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(sel));
        } catch (TimeoutException e) {
            return null;
        }
    }

    private void waitShort() {
        try { Thread.sleep(150); } catch (InterruptedException ignored) {}
    }

    // Basit CSS escape (id içinde “radix-«r37»” gibi karakterler olabilir)
    private String cssEscape(String s) {
        // En güvenlisi: köşeli parantezle attribute seçici kullanmak;
        // ama biz id ile gidiyoruz, sorunlu karakterleri backslashlayalım.
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
