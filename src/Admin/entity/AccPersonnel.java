package Admin.entity;

public class AccPersonnel {
    private Integer id;
    private  String username,password,pw_encode;

    public AccPersonnel(Integer id, String username, String password, String pw_encode) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.pw_encode = pw_encode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPw_encode() {
        return pw_encode;
    }

    public void setPw_encode(String pw_encode) {
        this.pw_encode = pw_encode;
    }
}
