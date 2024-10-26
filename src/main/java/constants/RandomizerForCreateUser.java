package constants;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomizerForCreateUser {

    public static String RANDOM_EMAIL = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@gmail.com";
    public static String RANDOM_PASSWORD = RandomStringUtils.randomNumeric(8);
    public static String RANDOM_NAME = RandomStringUtils.randomAlphabetic(6);
}
