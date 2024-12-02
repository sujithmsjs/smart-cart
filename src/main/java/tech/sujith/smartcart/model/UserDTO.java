package tech.sujith.smartcart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private UUID id;

    @NotNull
    @Size(max = 30)
    private String name;

    @Size(max = 30)
    private String city;

}
