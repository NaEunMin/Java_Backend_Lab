package java.generic;

import java.api.java.generic.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApiResponseTest {

    @Test
    public void String응답은_형변환없이_데이터를_반환한다(){
        ApiResponse<String> response = new ApiResponse<>(
                "호출 성공",
                200,
                "회원 데이터"
        );

        String data = response.getData();

        assertEquals("호출 성공", response.getMessage());
        assertEquals(200, response.getHttpStatus());
        assertEquals("회원 데이터", data);
    }

    @Test
    public void Integer응답은_형변환없이_데이터를_반환한다(){
        ApiResponse<Integer> response = new ApiResponse<>(
                "호출 성공",
                200,
                3
        );

        int data = response.getData();

        assertEquals("호출 성공", response.getMessage());
        assertEquals(200, response.getHttpStatus());
        assertEquals(3, data);
    }

    @Test
    public void success는_String응답의_타입을_추론한다() {
        ApiResponse<String> response = ApiResponse.success(
                "회원 데이터 조회 성공",
                "회원 데이터"
        );

        String data = response.getData();

        assertEquals("회원 데이터 조회 성공", response.getMessage());
        assertEquals(200, response.getHttpStatus());
        assertEquals("회원 데이터", data);
    }

    @Test
    public void success는_Integer응답의_타입을_추론한다(){
        ApiResponse<Integer> response = ApiResponse.success(
                "회원 수 조회 성공",
                3
        );

        int data = response.getData();

        assertEquals("회원 수 조회 성공", response.getMessage());
        assertEquals(200, response.getHttpStatus());
        assertEquals(3, data);
    }
}
