package pagel.thesis;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;
import com.mongodb.*;
import org.bson.io.BasicOutputBuffer;
import org.bson.io.OutputBuffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Server extends DefaultRecoverable {

    private static DBCollection collection;
    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = new MongoClient();
        DB test = mongoClient.getDB("db");
        collection =  test.getCollection("msg" + args[0]);

        new Server(Integer.parseInt(args[0]));
    }

    private final ServiceReplica replica;
    private int counter = 0;
    public Server(int id) {
        replica = new ServiceReplica(id, this, this);
        DBCursor lastEntryCursor = collection.find().sort(new BasicDBObject("_id", -1)).limit(1);
        if (lastEntryCursor.hasNext()) {
            counter = (int) lastEntryCursor.next().get("_id") + 1;
        }
    }

    @Override
    public void installSnapshot(byte[] state) {
        DBDecoder decoder = new DefaultDBDecoder();
        ByteArrayInputStream in = new ByteArrayInputStream(state);
        List<DBObject> objects = new ArrayList<>();
        DBCursor lastEntryCursor = collection.find().sort(new BasicDBObject("_id", -1)).limit(1);
        if (lastEntryCursor.hasNext()) {
            counter = (int) lastEntryCursor.next().get("_id");
            System.out.println("counter = " + counter);
        }
        int realCounter = 0;
        while (in.available() > 0) {
            realCounter++;
            try {
                DBObject entry = decoder.decode(in, collection);
                if ((int) entry.get("_id") > counter) {
                    objects.add(entry);
                }
            } catch (IOException ignored) {}
        }
        counter = realCounter;
        System.out.println("realCounter = " + realCounter);
        System.out.println("objects.size() = " + objects.size());
        if (objects.size() > 0) {
            collection.insert(objects);
        }
    }

    @Override
    public byte[] getSnapshot() {
        DBCursor dbObjects = collection.find();
        DBEncoder encoder = DefaultDBEncoder.FACTORY.create();
        OutputBuffer buf = new BasicOutputBuffer();
        while(dbObjects.hasNext()) {
            encoder.writeObject(buf, dbObjects.next());
        }
        return buf.toByteArray();
    }

    @Override
    public byte[][] appExecuteBatch(byte[][] requests, MessageContext[] msgCtxs) {
        List<DBObject> requestList = new ArrayList<>();
        for (byte[] request: requests) {
            requestList.add(new BasicDBObject("msg", request).append("_id", counter++));
        }
        collection.insert(requestList);
        return new byte[requests.length][];
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        return new byte[0];
    }
}
