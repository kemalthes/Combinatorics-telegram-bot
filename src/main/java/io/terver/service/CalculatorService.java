package io.terver.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CalculatorService {

    private static final Pattern SINGLE_NUMBER_PATTERN = Pattern.compile("^\\s*\\d+\\s*$");
    private static final Pattern TWO_NUMBERS_PATTERN = Pattern.compile("^\\s*\\d+\\s+\\d+\\s*$");
    private static final Pattern THREE_NUMBERS_PATTERN = Pattern.compile("^\\s*\\d+\\s+\\d+\\s+\\d+\\s*$");
    private static final Pattern FOUR_NUMBERS_PATTERN = Pattern.compile("^\\s*\\d+\\s+\\d+\\s+\\d+\\s+\\d+\\s*$");

    private boolean isNotMatch(Pattern pattern, String message) {
        return !pattern.matcher(message).matches();
    }

    private int[] parseTwoNumbers(String message) {
        String[] parts = message.trim().split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException();
        }
        return new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) };
    }

    private int[] parseThreeNumbers(String message) {
        String[] parts = message.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException();
        }
        return new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]) };
    }

    private int[] parseFourNumbers(String message) {
        String[] parts = message.trim().split("\\s+");
        if (parts.length != 4) {
            throw new IllegalArgumentException();
        }
        return new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]) };
    }

    public int permutations(String message) {
        if (isNotMatch(SINGLE_NUMBER_PATTERN, message)) {
            throw new NumberFormatException();
        }
        int n = Integer.parseInt(message.trim());
        if (n < 0) throw new IllegalArgumentException();
        int res = 1;
        for (int i = 2; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    public int permutationsRepeat(String message) {
        if (isNotMatch(SINGLE_NUMBER_PATTERN, message)) {
            throw new NumberFormatException();
        }
        double n = Integer.parseInt(message.trim());
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        return (int) Math.pow(n, n);
    }

    public int distribution(String message) {
        if (isNotMatch(TWO_NUMBERS_PATTERN, message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseTwoNumbers(message);
        int n = nums[0], k = nums[1];
        if (n < k || n < 0 || k < 0) {
            throw new IllegalArgumentException();
        }
        int res = 1;
        for (int i = n - k + 1; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    public int distributionRepeat(String message) {
        if (isNotMatch(TWO_NUMBERS_PATTERN, message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseTwoNumbers(message);
        int n = nums[0], k = nums[1];
        if (n < k|| n < 0 || k < 0) {
            throw new IllegalArgumentException();
        }
        return (int) Math.pow(n, k);
    }

    public int combination(String message) {
        if (isNotMatch(TWO_NUMBERS_PATTERN, message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseTwoNumbers(message);
        int n = nums[0], k = nums[1];
        if (n < k || n < 0 || k < 0) {
            throw new IllegalArgumentException();
        }
        int res = 1;
        for (int i = n - k + 1; i <= n; i++) {
            res *= i;
        }
        for (int i = 1; i <= k; i++) {
            res /= i;
        }
        return res;
    }

    public int combinationRepeat(String message) {
        if (isNotMatch(TWO_NUMBERS_PATTERN, message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseTwoNumbers(message);
        int n = nums[0], k = nums[1];
        if (n < k || n < 0 || k < 0) {
            throw new IllegalArgumentException();
        }
        int res = 1;
        for (int i = n; i <= n + k - 1; i++) {
            res *= i;
        }
        for (int i = 1; i <= k; i++) {
            res /= i;
        }
        return res;
    }

    public double urnModelOne(String message) {
        if (isNotMatch(THREE_NUMBERS_PATTERN, message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseThreeNumbers(message);
        int n = nums[0], m = nums[1], k = nums[2];
        if (n < m || m <= k || k < 0) {
            throw new IllegalArgumentException();
        }
        double res = 1;
        for (int i = m - k + 1; i <= m; i++) {
            res *= i;
        }
        for (int i = n - k + 1; i <= n; i++) {
            res /= i;
        }
        return res;
    }

    public double urnModelTwo(String message) {
        if (isNotMatch(FOUR_NUMBERS_PATTERN, message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseFourNumbers(message);
        int n = nums[0], m = nums[1], k = nums[2], r = nums[3];
        if (n < m || m <= k || k < r || n < 0 || m < 0 || k < 0 || r < 0) {
            throw new IllegalArgumentException();
        }
        double res = 1;
        for (int i = m - r + 1; i <= m; i++) {
            res *= i;
        }
        for (int i = 1; i <= r; i++) {
            res /= i;
        }
        for (int i = n - m - k + r + 1; i <= n - m; i++) {
            res *= i;
        }
        for (int i = 1; i <= k - r; i++) {
            res /= i;
        }
        for (int i = 1; i <= k ; i++) {
            res *= i;
        }
        for (int i = n - k + 1; i <= n; i++) {
            res /= i;
        }
        return res;
    }
}
