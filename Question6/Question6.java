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

// ExtendedSwingFrame class extending JFrame
class ExtendedSwingFrame extends JFrame {

    // Components for the GUI
    private JTextField textField;
    private JButton addUrlButton;
    private JButton downloadButton;
    private JButton clearUrlButton;
    private JButton cancelButton;
    private JPanel progressBarPanel;

    // ExecutorService for managing threads
    private ExecutorService executorService;

    // Lists to store URLs, progress bars, and download workers
    private List<String> urlList;
    private List<JProgressBar> progressBars;
    private List<DownloadWorker> workers;

    // AtomicBoolean for managing download cancellation
    private AtomicBoolean isCanceled;

    // Constructor for the ExtendedSwingFrame
    public ExtendedSwingFrame() {
        // Set frame properties
        setTitle("Extended Swing Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel for components
        JPanel panel = new JPanel();

        // Initialize GUI components
        textField = new JTextField(20);
        addUrlButton = new JButton("Add URL");
        downloadButton = new JButton("Download Images");
        clearUrlButton = new JButton("Clear URLs");
        cancelButton = new JButton("Cancel All Downloads");
        progressBarPanel = new JPanel(new GridLayout(0, 1));

        // Initialize lists and AtomicBoolean
        urlList = new ArrayList<>();
        progressBars = new ArrayList<>();
        workers = new ArrayList<>();
        isCanceled = new AtomicBoolean(false);

        // Add ActionListener for "Add URL" button
        addUrlButton.addActionListener(e -> {
            // Get URL from text field, add to list, and add progress bar
            String imageUrl = textField.getText();
            if (!imageUrl.isEmpty()) {
                urlList.add(imageUrl);
                textField.setText("");
                addProgressBar(urlList.size());
            }
        });

        // Add ActionListener for "Download Images" button
        downloadButton.addActionListener(e -> {
            // Download images for each URL in the list
            if (!urlList.isEmpty()) {
                for (int i = 0; i < urlList.size(); i++) {
                    downloadImage(urlList.get(i), progressBars.get(i), i + 1);
                }
            } else {
                // Show message if no URLs to download
                JOptionPane.showMessageDialog(this,
                        "No URLs to download.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add ActionListener for "Clear URLs" button
        clearUrlButton.addActionListener(e -> {
            // Clear URL list and progress bars
            urlList.clear();
            progressBars.clear();
            progressBarPanel.removeAll();
            progressBarPanel.revalidate();
            progressBarPanel.repaint();
        });

        // Add ActionListener for "Cancel All Downloads" button
        cancelButton.addActionListener(e -> {
            // Set cancellation flag and cancel all downloads
            isCanceled.set(true);
            cancelAllDownloads();
        });

        // Add components to the panel
        panel.add(new JLabel("Image URL:"));
        panel.add(textField);
        panel.add(addUrlButton);
        panel.add(downloadButton);
        panel.add(clearUrlButton);
        panel.add(cancelButton);
        panel.add(progressBarPanel);

        // Add panel to the content pane
        getContentPane().add(panel);

        // Set frame size and make it visible
        setSize(400, 300);
        setVisible(true);

        // Initialize ExecutorService with a fixed thread pool
        executorService = Executors.newFixedThreadPool(10);
    }

    // Method to add a progress bar to the GUI
    private void addProgressBar(int imageNumber) {
        // Create label, progress bar, and pause button for each image
        JLabel label = new JLabel("Image " + imageNumber + ": ");
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> {
            // Pause download when the pause button is clicked
            int workerIndex = findWorkerIndexByProgressBar(progressBar);
            if (workerIndex != -1) {
                workers.get(workerIndex).pauseDownload();
            }
        });

        // Create button panel and progress bar panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(pauseButton);

        JPanel progressBarPanel = new JPanel(new BorderLayout());
        progressBarPanel.add(label, BorderLayout.WEST);
        progressBarPanel.add(progressBar, BorderLayout.CENTER);
        progressBarPanel.add(buttonPanel, BorderLayout.EAST);

        // Add progress bar panel to the main panel and progress bar to the list
        this.progressBarPanel.add(progressBarPanel);
        progressBars.add(progressBar);

        // Update GUI
        revalidate();
        repaint();

        // Introduce a slight delay before initiating download
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Initiate image download
        downloadImage(urlList.get(imageNumber - 1), progressBar, imageNumber);
    }

    // Method to cancel all ongoing downloads
    private void cancelAllDownloads() {
        if (workers != null && !workers.isEmpty()) {
            for (DownloadWorker worker : workers) {
                worker.cancel(true);
            }
        }
    }

    // Method to find the index of a DownloadWorker based on its progress bar
    private int findWorkerIndexByProgressBar(JProgressBar progressBar) {
        for (int i = 0; i < progressBars.size(); i++) {
            if (progressBars.get(i) == progressBar) {
                return i;
            }
        }
        return -1;
    }

    // Method to initiate the download of an image
    private void downloadImage(String imageUrl, JProgressBar progressBar, int imageNumber) {
        DownloadWorker worker = new DownloadWorker(imageUrl, progressBar, imageNumber);
        executorService.execute(worker);
        workers.add(worker);
    }

    // Inner class representing the DownloadWorker
    private class DownloadWorker extends SwingWorker<Void, Integer> {

        private final String imageUrl;
        private final JProgressBar progressBar;
        private final int imageNumber;
        private final AtomicBoolean isPaused;

        // Constructor for the DownloadWorker
        public DownloadWorker(String imageUrl, JProgressBar progressBar, int imageNumber) {
            this.imageUrl = imageUrl;
            this.progressBar = progressBar;
            this.imageNumber = imageNumber;
            this.isPaused = new AtomicBoolean(false);
        }

        // Method to pause the download
        public void pauseDownload() {
            isPaused.set(!isPaused.get());
            if (isPaused.get()) {
                JOptionPane.showMessageDialog(ExtendedSwingFrame.this,
                        "Download paused for Image " + imageNumber,
                        "Paused", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // Override doInBackground method for background task
        @Override
        protected Void doInBackground() {
            try {
                // Check if the URL is valid
                if (!isValidUrl(imageUrl)) {
                    throw new MalformedURLException("Invalid URL: " + imageUrl);
                }

                URL url = new URL(imageUrl);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int responseCode = connection.getResponseCode();

                // Check for a successful HTTP response
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }

                String contentType = connection.getContentType();

                // Move the disconnection after obtaining the content length
                int contentLength = connection.getContentLength();
                connection.disconnect();

                // Check if the content type is an image
                if (contentType == null || !contentType.startsWith("image")) {
                    throw new IOException("URL does not point to an image: " + imageUrl);
                }

                // Extract the file name from the URL
                String fileName = url.getFile();
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);

                // Handle query parameters in the file name
                fileName = fileName.split("\\?")[0];

                // Generate a dynamic file name to avoid conflicts
                String newFileName = getDynamicFileName(System.getProperty("user.home") + "/Desktop/", fileName);
                Path outputPath = Paths.get(System.getProperty("user.home") + "/Desktop/", newFileName);

                // Create necessary directories
                Files.createDirectories(outputPath.getParent());

                // Initialize variables for download progress
                int totalBytesRead = 0;

                // Open input stream from the URL and output stream to the local file
                try (InputStream in = url.openStream();
                     OutputStream out = Files.newOutputStream(outputPath)) {

                    // Buffer for reading data
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    // Loop to read and write data
                    while ((bytesRead = in.read(buffer)) != -1) {
                        // Check for pause and wait until resumed
                        if (isPaused.get()) {
                            while (isPaused.get()) {
                                if (isCancelled()) {
                                    return null;
                                }
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    return null;
                                }
                            }
                        }

                        // Write data to the output stream
                        out.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        // Calculate and publish download progress
                        int progress = (int) ((double) totalBytesRead / contentLength * 100);
                        publish(progress);

                        // Introduce a delay to slow down the download progress
                        try {
                            Thread.sleep(100); // Adjust the sleep duration as needed
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return null;
                        }
                    }
                }

                // Publish 100% progress when download is complete
                publish(100);

                // Move the downloaded file to the final location
                Files.move(Paths.get(System.getProperty("user.home") + "/Desktop/", fileName),
                        outputPath, StandardCopyOption.REPLACE_EXISTING);

            } catch (MalformedURLException e) {
                // Handle MalformedURLException and show error message
                e.printStackTrace();
                showError("Error downloading image: Invalid URL", imageUrl);
            } catch (IOException e) {
                // Handle IOException and show error message
                e.printStackTrace();
                showError("Error downloading image: " + e.getMessage(), imageUrl);
            }

            return null;
        }

        // Override process method to update the progress bar
        @Override
        protected void process(List<Integer> chunks) {
            for (int progress : chunks) {
                progressBar.setValue(progress);
            }
        }

        // Override done method for cleanup after background task completion
        @Override
        protected void done() {
            // Check if all download workers are done or canceled
            boolean allDone = true;
            for (DownloadWorker worker : workers) {
                if (!worker.isDone() || worker.isCancelled()) {
                    allDone = false;
                    break;
                }
            }
            // Show success message if all downloads are complete
            if (allDone) {
                urlList.clear();
                JOptionPane.showMessageDialog(ExtendedSwingFrame.this,
                        "All images downloaded successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Method to check if a given string is a valid URL
    private boolean isValidUrl(String urlString) {
        try {
            new URL(urlString).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to generate a dynamic file name to avoid conflicts
    private String getDynamicFileName(String directory, String fileName) {
        String baseName = fileName.substring(0, Math.min(fileName.lastIndexOf('.'), 255));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        Path filePath = Paths.get(directory, fileName);
        int count = 1;

        // Check for existing file and generate a new name if needed
        while (Files.exists(filePath)) {
            String newFileName = MessageFormat.format("{0}_{1}{2}", baseName, count++, extension);
            filePath = Paths.get(directory, newFileName);
        }

        return filePath.getFileName().toString();
    }

    // Method to show an error message dialog
    private void showError(String message, String imageUrl) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    message + "\nURL: " + imageUrl,
                    "Error", JOptionPane.ERROR_MESSAGE);
        });
    }

    // Main method to launch the Swing application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExtendedSwingFrame());
    }
}
