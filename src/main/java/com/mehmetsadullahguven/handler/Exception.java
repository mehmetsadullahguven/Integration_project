package com.mehmetsadullahguven.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exception<E> {
    private String hostName;

    private String path;

    private Date createdTime;

    private E message;
}
