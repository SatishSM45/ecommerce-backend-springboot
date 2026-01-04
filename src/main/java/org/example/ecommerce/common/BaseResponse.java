package org.example.ecommerce.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    T data;
    Result result;

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data, new Result(CommonConstants.SUCCESS_CODE, CommonConstants.SUCCESS));
    }
}
