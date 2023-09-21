package net.toiletmc.toiletcore.module.authmehook;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.SeparateSaltMethod;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;

@Recommendation(Usage.RECOMMENDED)
public class SaltedSha512Twice extends SeparateSaltMethod {
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