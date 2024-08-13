package net.toiletmc.toiletcore.module.authme;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.PasswordEncryptionEvent;
import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.SeparateSaltMethod;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AuthmeHook implements Listener {
    private final AuthMeApi authMeApi = AuthMeApi.getInstance();

    @EventHandler
    private void onPwdEncryption(PasswordEncryptionEvent event) {
        event.setMethod(new SaltedSha512Twice());
    }

    public void reloadAuthme() {
        try {
            Class<?> clazz = authMeApi.getClass();
            Field privateField = clazz.getDeclaredField("passwordSecurity");
            privateField.setAccessible(true);
            PasswordSecurity fieldValue = (PasswordSecurity) privateField.get(authMeApi);
            Method publicReloadMethod = fieldValue.getClass().getMethod("reload");
            publicReloadMethod.invoke(fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Recommendation(Usage.RECOMMENDED)
    private static class SaltedSha512Twice extends SeparateSaltMethod {
        @Override
        public String computeHash(String password, String salt, String name) {
            String str = HashUtils.sha512(password);
            return HashUtils.sha512(str + salt);
        }

        @Override
        public String generateSalt() {
            return RandomStringUtils.generate(10);
        }
    }
}
