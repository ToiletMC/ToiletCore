package net.toiletmc.toiletcore.module.impl.authme;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.PasswordEncryptionEvent;
import fr.xephi.authme.security.PasswordSecurity;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.interfaces.Module;
import org.bukkit.Bukkit;
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
}
