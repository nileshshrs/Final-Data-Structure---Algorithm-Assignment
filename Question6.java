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
import java.util.concurrent.atomic.AtomicBoolean;

class ExtendedSwingFrame extends JFrame {

    private JTextField textField;
    private JButton addUrlButton;
    private JButton downloadButton;
    private JButton clearUrlButton;
    private JButton cancelButton;
    private JPanel progressBarPanel;

    private ExecutorService executorService;
    private List<String> urlList;
    private List<JProgressBar> progressBars;
    private List<DownloadWorker> workers;

    public ExtendedSwingFrame() {
        setTitle("Extended Swing Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        textField = new JTextField(20);
        addUrlButton = new JButton("Add URL");
        downloadButton = new JButton("Download Images");
        clearUrlButton = new JButton("Clear URLs");
        cancelButton = new JButton("Cancel All Downloads");
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

        cancelButton.addActionListener(e -> {
            cancelAllDownloads();
        });

        panel.add(new JLabel("Image URL:"));
        panel.add(textField);
        panel.add(addUrlButton);
        panel.add(downloadButton);
        panel.add(clearUrlButton);
        panel.add(cancelButton);
        panel.add(progressBarPanel);

        getContentPane().add(panel);

        setSize(400, 300);
        setVisible(true);

        executorService = Executors.newFixedThreadPool(10);
    }

    private void addProgressBar(int imageNumber) {
        JLabel label = new JLabel("Image " + imageNumber + ": ");
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> {
            int workerIndex = findWorkerIndexByProgressBar(progressBar);
            if (workerIndex != -1) {
                workers.get(workerIndex).pauseDownload();
            }
        });

        JButton cancelDownloadButton = new JButton("Cancel");
        cancelDownloadButton.addActionListener(e -> {
            int workerIndex = findWorkerIndexByProgressBar(progressBar);
            if (workerIndex != -1) {
                workers.get(workerIndex).cancel(true);
                JOptionPane.showMessageDialog(ExtendedSwingFrame.this,
                        "Download canceled for Image " + imageNumber,
                        "Canceled", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(pauseButton);
        buttonPanel.add(cancelDownloadButton);

        JPanel progressBarPanel = new JPanel(new BorderLayout());
        progressBarPanel.add(label, BorderLayout.WEST);
        progressBarPanel.add(progressBar, BorderLayout.CENTER);
        progressBarPanel.add(buttonPanel, BorderLayout.EAST);

        this.progressBarPanel.add(progressBarPanel);
        progressBars.add(progressBar);

        revalidate();
        repaint();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        downloadImage(urlList.get(imageNumber - 1), progressBar, imageNumber);
    }

    private void cancelAllDownloads() {
        if (workers != null && !workers.isEmpty()) {
            for (DownloadWorker worker : workers) {
                worker.cancel(true);
            }
        }
    }

    private int findWorkerIndexByProgressBar(JProgressBar progressBar) {
        for (int i = 0; i < progressBars.size(); i++) {
            if (progressBars.get(i) == progressBar) {
                return i;
            }
        }
        return -1;
    }

    private void downloadImage(String imageUrl, JProgressBar progressBar, int imageNumber) {
        DownloadWorker worker = new DownloadWorker(imageUrl, progressBar, imageNumber);
        executorService.execute(worker);
        workers.add(worker);
    }

    private class DownloadWorker extends SwingWorker<Void, Integer> {

        private final String imageUrl;
        private final JProgressBar progressBar;
        private final int imageNumber;
        private final AtomicBoolean isPaused;

        public DownloadWorker(String imageUrl, JProgressBar progressBar, int imageNumber) {
            this.imageUrl = imageUrl;
            this.progressBar = progressBar;
            this.imageNumber = imageNumber;
            this.isPaused = new AtomicBoolean(false);
        }

        public void pauseDownload() {
            isPaused.set(!isPaused.get());
            if (isPaused.get()) {
                JOptionPane.showMessageDialog(ExtendedSwingFrame.this,
                        "Download paused for Image " + imageNumber,
                        "Paused", JOptionPane.INFORMATION_MESSAGE);
            }
        }

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
                        if (isPaused.get()) {
                            while (isPaused.get()) {
                                Thread.sleep(50);
                            }
                            if (isCancelled()) {
                                Files.deleteIfExists(outputPath);
                                throw new InterruptedException();
                            }
                        }

                        out.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        int progress = (int) ((double) totalBytesRead / contentLength * 100);
                        publish(progress);
                        Thread.sleep(50);
                    }
                }

                publish(100);
                Files.move(Paths.get(System.getProperty("user.home") + "/Desktop/", fileName),
                        outputPath, StandardCopyOption.REPLACE_EXISTING);

            } catch (InterruptedException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(ExtendedSwingFrame.this,
                            "Download interrupted for Image " + imageNumber,
                            "Interrupted", JOptionPane.WARNING_MESSAGE);
                });
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
            boolean allDone = true;
            for (DownloadWorker worker : workers) {
                if (!worker.isDone() || worker.isCancelled()) {
                    allDone = false;
                    break;
                }
            }
            if (allDone) {
                urlList.clear();
                JOptionPane.showMessageDialog(ExtendedSwingFrame.this,
                        "All images downloaded successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
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
