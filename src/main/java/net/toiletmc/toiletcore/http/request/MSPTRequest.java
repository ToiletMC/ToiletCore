package net.toiletmc.toiletcore.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MSPTRequest implements Request{
    private int mspt;
    private String note;
}
