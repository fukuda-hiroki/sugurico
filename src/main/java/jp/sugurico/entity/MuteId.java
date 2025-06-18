package jp.sugurico.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class MuteId implements Serializable {
    private Long mutingUser;
    private Long mutedUser;
}