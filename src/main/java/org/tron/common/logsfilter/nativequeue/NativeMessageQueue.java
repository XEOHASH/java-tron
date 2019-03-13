package org.tron.common.logsfilter.nativequeue;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import java.util.Objects;

public class NativeMessageQueue {
  ZContext context = null;
  private ZMQ.Socket publisher = null;
  private static NativeMessageQueue instance;
  private static final int DEFAULT_BIND_PORT = 5555;
  public static NativeMessageQueue getInstance() {
      if (Objects.isNull(instance)) {
          synchronized (NativeMessageQueue.class) {
              if (Objects.isNull(instance)) {
                  instance = new NativeMessageQueue();
              }
          }
      }
      return instance;
  }

  public boolean start(int bindPort) {
    context = new ZContext();
    publisher = context.createSocket(SocketType.PUB);

    if (Objects.isNull(publisher)){
      return false;
    }

    if (bindPort == 0) {
      bindPort = DEFAULT_BIND_PORT;
    }

    String bindAddress = String.format("tcp://*:%d", bindPort);
    publisher.bind(bindAddress);

    return true;
  }

  public void stop(){
      if (Objects.nonNull(publisher)){
          publisher.close();
      }

      if (Objects.nonNull(context)){
          context.close();
      }
  }

  public void publishTrigger(String data, String topic){
    if (Objects.isNull(publisher) || Objects.isNull(context.isClosed()) || context.isClosed()) {
      return;
    }

    publisher.sendMore(topic);
    publisher.send(data);

    System.out.println("topic " + topic);
    System.out.println("trigger " + data);
  }
}