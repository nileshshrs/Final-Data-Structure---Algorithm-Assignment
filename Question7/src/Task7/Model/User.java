package Task7.Model;

public class User {
    private int id;
    private String userName;
    private String age;
    private String location;
    private String followers;
    private String details;
    private  String imagePath;

    public User(int id, String userName, String age, String location, String followers,String imagePath, String details) {
        this.id = id;
        this.userName = userName;
        this.age = age;
        this.location = location;
        this.followers = followers;
        this.imagePath = imagePath;
        this.details = details;
    }
    public String toString() {
        return "User ID: " + id + "\n" +
                "Username: " + userName + "\n" +
                "Age: " + age + "\n" +
                "Location: " + location + "\n" ;
//                "Followers: " + followers + "\n" +
//                "Image Path: " + imagePath + "\n" +
//                "Details: " + details;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }

    public String getFollowers() {
        return followers;
    }

    public String getDetails() {
        return details;
    }

    public String getImagePath() {
        return imagePath;
    }
}
