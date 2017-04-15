package com.artisankid.elementwar.tcpconnection.gate.starter;


import com.artisankid.elementwar.tcpconnection.gate.GateServer;

/**
 * Created by Qzy on 2016/1/28.
 */

public class GateStarter {

    public static void main(String[] args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GateServer.startGateServer(5160);
            }
        }).start();

    }
}
