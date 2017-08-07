import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CriptoLib {
    static final String HEXA_KEY = "0123456789abcdef";
    static final int BIN_SIZE = 16;
    static final String MAPPED_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ -=+!@#$%®&*()_;.,<>:?/^}~]`{¥['|È·ÌÛ˙Í‚ÓÙıÒÏ‡ËÚ˘…¡Õ”⁄ ¬Œ‘’√—¿Ã“Ÿ»„√π≤≥£¢¨∞∫™Á«˝^";
    private static final int MASTER_ENCRYPTO = 3;
    public Map<String, String> bintochar = new HashMap<>();
    public Map<String, String> chartobin = new HashMap<>();
    private Random randomKey;

    public CriptoLib() {
        initiateMap(MAPPED_CHARS);
    }

    private void initiateMap(String chs) {
        for (int i = 0; i < chs.length(); i++) {
            char c = chs.charAt(i);
            String bin = charToBin(c);
            bintochar.put(bin, Character.toString(c));
            chartobin.put(Character.toString(c), bin);
        }
    }

    private String charToBin(char c) {
        String bin = Integer.toBinaryString(getHashedIntFromChar(c));
        while (bin.length() < BIN_SIZE) {
            bin = "0" + bin;
        }
        return bin;
    }

    private int getHashedIntFromChar(char c) {
        return (int) c << 5;
    }

    public String encrypto(String text, String key) {
        randomKey = null;
        String cripto = text + "$";
        String hexaKey = true ? key : binaryToHexadecimal(key);
        try {
            cripto = textToHexadecimal(cripto);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < text.length(); i++) {
            cripto = round(shiftString(cripto, false), hexaKey);
        }

        return text.length() + "-" + shiftString(cripto, getRandomFromKey(hexaKey).nextBoolean());
    }

    private boolean isHex(String key) {
        if (key.contains("2")) {
            return true;
        }
        if (key.contains("3")) {
            return true;
        }
        if (key.contains("4")) {
            return true;
        }
        if (key.contains("5")) {
            return true;
        }
        if (key.contains("6")) {
            return true;
        }
        if (key.contains("7")) {
            return true;
        }
        if (key.contains("8")) {
            return true;
        }
        if (key.contains("9")) {
            return true;
        }
        if (key.contains("a")) {
            return true;
        }
        if (key.contains("b")) {
            return true;
        }
        if (key.contains("c")) {
            return true;
        }
        if (key.contains("d")) {
            return true;
        }
        if (key.contains("e")) {
            return true;
        }
        if (key.contains("f")) {
            return true;
        }
        return false;
    }

    private int getRandomFromText(int i) {
        return new Random((int) Math.pow(2, i)).nextInt(i);
    }

    public String decrypto(String text, String key) {
        try {
            randomKey = null;
            int lenght = Integer.parseInt(text.split("-")[0]);
            String hexaKey = isHex(key) ? key : binaryToHexadecimal(key);
            String cripto = shiftString(text.split("-")[1],
                                        !getRandomFromKey(hexaKey).nextBoolean());
            for (int i = 0; i < lenght; i++) {
                cripto = shiftString(deround(cripto, hexaKey), true);
            }

            try {
                return hexadecimalToText(cripto);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String hexadecimalToText(String s) throws UnsupportedEncodingException {
        return binaryToText(hexadecimalToBinary(s));
    }

    private String binaryToText(String hexa) {
        String st = "";
        for (int i = 0; i < hexa.length() / BIN_SIZE; i++) {
            st += binToCharCanReverse(hexa.substring(BIN_SIZE * (i), BIN_SIZE * (i + 1)));
        }
        return st;
    }

    private String binToCharCanReverse(String s) {
        return bintochar.get(s) != null ? bintochar.get(s) : chartobin.get(s);
    }

    private String hexadecimalToBinary(String s) {
        String hexa = "";
        for (int i = 0; i < s.length(); i++) {
            hexa += getBin(s.charAt(i));
        }
        return hexa;
    }

    private String textToHexadecimal(String s) throws UnsupportedEncodingException {
        return binaryToHexadecimal(textToBinary(s));
    }

    private String textToBinary(String s) throws UnsupportedEncodingException {
        String st = "";
        for (int i = 0; i < s.length(); i++) {
            st += binToCharCanReverse(Character.toString(s.charAt(i)));
        }
        return st;
    }

    private String shiftString(String s, boolean antishift) {
        String str = "";
        if (!antishift) {
            str = s.substring(1) + s.charAt(0);
        } else {
            str = s.charAt(s.length() - 1) + s.substring(0, s.length() - 1);
        }
        return str;
    }

    private String deround(String text, String key) {
        String result = "";
        Random r = getRandomFromKey(key);
        for (int i = 0; i < text.length(); i++) {
            result += shift(text.charAt(i),
                            r.nextBoolean(),
                            r.nextBoolean(),
                            r.nextBoolean(),
                            r.nextBoolean(),
                            false);
        }
        return result;
    }

    private Random getRandomFromKey(String key) {
        long[] keyArray = divideKey(key);
        long genIndex = keyArray[0];
        for (int i = 1; i < keyArray.length; i++) {
            genIndex *= keyArray[i];
        }
        this.randomKey = new Random(genIndex);
        return this.randomKey;
    }

    private String round(String text, String key) {
        String result = "";
        Random r = getRandomFromKey(key);
        for (int i = 0; i < text.length(); i++) {
            result += shift(text.charAt(i),
                            r.nextBoolean(),
                            r.nextBoolean(),
                            r.nextBoolean(),
                            r.nextBoolean(),
                            true);
        }
        return result;
    }

    private char shift(char c, boolean b0, boolean b1, boolean b2, boolean b3, boolean s) {
        if (b0) {
            if (b1) {
                if (b2) {
                    if (b3) {
                        return shift(c, 0, s);
                    } else {
                        return shift(c, 1, s);
                    }
                } else {
                    if (b3) {
                        return shift(c, 2, s);
                    } else {
                        return shift(c, 3, s);
                    }
                }
            } else {
                if (b2) {
                    if (b3) {
                        return shift(c, 4, s);
                    } else {
                        return shift(c, 5, s);
                    }
                } else {
                    if (b3) {
                        return shift(c, 6, s);
                    } else {
                        return shift(c, 7, s);
                    }
                }
            }
        } else {
            if (b1) {
                if (b2) {
                    if (b3) {
                        return shift(c, 8, s);
                    } else {
                        return shift(c, 9, s);
                    }
                } else {
                    if (b3) {
                        return shift(c, 10, s);
                    } else {
                        return shift(c, 11, s);
                    }
                }
            } else {
                if (b2) {
                    if (b3) {
                        return shift(c, 12, s);
                    } else {
                        return shift(c, 13, s);
                    }
                } else {
                    if (b3) {
                        return shift(c, 14, s);
                    } else {
                        return shift(c, 15, s);
                    }
                }
            }
        }
    }

    private char shift(char c, int e, boolean s) {
        char ch = ' ';
        int iter = 1;
        boolean fst = false;
        int pos = HEXA_KEY.indexOf(c);
        try {
            if (s) {
                for (int i = HEXA_KEY.indexOf(c); i < HEXA_KEY.indexOf(c) + e; i++) {
                    if (i >= HEXA_KEY.length() - 1) {
                        pos = pos + 1 - (!fst ? (iter * HEXA_KEY.length()) : 0);
                        fst = true;
                    } else {
                        pos = pos + 1;
                    }
                }
                ch = HEXA_KEY.charAt(pos);

            } else {
                for (int i = HEXA_KEY.indexOf(c); i < HEXA_KEY.indexOf(c) + e; i++) {
                    if (pos <= 0) {
                        pos = pos - 1 + (!fst ? (iter * HEXA_KEY.length()) : 0);
                        fst = true;
                    } else {
                        pos = pos - 1;
                    }
                }
                ch = HEXA_KEY.charAt(pos);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ch;
    }

    private long[] divideKey(String key) {
        long l[] = new long[key.length() / 8];
        for (int i = 0; i < l.length; i++) {
            l[i] = Long.parseLong(key.substring(i * 8, (i + 1) * 8), 16);
        }
        return l;
    }

    public String binaryToHexadecimal(String bin) {
        String hexa = "";
        for (int i = 0; i < bin.length() - 4; i += 4) {
            hexa += HEXA_KEY.charAt(getTinyInt(bin.substring(i, i + 4)));
        }
        return hexa;
    }

    private String getBin(char c) {
        switch (c) {
        case '0':
            return "0000";
        case '1':
            return "0001";
        case '2':
            return "0010";
        case '3':
            return "0011";
        case '4':
            return "0100";
        case '5':
            return "0101";
        case '6':
            return "0110";
        case '7':
            return "0111";
        case '8':
            return "1000";
        case '9':
            return "1001";
        case 'a':
            return "1010";
        case 'b':
            return "1011";
        case 'c':
            return "1100";
        case 'd':
            return "1101";
        case 'e':
            return "1110";
        case 'f':
            return "1111";
        default:
            return "nan";
        }
    }

    private int getTinyInt(String s) {
        int ret = 0;
        int up = 1;
        for (int i = 3; i >= 0; i--) {
            ret += s.charAt(i) == '0' ? 0 : up;
            up *= 2;
        }
        return ret;
    }

    public String doubleEncrypto(String text, String key) {
        String c = encrypto(text, key);
        c = encrypto(c, key);
        return c;
    }

    public String masterEncrypto(String text, String key) {
        System.out.println("This may take a long while depending on the size of the text");
        String c = encrypto(text, key);
        for (int i = 0; i < MASTER_ENCRYPTO; i++) {
            c = encrypto(c, key);
        }
        return c;
    }

    public BufferedImage putIntoImage(Image img, String s) {
        int l = (int) (Math.sqrt(s.length()) + 1);
        BufferedImage bi = new BufferedImage(l, l, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        BufferedImage bi2 = scale(toBufferedImage(img), l + 1, l + 1);
        int ss = 0;
        for (int x = 0; x < l; x++) {
            for (int y = 0; y < l; y++) {
                if (ss >= s.length()) {
                    g.setColor(getColor(getColorFromRGB(bi2.getRGB(x, y)), 'a'));
                } else {
                    g.setColor(getColor(getColorFromRGB(bi2.getRGB(x, y)), s.charAt(ss)));
                    g.drawLine(x, y, x, y);
                    ss++;
                }
            }
        }
        return bi;
    }

    public BufferedImage putIntoImage(String s) {
        int l = (int) (Math.sqrt(s.length()) + 1);
        BufferedImage bi = new BufferedImage(l, l, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        int ss = 0;
        for (int x = 0; x < l; x++) {
            for (int y = 0; y < l; y++) {
                if (ss >= s.length()) {
                    break;
                }
                g.setColor(getColor(null, s.charAt(ss)));
                g.drawLine(x, y, x, y);
                ss++;
            }
        }
        return bi;
    }

    public Color getColorFromRGB(int argb) {
        int r = (argb) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = (argb >> 16) & 0xFF;
        return new Color(r, g, b);
    }

    public BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight) {

        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
                : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;

        int w = img.getWidth();
        int h = img.getHeight();

        int prevW = w;
        int prevH = h;

        do {
            if (w > targetWidth) {
                w /= 2;
                w = (w < targetWidth) ? targetWidth : w;
            }

            if (h > targetHeight) {
                h /= 2;
                h = (h < targetHeight) ? targetHeight : h;
            }

            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        if (g2 != null) {
            g2.dispose();
        }

        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }

        return ret;

    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null),
                                                 img.getHeight(null),
                                                 BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    private Color getColor(Color color, char c) {
        switch (c) {
        case '0':
            return getColor(255, color);
        case '1':
            return getColor(250, color);
        case '2':
            return getColor(245, color);
        case '3':
            return getColor(240, color);
        case '4':
            return getColor(235, color);
        case '5':
            return getColor(230, color);
        case '6':
            return getColor(225, color);
        case '7':
            return getColor(220, color);
        case '8':
            return getColor(215, color);
        case '9':
            return getColor(210, color);
        case 'a':
            return getColor(205, color);
        case 'b':
            return getColor(200, color);
        case 'c':
            return getColor(195, color);
        case 'd':
            return getColor(190, color);
        case 'e':
            return getColor(185, color);
        case 'f':
            return getColor(180, color);
        default:
            return getColor(175, color);
        }
    }

    private Color getColor(int i, Color c) {
        if (c != null) {
            return new Color(c.getRed(), c.getGreen(), c.getBlue(), i);
        } else {
            return new Color(i, i, i);
        }
    }

    public String doubleDecrypt(String text, String key64) {
        return decrypto(decrypto(text, key64), key64);
    }

}
