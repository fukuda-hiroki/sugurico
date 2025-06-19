package jp.sugurico.sugurico.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class TagId implements Serializable {
    private Long forum;
    private Long tag;
}