package com.example.R;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cc
 * @date 2023年08月21日 17:02
 */

@Data
@Getter
@Setter
@ToString
public class memberRegisterRequest {

    @NotNull
    @NotBlank(message = "手机号不能为空")
    private String mobile;
}
