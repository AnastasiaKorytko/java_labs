package org.example;

import java.util.*;
import java.text.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class StringProcess {
    private String inputString;
    private String separators;
    private ArrayList<String> tokens;
    private ArrayList<Double> numbers;
    private ArrayList<String> timeTokens;

    public StringProcess(String inputString, String separators) {
        this.inputString = inputString;
        this.separators = separators;
        this.tokens = new ArrayList<>();
        this.numbers = new ArrayList<>();
        this.timeTokens = new ArrayList<>();
    }

    public String process() {
        StringBuilder res = new StringBuilder();
        tokenizeString();
        res.append("Input string: ").append(inputString).append("\n");
        res.append("Separators: ").append(separators).append("\n");
        res.append("Tokens: ").append(tokens).append("\n");
        findNumbers();
        res.append("Double numbers: ").append(numbers).append("\n");
        findTime();
        res.append("Time (HH-MM): ").append(timeTokens).append("\n");
        String rand = addRandomNumber();
        res.append("Add random number: ").append(rand).append("\n");
        String minL = removeMinLengthToken();
        res.append("Delete min token ending with -: ").append(minL).append("\n");
        sortTimeTokens();
        res.append("Time sort: ").append(timeTokens).append("\n");
        return res.toString().replace("[", "").replace("]", "");
    }

    private void tokenizeString() {
        StringTokenizer tok = new StringTokenizer(inputString, separators);
        while (tok.hasMoreTokens()) {
            String token = tok.nextToken().trim();
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
    }

    private void findNumbers() {
        Pattern numberPattern = Pattern.compile("-?\\d+\\.\\d+");
        for (String token : tokens) {
            Matcher matcher = numberPattern.matcher(token);
            if (matcher.matches()) {
                try {
                    numbers.add(Double.parseDouble(token));
                } catch (NumberFormatException e) {
                    System.out.println("Error");
                }
            }
        }
    }

    private void findTime() {
        Pattern timePattern = Pattern.compile("\\d{2}-\\d{2}");
        for (String token : tokens) {
            Matcher match = timePattern.matcher(token);
            if (match.matches()) {
                if (isTime(token)) {
                    timeTokens.add(token);
                }
            }
        }
    }

    private boolean isTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH-mm");
            sdf.setLenient(false);
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private String addRandomNumber() {
        StringBuffer sb = new StringBuffer(inputString);
        Random random = new Random();
        double randomNum = random.nextDouble() * 100;
        Pattern numberPattern = Pattern.compile("-?\\d+\\.\\d+");
        Matcher matcher = numberPattern.matcher(inputString);
        if (matcher.find()) {
            int position = matcher.end();
            sb.insert(position, " " + String.valueOf(randomNum) + " ");
        } else {
            int middle = sb.length() / 2;
            sb.insert(middle, " " + String.valueOf(randomNum) + " ");
        }
        return sb.toString();
    }

    private String removeMinLengthToken() {
        if (tokens.isEmpty()) {
            return inputString;
        }

        ArrayList<String> endingTokens = new ArrayList<>();
        for (String token : tokens) {
            if (!token.isEmpty() && token.charAt(token.length() - 1) == '-') {
                endingTokens.add(token);
            }
        }
        if (endingTokens.isEmpty()) {
            return inputString;
        }
        String minToken = Collections.min(endingTokens, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });
        StringBuffer sb = new StringBuffer(inputString);
        int index = sb.indexOf(minToken);

        if (index != -1) {
            sb.delete(index, index + minToken.length());
        }
        return sb.toString();
    }

    private void sortTimeTokens() {
        Collections.sort(timeTokens, new Comparator<String>() {
            public int compare(String t1, String t2) {
                return t1.compareTo(t2);
            }
        });
    }

    public ArrayList<String> getTimeTokens() {
        return timeTokens;
    }

    public ArrayList<Double> getNumbers() {
        return new ArrayList<>(numbers);
    }

}
