package course.hw3_1;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonString;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(15).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);

        MongoDatabase schoolDB = client.getDatabase("school").withReadPreference(ReadPreference.secondary());

        MongoCollection<BsonDocument> studentsCollection = schoolDB.getCollection("students", BsonDocument.class);
        FindIterable<BsonDocument> students = studentsCollection.find();

        System.out.println("====================================BEFORE===================================================");
        for (BsonDocument student : students) {
            System.out.println(student);
        }

        for (BsonDocument student : students) {
            BsonArray curScores = student.getArray("scores");
            BsonDouble lowestHomeworkScore = findLowestHomeworkScore(curScores);

            student
                .getArray("scores")
                .removeIf(scoreEntry -> {
                    BsonDocument scoreEntryAsDoc = scoreEntry.asDocument();
                    boolean isHomework = scoreEntryAsDoc.getString("type").equals(new BsonString("homework"));
                    boolean isLowestHomeworkScore =
                        scoreEntryAsDoc.getDouble("score").equals(lowestHomeworkScore);

                    return isHomework && isLowestHomeworkScore;
                });

            studentsCollection.replaceOne(new BsonDocument("_id", student.get("_id")), student);
        }

        students = studentsCollection.find();

        System.out.println("====================================AFTER===================================================");
        for (BsonDocument student : students) {
            System.out.println(student);
        }
    }

    private static BsonDouble findLowestHomeworkScore(BsonArray scores) {
        List<BsonDocument> scoresList = Arrays.asList(scores.toArray(new BsonDocument[0]));

        return scoresList
            .stream()
            .filter(bsonDocument -> bsonDocument.getString("type").equals(new BsonString("homework")))
            .sorted((o1, o2) -> o1.getDouble("score").compareTo(o2.getDouble("score")))
            .findFirst()
            .get()
            .getDouble("score");
    }
}
