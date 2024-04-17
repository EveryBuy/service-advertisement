package ua.everybuy.routing.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StatusResponse {
    private int status;
    private Object data;
}