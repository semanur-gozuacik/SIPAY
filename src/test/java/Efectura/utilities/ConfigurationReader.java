package Efectura.utilities;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationReader {
    private static Properties configFile;
    static {

        try {
            // Özelliklerin konumu
            String path = System.getProperty("user.dir") + "/Configuration.properties";
            // Bu dosyayı akış olarak alıyoruz
            FileInputStream input = new FileInputStream(path);
            // Properties sınıfından bir nesne oluşturuluyor
            configFile = new Properties();
            // FileInputStream nesnesindeki bilgilerin, load yöntemiyle Properties nesnesine yüklenmesi.
            configFile.load(input);

            input.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load properties file!");
        }
    }
    /**
     * Bu yöntem, yapılandırma.properties dosyasından özellik değerini döndürür
     *
     * @param keyName özellik adı
     * @return özellik değeri
     */
    public static String getProperty(String keyName) {
        return configFile.getProperty(keyName);
    }

}