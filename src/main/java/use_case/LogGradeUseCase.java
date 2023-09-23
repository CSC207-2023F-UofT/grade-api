package use_case;
import api.GradeDB;
import org.json.JSONException;

public final class LogGradeUseCase {
    private final GradeDB gradeDB;

    public LogGradeUseCase(GradeDB gradeDB) {
        this.gradeDB = gradeDB;
    }

    public void logGrade(String course, int grade) throws JSONException {
        gradeDB.logGrade(course, grade);
    }
}
