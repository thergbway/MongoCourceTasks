package course;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

public class Helper {
    public static void prettyPrintJSON(final Document doc) {
        System.out.println(doc.toJson(
            new JsonWriterSettings(JsonMode.SHELL, true)
        ));
    }

    public static void prettyPrintJSON(final BsonDocument doc) {
        System.out.println(doc.toJson(
            new JsonWriterSettings(JsonMode.SHELL, true)
        ));
    }


}