package main;

import tile.Map;

import java.util.HashMap;
import java.util.Set;

public class AncientLanguage {

    private static final java.util.Map<Character, String> toAncient = new HashMap<>();
    private static final java.util.Map<String,Character> toNormal = new HashMap<>();

    static {
        String[] symbols = {
                "ꋫ","ꃳ","ꉔ","ꂠ","ꏂ","ꊰ","ꍌ","ꁁ","ꂑ","ꆜ",
                "ꀗ","ꂡ","ꂵ","ꋊ","ꂦ","ꉣ","ꆰ","ꋧ","ꌗ","ꐟ",
                "ꐇ","ꅐ","ꅏ","ꊟ","ꌩ","ꁴ"
        };
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < alpha.length(); i++) {
            char c = alpha.charAt(i);
            String s = symbols[i];
            toAncient.put(c,s);
            toAncient.put(Character.toUpperCase(c), s.toUpperCase());
            toNormal.put(s,c);
            toNormal.put(s.toUpperCase(),Character.toUpperCase(c));
        }
    }

    public static String encode(String text) {
        StringBuilder sb = new StringBuilder();
        for(char c : text.toCharArray()) {
            String sym = toAncient.get(c);
            sb.append(sym != null ? sym : c);
        }
        return sb.toString();
    }

    public static String decode(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()){
            Character orig = toNormal.get(String.valueOf(c));
            sb.append(orig != null ?  orig : c);
        }
        return sb.toString();
    }

    public static String partialDecode(String text, Set<String> knowWords) {
        String[] words = text.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            String decoded = decode(word.replaceAll("[^a-zA-Zꋫ-ꌩ]", ""));
            if (knowWords.contains(decoded.toLowerCase())) {
                sb.append(decoded);
            }else {
                sb.append(decoded);
            }
            sb.append(" ");
        }
        return sb.toString().trim();
    }

}
