package top.kelton.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserRequestDTO {
    private String name;
    private Integer age;
}
