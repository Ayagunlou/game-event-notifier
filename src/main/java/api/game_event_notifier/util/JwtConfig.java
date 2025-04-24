package api.game_event_notifier.util;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;

@Getter
public class JwtConfig {
    private String secret;
    private String secretR;

    public JwtConfig() {
        loadConfig();
    }

    private void loadConfig() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("jwt-config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find jwt-config.properties");
                return;
            }
            properties.load(input);

            // ดึงค่าจากไฟล์ properties
            this.secret = properties.getProperty("jwt.secret");
            this.secretR = properties.getProperty("jwt.secretR");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
