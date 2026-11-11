package com.example.devquest;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserRepository {

    public static class Profile {
        public String fullName = "Student";
        public String email = "";
        public String photoBase64 = "";
    }

    public interface ProfileCallback {
        void onLoaded(Profile profile);
    }

    public static class Stats {
        public int quizzesTaken = 0;
        public int correctAnswers = 0;
        public int streak = 0;
        public Map<String, Integer> percent = new HashMap<>();
    }

    public interface StatsCallback {
        void onLoaded(Stats stats);
    }

    public static DocumentReference currentUserDoc() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return null;
        return FirebaseFirestore.getInstance().collection("users").document(user.getUid());
    }

    public static Task<Void> createProfile(String fullName, String email) {
        DocumentReference doc = currentUserDoc();
        if (doc == null) {
            return Tasks.forException(new IllegalStateException("No logged in user"));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("fullName", fullName);
        data.put("email", email);
        data.put("photoBase64", "");
        data.put("quizzesTaken", 0);
        data.put("correctAnswers", 0);
        data.put("streak", 0);
        data.put("lastLoginDate", "");
        data.put("languages", emptyLanguages());

        return doc.set(data);
    }

    public static void loadProfile(ProfileCallback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference doc = currentUserDoc();
        if (user == null || doc == null) return;

        doc.get().addOnCompleteListener(task -> {
            Profile profile = new Profile();
            profile.email = user.getEmail() != null ? user.getEmail() : "";
            profile.fullName = nameFromEmail(profile.email);

            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot snap = task.getResult();

                if (snap.exists()) {
                    String name = snap.getString("fullName");
                    if (name != null && !name.trim().isEmpty()) {
                        profile.fullName = name.trim();
                    }
                    String photo = snap.getString("photoBase64");
                    if (photo != null) {
                        profile.photoBase64 = photo;
                    }
                    callback.onLoaded(profile);
                } else {
                    createProfile(profile.fullName, profile.email)
                            .addOnCompleteListener(t -> callback.onLoaded(profile));
                }
            } else {
                callback.onLoaded(profile);
            }
        });
    }

    public static void updateStreak() {
        DocumentReference doc = currentUserDoc();
        if (doc == null) return;

        String today = dateString(0);
        String yesterday = dateString(-1);

        FirebaseFirestore.getInstance().runTransaction(tx -> {
            DocumentSnapshot snap = tx.get(doc);
            String last = snap.getString("lastLoginDate");
            Long current = snap.getLong("streak");
            int streak = current == null ? 0 : current.intValue();

            if (today.equals(last)) return streak;

            streak = yesterday.equals(last) ? streak + 1 : 1;

            Map<String, Object> update = new HashMap<>();
            update.put("streak", streak);
            update.put("lastLoginDate", today);
            tx.set(doc, update, SetOptions.merge());
            return streak;
        });
    }

    public static void loadStats(StatsCallback callback) {
        DocumentReference doc = currentUserDoc();
        if (doc == null) return;

        doc.get().addOnCompleteListener(task -> {
            Stats stats = new Stats();

            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                DocumentSnapshot snap = task.getResult();
                stats.quizzesTaken = toInt(snap.get("quizzesTaken"));
                stats.correctAnswers = toInt(snap.get("correctAnswers"));
                stats.streak = toInt(snap.get("streak"));

                Object languages = snap.get("languages");
                if (languages instanceof Map) {
                    Map<?, ?> all = (Map<?, ?>) languages;
                    for (String key : new String[]{"cpp", "java", "csharp", "javascript"}) {
                        Object one = all.get(key);
                        int percent = 0;
                        if (one instanceof Map) {
                            int correct = toInt(((Map<?, ?>) one).get("correct"));
                            int total = toInt(((Map<?, ?>) one).get("total"));
                            if (total > 0) percent = Math.round(correct * 100f / total);
                        }
                        stats.percent.put(key, percent);
                    }
                }
            }

            callback.onLoaded(stats);
        });
    }

    public static void saveResult(String language, String mode, int score, int total) {
        DocumentReference doc = currentUserDoc();
        if (doc == null) {
            Tasks.forException(new IllegalStateException("No logged in user"));
            return;
        }

        String key = languageKey(language);

        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        batch.update(doc,
                "quizzesTaken", FieldValue.increment(1),
                "correctAnswers", FieldValue.increment(score),
                "languages." + key + ".correct", FieldValue.increment(score),
                "languages." + key + ".total", FieldValue.increment(total));

        Map<String, Object> result = new HashMap<>();
        result.put("language", language);
        result.put("mode", mode);
        result.put("score", score);
        result.put("total", total);
        result.put("date", new Date());
        batch.set(doc.collection("results").document(), result);

        batch.commit();
    }

    private static String languageKey(String language) {
        if (language == null) return "java";
        switch (language.trim().toLowerCase(Locale.US)) {
            case "c++":        return "cpp";
            case "c#":         return "csharp";
            case "javascript": return "javascript";
            default:           return "java";
        }
    }

    public static Task<Void> updateProfile(String fullName, String photoBase64) {
        DocumentReference doc = currentUserDoc();
        if (doc == null) {
            return Tasks.forException(new IllegalStateException("No logged in user"));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("fullName", fullName);
        if (photoBase64 != null) {
            data.put("photoBase64", photoBase64);
        }
        return doc.set(data, SetOptions.merge());
    }

    private static int toInt(Object value) {
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    private static String dateString(int offsetDays) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, offsetDays);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(c.getTime());
    }

    private static String nameFromEmail(String email) {
        if (email == null || email.isEmpty()) return "Student";
        String name = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        if (name.isEmpty()) return "Student";
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private static Map<String, Object> emptyLanguages() {
        Map<String, Object> languages = new HashMap<>();
        for (String key : new String[]{"cpp", "java", "csharp", "javascript"}) {
            Map<String, Object> score = new HashMap<>();
            score.put("correct", 0);
            score.put("total", 0);
            languages.put(key, score);
        }
        return languages;
    }
}