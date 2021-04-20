public class PasswordChecker {
    private final int MIN_PASSWORD_LENGTH = 8;

    /**
     * Checks whether password entry meets all criterias:
     * - Min 8 characters long
     * - has lower-case letter
     * - has upper-case letter
     * - has digit
     * - has special character
     * 
     * @param passwordEntry entered password to check
     * @return true if meets all criterias, else false
     */
    public boolean checkPassword(String passwordEntry) {
        if (passwordEntry.length() < MIN_PASSWORD_LENGTH)
            return false;

        int hasLowerCase = 0;
        int hasUpperCase = 0;
        int hasDigit = 0;
        int hasSpecialCharacter = 0;

        for (char ch : passwordEntry.toCharArray()) {
            if (isLowerCase(ch))
                hasLowerCase++;
            else if (isUpperCase(ch))
                hasUpperCase++;
            else if (isDigit(ch))
                hasDigit++;
            else if (isSpecialCharacter(ch))
                hasSpecialCharacter++;
        }
        return (hasLowerCase > 0 && hasUpperCase > 0 && hasDigit > 0 && hasSpecialCharacter > 0);
    }

    /**
     * Checks if character is lower case letter
     * 
     * @param c charachter to check
     * @return true if lower case letter, else false
     */
    private boolean isLowerCase(char c) {
        return (c >= 'a' && c <= 'z');
    }

    /**
     * Checks if character is upper case letter
     * 
     * @param c charachter to check
     * @return true if upper case letter, else false
     */
    private boolean isUpperCase(char c) {
        return (c >= 'A' && c <= 'Z');
    }

    /**
     * Checks if character is digit
     * 
     * @param c charachter to check
     * @return true if digit, else false
     */
    private boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    /**
     * Checks if character is special charachter
     * 
     * @param c charachter to check
     * @return true if special charachter, else false
     */
    private boolean isSpecialCharacter(char c) {
        return "!@#$%&*()'+,-./:;<=>?[]^_`{|}".contains(Character.toString(c));
    }
}
