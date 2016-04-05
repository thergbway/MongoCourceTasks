package course;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;

public class Main {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(15).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);

        MongoDatabase testDB = client.getDatabase("test").withReadPreference(ReadPreference.secondary());

        MongoCollection<BsonDocument> users = testDB.getCollection("users", BsonDocument.class);
    }
}
