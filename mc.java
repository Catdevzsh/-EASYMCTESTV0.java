import javax.swing.*;
import java.awt.event.*;
import java.net.http.*;
import java.net.URI;
import java.io.IOException;
import java.net.http.HttpResponse.BodyHandlers;

public class EasyMCTokenRedeemer extends JFrame {
    private JTextField tokenField;
    private JButton redeemButton;
    private JTextArea responseArea;

    public EasyMCTokenRedeemer() {
        setTitle("Token Redeemer");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        add(panel);

        panel.setLayout(null);

        JLabel label = new JLabel("Token:");
        label.setBounds(10, 20, 80, 25);
        panel.add(label);

        tokenField = new JTextField(20);
        tokenField.setBounds(100, 20, 165, 25);
        panel.add(tokenField);

        redeemButton = new JButton("Redeem");
        redeemButton.setBounds(270, 20, 100, 25);
        panel.add(redeemButton);

        responseArea = new JTextArea();
        responseArea.setBounds(10, 60, 360, 100);
        panel.add(responseArea);

        redeemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redeemToken();
            }
        });
    }

    private void redeemToken() {
        String token = tokenField.getText();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.easymc.io/v1/token/redeem"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"token\":\"" + token + "\"}"))
                .build();

        client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::updateResponseArea)
                .join();
    }

    private void updateResponseArea(String response) {
        responseArea.setText(response);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EasyMCTokenRedeemer frame = new EasyMCTokenRedeemer();
            frame.setVisible(true);
        });
    }
}
