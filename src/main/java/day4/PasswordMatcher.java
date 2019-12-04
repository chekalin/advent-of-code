package day4;

class PasswordMatcher {

    static boolean hasAtLeastTwoAdjacentDigits(int password) {
        String passwordAsString = String.valueOf(password);
        if (passwordAsString.length() < 2) return false;
        for (int i = 1; i < passwordAsString.length(); i++) {
            if (passwordAsString.charAt(i) == passwordAsString.charAt(i - 1)) {
                return true;
            }
        }
        return false;
    }

    static boolean hasExactlyTwoAdjacentDigits(int password) {
        String passwordAsString = String.valueOf(password);
        if (passwordAsString.length() < 2) return false;
        int groupLength = 1;
        for (int i = 1; i < passwordAsString.length(); i++) {
            if (passwordAsString.charAt(i) == passwordAsString.charAt(i - 1)) {
                groupLength++;
            } else if (groupLength == 2) {
                return true;
            } else {
                groupLength = 1;
            }
        }
        return groupLength == 2;
    }

    static boolean hasNoDecreasingNumbers(int password) {
        String passwordAsString = String.valueOf(password);
        if (passwordAsString.length() < 2) return true;
        for (int i = 1; i < passwordAsString.length(); i++) {
            if (passwordAsString.charAt(i) < passwordAsString.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }

}
