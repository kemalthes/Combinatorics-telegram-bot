package io.terver.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Arrays;
import java.util.regex.Pattern;

@Service
public class CalculatorService {

    private Pattern compilePatternWithNums(int k) {
        if (k < 1) throw new IllegalArgumentException();
        return Pattern.compile("^\\s*\\d+" + "\\s+\\d+".repeat(k - 1) + "\\s*$");
    }

    private boolean isNotMatch(Pattern pattern, String message) {
        return !pattern.matcher(message).matches();
    }

    private int[] parseNumbers(String message, int k) {
        String[] parts = message.strip().split("\\s+");
        if (parts.length != k) {
            throw new IllegalArgumentException();
        }
        return Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
    }

    public BigInteger permutations(String message) {
        if (isNotMatch(compilePatternWithNums(1), message)) {
            throw new NumberFormatException();
        }
        int n = Integer.parseInt(message.trim());
        if (n < 0) throw new IllegalArgumentException();
        BigInteger res = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            res = res.multiply(BigInteger.valueOf(i));
        }
        return res;
    }

    public BigInteger permutationsRepeat(String message) {
        int total = Integer.parseInt(message.strip().trim().split("\\s+")[0]) + 1;
        if (isNotMatch(compilePatternWithNums(total), message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseNumbers(message, total);
        int sum = Arrays.stream(nums).sum() - nums[0];
        BigInteger res = BigInteger.ONE;
        for (int i = 2; i <= sum; i++) {
            res = res.multiply(BigInteger.valueOf(i));
        }
        for (int j = 1; j < total; j++) {
            if (nums[j] < 0) {
                throw new IllegalArgumentException();
            }
            for (int i = 2; i <= nums[j]; i++) {
                res = res.divide(BigInteger.valueOf(i));
            }
        }
        return res;
    }

    public BigInteger distribution(String message) {
        if (isNotMatch(compilePatternWithNums(2), message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseNumbers(message, 2);
        int n = nums[0], k = nums[1];
        if (n < k || n < 0 || k < 0) {
            throw new IllegalArgumentException();
        }
        BigInteger res = BigInteger.ONE;
        for (int i = n - k + 1; i <= n; i++) {
            res = res.multiply(BigInteger.valueOf(i));
        }
        return res;
    }

    public BigInteger distributionRepeat(String message) {
        if (isNotMatch(compilePatternWithNums(2), message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseNumbers(message, 2);
        int n = nums[0], k = nums[1];
        if (n < k || n < 0 || k < 0) {
            throw new IllegalArgumentException();
        }
        return BigInteger.valueOf(n).pow(k);
    }

    public BigInteger combination(String message) {
        if (isNotMatch(compilePatternWithNums(2), message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseNumbers(message, 2);
        int n = nums[0], k = nums[1];
        if (n < k || n < 0 || k < 0) {
            throw new IllegalArgumentException();
        }
        int r = Math.min(k, n - k);
        BigInteger res = BigInteger.ONE;
        for (int i = 1; i <= r; i++) {
            res = res.multiply(BigInteger.valueOf(n - r + i));
            res = res.divide(BigInteger.valueOf(i));
        }
        return res;
    }

    public BigInteger combinationRepeat(String message) {
        if (isNotMatch(compilePatternWithNums(2), message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseNumbers(message, 2);
        int n = nums[0], k = nums[1];
        if (n < k || n < 0 || k < 0) {
            throw new IllegalArgumentException();
        }
        int r = Math.min(k, n - 1);
        BigInteger res = BigInteger.ONE;
        for (int i = 1; i <= r; i++) {
            res = res.multiply(BigInteger.valueOf(n + k - 1 - r + i));
            res = res.divide(BigInteger.valueOf(i));
        }
        return res;
    }

    public BigDecimal urnModelOne(String message) {
        if (isNotMatch(compilePatternWithNums(3), message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseNumbers(message, 3);
        int n = nums[0], m = nums[1], k = nums[2];
        if (n < m || m <= k || k < 0) {
            throw new IllegalArgumentException();
        }
        BigInteger num = combination(" " + m + " " + k);
        BigInteger den = combination(" " + n + " " + k);
        return new BigDecimal(num).divide(new BigDecimal(den), MathContext.DECIMAL128);
    }

    public BigDecimal urnModelTwo(String message) {
        if (isNotMatch(compilePatternWithNums(4), message)) {
            throw new NumberFormatException();
        }
        int[] nums = parseNumbers(message, 4);
        int n = nums[0], m = nums[1], k = nums[2], r = nums[3];
        if (n < m || m <= k || k < r || n < 0 || m < 0 || k < 0 || r < 0) {
            throw new IllegalArgumentException();
        }
        BigInteger a = combination(" " + m + " " + r);
        BigInteger b = combination(" " + (n - m) + " " + (k - r));
        BigInteger num = a.multiply(b);
        BigInteger den = combination(" " + n + " " + k);
        return new BigDecimal(num).divide(new BigDecimal(den), MathContext.DECIMAL128);
    }
}