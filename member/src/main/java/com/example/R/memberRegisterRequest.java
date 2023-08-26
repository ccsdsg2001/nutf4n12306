package com.example.R;

import jakarta.validation.constraints.NotBlank;

/**
 * @author cc
 * @date 2023年08月21日 17:02
 */

public class memberRegisterRequest {

    @NotBlank(message = "【手机号】不能为空")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MemberRegisterReq{" +
                "mobile='" + mobile + '\'' +
                '}';
    }
}
