package net.toiletmc.toiletcore.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BindQQRequest implements Request{
    private String code;
    private String mc;
}
