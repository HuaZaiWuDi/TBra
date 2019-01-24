package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName LoginInfoBean
 * @Date 2019/1/3 16:43
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class LoginInfoBean {


    /**
     * success : true
     * userId : c82e9e7612a447358c2a82ef437f3d11
     * token : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5ZGJmNTRjMDlkNDE0YmE4YTA3Y2I5MDE3NTExZDY2NCIsImlhdCI6MTUyNjcwMDIzNCwiZXhwIjoxNTQyNTk3ODM0LCJzdWIiOiJ1c2VyIn0.E27o7o31N54aoHV5aGz_m1PrWw4W16MNdOr7UMZjgZA
     */

    private Boolean success;
    private String userId;
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
