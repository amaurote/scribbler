import sun.misc.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class Scribbler {

    private final int NAMES_COUNT = 1000;

    private final int FEMALE = 0;
    private final int MALE = 1;
    private final int SURNAME = 2;

    private final String DELIMITER = ",";

    private final Random random = new Random();

    private List<String[]> lines;

    // preferences
    private int doubleSurnameRate = 15;

    // constructor and initialisation
    public Scribbler() throws IOException {
        init();
    }

    public static void main(String[] args) throws IOException {
        Scribbler scribbler = new Scribbler();
        scribbler.setDoubleSurnameRate(20);
        System.out.println(scribbler.getFullNames(Gender.ANY, 100));
    }

    private void init() throws IOException {
        InputStream namesInputStream = Scribbler.class.getResourceAsStream("/names.csv");
        byte[] strBytes = IOUtils.readFully(namesInputStream, -1, true);

        namesInputStream.close();

        String str = new String(strBytes);
        String[] parts = str.split("\\r?\\n");
        lines = new ArrayList<>();

        for (String s : parts) {
            lines.add(s.split(Pattern.quote(DELIMITER)));
        }
    }

    // private helper methods
    private String getValue(int row, int column) {
        try {
            return lines.get(row)[column];
        } catch (Exception e) {
            return "";
        }
    }

    private boolean percentageToBoolean(int percentage) {
        return (random.nextInt(100) + 1) <= Math.max(0, Math.min(100, percentage));
    }

    // generator methods
    public String getFirstName(Gender gender) {
        if (gender == Gender.FEMALE) {
            return getValue(random.nextInt(NAMES_COUNT), FEMALE);
        } else if (gender == Gender.MALE) {
            return getValue(random.nextInt(NAMES_COUNT), MALE);
        } else {
            return getValue(random.nextInt(NAMES_COUNT), (random.nextBoolean()) ? FEMALE : MALE);
        }
    }

    public String getLastName() {
        if (percentageToBoolean(doubleSurnameRate))
            return getValue(random.nextInt(NAMES_COUNT), SURNAME) + "-" + getValue(random.nextInt(NAMES_COUNT), SURNAME);
        else
            return getValue(random.nextInt(NAMES_COUNT), SURNAME);
    }

    public String getFullName(Gender gender) {
        return getFirstName(gender) + " " + getLastName();
    }

    public List<String> getFirstNames(Gender gender, int count) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            names.add(getFirstName(gender));
        }
        return names;
    }

    public List<String> getLastNames(int count) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            names.add(getLastName());
        }
        return names;
    }

    public List<String> getFullNames(Gender gender, int count) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            names.add(getFullName(gender));
        }
        return names;
    }

    // getters setters
    public int getDoubleSurnameRate() {
        return doubleSurnameRate;
    }

    public void setDoubleSurnameRate(int doubleSurnameChance) {
        this.doubleSurnameRate = doubleSurnameChance;
    }
}
