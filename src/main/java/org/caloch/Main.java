package org.caloch;


import org.caloch.core.*;


public class Main {
    public static void main(String[] args) throws Exception {
        new JMvcServer(args).addDbContext(false).start();
    }


}
