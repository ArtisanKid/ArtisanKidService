package com.artisankid.elementwar.tcpconnection.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ActionRequestMap {
    int actionKey();
}