package use_case;
import api.GradeDB;
import entity.Grade;
import entity.Team;

public final class GetAverageGradeUseCase {
    private final GradeDB gradeDB;

    public GetAverageGradeUseCase(GradeDB gradeDB) {
        this.gradeDB = gradeDB;
    }

    public float getAverageGrade(String course) {
        Team team = gradeDB.getMyTeam();
        float sum = 0.0f;
        int n = 0;

        assert team != null;

        for (String member : team.getMembers()) {
            Grade grade = gradeDB.getGrade(member, course);
            if (grade == null) {
                continue;
            }

            sum += grade.getGrade();
            n += 1;
        }

        return n == 0 ? 0.0f : sum / n;
    }
}
