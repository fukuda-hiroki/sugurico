package jp.sugurico.sugurico.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class HistoryId implements Serializable {
    private Long user;
    private Long forum;
}