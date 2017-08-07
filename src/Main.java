import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Main implements Runnable {

    private static final String IMAGE_OUT = "C:\\Users\\TECBMGFI\\Desktop\\encrypted.png";
    private static final String IMAGE_IN = "C:\\Users\\TECBMGFI\\Desktop\\java.jpg";
    private static String ZEROES = "";
    volatile private static long i;
    private static Label label;
    private static long strt = 0;
    private static Label label2;
    private static Label label3;
    private static Label label4;
    private static Label label5;
    private static Label label6;
    private static String dec;
    private static Label label7;
    private static Label label8;
    private static Label label9;
    private static JTextField textField;
    private static Label label0;
    private static JButton button;
    private static JTextField textFieldKey;
    private static Label labekey;
    private static Label label10;
    private static JTextField textFieldText;
    private static JButton button3;
    private static JTextField textFieldTextEnc;
    private static JTextField textFieldTextEncKey;
    private static Label label11;
    private static Label label12;

    public static void main(String[] args) {

        String key64 = "0000def1";
        ZEROES = key64.replaceAll(".", "0");
        JFrame frame = new JFrame("FRAME");
        frame.setBounds(10, 10, 700, 500);
        frame.getContentPane().setLayout(new GridLayout(0, 1));
        label0 = new Label("Insira o código");
        frame.getContentPane().add(label0);
        textField = new JTextField();
        frame.getContentPane().add(textField);
        labekey = new Label("Chave:");
        frame.getContentPane().add(labekey);
        textFieldKey = new JTextField();
        frame.getContentPane().add(textFieldKey);
        button = new JButton("Processar");
        frame.getContentPane().add(button);
        label = new Label("Tentativas: " + i);
        frame.getContentPane().add(label);
        label2 = new Label("VM: " + i);
        frame.getContentPane().add(label2);
        label3 = new Label("Time processing: 0");
        frame.getContentPane().add(label3);
        label4 = new Label("Time processing: 0");
        frame.getContentPane().add(label4);
        label5 = new Label("Time processing: 0");
        frame.getContentPane().add(label5);
        label6 = new Label("Time processing: 0");
        frame.getContentPane().add(label6);
        label7 = new Label("Time processing: 0");
        frame.getContentPane().add(label7);
        label8 = new Label();
        frame.getContentPane().add(label8);
        label9 = new Label("Tamanho da chave: " + (long) (key64.length() * 4) + " bits");
        frame.getContentPane().add(label9);
        label10 = new Label("Encriptar texto");
        frame.getContentPane().add(label10);
        textFieldText = new JTextField();
        frame.getContentPane().add(textFieldText);
        button3 = new JButton("Encriptar");
        frame.getContentPane().add(button3);
        label11 = new Label("Resultado:");
        frame.getContentPane().add(label11);
        textFieldTextEnc = new JTextField();
        frame.getContentPane().add(textFieldTextEnc);
        label12 = new Label("Chave");
        frame.getContentPane().add(label12);
        textFieldTextEncKey = new JTextField();
        frame.getContentPane().add(textFieldTextEncKey);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        long l = System.nanoTime();
        // Initialize criptolib
        CriptoLib cl = new CriptoLib();
        String toEncrypt = "Tem algo pra fazer? espero que tenha descriptografado";
        String encrypted = cl.encrypto(toEncrypt, key64);
        label8.setText("Procurando significado para: nada");
        System.out.println("Encrypted: " + encrypted);
        String decrypted = cl.decrypto(encrypted, key64);
        System.out.println("Decrypted: " + decrypted);
        button3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String hex = "";
                if (textFieldTextEncKey.getText().equals("")) {
                    hex = "0000" + getRandomHexString(4);
                } else {
                    hex = textFieldTextEncKey.getText();
                }
                textFieldTextEncKey.setText(hex);
                try {
                    textFieldTextEnc.setText(cl.encrypto(textFieldText.getText(), hex));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    textFieldTextEnc.setText("Problemas ao encriptar texto");
                }

            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        running = true;
                        Thread t = new Thread(new Main());
                        t.start();
                        // Brute force test

                        if (!textFieldKey.getText().equals("")
                                && textFieldKey.getText().length() > 3) {
                            dec = cl.decrypto(textField.getText().toString(),
                                              textFieldKey.getText());
                        } else {
                            if (textFieldKey.getText().length() <= 3
                                    && !textFieldKey.getText().equals("")) {
                                completeZeroes(Integer.parseInt(textFieldKey.getText()));
                            }
                            label8.setText("Procurando significado para: " + textField.getText());
                            for (i = strt; i < 1E12; i++) {
                                String key8 = generateKey(i);
                                dec = cl.decrypto(textField.getText().toString(), key8);
                                if (dec != null) {
                                    if (!dec.contains("null")) {
                                        System.out
                                                .println("Decrypted: " + dec + "    key: " + key8);
                                        break;
                                    }
                                }
                            }
                        }
                        t = null;
                        running = false;
                        label.setText("Tentativas: " + (Main.i - strt));
                        label4.setText("Tentativa atual: " + generateKey(Main.i));
                        label6.setText("Resultado: " + dec);
                        label6.setForeground(Color.RED);
                        System.out.println((System.nanoTime() - l) / 1E9f);
                    }

                    private void completeZeroes(int length) {
                        ZEROES = "";
                        for (int i = 0; i < length; i++) {
                            ZEROES += "0";
                        }

                    }
                });
                thread.run();
            }
        });
    }

    public static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

    private static String timeDown(String hex, int vel) {
        try {
            long l = (Long.parseLong(hex, 16) - (int) Main.i) / vel;
            long years = l / 31536000;
            long days = (l % 31536000) / 86400;
            long hours = (l % 86400) / 3600;
            long minutes = (l % 3600) / 60;
            long seconds = l % 60;
            return years + "a" + days + "d" + hours + "h" + minutes + "m" + seconds + "s";
        } catch (Exception e) {
            long t = (long) (Math.pow(16, hex.length()) / vel / 31536000);
            return "muito tempo (" + (t > 11E9 ? (long) (t / 11E9) + " universos" : t + " anos")
                    + ")";
        }
    }

    private static String generateKey(long i) {
        String s = Long.toString(i, 16);
        String intAsString = s.length() <= 10 ? ZEROES.substring(s.length()) + s : s;
        return intAsString;
    }

    public static void imagefy(String encrypted, CriptoLib cl) {
        try {
            Image image = ImageIO.read(new File(IMAGE_IN));
            BufferedImage bi = cl.putIntoImage(image, encrypted);
            File outputfile = new File(IMAGE_OUT);
            ImageIO.write(bi, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean running;
    private static String target;

    @Override
    public void run() {
        long lastI = i;
        long precision = 10;
        int i = 0;
        while (running) {
            try {
                label.setText("Tentativas: " + (Main.i - strt));
                label2.setText("Va: " + (Main.i - lastI) / (1f / precision) + "t/s");
                label3.setText("Time processing: " + i * 1f / precision + "s");
                label4.setText("Tentativa atual: " + generateKey(Main.i));
                label5.setText("VM: " + (Main.i - strt) / (i * 1f / precision) + "t/s");
                label6.setText("Resultado: " + dec == null ? "" : dec.replace("null", ""));
                if (i > precision * 2) {
                    label7.setText("Tempo previsto: "
                            + timeDown(target == null ? ZEROES.replace("0", "f") : target,
                                       (int) ((Main.i - strt) / (i * 1f / precision))));
                }
                lastI = Main.i;
                i++;
                try {
                    Thread.sleep(1000 / precision);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            }
        }

    }

}
