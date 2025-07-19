package model;

public class User {
    private String username;
    private String password;
    private String role;
    private String fullName;
    private String className;
    private String nim;

    public User(String username, String password, String role, String fullName, String className, String nim) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.className = className;
        this.nim = nim;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getClassName() { return className; } // Untuk BorrowPanel
    public String getNim() { return nim; } // Untuk BorrowPanel
}