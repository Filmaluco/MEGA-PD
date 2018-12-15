package Helpers;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Original code at: http://www.appsdeveloperblog.com/encrypt-user-password-example-java/
 *
 * @version 1.0
 *
 * <br>
 *   <b>Example on how to use</b>
 *   <pre>{@code
 *         String providedPassword = "password";
 *         //Store this
 *         String securePassword = PD.Core.PasswordHasher.generateSecurePassword(providedPassword);
 *         //Compare with this
 *         boolean passwordMatch = PD.Core.PasswordHasher.verifyUserPassword(providedPassword, securePassword);
 *  }</pre>
 */
public final class PasswordHasher {

        private static final int ITERATIONS = 10000;
        private static final int KEY_LENGTH = 256;
        private static final String SALT = "EqdmPh53c9x33EygXpTpcoJvc4VXLK";
        //to anyone looking at this, reminder this is a school project, so yeah its just to prove we can do it, we know this should be more secure

    /**
     *
     * @return PD.Core.PasswordHasher salt
     */
    private static String getSalt() {
            return SALT;
        }

    /**
     * Hash's a given password in char char[]
     * @param password password
     * @return hashed version of the password
     */
    private static byte[] hash(char[] password) {
        PBEKeySpec spec = new PBEKeySpec(password, SALT.getBytes(), ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Hash's a given password
     * @param password plain text password
     * @return hashed version of the password
     */
    public static String generateSecurePassword(String password) {
        String returnValue = null;
        byte[] securePassword = hash(password.toCharArray());

        returnValue = Base64.getEncoder().encodeToString(securePassword);

        return returnValue;
    }

    /**
     * Compares hashed password with plain text password
     * @param providedPassword plain textPassword
     * @param securedPassword hashed password
     * @return true if they match
     */
    public static boolean verifyUserPassword(String providedPassword, String securedPassword)
    {
        boolean returnValue ;

        // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword);

        // Check if two passwords are equal
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }

}
