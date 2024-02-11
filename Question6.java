import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ExtendedSwingFrame extends JFrame {

    private JTextField textField;
    private JButton addUrlButton;
    private JButton downloadButton;
    private JButton clearUrlButton; // Added clear URL button
    private JPanel progressBarPanel;

    private ExecutorService executorService;
    private List<String> urlList;
    private List<JProgressBar> progressBars;
    private List<SwingWorker<Void, Integer>> workers;

    public ExtendedSwingFrame() {
        setTitle("Extended Swing Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        textField = new JTextField(20);
        addUrlButton = new JButton("Add URL");
        downloadButton = new JButton("Download Images");
        clearUrlButton = new JButton("Clear URLs"); // Initialize clear URL button
        progressBarPanel = new JPanel(new GridLayout(0, 1));

        urlList = new ArrayList<>();
        progressBars = new ArrayList<>();
        workers = new ArrayList<>();

        addUrlButton.addActionListener(e -> {
            String imageUrl = textField.getText();
            if (!imageUrl.isEmpty()) {
                urlList.add(imageUrl);
                textField.setText("");
                addProgressBar(urlList.size());
            }
        });

        downloadButton.addActionListener(e -> {
            if (!urlList.isEmpty()) {
                for (int i = 0; i < urlList.size(); i++) {
                    downloadImage(urlList.get(i), progressBars.get(i), i + 1);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No URLs to download.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        clearUrlButton.addActionListener(e -> {
            urlList.clear();
            progressBars.clear();
            progressBarPanel.removeAll();
            progressBarPanel.revalidate();
            progressBarPanel.repaint();
        });

        panel.add(new JLabel("Image URL:"));
        panel.add(textField);
        panel.add(addUrlButton);
        panel.add(downloadButton);
        panel.add(clearUrlButton); // Add clear URL button to the panel
        panel.add(progressBarPanel);

        getContentPane().add(panel);

        setSize(400, 300);
        setVisible(true);

        // Use a fixed thread pool with a maximum of 10 threads
        executorService = Executors.newFixedThreadPool(10);
    }

    private void addProgressBar(int imageNumber) {
        JLabel label = new JLabel("Image " + imageNumber + ": ");
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JPanel progressBarPanel = new JPanel(new BorderLayout());
        progressBarPanel.add(label, BorderLayout.WEST);
        progressBarPanel.add(progressBar, BorderLayout.CENTER);

        this.progressBarPanel.add(progressBarPanel);

        progressBars.add(progressBar);

        revalidate();
        repaint();
    }

    private void downloadImage(String imageUrl, JProgressBar progressBar, int imageNumber) {
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    if (!isValidUrl(imageUrl)) {
                        throw new MalformedURLException("Invalid URL: " + imageUrl);
                    }

                    URL url = new URL(imageUrl);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int responseCode = connection.getResponseCode();

                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code: " + responseCode);
                    }

                    String contentType = connection.getContentType();
                    connection.disconnect();

                    if (contentType == null || !contentType.startsWith("image")) {
                        throw new IOException("URL does not point to an image: " + imageUrl);
                    }

                    String fileName = url.getFile();
                    fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                    fileName = fileName.split("\\?")[0];

                    String newFileName = getDynamicFileName(System.getProperty("user.home") + "/Desktop/", fileName);
                    Path outputPath = Paths.get(System.getProperty("user.home") + "/Desktop/", newFileName);

                    Files.createDirectories(outputPath.getParent());

                    int contentLength = connection.getContentLength();
                    int totalBytesRead = 0;

                    try (InputStream in = url.openStream();
                         OutputStream out = Files.newOutputStream(outputPath)) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;

                            int progress = (int) ((double) totalBytesRead / contentLength * 100);
                            publish(progress);
                        }
                    }

                    publish(100);
                    // Wait until the file is saved to the directory before completing
                    Files.move(Paths.get(System.getProperty("user.home") + "/Desktop/", fileName),
                            outputPath, StandardCopyOption.REPLACE_EXISTING);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    showError("Error downloading image: Invalid URL", imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    showError("Error downloading image: " + e.getMessage(), imageUrl);
                }

                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                for (int progress : chunks) {
                    progressBar.setValue(progress);
                }
            }

            @Override
            protected void done() {
                // Check if all workers are done
                boolean allDone = true;
                for (SwingWorker<Void, Integer> worker : workers) {
                    if (!worker.isDone()) {
                        allDone = false;
                        break;
                    }
                }
                if (allDone) {
                    // Clear URLs when all downloads are completed
                    urlList.clear();
                    JOptionPane.showMessageDialog(ExtendedSwingFrame.this,
                            "All images downloaded successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };

        executorService.execute(() -> {
            worker.execute();
            workers.add(worker);
        });
    }

    private boolean isValidUrl(String urlString) {
        try {
            new URL(urlString).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getDynamicFileName(String directory, String fileName) {
        String baseName = fileName.substring(0, Math.min(fileName.lastIndexOf('.'), 255));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        Path filePath = Paths.get(directory, fileName);
        int count = 1;

        while (Files.exists(filePath)) {
            String newFileName = MessageFormat.format("{0}_{1}{2}", baseName, count++, extension);
            filePath = Paths.get(directory, newFileName);
        }

        return filePath.getFileName().toString();
    }

    private void showError(String message, String imageUrl) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    message + "\nURL: " + imageUrl,
                    "Error", JOptionPane.ERROR_MESSAGE);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExtendedSwingFrame());
    }
}
