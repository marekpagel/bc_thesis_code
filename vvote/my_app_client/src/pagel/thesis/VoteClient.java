package pagel.thesis;

import org.json.JSONObject;
import uk.ac.surrey.cs.tvs.client.Client;
import uk.ac.surrey.cs.tvs.fields.messages.ConfigFiles;
import uk.ac.surrey.cs.tvs.utils.io.MBBConfig;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VoteClient {
    static Client client;
    static MBBConfig mbbConf;
    static List<InetSocketAddress> addressList;
    static AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) throws Exception{
        client = new Client("client.conf");
        mbbConf = new MBBConfig(client.getClientConf().getStringParameter(ConfigFiles.ClientConfig.MBB_CONF_FILE));
        Collection<JSONObject> values = mbbConf.getPeers().values();
        addressList = new ArrayList<>(values.size());
        for (JSONObject peer: values) {
            addressList.add(new InetSocketAddress(peer.getString("address"), peer.getInt("port")));
        }
        JSONObject msg = new JSONObject(new String(Files.readAllBytes(Paths.get("message"))));
        Loader loader = new Loader(10, 300, 60);
        loader.start(() -> {
            try {
                sendMsg3(msg);
                int i;
                if ((i = counter.getAndIncrement()) % 1000 == 0) {
                    System.out.println(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void sendMsg3(JSONObject msg) throws Exception {
        msg.put("serialNo", "TestDeviceOne:" + String.valueOf((int) (Math.random() * 10000000)));
        for (InetSocketAddress address: addressList) {
            Socket s = new Socket();
            s.connect(address);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write(msg.toString());
            bw.newLine();
            bw.flush();
        }
    }
}