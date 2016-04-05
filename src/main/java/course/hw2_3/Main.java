package course.hw2_3;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import course.Helper;
import org.bson.BsonDocument;
import org.bson.Document;

public class Main {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(15).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);

        MongoDatabase students = client.getDatabase("students").withReadPreference(ReadPreference.secondary());

        MongoCollection<BsonDocument> grades = students.getCollection("grades", BsonDocument.class);


        FindIterable<BsonDocument> docs = grades.find(new Document("type", "homework")).sort(new Document("student_id", 1).append("score", -1));

        MongoCursor<BsonDocument> it = docs.iterator();

        BsonDocument prev = it.next();
        while (it.hasNext()) {
            BsonDocument next = it.next();
            if (!prev.getNumber("student_id").equals(next.getNumber("student_id"))) {
                grades.deleteOne(Filters.eq("_id", prev.getObjectId("_id")));
            }
            prev = next;
        }
        grades.deleteOne(Filters.eq("_id", prev.getObjectId("_id")));

        docs = grades.find(new Document("type", "homework")).sort(new Document("student_id", 1).append("score", -1));
        for (BsonDocument doc : docs) {
            Helper.prettyPrintJSON(doc);
        }
    }
}
