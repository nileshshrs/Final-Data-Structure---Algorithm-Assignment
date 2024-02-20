//package Task7.View;
//
//import Task7.Model.User;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.*;
//import java.util.List;
//
//public class UserDetailFrame extends JFrame {
//    private final User user;
//    private final Map<Integer, List<Integer>> connectionMap;
//    private final Map<Integer, User> userMap;
//    private final Map<Integer, Integer> likesMap;
//    private final Map<Integer, Integer> messagesMap;
//    private final Map<Integer, Integer> commentsMap;
//    private JPanel connectionsPanel;
//
//    private JPanel friendSuggestionsPanel;
//
//    public UserDetailFrame(User user, Map<Integer, List<Integer>> connectionMap, Map<Integer, User> userMap,
//                           Map<Integer, Integer> likesMap, Map<Integer, Integer> messagesMap,
//                           Map<Integer, Integer> commentsMap) {
//        this.user = user;
//        this.connectionMap = connectionMap;
//        this.userMap = userMap;
//        this.likesMap = likesMap;
//        this.messagesMap = messagesMap;
//        this.commentsMap = commentsMap;
//
//        setTitle(user.getUserName() + " Details");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setPreferredSize(new Dimension(1000, 800));
//        setLayout(new BorderLayout());
//
//        // Create a panel for the main content
//        JPanel mainContentPanel = new JPanel(new BorderLayout());
//
//        // Create a panel for user details
//        JPanel userDetailsPanel = new JPanel(new BorderLayout());
//        userDetailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        // Add user image
//        ImageIcon userImage = new ImageIcon(user.getImagePath());
//        Image scaledImage = userImage.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
//        ImageIcon scaledUserImage = new ImageIcon(scaledImage);
//        JLabel imageLabel = new JLabel(scaledUserImage);
//        userDetailsPanel.add(imageLabel, BorderLayout.WEST);
//
//        // Create a panel for user information labels
//        JPanel userInfoPanel = new JPanel(new GridBagLayout());
//        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//
//        // Add user information labels
//        addDetailLabel(userInfoPanel, "ID:", String.valueOf(user.getId()), 0);
//        addDetailLabel(userInfoPanel, "Name:", user.getUserName(), 1);
//        addDetailLabel(userInfoPanel, "Age:", user.getAge(), 2);
//        addDetailLabel(userInfoPanel, "Location:", user.getLocation(), 3);
//        addDetailLabel(userInfoPanel, "Followers:", user.getFollowers(), 4);
//        addDetailLabel(userInfoPanel, "Details:", user.getDetails(), 5);
//
//        userDetailsPanel.add(userInfoPanel, BorderLayout.CENTER);
//
//        // Create a panel for friend suggestions
//        friendSuggestionsPanel = createFriendSuggestionsPanel();
//
//        // Create a panel for friend suggestions and connection details
//        JPanel suggestionsAndConnectionsPanel = new JPanel(new BorderLayout());
//        suggestionsAndConnectionsPanel.add(friendSuggestionsPanel, BorderLayout.NORTH);
//        suggestionsAndConnectionsPanel.add(createConnectionsPanel(), FlowLayout.CENTER);
//
//        // Add panels to the main content panel
//        mainContentPanel.add(userDetailsPanel, BorderLayout.CENTER);
//        mainContentPanel.add(suggestionsAndConnectionsPanel, BorderLayout.EAST);
//
//        add(mainContentPanel);
//
//        pack();
//        setLocationRelativeTo(null);
//    }
//
//    private JPanel createConnectionsPanel() {
//        connectionsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
//        connectionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        List<Integer> connections = connectionMap.get(user.getId());
//        if (connections != null) {
//            for (int connectedTo : connections) {
//                User connectedUser = userMap.get(connectedTo);
//                if (connectedUser != null) {
//                    JPanel connectionCard = createConnectionCard(connectedUser);
//                    connectionsPanel.add(connectionCard);
//                }
//            }
//        }
//
//        // Wrap the connections panel in a scroll pane with vertical scrolling only
//        JScrollPane connectionsScrollPane = new JScrollPane(connectionsPanel);
//        connectionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        connectionsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.add(connectionsScrollPane, BorderLayout.CENTER);
//
//        return panel;
//    }
//
//    private void addDetailLabel(JPanel panel, String labelText, String value, int gridY) {
//        JLabel label = new JLabel(labelText);
//        JLabel valueLabel = new JLabel(value);
//
//        panel.add(label, createConstraints(0, gridY, 1, 1, GridBagConstraints.CENTER));
//        panel.add(valueLabel, createConstraints(1, gridY, 1, 1, GridBagConstraints.CENTER));
//    }
//
//    private GridBagConstraints createConstraints(int gridX, int gridY, int gridWidth, int gridHeight, int anchor) {
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.gridx = gridX;
//        constraints.gridy = gridY;
//        constraints.gridwidth = gridWidth;
//        constraints.gridheight = gridHeight;
//        constraints.anchor = anchor;
//        constraints.fill = GridBagConstraints.HORIZONTAL;
//        constraints.insets = new Insets(5, 5, 5, 5);
//        return constraints;
//    }
//
//    private JPanel createConnectionCard(User connectedUser) {
//        JPanel cardPanel = new JPanel(new BorderLayout());
//        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        cardPanel.setPreferredSize(new Dimension(200, 120));
//
//        ImageIcon userImage = new ImageIcon(connectedUser.getImagePath());
//        Image scaledImage = userImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
//        ImageIcon scaledUserImage = new ImageIcon(scaledImage);
//        JLabel imageLabel = new JLabel(scaledUserImage);
//        cardPanel.add(imageLabel, BorderLayout.WEST);
//
//        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
//        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JLabel nameLabel = new JLabel("Name: " + connectedUser.getUserName());
//        detailsPanel.add(nameLabel);
//
//        int connectedUserId = connectedUser.getId();
//        int likes = likesMap.getOrDefault(connectedUserId, 0);
//        int messages = messagesMap.getOrDefault(connectedUserId, 0);
//        int comments = commentsMap.getOrDefault(connectedUserId, 0);
//
//        JLabel likesLabel = new JLabel("Likes: " + likes);
//        detailsPanel.add(likesLabel);
//
//        JLabel messagesLabel = new JLabel("Messages: " + messages);
//        detailsPanel.add(messagesLabel);
//
//        JLabel commentsLabel = new JLabel("Comments: " + comments);
//        detailsPanel.add(commentsLabel);
//
//        JButton profileButton = new JButton("View Details");
//        profileButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                UserDetailFrame frame = new UserDetailFrame(connectedUser, connectionMap, userMap,
//                        likesMap, messagesMap, commentsMap);
//                frame.setVisible(true);
//            }
//        });
//        detailsPanel.add(profileButton);
//
//        JButton removeButton = new JButton("Remove Friend");
//        removeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Remove friend logic here
//
//                // Confirm removal
//                String message = "Are you sure you want to remove " + connectedUser.getUserName() + " from your friend list?";
//                int result = JOptionPane.showConfirmDialog(UserDetailFrame.this, message, "Remove Friend",
//                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//
//                if (result == JOptionPane.YES_OPTION) {
//                    int connectedUserId = connectedUser.getId();
//                    connectionMap.get(user.getId()).remove((Integer) connectedUserId);
//                    connectionMap.get(connectedUserId).remove((Integer) user.getId());
//
//                    connectionsPanel.remove(cardPanel);
//                    connectionsPanel.revalidate();
//                    connectionsPanel.repaint();
//
//                    JOptionPane.showMessageDialog(UserDetailFrame.this, "Friend Removed: " + connectedUser.getUserName());
//                }
//            }
//        });
//        detailsPanel.add(removeButton);
//
//        cardPanel.add(detailsPanel, BorderLayout.CENTER);
//
//        return cardPanel;
//    }
//
//    private JPanel createFriendSuggestionsPanel() {
//        JPanel suggestionsPanel = new JPanel(new BorderLayout());
//        suggestionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JLabel titleLabel = new JLabel("Friend Suggestions");
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        suggestionsPanel.add(titleLabel, BorderLayout.NORTH);
//
//        JPanel cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
//        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
//
//        List<Integer> connections = connectionMap.get(user.getId());
//        Set<Integer> friendSuggestions = new HashSet<>();
//        if (connections != null) {
//            for (int connectedTo : connections) {
//                List<Integer> friendOfFriend = connectionMap.get(connectedTo);
//                if (friendOfFriend != null) {
//                    for (int friendId : friendOfFriend) {
//                        if (friendId != user.getId() && !connections.contains(friendId)) {
//                            friendSuggestions.add(friendId);
//                        }
//                    }
//                }
//            }
//        }
//
//        for (int friendSuggestionId : friendSuggestions) {
//            User friendSuggestion = userMap.get(friendSuggestionId);
//            if (friendSuggestion != null) {
//                JPanel userCard = createFriendSuggestionCard(friendSuggestion);
//                cardsPanel.add(userCard);
//            }
//        }
//
//        // Create the scroll pane with a fixed size and vertical scrolling only
//        JScrollPane scrollPane = new JScrollPane(cardsPanel);
//        scrollPane.setPreferredSize(new Dimension(400, 300));
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//
//        suggestionsPanel.add(scrollPane, BorderLayout.CENTER);
//
//        return suggestionsPanel;
//    }
//
//    private JPanel createFriendSuggestionCard(User friendSuggestion) {
//        JPanel cardPanel = new JPanel(new BorderLayout());
//        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        cardPanel.setPreferredSize(new Dimension(200, 120));
//
//        JLabel imageLabel = new JLabel();
//        ImageIcon userImage = new ImageIcon(friendSuggestion.getImagePath());
//        Image scaledImage = userImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
//        ImageIcon scaledUserImage = new ImageIcon(scaledImage);
//        imageLabel.setIcon(scaledUserImage);
//
//        JLabel nameLabel = new JLabel("Name: " + friendSuggestion.getUserName());
//        JLabel ageLabel = new JLabel("Age: " + friendSuggestion.getAge());
//        JLabel locationLabel = new JLabel("Location: " + friendSuggestion.getLocation());
//
//        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
//        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        cardPanel.add(imageLabel, BorderLayout.WEST);
//        detailsPanel.add(nameLabel);
//        detailsPanel.add(ageLabel);
//        detailsPanel.add(locationLabel);
//
//        JButton addButton = new JButton("Add Friend");
//        addButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int friendSuggestionId = friendSuggestion.getId();
//                connectionMap.get(user.getId()).add(friendSuggestionId);
//                connectionMap.get(friendSuggestionId).add(user.getId());
//
//                friendSuggestionsPanel.remove(cardPanel);
//                friendSuggestionsPanel.revalidate();
//                friendSuggestionsPanel.repaint();
//
//                // Refresh the connections panel to reflect the added friend
//                connectionsPanel.removeAll();
//
//                for (int connectedTo : connectionMap.get(user.getId())) {
//                    User connectedUser = userMap.get(connectedTo);
//                    if (connectedUser != null) {
//                        JPanel connectionCard = createConnectionCard(connectedUser);
//                        connectionsPanel.add(connectionCard);
//                    }
//                }
//                friendSuggestionsPanel.remove(cardPanel);
//                friendSuggestionsPanel.revalidate();
//                friendSuggestionsPanel.repaint();
//                connectionsPanel.revalidate();
//                connectionsPanel.repaint();
//
//                JOptionPane.showMessageDialog(UserDetailFrame.this, "Friend Added: " + friendSuggestion.getUserName());
//            }
//        });
//        detailsPanel.add(addButton);
//
//        cardPanel.add(detailsPanel, BorderLayout.CENTER);
//
//        return cardPanel;
//    }
//
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            // Sample data
//            User user = new User(1, "John Doe", "30", "New York", "1000", "src/Task7/profilePic/1.jpg", "Some details");
//            Map<Integer, List<Integer>> connectionMap = null; // Your connection map
//            Map<Integer, User> userMap = null; // Your user map
//            Map<Integer, Integer> likesMap = null; // Your likes map
//            Map<Integer, Integer> messagesMap = null; // Your messages map
//            Map<Integer, Integer> commentsMap = null; // Your comments map
//
//            UserDetailFrame frame = new UserDetailFrame(user, connectionMap, userMap, likesMap, messagesMap, commentsMap);
//            frame.setVisible(true);
//        });
//    }
//}

package Task7.View;

import Task7.Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class UserDetailFrame extends JFrame {
    private final User user;
    private final Map<Integer, List<Integer>> connectionMap;
    private final Map<Integer, User> userMap;
    private final Map<Integer, Integer> likesMap;
    private final Map<Integer, Integer> messagesMap;
    private final Map<Integer, Integer> commentsMap;
    private JPanel connectionsPanel;
    private JPanel suggestionsPanel;

    private List<User> friendSuggestions;


    public UserDetailFrame(User user, Map<Integer, List<Integer>> connectionMap, Map<Integer, User> userMap,
                           Map<Integer, Integer> likesMap, Map<Integer, Integer> messagesMap,
                           Map<Integer, Integer> commentsMap) {
        this.user = user;
        this.connectionMap = connectionMap;
        this.userMap = userMap;
        this.likesMap = likesMap;
        this.messagesMap = messagesMap;
        this.commentsMap = commentsMap;
        friendSuggestions = new ArrayList<>();


        setTitle(user.getUserName() + " Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 800));
        setLayout(new BorderLayout());

        // Create a panel for the main content
        JPanel mainContentPanel = new JPanel(new BorderLayout());

        // Create a panel for user details
        JPanel userDetailsPanel = new JPanel(new BorderLayout());
        userDetailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add user image
        ImageIcon userImage = new ImageIcon(user.getImagePath());
        Image scaledImage = userImage.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        ImageIcon scaledUserImage = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledUserImage);
        userDetailsPanel.add(imageLabel, BorderLayout.WEST);

        // Create a panel for user information labels
        JPanel userInfoPanel = new JPanel(new GridBagLayout());
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Add user information labels
        addDetailLabel(userInfoPanel, "ID:", String.valueOf(user.getId()), 0);
        addDetailLabel(userInfoPanel, "Name:", user.getUserName(), 1);
        addDetailLabel(userInfoPanel, "Age:", user.getAge(), 2);
        addDetailLabel(userInfoPanel, "Location:", user.getLocation(), 3);
        addDetailLabel(userInfoPanel, "Followers:", user.getFollowers(), 4);
        addDetailLabel(userInfoPanel, "Details:", user.getDetails(), 5);

        userDetailsPanel.add(userInfoPanel, BorderLayout.CENTER);

        // Create a panel for friend suggestions
        JPanel friendSuggestionsPanel = createFriendSuggestionsPanel();

        // Add panels to the main content panel
        mainContentPanel.add(userDetailsPanel, BorderLayout.CENTER);
        mainContentPanel.add(friendSuggestionsPanel, BorderLayout.EAST);

        // Create a panel for connection details
        connectionsPanel = createConnectionsPanel();
        connectionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a scroll pane for the connections panel
        JScrollPane connectionsScrollPane = new JScrollPane(connectionsPanel);
        connectionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        connectionsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Create a panel for connections and scroll pane
        JPanel connectionsWrapperPanel = new JPanel(new BorderLayout());
        connectionsWrapperPanel.add(connectionsScrollPane, BorderLayout.CENTER);

        // Add the connections wrapper panel to the main content panel
        mainContentPanel.add(connectionsWrapperPanel, BorderLayout.SOUTH);

        add(mainContentPanel);

        pack();
        setLocationRelativeTo(null);
    }

    private void addDetailLabel(JPanel panel, String labelText, String value, int gridY) {
        JLabel label = new JLabel(labelText);
        JLabel valueLabel = new JLabel(value);

        panel.add(label, createConstraints(0, gridY, 1, 1, GridBagConstraints.WEST));
        panel.add(valueLabel, createConstraints(1, gridY, 1, 1, GridBagConstraints.WEST));
    }

    private GridBagConstraints createConstraints(int gridX, int gridY, int gridWidth, int gridHeight, int anchor) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;
        constraints.anchor = anchor;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        return constraints;
    }

    private JPanel createConnectionsPanel() {
        connectionsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        connectionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Integer> connections = connectionMap.get(user.getId());
        if (connections != null) {
            for (int connectedTo : connections) {
                User connectedUser = userMap.get(connectedTo);
                if (connectedUser != null) {
                    JPanel connectionCard = createConnectionCard(connectedUser);
                    connectionsPanel.add(connectionCard);
                }
            }
        }

        // Wrap the connections panel in a scroll pane with vertical scrolling only
        JScrollPane connectionsScrollPane = new JScrollPane(connectionsPanel);
        connectionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        connectionsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(connectionsScrollPane, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createConnectionCard(User connectedUser) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cardPanel.setPreferredSize(new Dimension(200, 120));

        ImageIcon userImage = new ImageIcon(connectedUser.getImagePath());
        Image scaledImage = userImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledUserImage = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledUserImage);
        cardPanel.add(imageLabel, BorderLayout.WEST);

        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name: " + connectedUser.getUserName());
        detailsPanel.add(nameLabel);

        int connectedUserId = connectedUser.getId();
        int likes = likesMap.getOrDefault(connectedUserId, 0);
        int messages = messagesMap.getOrDefault(connectedUserId, 0);
        int comments = commentsMap.getOrDefault(connectedUserId, 0);

        JLabel likesLabel = new JLabel("Likes: " + likes);
        detailsPanel.add(likesLabel);

        JLabel messagesLabel = new JLabel("Messages: " + messages);
        detailsPanel.add(messagesLabel);

        JLabel commentsLabel = new JLabel("Comments: " + comments);
        detailsPanel.add(commentsLabel);

        JButton profileButton = new JButton("View Details");
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserDetailFrame frame = new UserDetailFrame(connectedUser, connectionMap, userMap,
                        likesMap, messagesMap, commentsMap);
                frame.setVisible(true);
            }
        });
        detailsPanel.add(profileButton);

        JButton removeButton = new JButton("Remove Friend");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove friend logic here

                // Confirm removal
                String message = "Are you sure you want to remove " + connectedUser.getUserName() + " from your friend list?";
                int result = JOptionPane.showConfirmDialog(UserDetailFrame.this, message, "Remove Friend",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    int connectedUserId = connectedUser.getId();
                    connectionMap.get(user.getId()).remove((Integer) connectedUserId);
                    connectionMap.get(connectedUserId).remove((Integer) user.getId());
                    
                    connectionsPanel.remove(cardPanel);
                    connectionsPanel.revalidate();
                    connectionsPanel.repaint();

                    JOptionPane.showMessageDialog(UserDetailFrame.this, "Friend Removed: " + connectedUser.getUserName());
                }
            }
        });
        detailsPanel.add(removeButton);

        cardPanel.add(detailsPanel, BorderLayout.CENTER);

        return cardPanel;
    }


    private JPanel createFriendSuggestionsPanel() {
        suggestionsPanel = new JPanel(new BorderLayout());
        suggestionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Friend Suggestions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        suggestionsPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        List<Integer> connections = connectionMap.get(user.getId());
        Set<Integer> friendSuggestions = new HashSet<>();
        if (connections != null) {
            for (int connectedTo : connections) {
                List<Integer> friendOfFriend = connectionMap.get(connectedTo);
                if (friendOfFriend != null) {
                    for (int friendId : friendOfFriend) {
                        if (friendId != user.getId() && !connections.contains(friendId)) {
                            friendSuggestions.add(friendId);
                        }
                    }
                }
            }
        }

        for (int friendSuggestionId : friendSuggestions) {
            User friendSuggestion = userMap.get(friendSuggestionId);
            if (friendSuggestion != null) {
                JPanel userCard = createFriendSuggestionCard(friendSuggestion);
                cardsPanel.add(userCard);
            }
        }

        // Create the scroll pane with a fixed size and vertical scrolling only
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        suggestionsPanel.add(scrollPane, BorderLayout.CENTER);

        return suggestionsPanel;
    }

    private JPanel createFriendSuggestionCard(User friendSuggestion) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cardPanel.setPreferredSize(new Dimension(200, 120));

        JLabel imageLabel = new JLabel();
        ImageIcon userImage = new ImageIcon(friendSuggestion.getImagePath());
        Image scaledImage = userImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledUserImage = new ImageIcon(scaledImage);
        imageLabel.setIcon(scaledUserImage);

        JLabel nameLabel = new JLabel("Name: " + friendSuggestion.getUserName());
        JLabel ageLabel = new JLabel("Age: " + friendSuggestion.getAge());
        JLabel locationLabel = new JLabel("Location: " + friendSuggestion.getLocation());

        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cardPanel.add(imageLabel, BorderLayout.WEST);
        detailsPanel.add(nameLabel);
        detailsPanel.add(ageLabel);
        detailsPanel.add(locationLabel);

        JButton addButton = new JButton("Add Friend");
        addButton.addActionListener(e -> {
            int friendSuggestionId = friendSuggestion.getId();
            connectionMap.get(user.getId()).add(friendSuggestionId);
            connectionMap.get(friendSuggestionId).add(user.getId());

            // Remove the card from the suggestion panel
            Component[] components = suggestionsPanel.getComponents();
            for (Component component : components) {
                if (component instanceof JPanel) {
                    JPanel suggestionCard = (JPanel) component;
                    User suggestionUser = (User) suggestionCard.getClientProperty("User");
                    if (suggestionUser != null && suggestionUser.equals(friendSuggestion)) {
                        suggestionsPanel.remove(suggestionCard);
                        break;
                    }
                }
            }

            // Refresh the connections panel to reflect the added friend
            JPanel connectionCard = createConnectionCard(friendSuggestion);
            connectionsPanel.add(connectionCard);

            // Refresh the panels
            connectionsPanel.revalidate();
            connectionsPanel.repaint();
            suggestionsPanel.revalidate();
            suggestionsPanel.repaint();

            JOptionPane.showMessageDialog(UserDetailFrame.this, "Friend Added: " + friendSuggestion.getUserName());
        });
        detailsPanel.add(addButton);

        cardPanel.add(detailsPanel, BorderLayout.CENTER);

        return cardPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Sample data
            User user = new User(1, "John Doe", "30", "New York", "1000", "src/Task7/profilePic/1.jpg", "Some details");
            Map<Integer, List<Integer>> connectionMap = null; // Your connection map
            Map<Integer, User> userMap = null; // Your user map
            Map<Integer, Integer> likesMap = null; // Your likes map
            Map<Integer, Integer> messagesMap = null; // Your messages map
            Map<Integer, Integer> commentsMap = null; // Your comments map

            UserDetailFrame frame = new UserDetailFrame(user, connectionMap, userMap, likesMap, messagesMap, commentsMap);
            frame.setVisible(true);
        });
    }
}
