package com.genvetclinic.models;

/**
 * The {@code Admin} class represents an administrator in the veterinary clinic system.
 * Administrators have various attributes such as admin ID, username, password, email,
 * security question, security answer, activation code, and an indicator of whether
 * the admin is currently active.
 *
 * <p>This class provides methods to access and modify these attributes, allowing
 * for the management of administrator information within the system.
 *
 * @author vcms-group
 * @version 1.0
 * @since 2023-11-19
 */
public class Admin {

    /**
     * The unique identifier for the administrator.
     */
    private String adminId;

    /**
     * The username associated with the administrator.
     */
    private String username;

    /**
     * The password associated with the administrator's account.
     */
    private String password;

    /**
     * The email address of the administrator.
     */
    private String adminEmail;

    /**
     * The security question chosen by the administrator for account recovery.
     */
    private String securityQuestion;

    /**
     * The answer to the security question for account recovery.
     */
    private String securityAnswer;

    /**
     * The activation code used to activate the administrator's account.
     */
    private String adminCode;

    /**
     * A boolean indicating whether the administrator's account is currently active.
     */
    private boolean isActive;

    /**
     * Constructs a new {@code Admin} instance with default values.
     */
    public Admin() {
    }

    /**
     * Constructs a new {@code Admin} instance with the specified parameters.
     *
     * @param adminId           The unique identifier for the administrator.
     * @param username          The username associated with the administrator.
     * @param password          The password associated with the administrator's account.
     * @param adminEmail        The email address of the administrator.
     * @param securityQuestion  The security question chosen by the administrator for account recovery.
     * @param securityAnswer    The answer to the security question for account recovery.
     * @param adminCode         The activation code used to activate the administrator's account.
     * @param isActive          A boolean indicating whether the administrator's account is currently active.
     */
    public Admin(String adminId, String username, String password, String adminEmail,
                 String securityQuestion, String securityAnswer, String adminCode, boolean isActive) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.adminEmail = adminEmail;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.adminCode = adminCode;
        this.isActive = isActive;
    }

    // Getter and setter methods for each attribute...

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String activationCode) {
        this.adminCode = activationCode;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
