package pagel.thesis;

import bftsmart.communication.client.ReplyListener;
import bftsmart.tom.AsynchServiceProxy;
import bftsmart.tom.RequestContext;
import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.core.messages.TOMMessageType;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {


    static final String msg = "{\"peerID\":\"Peer1\",\"signature\":\"MC0CFQCttVVXMB/hsovfQK7jIGHz3wpU3gIUVkPhn20SDHGLGqKwJvKuakw5cx8=\",\"_uuid\":\"afec4934-bc61-47d5-9b2a-92543625fa14\",\"commitID\":\"18:0\",\"type\":\"vote\",\"serialSig\":\"JFd8uhOXbwNBOh0qUv/UMRQFwxg0MfsyXXzYQwBOV/nThIUDJ2WXaQ==\",\"boothID\":\"TestEVMOne\",\"serialNo\":\"TestDeviceOne:2\",\"_vPrefs\":\"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15:1:1:\",\"commitTime\":\"18:0\",\"races\":[{\"preferences\":[\"1\",\"2\",\"3\",\"4\",\"5\"],\"id\":\"LA\"},{\"preferences\":[\"1\"],\"id\":\"LC_ATL\"},{\"preferences\":[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"],\"id\":\"LC_BTL\"}],\"district\":\"Warrandyte\",\"boothSig\":\"ChvEwm02BCPxQ0+J38gw6NAi7Ic3jCxVTbjYBikT0j1dG3PsHS+YDQ==\",\"_fromPeer\":\"Peer1\",\"startEVMSig\":\"UGtHeNV3IHuwdFEfzCnY+n9uHw9rvwFq576L2GxXkkgoUcCO2X9d7g==\"}\n";
    static final Callback callback = new Callback();
    static AtomicInteger counter = new AtomicInteger();
    public static void main(String[] args) throws Exception {
        AsynchServiceProxy proxy = new AsynchServiceProxy(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(msg.getBytes());

        Loader loader = new Loader(10, 300, 60);
        loader.start(() -> {
            proxy.invokeAsynchRequest(out.toByteArray(), callback, TOMMessageType.ORDERED_REQUEST);
            int i;
            if ((i = counter.getAndIncrement()) % 100 == 0) {
                System.out.println(i);
            }
        });
    }

    private static class Callback implements ReplyListener {

        @Override
        public void replyReceived(RequestContext context, TOMMessage reply) {}
    }
}
