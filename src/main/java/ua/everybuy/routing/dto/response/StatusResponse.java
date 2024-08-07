package ua.everybuy.routing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StatusResponse<T> {
    private int status;
    private T data;
}